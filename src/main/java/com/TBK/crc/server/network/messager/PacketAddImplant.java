package com.TBK.crc.server.network.messager;

import com.TBK.crc.common.Util;
import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAddImplant implements Packet<PacketListener> {

    public final ItemStack stack;
    public final int index;
    public PacketAddImplant(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
        this.index = buf.readInt();
    }

    public PacketAddImplant(ItemStack stack, int index) {
        this.stack = stack;
        this.index = index;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
        buf.writeInt(this.index);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            assert context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER;
            MultiArmCapability cap = MultiArmCapability.get(context.get().getSender());
            if(cap!=null){
                cap.implantStore.setImplant(this.stack,this.index);
            }
        });
        context.get().setPacketHandled(true);
    }



    @Override
    public void handle(PacketListener p_131342_) {

    }
}
