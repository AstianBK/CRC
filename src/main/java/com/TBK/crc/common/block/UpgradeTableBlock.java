package com.TBK.crc.common.block;

import com.TBK.crc.common.menu.UpgradeTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public class UpgradeTableBlock extends Block {
    private static final Component CONTAINER_TITLE = Component.translatable("crc.upgrade_table");

    public UpgradeTableBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_BLACK).strength(2.0f).noOcclusion().pushReaction(PushReaction.DESTROY)
        );
    }

    public InteractionResult use(BlockState p_52233_, Level p_52234_, BlockPos p_52235_, Player p_52236_, InteractionHand p_52237_, BlockHitResult p_52238_) {
        if (p_52234_.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            p_52236_.openMenu(p_52233_.getMenuProvider(p_52234_, p_52235_));
            p_52236_.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return InteractionResult.CONSUME;
        }
    }
    public MenuProvider getMenuProvider(BlockState p_52240_, Level p_52241_, BlockPos p_52242_) {
        return new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) -> {
                return new UpgradeTableMenu(p_52229_, p_52230_,new SimpleContainer(3),new SimpleContainerData(1));
        }, CONTAINER_TITLE);
    }
}
