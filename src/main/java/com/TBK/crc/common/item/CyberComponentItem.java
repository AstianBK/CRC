package com.TBK.crc.common.item;

import com.TBK.crc.UpgradeableParts;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CyberComponentItem extends ItemCyborg{

    public CyberComponentItem(Properties p_41383_) {
        super(p_41383_, UpgradeableParts.COMPONENT,0);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.translatable("tooltip.cyber_component").withStyle(ChatFormatting.GRAY));
    }
}
