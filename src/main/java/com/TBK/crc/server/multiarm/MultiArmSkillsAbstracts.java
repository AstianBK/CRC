package com.TBK.crc.server.multiarm;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.manager.MultiArmSkillAbstractInstance;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MultiArmSkillsAbstracts {
    public Map<Integer, MultiArmSkillAbstractInstance> powers= Maps.newHashMap();

    public MultiArmSkillsAbstracts(Map<Integer,MultiArmSkillAbstractInstance> powers){
        this.powers=powers;
    }
    public MultiArmSkillsAbstracts(FriendlyByteBuf buf){
        Map<Integer,MultiArmSkillAbstractInstance> map = new HashMap<>();
        this.powers = buf.readMap(FriendlyByteBuf::readInt, buf1 -> {
            return new MultiArmSkillAbstractInstance(Util.getTypeSkillForName(buf1.readUtf()),0);
        });
    }

    public void write(FriendlyByteBuf buf){
        buf.writeMap(this.powers, FriendlyByteBuf::writeInt,(buf1, multiArmSkillAbstractInstance) -> buf1.writeUtf(multiArmSkillAbstractInstance.getSkillAbstract().name));
    }

    public MultiArmSkillsAbstracts(CompoundTag tag){
        Map<Integer,MultiArmSkillAbstractInstance> map = new HashMap<>();
        if(tag.contains("skills")){
            ListTag listTag = tag.getList("skills",10);
            for(int i = 0 ; i<listTag.size() ; i++){
                CompoundTag tag1=listTag.getCompound(i);
                if(tag1.contains("name")){
                    int pos=tag1.getInt("pos");
                    MultiArmSkillAbstract power = Util.getTypeSkillForName(tag1.getString("name"));
                    if(power!=null){
                        map.put(pos, new MultiArmSkillAbstractInstance(power,0));
                    }
                }
            }
        }
        this.powers = map;
    }


    public void save(CompoundTag tag){
        ListTag listtag = new ListTag();
        for (int i=0;i<this.powers.size();i++){
            if(this.powers.get(i)!=null){
                MultiArmSkillAbstract power=this.powers.get(i).getSkillAbstract();
                CompoundTag tag1=new CompoundTag();
                tag1.putString("name",power.name);
                tag1.putInt("pos",i);
                power.save(tag1);
                listtag.add(tag1);
            }
        }
        if(!listtag.isEmpty()){
            tag.put("skills",listtag);
        }
    }

    public MultiArmSkillAbstract getForName(String name){
        MultiArmSkillAbstract power =MultiArmSkillAbstract.NONE;
        for (MultiArmSkillAbstractInstance powerInstance:this.getSkills()){
            MultiArmSkillAbstract power1=powerInstance.getSkillAbstract();
            if(power1.name.equals(name)){
                power=power1;
            }
        }
        return power;
    }

    public boolean hasMultiArmSkillAbstract(String id){
        return this.getForName(id)!=null;
    }

    public void addMultiArmSkillAbstracts(int pos,MultiArmSkillAbstract power){
        this.powers.put(pos,new MultiArmSkillAbstractInstance(power,0));
    }

    public Collection<MultiArmSkillAbstractInstance> getSkills() {
        return this.powers.values();
    }

    public MultiArmSkillAbstract get(int pos){
        if(this.powers.containsKey(pos)){
            return this.powers.get(pos).getSkillAbstract();
        }
        return MultiArmSkillAbstract.NONE;
    }
}
