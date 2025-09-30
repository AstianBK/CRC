package com.TBK.crc.common;

import com.TBK.crc.CRC;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketKeySync;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CRC.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InputEvents {
    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getButton(), event.getAction());
    }
    private static boolean onInput(Minecraft mc, int key, int action) {
        if (mc.screen == null && (key==1)) {
            PacketHandler.sendToServer(new PacketKeySync(0x52,action,-1));
            return true;
        }
        return false;
    }
}
