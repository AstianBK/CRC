package com.TBK.crc.common;

import com.TBK.crc.CRC;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.contents.KeybindContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.KeyEvent;

@Mod.EventBusSubscriber(modid = CRC.MODID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CRCKeybinds {
    public static KeyMapping attackKey3;
    public static KeyMapping bottomImplantStore;

    @SubscribeEvent
    public static void register(final RegisterKeyMappingsEvent event) {
        attackKey3 = create("attack_key3", 342);
        bottomImplantStore = create("bottom_implant_store",86);

        event.register(attackKey3);
        event.register(bottomImplantStore);
    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + CRC.MODID + "." + name, key, "key.category." + CRC.MODID);
    }
}
