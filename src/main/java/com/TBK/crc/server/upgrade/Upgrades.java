package com.TBK.crc.server.upgrade;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.manager.UpgradeInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Upgrades {
    public Map<Integer, UpgradeInstance> upgrades;

    public Upgrades(Map<Integer, UpgradeInstance> upgrades){
        this.upgrades = upgrades;
    }

    public void write(FriendlyByteBuf buf){
        buf.writeMap(this.upgrades, FriendlyByteBuf::writeInt,(buf1, multiArmSkillAbstractInstance) -> buf1.writeUtf(multiArmSkillAbstractInstance.getUpgrade().name));
    }

    public Upgrades(CompoundTag tag){
        Map<Integer, UpgradeInstance> map = new HashMap<>();
        if(tag.contains("upgrades")){
            ListTag listTag = tag.getList("upgrades",10);
            for(int i = 0 ; i<listTag.size() ; i++){
                CompoundTag tag1=listTag.getCompound(i);
                if(tag1.contains("name")){
                    int pos=tag1.getInt("pos");
                    Upgrade power = Util.getTypeSkillForName(tag1.getString("name"));
                    ListTag tags = tag1.getList("refinements",10);
                    for(int j = 0 ; j<tags.size() ; j++){
                        power.refinements.add(tags.getCompound(j).getString("name"));
                    }
                    map.put(pos, new UpgradeInstance(power,0));
                }
            }
        }
        this.upgrades = map;
    }


    public void save(CompoundTag tag){
        ListTag listtag = new ListTag();
        for (int i = 0; i<this.upgrades.size(); i++){
            if(this.upgrades.get(i)!=null){
                Upgrade power=this.upgrades.get(i).getUpgrade();
                CompoundTag tag1=new CompoundTag();
                power.save(tag1);
                tag1.putInt("pos",i);
                power.save(tag1);
                listtag.add(tag1);
            }
        }
        if(!listtag.isEmpty()){
            tag.put("upgrades",listtag);
        }
    }

    public Upgrade getForName(String name){
        Upgrade power = Upgrade.NONE;
        for (UpgradeInstance powerInstance:this.getSkills()){
            Upgrade power1=powerInstance.getUpgrade();
            if(power1.name.equals(name)){
                power=power1;
            }
        }
        return power;
    }

    public Map<Integer, UpgradeInstance> getUpgrades() {
        Map<Integer, UpgradeInstance> map = new HashMap<>();
        for (Map.Entry<Integer, UpgradeInstance> entry : this.upgrades.entrySet()){
            if(!entry.getValue().getUpgrade().name.equals("none")){
                map.put(entry.getKey(),entry.getValue());
            }
        }
        return map;
    }

    public boolean hasMultiArmSkillAbstract(String id){
        return !this.getForName(id).name.equals("none");
    }

    public void addMultiArmSkillAbstracts(int pos, Upgrade power){
        this.upgrades.put(pos,new UpgradeInstance(power,0));
    }

    public Collection<UpgradeInstance> getSkills() {
        return this.upgrades.values();
    }


    public Upgrade get(int pos){
        if(this.upgrades.containsKey(pos)){
            return this.upgrades.get(pos).getUpgrade();
        }
        return Upgrade.NONE;
    }
}
