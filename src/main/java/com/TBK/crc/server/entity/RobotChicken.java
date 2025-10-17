package com.TBK.crc.server.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class RobotChicken extends PathfinderMob {
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
}
