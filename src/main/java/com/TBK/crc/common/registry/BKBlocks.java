package com.TBK.crc.common.registry;

import com.TBK.crc.CRC;
import com.TBK.crc.common.block.CyborgTableBlock;
import com.TBK.crc.common.block.UpgradeTableBlock;
import com.TBK.crc.common.menu.UpgradeTableMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BKBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CRC.MODID);

    public static final RegistryObject<Block> CYBORG_TABLE = registerBlock("cyborg_table",
            CyborgTableBlock::new);


    public static final RegistryObject<Block> UPGRADE_TABLE = registerBlock("upgrade_table",
            UpgradeTableBlock::new);



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block ) {
        return BKItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()){
            @Override
            public void appendHoverText(ItemStack p_40572_, @Nullable Level p_40573_, List<Component> p_40574_, TooltipFlag p_40575_) {
                super.appendHoverText(p_40572_, p_40573_, p_40574_, p_40575_);
                p_40574_.add(Component.translatable("crc.tooltip."+name));
            }
        });
    }

}

