package com.TBK.crc.common.item;

import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.server.upgrade.Upgrade;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CyberRefinementItem extends ItemCyborg{
    public Upgrade skill;
    public String name;

    public CyberRefinementItem(Properties p_41383_, Upgrade skill, String nameRefinement) {
        super(p_41383_, UpgradeableParts.REFINEMENT,0);
        this.skill = skill;
        this.name = nameRefinement;
    }

    public Upgrade getSkill() {
        return skill;
    }
    public String getName(){
        return name;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.translatable("tooltip.refinement."+this.name));
    }
}
