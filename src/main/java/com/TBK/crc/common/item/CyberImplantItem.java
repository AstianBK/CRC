package com.TBK.crc.common.item;

import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.upgrade.Upgrade;
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
    public Upgrade skill;
    public CyberImplantItem(Properties p_41383_, UpgradeableParts part, Upgrade skillAbstract) {
        super(p_41383_, part,0);
        this.skill = skillAbstract;
    }

    public static boolean canAddUpgrade(CompoundTag tag, Upgrade upgrade){
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

    public static boolean canAddRefinement(CompoundTag tag, CyberRefinementItem refinementItem,Upgrade upgrade){
        if(!refinementItem.skill.name.equals(upgrade.name))return false;
        if(tag.contains("storeRefinement")){
            ListTag list = tag.getList("storeRefinement",10);
            if(!list.isEmpty()){
                for (int i = 0 ; i<list.size() ; i++){
                    CompoundTag nbt = list.getCompound(i);
                    if(nbt.getString("nameRefinement").equals(refinementItem.name)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void addUpgrade(CompoundTag tag, Upgrade upgrade){
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
    public static void addRefinement(CompoundTag tag, String refinement){
        if(tag.contains("storeRefinement")){
            ListTag list = tag.getList("storeRefinement",10);
            if(!list.isEmpty()){
                CompoundTag nbt = new CompoundTag();
                nbt.putString("nameRefinement",refinement);
                list.add(nbt);
            }
        }else {
            ListTag list = new ListTag();
            CompoundTag nbt = new CompoundTag();
            nbt.putString("nameRefinement",refinement);
            list.add(nbt);
            tag.put("storeRefinement",list);
        }
    }

    public static List<String> getRefinement(CompoundTag tag){
        List<String> upgrades = new ArrayList<>();
        if(tag.contains("storeRefinement")){
            ListTag list = tag.getList("storeRefinement",10);
            if(!list.isEmpty()){
                for (int i = 0 ; i<list.size() ; i++){
                    CompoundTag nbt = list.getCompound(i);
                    if(!nbt.getString("nameRefinement").equals(Upgrade.NONE.name)){
                        upgrades.add(nbt.getString("nameRefinement"));
                    }
                }
            }
        }
        return upgrades;
    }

    public static List<Upgrade> getUpgrade(CyberImplantItem implantItem, CompoundTag tag){
        List<Upgrade> upgrades = new ArrayList<>();
        if(!implantItem.skill.name.equals("none")){
            upgrades.add(Util.getTypeSkillForName(implantItem.skill.name));
        }
        if(tag.contains("storeUpgrade")){
            ListTag list = tag.getList("storeUpgrade",10);
            if(!list.isEmpty()){
                for (int i = 0 ; i<list.size() ; i++){
                    CompoundTag nbt = list.getCompound(i);
                    if(!nbt.getString("nameUpgrade").equals(Upgrade.NONE.name)){
                        Upgrade upgrade = Util.getTypeSkillForName(nbt.getString("nameUpgrade"));
                        upgrade.setRefinements(getRefinement(tag));
                        upgrades.add(upgrade);
                    }
                }
            }
        }
        return upgrades;
    }

    public static List<Upgrade> getUpgrade(CompoundTag tag){
        List<Upgrade> upgrades = new ArrayList<>();
        if(tag.contains("storeUpgrade")){
            ListTag list = tag.getList("storeUpgrade",10);
            if(!list.isEmpty()){
                for (int i = 0 ; i<list.size() ; i++){
                    CompoundTag nbt = list.getCompound(i);
                    if(!nbt.getString("nameUpgrade").equals(Upgrade.NONE.name)){
                        upgrades.add(Util.getTypeSkillForName(nbt.getString("nameUpgrade")));
                    }
                }
            }
        }
        return upgrades;
    }
    public static boolean equalsUpgrades(ItemStack stack, ItemStack stack1, boolean isArm){
        if (isArm){
            List<Upgrade> list = getUpgrade(stack.getOrCreateTag());
            List<Upgrade> list1 = getUpgrade(stack1.getOrCreateTag());
            if(list1.size() == list.size()){
                for (int i = 0 ; i < list.size() ; i++){
                    String upgrade = list.get(i).name;
                    String upgrade1 = list1.get(i).name;
                    if(!upgrade1.equals(upgrade)){
                        return false;
                    }
                }
                return true;
            }
            return false;
        }else {
            return false;
        }

    }
    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        List<Upgrade> upgrades = getUpgrade((CyberImplantItem) p_41421_.getItem(),p_41421_.getOrCreateTag());
        upgrades.remove(this.skill);
        if(!upgrades.isEmpty()){
            for (Upgrade upgrade : upgrades){
                if (upgrade== Upgrade.NONE)continue;
                p_41423_.add(Component.translatable("upgrades."+upgrade.name).withStyle(ChatFormatting.DARK_AQUA));
            }
        }
    }
}