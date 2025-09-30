package com.TBK.crc.server.multiarm;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class MultiArmSkillAbstract {
    public static MultiArmSkillAbstract NONE = new MultiArmSkillAbstract("none",30,false);
    public final String name;
    public final int cd;
    public boolean canReactive;
    public MultiArmSkillAbstract(String name , int cd,boolean canReactive){
        this.name = name;
        this.cd = cd;
        this.canReactive=canReactive;
    }
    public MultiArmSkillAbstract(CompoundTag tag){
        this.name = tag.getString("name");
        this.cd = tag.getInt("cd");
        this.canReactive = tag.getBoolean("canReactive");
        this.save(tag);
    }
    public MultiArmSkillAbstract(FriendlyByteBuf buf){
        this.name = buf.readUtf();
        this.cd = buf.readInt();
        this.canReactive = buf.readBoolean();
    }

    public void startAbility(MultiArmCapability multiArmCapability){

    }

    public void tick(MultiArmCapability multiArmCapability) {
    }

    public void save(CompoundTag nbt){

    }
    public boolean isCanReActive() {
        return canReactive;
    }
}
