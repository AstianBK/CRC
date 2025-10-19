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
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(CoilChicken.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_LAUNCH = SynchedEntityData.defineId(CoilChicken.class, EntityDataSerializers.BOOLEAN);
    public AnimationState idle = new AnimationState();
    public AnimationState stand = new AnimationState();
    public AnimationState attack = new AnimationState();

    public int idleAnimationTimeout = 0;

    public int standTimer = 0;
    public CoilChicken(Level p_21684_) {
        super(BKEntityType.COIL_CHICKEN.get(), p_21684_);
    }

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
        this.entityData.define(DATA_SWELL_DIR,-1);
        this.entityData.define(IS_LAUNCH,false);
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

            this.idleAnimationTimeout = 18;
            this.stand.start(this.tickCount);
            this.standTimer = 18;
        }else if(p_21375_ == 8){
            for (int i = 0 ; i<3 ; i++){
                this.level().addParticle(BKParticles.ELECTRO_EXPLOSION_PARTICLES.get(),this.getX()+this.random.nextInt(-2,2),this.getY()+this.random.nextInt(0,2),this.getZ()+this.random.nextInt(-2,2),0.0F,0.0F,0.0F);
            }
            this.level().playLocalSound(this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL,20.0F,1.0f,false);

        }
        super.handleEntityEvent(p_21375_);
    }

}
