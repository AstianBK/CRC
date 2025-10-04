package com.TBK.crc.server.network.messager;

import com.TBK.crc.CRC;
import com.TBK.crc.common.CRCKeybinds;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketKeySync implements Packet<PacketListener>{
    private final int key;
    private final int action;
    private final int idTarget;

    public PacketKeySync(FriendlyByteBuf buf) {
        this.key=buf.readInt();
        this.action=buf.readInt();
        this.idTarget=buf.readInt();
    }

    public PacketKeySync(int key,int action,int idTarget) {
        this.key = key;
        this.action=action;
        this.idTarget=idTarget;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.key);
        buf.writeInt(this.action);
        buf.writeInt(this.idTarget);
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            handlerAnim(context);
        });
        context.get().setPacketHandled(true);
    }

    private void handlerAnim(Supplier<NetworkEvent.Context> contextSupplier) {
        Player player=contextSupplier.get().getSender();
        MultiArmCapability skillPlayerCapability=MultiArmCapability.get(player);
        assert skillPlayerCapability != null;
        switch (this.key){
            case 0x52->{
                if(this.action==0){
                    skillPlayerCapability.stopCasting(player);
                    CRC.LOGGER.debug("Stop casting");
                }
                if(skillPlayerCapability.cooldownReUse<=0){
                    if(this.action==1){
                        skillPlayerCapability.startCasting(player);
                    }
                    skillPlayerCapability.cooldownReUse=2;
                }
            }
            case 0x12->{
                MultiArmSkillAbstract skill = skillPlayerCapability.getSelectSkill();
                skill.swapArm(skillPlayerCapability,skillPlayerCapability.getSkillForHotBar(this.action));
                skillPlayerCapability.setPosSelectMultiArmSkillAbstract(this.action);
                skillPlayerCapability.getSelectSkill().swapArm(skillPlayerCapability,skill);
            }

        }
    }
}
