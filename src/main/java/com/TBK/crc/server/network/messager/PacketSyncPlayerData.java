package com.TBK.crc.server.network.messager;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncPlayerData implements Packet<PacketListener> {
    public CompoundTag data;
    public boolean wasDeath;
    public PacketSyncPlayerData(CompoundTag data,boolean wasDeath){
        this.data = data;
        this.wasDeath = wasDeath;
    }
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.data);
        buf.writeBoolean(this.wasDeath);
    }

    public PacketSyncPlayerData(FriendlyByteBuf buf){
        this.data = buf.readNbt();
        this.wasDeath = buf.readBoolean();
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            handlerAnim(context);
        });
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void handlerAnim(Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level!=null || mc.player!=null;
        MultiArmCapability cap = MultiArmCapability.get(mc.player);
        if(cap!=null){
            if(this.wasDeath){
                cap.copyNbt(this.data);
            }else {
                cap.syncWarningLevel(this.data);
            }
        }
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }
}
