package com.TBK.crc.common.registry;

import com.TBK.crc.CRC;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BKCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CRC.MODID);

    public static final RegistryObject<CreativeModeTab> CRC_TAB = TABS.register("bk_items",()-> CreativeModeTab.builder()
            .icon(()->new ItemStack(BKItems.CYBORG_ARM.get()))
            .title(Component.translatable("itemGroup.crcTab"))
            .displayItems((s,a)-> {
                a.accept(new ItemStack(BKBlocks.CYBORG_TABLE.get()));
                a.accept(new ItemStack(BKBlocks.UPGRADE_TABLE.get()));

                a.accept(new ItemStack(BKItems.SIGNAL_JAMMER.get()));

                a.accept(new ItemStack(BKItems.CYBORG_ARM.get()));
                a.accept(new ItemStack(BKItems.CYBORG_SYNTHARM.get()));
                a.accept(new ItemStack(BKItems.CYBORG_EYE.get()));
                a.accept(new ItemStack(BKItems.CYBORG_EYE_HACKER.get()));
                a.accept(new ItemStack(BKItems.CYBORG_HEART.get()));
                a.accept(new ItemStack(BKItems.CYBORG_THORACIC_CAGE.get()));
                a.accept(new ItemStack(BKItems.CYBORG_KNEES.get()));
                a.accept(new ItemStack(BKItems.CYBORG_KNEES_SPIKED.get()));
                a.accept(new ItemStack(BKItems.CYBORG_COIL_FEET.get()));

                a.accept(new ItemStack(BKItems.UPGRADE_ARM_CLAWS.get()));
                a.accept(new ItemStack(BKItems.UPGRADE_ARM_CANNON.get()));
                a.accept(new ItemStack(BKItems.UPGRADE_ARM_GANCHO.get()));
                a.accept(new ItemStack(BKItems.UPGRADE_SYSTEM_HEART.get()));

                a.accept(new ItemStack(BKItems.TANTALUM_INGOT.get()));
                a.accept(new ItemStack(BKItems.CYBORG_MICROCHIP.get()));
                a.accept(new ItemStack(BKItems.CYBORG_RESISTOR.get()));
                a.accept(new ItemStack(BKItems.CYBORG_BONE.get()));
                a.accept(new ItemStack(BKItems.CYBORG_CAPACITOR.get()));

            })
            .build());
}
