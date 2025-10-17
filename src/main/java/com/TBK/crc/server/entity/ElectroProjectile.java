package com.TBK.crc.server.entity;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.common.registry.BKParticles;
import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ElectroProjectile extends AbstractArrow {
    private static final EntityDataAccessor<Integer> RECHARGE = SynchedEntityData.defineId(ElectroProjectile.class,EntityDataSerializers.INT);
    public ElectroProjectile(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }
    public ElectroProjectile(Level p_36722_,LivingEntity player,int time) {
        this(BKEntityType.ELECTRO.get(), p_36722_);
        this.setOwner(player);
        this.setTimeRecharge(time);
        this.pickup = Pickup.DISALLOWED;
    }


    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (result.getType() == HitResult.Type.MISS && this.isAlive()) {
                List<Entity> intersecting = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(getScale()), this::canHitEntity);
                if (!intersecting.isEmpty())
                    this.onHit(new EntityHitResult(intersecting.get(0)));
            }
        }
        this.refreshDimensions();
        Vec3 vec3;
        vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        double d4 = vec3.horizontalDistance();

        this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));

        this.setDeltaMovement(vec3);

        this.setPos(d7, d2, d3);
        this.checkInsideBlocks();
    }

    @Override
    public EntityDimensions getDimensions(Pose p_19975_) {
        float scale = getScale()*4;
        return super.getDimensions(p_19975_).scale(scale);
    }

    @Override
    protected boolean tryPickup(Player p_150121_) {
        return false;
    }

    public float getScale(){
        return (1.0F+(this.getTimeRecharge()/30.0F));
    }

    public int getTimeRecharge(){
        return this.entityData.get(RECHARGE);
    }
    public void setTimeRecharge(int recharge){
        this.entityData.set(RECHARGE,recharge);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RECHARGE,0);
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        if(p_36757_.getEntity() instanceof LivingEntity livingEntity){
            int time = this.getTimeRecharge();
            p_36757_.getEntity().hurt(damageSources().generic(), (float) this.getBaseDamage());
            if(time>=50 && !this.level().isClientSide){
                float maxCharge = ((float) time-50.0F) / 20.0F;
                BlockPos end = livingEntity.blockPosition();
                if(!this.level().isClientSide){
                    Util.createExplosion(this,this.level(),end,1.0F + 4.0F * maxCharge);
                    this.level().broadcastEntityEvent(this,(byte) 4);
                }
            }
            this.discard();
        }
    }

    @Override
    public double getBaseDamage() {
        return super.getBaseDamage() + 4 * this.getTimeRecharge()/30.0F;
    }


    @Override
    public void handleEntityEvent(byte p_19882_) {
        if(p_19882_==4){
            for (int i = 0 ; i<3 ; i++){
                this.level().addParticle(BKParticles.ELECTRO_EXPLOSION_PARTICLES.get(),this.getX()+this.random.nextInt(-2,2),this.getY()+this.random.nextInt(0,2),this.getZ()+this.random.nextInt(-2,2),0.0F,0.0F,0.0F);
            }
            this.level().playLocalSound(this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL,20.0F,1.0f,false);

        }
        super.handleEntityEvent(p_19882_);
    }


    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
}
