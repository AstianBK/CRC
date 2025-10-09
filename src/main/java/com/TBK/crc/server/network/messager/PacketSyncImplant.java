package com.TBK.crc.server.network.messager;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.manager.ImplantStore;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketSyncImplant implements Packet<PacketListener> {

    public final int id;
    public List<ItemStack> container;
    public PacketSyncImplant(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.container = buf.readList(FriendlyByteBuf::readItem);
    }

    public PacketSyncImplant(int id,SimpleContainer container) {
        this.id = id;
        this.container = getListForContainer(container);
    }

    public List<ItemStack> getListForContainer(SimpleContainer container){
        List<ItemStack> stacks = new ArrayList<>();
        for(int i = 0 ; i<container.getContainerSize() ; i++){
            stacks.add(container.getItem(i));
        }
        return stacks;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeCollection(this.container,FriendlyByteBuf::writeItem);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            assert context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;
            Player player = Minecraft.getInstance().player;
            MultiArmCapability cap = MultiArmCapability.get(player);
            if(cap!=null){
                cap.implantStore.setStoreForList(this.container);
            }
        });
        context.get().setPacketHandled(true);
    }



    @Override
    public void handle(PacketListener p_131342_) {

    }
}
