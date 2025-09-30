package com.TBK.crc.server.network;

import com.TBK.crc.CRC;
import com.TBK.crc.server.network.messager.PacketActionRex;
import com.TBK.crc.server.network.messager.PacketHandlerPowers;
import com.TBK.crc.server.network.messager.PacketKeySync;
import com.TBK.crc.server.network.messager.PacketSyncPosHotBar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static SimpleChannel MOD_CHANNEL;

    public static void registerMessages() {
        int index = 0;
        SimpleChannel channel= NetworkRegistry.ChannelBuilder.named(
                        new ResourceLocation(CRC.MODID, "messages"))
                .networkProtocolVersion(()-> PROTOCOL_VERSION)
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        MOD_CHANNEL=channel;


        channel.messageBuilder(PacketSyncPosHotBar.class,index++)
                .encoder(PacketSyncPosHotBar::write)
                .decoder(PacketSyncPosHotBar::new)
                .consumerNetworkThread(PacketSyncPosHotBar::handle).add();

        channel.registerMessage(index++, PacketActionRex.class, PacketActionRex::write,
                PacketActionRex::new, PacketActionRex::handle);
        channel.registerMessage(index++, PacketKeySync.class, PacketKeySync::write,
                PacketKeySync::new, PacketKeySync::handle);
        channel.registerMessage(index++, PacketHandlerPowers.class, PacketHandlerPowers::write,
                PacketHandlerPowers::new, PacketHandlerPowers::handle);



    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        MOD_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),message);
    }

    public static <MSG> void sendToServer(MSG message) {
        MOD_CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToAllTracking(MSG message, LivingEntity entity) {
        MOD_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
