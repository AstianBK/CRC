package com.TBK.crc.server.network.messager;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncPosHotBar {
    private final int pos;
    public PacketSyncPosHotBar(FriendlyByteBuf buf) {
        this.pos =buf.readInt();
    }

    public PacketSyncPosHotBar(int blood) {
        this.pos = blood;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.pos);
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(this::sync);
        context.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    public void sync(){
        Minecraft mc=Minecraft.getInstance();
        Player player=mc.player;
        assert player!=null;
        MultiArmCapability.get(player).setPosSelectUpgrade(this.pos);
    }
}
