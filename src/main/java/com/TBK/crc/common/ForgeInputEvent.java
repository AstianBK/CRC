package com.TBK.crc.common;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketKeySync;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CRC.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeInputEvent {
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
    @SubscribeEvent
    public static void onMouseScrolling(InputEvent.MouseScrollingEvent event){
        Minecraft mc = Minecraft.getInstance();
        if(mc.level == null) return;
        if(CRCKeybinds.attackKey3.isDown()){
            event.setCanceled(true);
            PacketHandler.sendToServer(new PacketKeySync(0x12,event.getScrollDelta()<0 ? 0 : 1,-1));
        }
    }

    private static boolean onInput(Minecraft mc, int key, int action) {
        MultiArmCapability cap = MultiArmCapability.get(mc.player);
        if(cap!=null){
            cap.hotbarActive = CRCKeybinds.attackKey3.isDown();
        }
        if (mc.screen == null && (key==1)) {
            PacketHandler.sendToServer(new PacketKeySync(0x52,action,-1));
            return true;
        }
        return false;
    }
}
