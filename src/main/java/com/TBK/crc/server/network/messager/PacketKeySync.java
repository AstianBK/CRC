package com.TBK.crc.server.network.messager;

import com.TBK.crc.common.menu.ImplantMenu;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

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
        ServerPlayer player=contextSupplier.get().getSender();
        MultiArmCapability cap =MultiArmCapability.get(player);
        assert cap != null;
        switch (this.key){
            case 0x52->{
                if(this.action==0){
                    cap.stopCasting(player);
                }
                if(this.action==1){
                    cap.startCasting(player);
                }
            }
            case 0x12->{
                MultiArmSkillAbstract skill = cap.getSelectSkill();
                cap.stopCasting(player);
                skill.swapArm(cap, cap.getSkillForHotBar(this.action));
                cap.setPosSelectMultiArmSkillAbstract(this.action);
                cap.getSelectSkill().swapArm(cap,skill);
            }
            case 86->{
                NetworkHooks.openScreen(player, new SimpleMenuProvider((id, inventory, player1)->{
                    return new ImplantMenu(id,inventory,cap.implantStore.store);
                }, Component.literal("")),player.blockPosition());
            }

        }
    }
}
