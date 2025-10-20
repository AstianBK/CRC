package com.TBK.crc.server.entity;

import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.common.registry.BKParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class CoilChicken extends RobotChicken {
    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(CoilChicken.class, EntityDataSerializers.BOOLEAN);

    public AnimationState idle = new AnimationState();
    public AnimationState stand = new AnimationState();
    public AnimationState attack = new AnimationState();

    public int idleAnimationTimeout = 0;

    private int attackTimer = 0;


    public CoilChicken(EntityType<CoilChicken> CoilChickenEntityType, Level level) {
        super(CoilChickenEntityType,level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false){
            @Override
            public boolean canUse() {
                return super.canUse() && CoilChicken.this.onGround();
            }
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING,false);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 45.D)
                .add(Attributes.MOVEMENT_SPEED, 0.28d)
                .add(Attributes.ATTACK_DAMAGE, 3.0d)
                .build();
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


    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 10;
            this.idle.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

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
    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }
    public void setAttacking(boolean flag){
        this.entityData.set(ATTACKING,flag);
        this.attackTimer = flag ? 20 : 0;
    }
    public void playAttack(){
        this.level().broadcastEntityEvent(this,(byte) 4);
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
            if (p_25558_ <= d0 && this.getTicksUntilNextAttack()<=0 && CoilChicken.this.attackTimer<=0) {
                this.resetAttackCooldown();
                this.mob.getNavigation().stop();
            }
        }


        @Override
        protected void resetAttackCooldown() {
            super.resetAttackCooldown();
            CoilChicken.this.setAttacking(true);
            if(!CoilChicken.this.level().isClientSide){
                CoilChicken.this.playAttack();
            }
        }
    }
}
