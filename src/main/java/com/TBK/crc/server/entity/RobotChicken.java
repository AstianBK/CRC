package com.TBK.crc.server.entity;

import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RobotChicken extends PathfinderMob implements PowerableMob {
    private static final EntityDataAccessor<Boolean> SHIELD = SynchedEntityData.defineId(RobotChicken.class, EntityDataSerializers.BOOLEAN);

    protected RobotChicken(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHIELD,false);
    }

    public void setShield(boolean flag){
        this.entityData.set(SHIELD,flag);
    }

    public boolean hasShield(){
        return this.entityData.get(SHIELD);
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if(hasShield()){
            if(p_21016_.is(Util.ELECTRIC_DAMAGE_PLAYER) || p_21016_.is(Util.ELECTRIC_DAMAGE_MOB)){
                this.setShield(false);
            }
            return false;
        }
        return super.hurt(p_21016_, p_21017_);
    }


    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return BKSounds.CHICKEN_MINION_HURT.get();
    }

    @Override
    public boolean isPowered() {
        return hasShield();
    }
}
