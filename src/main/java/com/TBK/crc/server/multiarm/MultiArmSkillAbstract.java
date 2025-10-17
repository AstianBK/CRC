package com.TBK.crc.server.multiarm;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

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
            multiArmCapability.getPlayer().level().playLocalSound(multiArmCapability.getPlayer().blockPosition(),getStartSound(), SoundSource.PLAYERS,10.0f,1.0f,false);
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
            multiArmCapability.getPlayer().level().playLocalSound(multiArmCapability.getPlayer().blockPosition(),getStopSound(), SoundSource.PLAYERS,10.0f,1.0f,false);
        }
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
    }
    public void load(CompoundTag nbt){

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

    public void swapArm(MultiArmCapability multiArmCapability,MultiArmSkillAbstract otherArm){

    }

    public void onAttack(MultiArmCapability multiArmCapability, LivingHurtEvent event){

    }

    public void onHurt(MultiArmCapability multiArmCapability, LivingHurtEvent event){

    }

    public boolean canInteractionOnWorld(MultiArmCapability cap) {

        return true;
    }
}
