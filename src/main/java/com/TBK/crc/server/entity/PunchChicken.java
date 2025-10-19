package com.TBK.crc.server.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;

public class PunchChicken extends PathfinderMob {
    public static final EntityDataAccessor<Boolean> CHARGING =
            SynchedEntityData.defineId(PunchChicken.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(PunchChicken.class, EntityDataSerializers.BOOLEAN);

    public AnimationState idle = new AnimationState();
    public AnimationState attack = new AnimationState();
    public AnimationState charge = new AnimationState();
    public int idleAnimationTimeout = 0;
    public int attackTimer = 0;
    public PunchChicken(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }
    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 45.D)
                .add(Attributes.MOVEMENT_SPEED, 0.3d)
                .add(Attributes.ATTACK_DAMAGE,12.0D)
                .build();

    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5,new RandomStrollGoal(this,1.0D));
        this.goalSelector.addGoal(3,new AttackGoal(this,1.1D,false));
        this.targetSelector.addGoal(4,new NearestAttackableTargetGoal<>(this,LivingEntity.class,false));
    }

    protected void updateWalkAnimation(float p_268362_) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(p_268362_ * 6.0F, 1.0F);
        } else {
            this.idleAnimationTimeout=1;
            this.idle.stop();
            f = 0.0F;
        }

        this.walkAnimation.update(f, 0.2F);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isAttacking()){
            this.attackTimer--;
            if(this.attackTimer<=0 ){
                if(this.getTarget()!=null){
                    this.doHurtTarget(this.getTarget());
                }
                this.setAttacking(false);
            }
        }
        if(this.level().isClientSide){
            this.setupAnimationStates();
        }
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING,false);
        this.entityData.define(CHARGING,false);
    }
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 10;
            this.idle.start(this.tickCount);
            this.attack.stop();
            this.charge.stop();
        } else {
            --this.idleAnimationTimeout;
        }

        this.charge.animateWhen(this.isCharging(),this.tickCount);
    }
    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }
    public void setAttacking(boolean flag){
        this.entityData.set(ATTACKING,flag);
        this.attackTimer = flag ? 20 : 0;
    }
    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }
    public void setCharging(boolean flag){
        this.entityData.set(CHARGING,flag);
    }

    public void playAttack(){
        this.level().broadcastEntityEvent(this,(byte) 4);
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if(p_21375_ == 4){
            this.idle.stop();
            this.attack.start(this.tickCount);
            this.idleAnimationTimeout = 10;
        }
        super.handleEntityEvent(p_21375_);
    }

    class AttackGoal extends MeleeAttackGoal {

        public AttackGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_) {
            super(p_25552_, p_25553_, p_25554_);
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.getTicksUntilNextAttack()<=0 && PunchChicken.this.attackTimer<=0) {
                this.resetAttackCooldown();
                this.mob.getNavigation().stop();
            }
        }


        @Override
        protected void resetAttackCooldown() {
            super.resetAttackCooldown();
            PunchChicken.this.setAttacking(true);
            if(!PunchChicken.this.level().isClientSide){
                PunchChicken.this.playAttack();
            }
        }
    }
}
