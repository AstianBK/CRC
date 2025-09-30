package com.TBK.crc.server.entity;

import com.TBK.crc.common.registry.BKEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BoomChicken extends PathfinderMob {
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(BoomChicken.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_LAUNCH = SynchedEntityData.defineId(BoomChicken.class, EntityDataSerializers.BOOLEAN);
    public AnimationState idle = new AnimationState();
    public AnimationState air = new AnimationState();
    public AnimationState stand = new AnimationState();
    public int idleAnimationTimeout = 0;
    private int oldSwell;
    private int swell;
    private int maxSwell = 30;
    private int explosionRadius = 3;
    public int standTimer = 0;
    public BoomChicken(Level p_21684_) {
        super(BKEntityType.BOOM_CHICKEN.get(), p_21684_);
    }

    public BoomChicken(EntityType<BoomChicken> boomChickenEntityType, Level level) {
        super(boomChickenEntityType,level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false){
            @Override
            public boolean canUse() {
                return super.canUse() && BoomChicken.this.onGround();
            }
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public boolean shouldDiscardFriction() {
        return this.isLaunch();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL_DIR,-1);
        this.entityData.define(IS_LAUNCH,false);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 45.D)
                .add(Attributes.MOVEMENT_SPEED, 0.3d)
                .build();
    }

    public void calculateRotPosition(BlockPos from, BlockPos to) {
        double g = 0.08;

        double dx = (to.getX() + 0.5) - (from.getX() + 0.5);
        double dy = to.getY() - from.getY();
        double dz = (to.getZ() + 0.5) - (from.getZ() + 0.5);


        int ticks = 70;

        double vx = dx / ticks;
        double vz = dz / ticks;
        double vy = (dy + 0.5 * g * ticks * ticks) / ticks;

        this.updateRot(new Vec3(vx,vy,vz),true);
    }
    private float lerpRotation(float currentYaw, float targetYaw, float maxTurnSpeed) {
        float deltaYaw = Mth.wrapDegrees(targetYaw - currentYaw);

        float clampedDelta = Mth.clamp(deltaYaw, -maxTurnSpeed, maxTurnSpeed);

        return currentYaw + clampedDelta;
    }
    public void updateRot(Vec3 vec3,boolean flag){
        if (flag) {
            this.setYRot((float)(Mth.atan2(-vec3.x, -vec3.z) * (double)(180F / (float)Math.PI)));
        } else {
            this.setYRot(lerpRotation(this.getYRot(),(float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)),5.0F));
        }

        this.setXRot(lerpRotation(this.getXRot(),(float)(Mth.atan2(vec3.y, vec3.horizontalDistance()) * (double)(180F / (float)Math.PI)),5.0F));
    }

    public Vec3 calculateJumpVelocity(BlockPos from, BlockPos to) {
        double g = 0.08;
        double dx = (to.getX() + 0.5) - (from.getX() + 0.5);
        double dy = to.getY() - from.getY();
        double dz = (to.getZ() + 0.5) - (from.getZ() + 0.5);

        //double horizontalDist = Math.sqrt(dx * dx + dz * dz);

        //double vHoriz = 0.6;

        int ticks = 40;

        double vx = dx / ticks;
        double vz = dz / ticks;
        double vy = (dy + 0.5 * g * ticks * ticks) / ticks;

        //int tickAltura = Mth.ceil(vy / g);
        //this.maxHeight = (vy * vy) / (2 * g);
        //this.maxTickAltura = tickAltura+this.tickCount+Mth.ceil(tickAltura*0.25F);

        return new Vec3(vx, vy, vz);
    }

    @Override
    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {
        if(!this.isLaunch()){
            super.checkFallDamage(p_20990_, p_20991_, p_20992_, p_20993_);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            this.oldSwell = this.swell;

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
            if(this.standTimer>0){
                this.standTimer--;
                this.setDeltaMovement(Vec3.ZERO);
                if(this.standTimer <= 0){
                    if(this.level().isClientSide){
                        this.stand.stop();
                    }
                }
            }
            if(this.isLaunch()){
                if(this.onGround()){
                    this.standTimer = 18;
                    this.setIsLaunch(false);
                    if(!this.level().isClientSide){
                        this.level().broadcastEntityEvent(this,(byte) 4);
                    }
                }
            }

        }
        if(this.level().isClientSide){
            this.setupAnimationStates();
        }
    }
    private void explodeCreeper() {
        if (!this.level().isClientSide) {
            float f =  1.0F;
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, Level.ExplosionInteraction.MOB);
            this.discard();
        }

    }
    public boolean isLaunch(){
        return this.entityData.get(IS_LAUNCH);
    }
    public void setIsLaunch(boolean isLaunch){
        this.entityData.set(IS_LAUNCH,isLaunch);
    }
    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int p_32284_) {
        this.entityData.set(DATA_SWELL_DIR, p_32284_);
    }
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 10;
            this.idle.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        this.air.animateWhen(!this.onGround(),this.tickCount);
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if(p_21375_ == 4){
            this.idle.stop();
            this.air.stop();
            this.idleAnimationTimeout = 18;
            this.stand.start(this.tickCount);
            this.standTimer = 18;
            this.setIsLaunch(false);
        }
        super.handleEntityEvent(p_21375_);
    }

    static class SwellGoal extends Goal {
        private final BoomChicken creeper;
        @Nullable
        private LivingEntity target;

        public SwellGoal(BoomChicken p_25919_) {
            this.creeper = p_25919_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.creeper.getTarget();
            return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity) < 9.0D;
        }

        public void start() {
            this.creeper.getNavigation().stop();
            this.target = this.creeper.getTarget();
        }

        public void stop() {
            this.target = null;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.target == null) {
                this.creeper.setSwellDir(-1);
            } else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
                this.creeper.setSwellDir(-1);
            } else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
                this.creeper.setSwellDir(-1);
            } else {
                this.creeper.setSwellDir(1);
            }
        }
    }
}
