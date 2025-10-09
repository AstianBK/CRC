package com.TBK.crc.common.item;

import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CyberImplantItem extends ItemCyborg{
    public CyberImplantItem(Properties p_41383_, UpgradeableParts part) {
        super(p_41383_, part,0);
    }
    public int getTier(CompoundTag tag){
        return tag.contains("tier") ? tag.getInt("tier") : 0;
    }
    public void setTier(CompoundTag tag,int tier){
        tag.putInt("tier",tier);
    }
    public static boolean canAddUpgrade(CompoundTag tag,MultiArmSkillAbstract upgrade){
        if(tag.contains("storeUpgrade")){
            ListTag list = tag.getList("storeUpgrade",10);
            if(!list.isEmpty()){
                for (int i = 0 ; i<list.size() ; i++){
                    CompoundTag nbt = list.getCompound(i);
                    if(nbt.getString("nameUpgrade").equals(upgrade.name)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public static void addUpgrade(CompoundTag tag, MultiArmSkillAbstract upgrade){
        if(tag.contains("storeUpgrade")){
            ListTag list = tag.getList("storeUpgrade",10);
            if(!list.isEmpty()){
                CompoundTag nbt = new CompoundTag();
                nbt.putString("nameUpgrade",upgrade.name);
                list.add(nbt);
            }
        }else {
            ListTag list = new ListTag();
            CompoundTag nbt = new CompoundTag();
            nbt.putString("nameUpgrade",upgrade.name);
            list.add(nbt);
            tag.put("storeUpgrade",list);
        }
    }
    public static List<MultiArmSkillAbstract> getUpgrade(CompoundTag tag){
        List<MultiArmSkillAbstract> upgrades = new ArrayList<>();
        upgrades.add(MultiArmSkillAbstract.NONE);
        if(tag.contains("storeUpgrade")){
            ListTag list = tag.getList("storeUpgrade",10);
            if(!list.isEmpty()){
                for (int i = 0 ; i<list.size() ; i++){
                    CompoundTag nbt = list.getCompound(i);
                    upgrades.add(Util.getTypeSkillForName(nbt.getString("nameUpgrade")));
                }
            }
        }
        return upgrades;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        List<MultiArmSkillAbstract> upgrades = getUpgrade(p_41421_.getOrCreateTag());
        if(!upgrades.isEmpty()){
            for (MultiArmSkillAbstract upgrade : upgrades){
                if (upgrade==MultiArmSkillAbstract.NONE)continue;
                p_41423_.add(Component.translatable("upgrades."+upgrade.name).withStyle(ChatFormatting.DARK_AQUA));
            }
        }
    }
}