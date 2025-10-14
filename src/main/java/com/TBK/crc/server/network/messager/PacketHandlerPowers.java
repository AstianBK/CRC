package com.TBK.crc.server.network.messager;

import com.TBK.crc.CRC;
import com.TBK.crc.common.menu.CyborgTableMenu;
import com.TBK.crc.common.registry.BKParticles;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.manager.MultiArmSkillAbstractInstance;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import com.TBK.crc.server.multiarm.PassivePart;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHandlerPowers implements Packet<PacketListener> {
    private final int id;
    private final Entity newEntity;
    private final Entity oldEntity;
    public PacketHandlerPowers(FriendlyByteBuf buf) {
        Minecraft mc=Minecraft.getInstance();
        assert mc.level!=null;
        this.id=buf.readInt();
        this.newEntity =mc.level.getEntity(buf.readInt());
        this.oldEntity = mc.level.getEntity(buf.readInt());
    }


    public PacketHandlerPowers(int id, Entity entity, Player player) {
        this.id=id;
        this.newEntity = entity;
        this.oldEntity = player;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeInt(this.newEntity!=null ? this.newEntity.getId() : -1);
        buf.writeInt(this.oldEntity!=null ? this.oldEntity.getId() : -1);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
                this.handler();
            }else {
                Player player=context.get().getSender();
                MultiArmCapability cap=MultiArmCapability.get(player);
                if(cap!=null){
                    switch (id){
                        case 3-> cap.clearAbilityStore();
                        case 4 -> cap.clearForUpgradeStore();
                        case 5 ->{
                            AbstractContainerMenu menu = context.get().getSender().containerMenu;
                            if(menu instanceof CyborgTableMenu tableMenu){
                                cap.dirty = true;
                            }
                        }
                    }
                }
            }
        });
        context.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private void handler() {
        Player player=Minecraft.getInstance().player;
        MultiArmCapability cap=MultiArmCapability.get(player);
        if(cap!=null){
            switch (this.id){
                case 0->{
                    this.start(cap,player);
                }
                case 1->{
                    cap.catchEntity = this.newEntity;
                }
                case 2->{
                    if (cap.getLastUsingSkill() == cap.getSelectSkill()){
                        cap.stopSkill(cap.getSelectSkill());
                    }
                }
                case 5 ->{
                    for (MultiArmSkillAbstractInstance instance : cap.getPassives().getSkills()){
                        ((PassivePart)instance.getSkillAbstract()).onDie(cap,this.newEntity);
                    }
                }
                case 6->{
                    Minecraft.getInstance().particleEngine.createTrackingEmitter(this.newEntity, BKParticles.LIGHTNING_TRAIL_PARTICLES.get());
                }
            }
        }
    }
    @OnlyIn(Dist.CLIENT)
    public void start(MultiArmCapability cap,Player player){
        if(cap.canUseSkill(cap.getSelectSkill())){
            if(cap.getSelectSkill().isCasting){
                cap.startCasting(cap.getSelectSkill(),player);
            }else {
                if(cap.getSelectSkill().isCanReActive()){
                    MultiArmSkillAbstract power=cap.getSelectSkill();
                    cap.setLastUsingSkill(cap.getSelectSkill());
                    cap.handledSkill(power);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void stop(MultiArmCapability cap,Player player){
        cap.stopCasting(player);
    }
    @Override
    public void handle(PacketListener p_131342_) {


    }
}
