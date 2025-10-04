package com.TBK.crc.common.registry;

import com.TBK.crc.CRC;
import com.TBK.crc.common.menu.CyborgTableMenu;
import com.TBK.crc.common.menu.UpgradeTableMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BKContainers {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CRC.MODID);

    public static final RegistryObject<MenuType<CyborgTableMenu>> POTION_MENU = registerMenuType(CyborgTableMenu::new,"potion_menu");
    public static final RegistryObject<MenuType<UpgradeTableMenu>> UPGRADE_MENU = registerMenuType(UpgradeTableMenu::new,"upgrade_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return CONTAINERS.register(name, () -> IForgeMenuType.create(factory));
    }
}
