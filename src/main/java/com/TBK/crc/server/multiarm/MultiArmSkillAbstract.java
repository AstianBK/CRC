package com.TBK.crc.server.multiarm;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.manager.DurationInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class MultiArmSkillAbstract {
    public static MultiArmSkillAbstract NONE = new MultiArmSkillAbstract("none",30,false, false);
    public final String name;
    public final int cd;
    public boolean canReactive;
    public boolean isCasting;
    public MultiArmSkillAbstract(String name , int cd,boolean canReactive,boolean isCasting){
        this.name = name;
        this.cd = cd;
        this.canReactive=canReactive;
        this.isCasting = isCasting;
    }
    public MultiArmSkillAbstract(CompoundTag tag){
        this.name = tag.getString("name");
        this.cd = tag.getInt("cd");
        this.canReactive = tag.getBoolean("canReactive");
        this.isCasting = tag.getBoolean("isCasting");
        this.load(tag);
    }
    public MultiArmSkillAbstract(FriendlyByteBuf buf){
        this.name = buf.readUtf();
        this.cd = buf.readInt();
        this.canReactive = buf.readBoolean();
        this.isCasting = buf.readBoolean();
    }

    public void startAbility(MultiArmCapability multiArmCapability){

    }

    public void stopAbility(MultiArmCapability multiArmCapability){

    }

    public void tick(MultiArmCapability multiArmCapability) {
    }

    public void save(CompoundTag nbt){
        nbt.putString("name",this.name);
        nbt.putInt("cd",this.cd);
        nbt.putBoolean("canReactive",this.canReactive);
        nbt.putBoolean("isCasting",this.isCasting);
    }
    public void load(CompoundTag nbt){

    }
    public boolean isCanReActive() {
        return canReactive;
    }
    public boolean isCasting(){
        return this.isCasting;
    }

    public void swapArm(MultiArmCapability multiArmCapability,MultiArmSkillAbstract otherArm){

    }

}
