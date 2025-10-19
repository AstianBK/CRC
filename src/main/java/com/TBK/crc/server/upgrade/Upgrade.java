package com.TBK.crc.server.upgrade;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.List;

public class Upgrade {
    public static Upgrade NONE = new Upgrade("none",30,false, false);
    public final String name;
    public final int cd;
    public boolean canReactive;
    public boolean isCasting;
    public List<String> refinements =new ArrayList<>();
    public Upgrade(String name , int cd, boolean canReactive, boolean isCasting){
        this.name = name;
        this.cd = cd;
        this.canReactive=canReactive;
        this.isCasting = isCasting;
    }
    public Upgrade(CompoundTag tag){
        this.name = tag.getString("name");
        this.cd = tag.getInt("cd");
        this.canReactive = tag.getBoolean("canReactive");
        this.isCasting = tag.getBoolean("isCasting");
        this.load(tag);
    }
    public Upgrade(FriendlyByteBuf buf){
        this.name = buf.readUtf();
        this.cd = buf.readInt();
        this.canReactive = buf.readBoolean();
        this.isCasting = buf.readBoolean();
    }

    public void startCooldown(MultiArmCapability multiArmCapability){
        ItemStack stack = multiArmCapability.implantStore.getImplantForSkill(this);
        if(stack!=null){
            multiArmCapability.getPlayer().getCooldowns().addCooldown(stack.getItem(),this.cd);
        }
    }
    public boolean canActiveAbility(MultiArmCapability multiArmCapability){
        return true;
    }
    public void startAbility(MultiArmCapability multiArmCapability){
        if(multiArmCapability.getPlayer().level().isClientSide && getStartSound() != null){
            multiArmCapability.getPlayer().level().playLocalSound(multiArmCapability.getPlayer().blockPosition(),getStartSound(), SoundSource.PLAYERS,getVolumenStartAbility(),1.0f,false);
        }
    }
    public SoundEvent getStartSound(){
        return null;
    }

    public SoundEvent getStopSound(){
        return null;
    }
    public void stopAbility(MultiArmCapability multiArmCapability){
        if(multiArmCapability.getPlayer().level().isClientSide && getStopSound() != null){
            multiArmCapability.getPlayer().level().playLocalSound(multiArmCapability.getPlayer().blockPosition(),getStopSound(), SoundSource.PLAYERS,this.getVolumenStopAbility(),1.0f,false);
        }
    }

    private float getVolumenStopAbility() {
        return 5.0f;
    }
    private float getVolumenStartAbility() {
        return 5.0f;
    }

    public void setRefinements(List<String> refinements){
        this.refinements = refinements;
    }

    public void tick(MultiArmCapability multiArmCapability) {
    }

    public MultiArmCapability.SkillPose getPose(){
        return MultiArmCapability.SkillPose.NONE;
    }

    public void save(CompoundTag nbt){
        nbt.putString("name",this.name);
        nbt.putInt("cd",this.cd);
        nbt.putBoolean("canReactive",this.canReactive);
        nbt.putBoolean("isCasting",this.isCasting);
        ListTag tags = new ListTag();
        for (String name : this.refinements){
            CompoundTag tag = new CompoundTag();
            tag.putString("name",name);
            tags.add(tag);
        }

        nbt.put("refinements",tags);
    }
    public void load(CompoundTag nbt){
        List<String> list = new ArrayList<>();
        if(nbt.contains("refinements")){
            ListTag tags = nbt.getList("refinements",10);
            for (int i = 0; i<tags.size() ; i++){
                CompoundTag tag = tags.getCompound(i);
                list.add(tag.getString("name"));
            }
        }
        this.refinements =list;
    }
    public boolean isCanReActive() {
        return canReactive;
    }

    public boolean hasEffect(MultiArmCapability multiArmCapability, MobEffect effect){
        return false;
    }

    public float[] getWindowsColor(MultiArmCapability multiArmCapability){
        return new float[]{0.0F,0.0F,0.0F,0.0F};
    }
    public boolean canEffect(MultiArmCapability multiArmCapability, MobEffect effect){
        return true;
    }

    public void swapArm(MultiArmCapability multiArmCapability, Upgrade otherArm){

    }

    public void onAttack(MultiArmCapability multiArmCapability, LivingHurtEvent event){

    }

    public void onHurt(MultiArmCapability multiArmCapability, LivingHurtEvent event){

    }

}
