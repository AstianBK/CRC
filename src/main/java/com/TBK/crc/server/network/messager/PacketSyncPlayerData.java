package com.TBK.crc.server.network.messager;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncPlayerData implements Packet<PacketListener> {
    public CompoundTag data;
    public boolean wasDeath;
    public int id;
    public PacketSyncPlayerData(CompoundTag data,boolean wasDeath, int id){
        this.data = data;
        this.wasDeath = wasDeath;
        this.id = id;
    }
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.data);
        buf.writeBoolean(this.wasDeath);
        buf.writeInt(id);
    }

    public PacketSyncPlayerData(FriendlyByteBuf buf){
        this.data = buf.readNbt();
        this.wasDeath = buf.readBoolean();
        this.id = buf.readInt();
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
        assert mc.level!=null && mc.player!=null;
        Entity entity = mc.level.getEntity(this.id);
        assert entity instanceof Player;
        Player player = (Player) entity;
        MultiArmCapability cap = MultiArmCapability.get(player);
        if(cap!=null){
            if(this.wasDeath){
                cap.copyNbt(this.data);
            }else {
                cap.loadWarningLevel(this.data);
            }
        }
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }
}
