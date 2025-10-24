package com.TBK.crc.server.entity;

import com.TBK.crc.client.animacion.RexChickenAnim;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.common.registry.BKSounds;
import com.TBK.crc.server.StructureData;
import com.TBK.crc.server.fight.CyberChickenFight;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketActionRex;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class RexChicken extends PathfinderMob implements PowerableMob{
    public static final EntityDataAccessor<Boolean> CHARGING =
            SynchedEntityData.defineId(RexChicken.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DEATH =
            SynchedEntityData.defineId(RexChicken.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(RexChicken.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> LASER =
            SynchedEntityData.defineId(RexChicken.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CIR_LASER =
            SynchedEntityData.defineId(RexChicken.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> SHIELD_AMOUNT =
            SynchedEntityData.defineId(RexChicken.class, EntityDataSerializers.INT);
    public AnimationState idle = new AnimationState();
    public AnimationState attack = new AnimationState();
    public AnimationState charge = new AnimationState();
    public AnimationState prepareCharge = new AnimationState();
    public AnimationState stunned = new AnimationState();
    public AnimationState recovery = new AnimationState();
    public AnimationState idleDeath = new AnimationState();
    public AnimationState death = new AnimationState();
    public Vec3 chargeDirection = Vec3.ZERO;
    public BlockPos lastBlockBeam = null;
    public BlockPos lastBlockPosBeamExplosion = null;
    public float rotHeadY = 0.0F;
    public float rotHeadY0 = 0.0F;
    public float rotHeadX = 0.0F;
    public float rotHeadX0 = 0.0F;
    public int idleAnimationTimeout = 0;
    public int attackTimer = 0;
    public int prepareChargeTimer = 0;
    public int chargeTimer = 0;
    public int cooldownCharge = 0;
    public int stunnedTick = 0;
    public int prepareLaser = 0;
    public int cooldownLaser = 0;
    public int restTime = 0;

    public int regenerationShieldTimer = 0;
    public float speedLaserModifier = 0.0F;
    public int deathAnimTime = 0;
    public int deathTime = 0;

    public int recoveryTimer = 0;
    public RexPart<?> head;
    public RexPart<?> body;
    public RexPart<?> towerMissile1;
    public RexPart<?> towerMissile2;
    public RexPart<?>[] towers;
    public RexPart<?>[] parts;
    public CyberChickenFight fight;
    public int hurtShield = 0;
    private boolean step1Played = false;
    private boolean step2Played = false;
    private boolean lift1Played = false;
    private boolean lift2Played = false;
    public RexChicken(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.head = new RexPart<RexChicken>(this,"head",2.0F,2.0F);
        this.body = new RexPart<RexChicken>(this,"body",8.0F,10.0F);
        this.towerMissile1 = new RexPart<RexChicken>(this,"towerMissile1",4.0F,4.0F,true);
        this.towerMissile2 = new RexPart<RexChicken>(this,"towerMissile2",4.0F,4.0F,true);
        this.parts = new RexPart[]{this.head,this.body,this.towerMissile1,this.towerMissile2};
        this.towers = new RexPart[]{this.towerMissile1,this.towerMissile2};
        this.setId(ENTITY_COUNTER.getAndAdd(this.parts.length + 1) + 1);
        this.setShieldAmount(0);
        if(!this.level().isClientSide){
            this.fight = StructureData.get().getCyberChickenFight();
        }else {
            this.fight = null;
        }
    }

    @Override
    public void setId(int p_20235_) {
        super.setId(p_20235_);
        for (int i = 0; i < this.parts.length; i++) // Forge: Fix MC-158205: Set part ids to successors of parent mob id
            this.parts[i].setId(p_20235_ + i + 1);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.FOLLOW_RANGE, 100.D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15d)
                .add(Attributes.ATTACK_DAMAGE,18.0D)
                .build();
    }


    public Entity getPartForId(int id){
        return Arrays.stream(this.parts).filter(e->e.getId()==id).findFirst().orElse(null);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5,new RandomStrollGoal(this,1.0D));
        this.goalSelector.addGoal(3,new AttackGoal(this,1.1D,false));
        this.goalSelector.addGoal(2, new ChargedGoal(this, 2.5D));
        this.targetSelector.addGoal(2,new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4,new NearestAttackableTargetGoal<>(this,Player.class,false));
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

    public Vec3 getHeadPos() {
        Vec3 base = this.head.position();
        double x = base.x;
        double y = base.y;
        double z = base.z;

        return new Vec3(x, y, z);
    }

    public Vec3 getBeamDirection(){
        Vec3 direction;
        if(this.lastBlockBeam!=null){
            direction = this.lastBlockBeam.getCenter().subtract(this.getHeadPos());
        }else{
            direction = viewHeadY().scale(100.0F);
        }

        return direction;
    }

    public Vec3 viewHeadY() {
        return this.calculateViewVector(this.rotHeadX,this.rotHeadY+this.getYRot());
    }

    private void tickPart(RexPart<?> p_31116_, double p_31117_, double p_31118_, double p_31119_) {
        p_31116_.setPos(this.getX() + p_31117_, this.getY() + p_31118_, this.getZ() + p_31119_);
        p_31116_.tick();
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if(this.level() instanceof ServerLevel serverLevel){
            p_21372_.hurt(Util.electricDamageMob(serverLevel,this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue());
        }
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isDeath() || this.deathAnimTime >0){
            if(this.deathAnimTime >0){

                this.deathAnimTime--;
                if(this.deathAnimTime ==0){
                    if(this.fight!=null){
                        this.fight.setDragonKilled(this);
                    }
                    this.setDeath(true);
                }
            }

            float f = (this.random.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + (double)f, this.getY() + 2.0D + (double)f1, this.getZ() + (double)f2, 0.0D, 0.0D, 0.0D);

            if(this.isDeath()){
                if(this.deathTime++>100){
                    ExperienceOrb.award((ServerLevel) this.level(), this.position(), 100);
                    this.discard();
                }
            }
            this.setDeltaMovement(0,this.getDeltaMovement().y,0);
        }else {
            this.rotHeadY0 = this.rotHeadY;
            this.rotHeadX0 = this.rotHeadX;
            float yawRad = (float)Math.toRadians(this.getYRot());
            float sin = (float)Math.sin(yawRad);
            float cos = (float)Math.cos(yawRad);

            double tower1X =  ( (-5) * cos) + ( 4 * sin);
            double tower1Y =  ( (-5) * sin) - ( 4 * cos);

            double tower2X =  ( (5) * cos) + ( 4* sin);
            double tower2Y =  ( (5) * sin) - ( 4* cos);

            double headX =  this.stunnedTick>0? -5*sin :-3*sin;
            double headY = this.stunnedTick>0 ? 2 : 7;
            double headZ =  this.stunnedTick>0 ? 5*cos : - ( -3 * cos);

            Vec3[] pos = new Vec3[this.parts.length];
            for (int j = 0 ; j<this.parts.length ; j++){
                pos[j]=new Vec3(this.parts[j].getX(), this.parts[j].getY(), this.parts[j].getZ());
            }
            this.tickPart(this.body,0,0,0);
            this.tickPart(this.head, headX,headY, headZ);
            this.tickPart(this.towerMissile1, tower1X,8,tower1Y);
            this.tickPart(this.towerMissile2, tower2X,8,tower2Y);

            for (int k = 0 ; k<this.parts.length ; k++){
                this.parts[k].xo = pos[k].x;
                this.parts[k].yo = pos[k].y;
                this.parts[k].zo = pos[k].z;
                this.parts[k].xOld = pos[k].x;
                this.parts[k].yOld = pos[k].y;
                this.parts[k].zOld = pos[k].z;
            }
            if(this.restTime > 0){
                this.restTime--;
            }

            this.tickHead();
            if(this.prepareLaser>0){
                this.getNavigation().stop();
                this.prepareLaser--;
                if(this.getTarget()!=null){
                    Vec3 vec32 = this.getTarget().getEyePosition().subtract(this.getHeadPos());
                    double f5 = -Math.toDegrees(Math.atan2(vec32.y,Math.sqrt(vec32.x*vec32.x + vec32.z*vec32.z)));
                    double f6 = Math.toDegrees(Math.atan2(vec32.z, vec32.x)) - 90.0F;
                    this.yHeadRot=(float)f6;
                    this.setYHeadRot((float) f6);
                    this.yBodyRot= (float) (f6);
                    this.setYRot((float) f6);
                    this.setXRot((float) f5);
                    this.setRot(this.getYRot(),this.getXRot());
                    if(this.isLaserSemiCir()){
                        this.rotHeadX = (float) f5;

                        this.rotHeadY = -60;
                    }else {
                        this.rotHeadX = 45;

                        this.rotHeadY = (float) f6-this.getYRot();
                    }
                }
                if(this.prepareLaser<=0 && !this.level().isClientSide){
                    this.setLaser(true);
                    this.level().broadcastEntityEvent(this,(byte) 18);
                }
            }
            if(this.recoveryTimer>0){
                this.recoveryTimer--;
                this.getNavigation().stop();
                if(this.recoveryTimer<=0){
                    if(!this.level().isClientSide){
                        this.level().broadcastEntityEvent(this,(byte) 16);
                    }
                }
            }
            if(this.stunnedTick>0){
                this.stunnedTick--;
                this.getNavigation().stop();
                if(this.stunnedTick>280){
                    Vec3 vec32 = this.chargeDirection;
                    double f5 = -Math.toDegrees(Math.atan2(vec32.y,Math.sqrt(vec32.x*vec32.x + vec32.z*vec32.z)));
                    double f6 = Math.toDegrees(Math.atan2(vec32.z, vec32.x)) - 90.0F;
                    this.yHeadRot=(float)f6;
                    this.setYHeadRot((float) f6);
                    this.yBodyRot= (float) (f6);
                    this.setYRot((float) f6);
                    this.setXRot((float) f5);
                    this.setRot(this.getYRot(),this.getXRot());
                    this.setDeltaMovement(this.chargeDirection.multiply(-1,1,-1));
                    this.chargeDirection=this.chargeDirection.scale(0.8);
                    boolean flag = false;
                    AABB aabb = this.getBoundingBox().inflate(1D);

                    for(BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
                        BlockState blockstate = this.level().getBlockState(blockpos);
                        if (blockstate.is(Blocks.SEA_LANTERN) || blockstate.is(Blocks.POLISHED_DEEPSLATE) || blockstate.is(Blocks.REDSTONE_LAMP)) {
                            flag = this.level().destroyBlock(blockpos, true, this) || flag;
                        }
                    }
                }else {
                    if(this.isPowered()){
                        this.stunnedTick=0;
                    }
                    this.setDeltaMovement(0,this.getDeltaMovement().y,0);
                }
                if(this.stunnedTick<=0){
                    if(!this.level().isClientSide){
                        this.recoveryTimer=21;
                        this.level().broadcastEntityEvent(this,(byte) 17);
                    }
                }
            }else if(this.regenerationShieldTimer>0){
                this.regenerationShieldTimer--;
                if(this.regenerationShieldTimer<=0){
                    if(!this.level().isClientSide){
                        this.level().broadcastEntityEvent(this,(byte) 32);
                        this.setShieldAmount(50);
                    }
                }
            }
            if(this.cooldownLaser>0){
                this.cooldownLaser--;
            }
            if(this.cooldownCharge>0){
                this.cooldownCharge--;
            }
            if(this.prepareChargeTimer>0){
                this.getNavigation().stop();
                this.setDeltaMovement(Vec3.ZERO);
                this.prepareChargeTimer--;
                if(this.getTarget()!=null){
                    this.chargeDirection = this.getTarget().position().subtract(this.position()).normalize().multiply(1,0,1);
                    if(!this.level().isClientSide){
                        Vec3 vec32 = this.chargeDirection;
                        double f5 = -Math.toDegrees(Math.atan2(vec32.y,Math.sqrt(vec32.x*vec32.x + vec32.z*vec32.z)));
                        double f6 = Math.toDegrees(Math.atan2(vec32.z, vec32.x)) - 90.0F;
                        this.yHeadRot=(float)f6;
                        this.setYHeadRot((float) f6);
                        this.yBodyRot=(float) (f6);
                        this.setYRot((float) f6);
                        this.setXRot((float) f5);
                        this.setRot(this.getYRot(),this.getXRot());
                    }
                }
                if(this.prepareChargeTimer<=0){
                    if(this.getTarget()!=null){
                        this.setCharging(true);
                        if (this.level().isClientSide){
                            this.charge.start(this.tickCount);
                        }
                    }
                }
            }

            if(this.isCharging()){
                this.chargeTimer++;
                if(!this.level().isClientSide){
                    Vec3 vec32 = this.chargeDirection;
                    double f5 = -Math.toDegrees(Math.atan2(vec32.y,Math.sqrt(vec32.x*vec32.x + vec32.z*vec32.z)));
                    double f6 = Math.toDegrees(Math.atan2(vec32.z, vec32.x)) - 90.0F;
                    this.yHeadRot=(float)f6;
                    this.setYHeadRot((float) f6);
                    this.yBodyRot=(float) (f6);
                    this.setYRot((float) f6);
                    this.setXRot((float) f5);
                    this.setRot(this.getYRot(),this.getXRot());

                }

                this.level().getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().inflate(3.0F),e->!this.is(e) && !(e instanceof RobotChicken)).forEach(e->{
                    this.doHurtTarget(e);
                    e.push(this);
                });
                if(this.chargeTimer>40){
                    this.setCharging(false);
                    if(this.level().isClientSide){
                        this.charge.stop();
                    }else {
                        if(this.level().random.nextBoolean()){
                            this.restTime = (int) (50 + 50*this.level().random.nextFloat());
                        }
                    }
                    this.chargeTimer = 0;
                    this.cooldownCharge = 100;

                }
                if (!this.level().isClientSide && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                    boolean flag = false;
                    AABB aabb = this.getBoundingBox().inflate(1D);

                    for(BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
                        BlockState blockstate = this.level().getBlockState(blockpos);
                        if (blockstate.is(Blocks.SEA_LANTERN) || blockstate.is(Blocks.POLISHED_DEEPSLATE) || blockstate.is(Blocks.REDSTONE_LAMP)) {
                            flag = this.level().destroyBlock(blockpos, true, this) || flag;
                        }
                    }

                    if (!flag){
                        if(this.horizontalCollision){
                            this.stunnedTick=300;
                            this.setCharging(false);
                            if(!this.level().isClientSide && !this.isPowered()){
                                this.level().broadcastEntityEvent(this,(byte) 12);
                                this.playSound(BKSounds.REX_CRASH.get(),6.0F,1.0F);
                            }
                            this.chargeTimer = 0;
                            this.cooldownCharge = 100;
                        }
                    }
                }

            }
            if(this.isAttacking()){
                this.attackTimer--;
                if(this.attackTimer==0 ){
                    this.level().getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().inflate(4.0F),e->!this.is(e) && !(e instanceof RobotChicken)).forEach(this::doHurtTarget);
                    this.setAttacking(false);
                    this.playSound(BKSounds.REX_STOMP.get(),5.0F,1.0f);
                    for (BlockPos pos2 : BlockPos.betweenClosed(this.getOnPos().offset(5,1,5),this.getOnPos().offset(-5,-1,-5))){
                        if(!this.level().getBlockState(pos2).isAir()){
                            float entityHitDistance = Math.max((float) Math.sqrt((pos2.getZ() - this.getZ()) * (pos2.getZ() - this.getZ()) + (pos2.getX() - this.getX()) * (pos2.getX() - this.getX())),0);
                            if (entityHitDistance <= 5 && entityHitDistance >=2) {
                                Random random1 = new Random();
                                double distance = 0.12F*Math.ceil(entityHitDistance) + random1.nextFloat(0.0F,1.0F);
                                BlockPos.MutableBlockPos pos1 = pos2.mutable();
                                boolean canSummon=true;
                                for (int i=0;i<Mth.ceil(distance);i++){
                                    if(canSummon && !this.level().getBlockState(pos1.above()).isAir()){
                                        canSummon=false;
                                    }
                                }
                                if(canSummon){
                                    for(int j=0;j<this.level().random.nextInt(2,3);j++){
                                        Minecraft.getInstance().particleEngine.crack(pos2, Direction.UP);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(this.level().isClientSide){
                this.setupAnimationStates();
            }

            while(this.rotHeadX - this.rotHeadX0 < -180.0F) {
                this.rotHeadX0 -= 360.0F;
            }

            while(this.rotHeadX - this.rotHeadX0 >= 180.0F) {
                this.rotHeadX0 += 360.0F;
            }
            while(this.rotHeadY - this.rotHeadY0 < -180.0F) {
                this.rotHeadY0 -= 360.0F;
            }

            while(this.rotHeadY - this.rotHeadY0 >= 180.0F) {
                this.rotHeadY0 += 360.0F;
            }

            if(this.fight!=null){
                this.fight.updateDragon(this);
            }
            if(this.level().isClientSide){
                if(!this.isCharging()){
                    this.tickStep();
                }
            }
        }

    }

    public void tickStep(){
        float animationProgress = this.walkAnimation.position();

        long time = (long)(animationProgress * 50.0F * 2.0F);

        float seconds = Util.getElapsedSeconds(RexChickenAnim.walklegs,time);

        if (!step1Played && seconds >= 0.92F) {
            level().playLocalSound(getX(), getY(), getZ(),
                    BKSounds.REX_STEP2.get(), SoundSource.HOSTILE, 4.0F, 1.0F,false);
            step1Played = true;
        }

        if (!step2Played && seconds >= 1.63F) {
            level().playLocalSound(getX(), getY(), getZ(),
                    BKSounds.REX_STEP2.get(), SoundSource.HOSTILE, 4.0F, 1.0F,false);
            step2Played = true;
        }

        if (!lift1Played && seconds >= 0.21F) {
            level().playLocalSound(getX(), getY(), getZ(),
                    BKSounds.REX_STEP1.get(), SoundSource.HOSTILE, 4.0F, 1.0F,false);
            lift1Played = true;
        }

        if (!lift2Played && seconds >= 1.000F) {
            level().playLocalSound(getX(), getY(), getZ(),
                    BKSounds.REX_STEP1.get(), SoundSource.HOSTILE, 4.0F, 1.0F,false);
            lift2Played = true;
        }

        if (seconds<0.200F) {
            step1Played = false;
            step2Played = false;
            lift1Played = false;
            lift2Played = false;
        }
    }


    public void tickHead(){
        if(this.isLaser()){
            this.setDeltaMovement(Vec3.ZERO);
            this.setPos(this.position());
            if(this.isLaserSemiCir()){
                this.speedLaserModifier= Math.min(10,this.speedLaserModifier+(this.isRage() ? 0.5F :0.25F));
                this.rotHeadY = Mth.clamp(lerpRotation(this.rotHeadY,(float) this.rotHeadY+this.speedLaserModifier,10.0F),-60,60);
                if(this.rotHeadY>=60.0F){
                    this.setLaser(false);
                    this.setCirLaser(false);
                    this.cooldownLaser=200;
                    this.lastBlockPosBeamExplosion = null;
                    this.lastBlockBeam = null;
                    if(!this.level().isClientSide){
                        if(this.level().random.nextBoolean()){
                            this.restTime = (int) (50 + 50*this.level().random.nextFloat());
                        }
                        this.level().broadcastEntityEvent(this,(byte) 33);
                    }
                }
            }else {
                this.speedLaserModifier= Math.min(10,this.speedLaserModifier+(this.isRage() ? 0.2F :0.1F));
                this.rotHeadX = Mth.clamp(lerpRotation(this.rotHeadX,(float) this.rotHeadX-this.speedLaserModifier,5.0F),0,45);
                if(this.rotHeadX<=0.0F){
                    this.setLaser(false);
                    this.setCirLaser(true);
                    this.cooldownLaser=200;
                    this.lastBlockPosBeamExplosion = null;
                    this.lastBlockBeam = null;
                    if(!this.level().isClientSide){
                        if(this.level().random.nextBoolean()){
                            this.restTime = (int) (50 + 50*this.level().random.nextFloat());
                        }
                        this.level().broadcastEntityEvent(this,(byte) 33);
                    }
                }
            }
            if(this.rotHeadY == this.rotHeadY0 && this.rotHeadX == this.rotHeadX0){
                this.applyLaserEffect(this.viewHeadY());
            }else{
                float diffY = Mth.abs(this.rotHeadY - this.rotHeadY0);
                float diffX = Mth.abs(this.rotHeadX - this.rotHeadX0);
                float maxDiff = Math.max(diffX,diffY);
                float rotExtra = 0.0F;
                while (rotExtra<maxDiff){
                    this.applyLaserEffect(this.calculateViewVector(this.rotHeadX0-Math.min(rotExtra,diffX),this.rotHeadY0 + this.getYRot() + Math.min(rotExtra,diffY)));
                    rotExtra+=1.0F;
                }
            }
        }

        if(this.isCharging() && this.isRage()){
            Vec3 vec32 = this.getDeltaMovement();
            double f5 = -Math.toDegrees(Math.atan2(vec32.y,Math.sqrt(vec32.x*vec32.x + vec32.z*vec32.z)));
            double f6 = Math.toDegrees(Math.atan2(vec32.z, vec32.x)) - 90.0F;
            this.yHeadRot=(float)f6;
            this.setYHeadRot((float) f6);
            this.yBodyRot= (float) (f6);
            this.setYRot((float) f6);
            this.setXRot((float) f5);
            this.setRot(this.getYRot(),this.getXRot());
            this.rotHeadX = 45;


            this.rotHeadY = (float) f6-this.getYRot();

            this.applyLaserEffect(this.viewHeadY());

        }
    }

    private boolean isRage() {
        return this.getHealth()<=this.getMaxHealth()*0.5F;
    }

    @Override
    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
    }

    public void applyLaserEffect(Vec3 view){
        Vec3 origin = this.getHeadPos();
        BlockHitResult blockEnd = this.level().clip(new ClipContext(origin,origin.add(view.scale(100.0D)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,this));
        List<EntityHitResult> hits = this.getBeamEntityHitResult(this.level(),this,origin,origin.add(view.scale(100.0D)),this.getBoundingBox().inflate(100.0F), e->!this.is(e) && !(e instanceof RobotChicken),1.5F);

        if(this.level() instanceof ServerLevel serverLevel){
            if(hits!=null){
                for (EntityHitResult hit : hits){
                    if(hit.getEntity() instanceof LivingEntity entity){
                        if(entity.hurt(Util.electricDamageMob(serverLevel,this),4.0F)){
                            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,3,10));
                        }
                    }
                }
            }
        }

        if(blockEnd.getType() != HitResult.Type.MISS){
            this.lastBlockBeam = blockEnd.getBlockPos();
        }else {
            this.lastBlockBeam = null;
        }

        if(!level().getBlockState(blockEnd.getBlockPos()).isAir() && (this.lastBlockPosBeamExplosion == null || Mth.sqrt((float) blockEnd.getBlockPos().distToCenterSqr(this.lastBlockPosBeamExplosion.getCenter())) > 2.0F)){
            BlockPos end = blockEnd.getBlockPos();
            this.lastBlockPosBeamExplosion = end;
            if(!this.level().isClientSide){
                BeamExplosionEntity beamCracking = new BeamExplosionEntity(BKEntityType.CRACKING_BEAM.get(),this.level(),this);
                beamCracking.setPos(end.getCenter());
                this.level().addFreshEntity(beamCracking);
            }
        }
    }
    public  List<EntityHitResult> getBeamEntityHitResult(Level p_150176_, Entity p_150177_, Vec3 p_150178_, Vec3 p_150179_, AABB p_150180_, Predicate<Entity> p_150181_, float p_150182_) {
        double d0 = Double.MAX_VALUE;
        List<EntityHitResult> results = new ArrayList<>();
        for(Entity entity1 : p_150176_.getEntities(p_150177_, p_150180_, p_150181_)) {
            AABB aabb = entity1.getBoundingBox().inflate((double)p_150182_);
            Optional<Vec3> optional = aabb.clip(p_150178_, p_150179_);
            if (optional.isPresent()) {
                results.add(new EntityHitResult(entity1));
            }
        }
        return results.isEmpty() ? null : results;
    }

    private float lerpRotation(float currentYaw, float targetYaw, float maxTurnSpeed) {
        float deltaYaw = Mth.wrapDegrees(targetYaw - currentYaw);

        float clampedDelta = Mth.clamp(deltaYaw, -maxTurnSpeed, maxTurnSpeed);

        return currentYaw + clampedDelta;
    }

    public boolean isLaserSemiCir() {
        return this.entityData.get(CIR_LASER);
    }

    public void setCirLaser(boolean flag){
        this.entityData.set(CIR_LASER,flag);
    }

    public boolean isLaser() {
        return this.entityData.get(LASER);
    }
    public void setLaser(boolean flag){
        this.entityData.set(LASER,flag);
        if(flag){
            this.speedLaserModifier = 0;
        }
    }

    @Override
    protected InteractionResult mobInteract(Player p_21472_, InteractionHand p_21473_) {
        return super.mobInteract(p_21472_, p_21473_);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
        if (!this.isCharging()) {
            super.doPush(entity);
        }
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        if (this.isCharging()) {
            return false;
        }
        return super.canCollideWith(entity);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LASER,false);
        this.entityData.define(CIR_LASER,false);
        this.entityData.define(ATTACKING,false);
        this.entityData.define(CHARGING,false);
        this.entityData.define(SHIELD_AMOUNT,0);
        this.entityData.define(DEATH,false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putInt("shieldAmount",this.getShieldAmount());
        for (RexPart<?> part : this.towers){
            part.addAdditionalSaveData(p_21484_);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.setShieldAmount(p_21450_.getInt("shieldAmount"));
        for (RexPart<?> part : this.towers){
            part.readAdditionalSaveData(p_21450_);
        }
    }

    private void setupAnimationStates() {
        if(this.hurtShield>0){
            this.hurtShield--;
        }
        if(!this.isCharging() && !this.isDeath()){
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = 83;
                this.idle.start(this.tickCount);
                this.attack.stop();
                this.prepareCharge.stop();
            } else {
                --this.idleAnimationTimeout;
            }
        }else {
            this.idle.stop();
            this.idleAnimationTimeout = 0;
        }
        this.idleDeath.animateWhen(this.isDeath(),this.tickCount);
        this.charge.animateWhen(this.isCharging(),this.tickCount);
    }
    public boolean isDeath() {
        return this.entityData.get(DEATH);
    }
    public void setDeath(boolean flag){
        this.entityData.set(DEATH,flag);
    }
    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }
    public void setAttacking(boolean flag){
        this.entityData.set(ATTACKING,flag);
        this.attackTimer = flag ? 40 : 0;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        this.setShieldAmount(50);
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
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
    public void die(DamageSource p_21014_) {
        super.die(p_21014_);

        if(!this.level().isClientSide){
            this.deathAnimTime =26;
            this.level().broadcastEntityEvent(this,(byte) 9);
        }

        this.setHealth(1.0F);
    }

    @Override
    public void checkDespawn() {

    }

    @Override
    public void kill() {
        super.kill();
        this.discard();
        if(this.fight!=null){
            this.fight.setDragonKilled(this);
        }
    }

    @Override
    public boolean isInvulnerable() {
        return super.isInvulnerable() || this.isDeath() || this.deathAnimTime >0;
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if(p_21375_ == 4){
            this.idle.stop();
            this.attack.start(this.tickCount);
            this.idleAnimationTimeout = 40;
        }else if(p_21375_ == 8){
            this.idle.stop();
            this.level().playLocalSound(this.blockPosition(), BKSounds.REX_START_CHARGE.get(), SoundSource.HOSTILE,5.0F,1.0F,false);
            this.prepareCharge.start(this.tickCount);
            this.prepareChargeTimer=23;
            this.idleAnimationTimeout=23;
        }else if(p_21375_ == 9){
            this.idle.stop();
            this.stunned.stop();
            this.charge.stop();
            this.recovery.stop();
            this.idleAnimationTimeout = 26;
            this.death.start(this.tickCount);
            this.deathAnimTime = 26;
        }else if(p_21375_ == 12){
            this.charge.stop();
            this.idle.stop();
            this.idleAnimationTimeout = 1000;
            this.stunnedTick=300;
            this.stunned.start(this.tickCount);
        }else if(p_21375_ == 16){
            this.recovery.stop();
        }else if(p_21375_ == 17){
            this.stunned.stop();
            this.idle.stop();
            this.idleAnimationTimeout=21;
            this.recovery.start(this.tickCount);
            this.stunnedTick=0;
            this.recoveryTimer=21;
        }else if(p_21375_ == 18){
            this.level().playLocalSound(this.blockPosition(),BKSounds.REX_LASER_SHOOT.get(),SoundSource.HOSTILE,4.0F,1.0F,false);
            this.setLaser(true);
        }else if(p_21375_ == 19){
            this.prepareLaser=23;
        }else if(p_21375_ == 32){
            level().playLocalSound(this.blockPosition(),BKSounds.REX_SHIELD_ON.get(),SoundSource.HOSTILE,10,1.0f,false);
            this.setShieldAmount(50);
        }else if(p_21375_ == 33){
            this.lastBlockPosBeamExplosion = null;
            this.lastBlockBeam = null;
        }else if(p_21375_ == 34){
            this.hurtShield = 10;
        }
        super.handleEntityEvent(p_21375_);
    }

    private void initLaser() {
        if(!this.level().isClientSide){
            this.prepareLaser=23;
            this.level().broadcastEntityEvent(this,(byte) 19);
            if(this.getTarget()!=null){
                PacketHandler.sendToAllTracking(new PacketActionRex(this.getId(),this.getTarget().getId(),0),this);
            }
        }
    }

    private void initCharge() {
        if(!this.level().isClientSide){
            this.prepareChargeTimer=23;
            this.level().broadcastEntityEvent(this,(byte) 8);
        }
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_218825_) {
        super.recreateFromPacket(p_218825_);
        if (true) return;
        RexPart<?>[] part = this.parts;


        for(int i = 0; i < part.length; ++i) {
            part[i].setId(i + p_218825_.getId());
        }

    }


    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        p_21017_= net.minecraftforge.common.ForgeHooks.onLivingHurt(this, p_21016_, p_21017_);

        if(this.isPowered()){
            int healthShieldActually = this.getShieldAmount();

            if ((!p_21016_.is(Util.ELECTRIC_DAMAGE_PLAYER) && !p_21016_.is(Util.ELECTRIC_DAMAGE_MOB))) {
                p_21017_ *= 0.3F;
            }

            if(healthShieldActually<=p_21017_){
                this.setShieldAmount(0);
                this.regenerationShieldTimer = 300;
                this.level().playSound(null,this.getX(),this.getY(),this.getZ(),BKSounds.REX_SHIELD_OFF.get(),SoundSource.HOSTILE,4.0F,1.0F);
            }else {
                this.setShieldAmount((int) (healthShieldActually-p_21017_));
                this.level().broadcastEntityEvent(this,(byte) 34);
            }
            return false;
        }

        if(this.getHealth()>1){
            this.invulnerableTime = 10;
            return super.hurt(p_21016_,Math.min(p_21017_,3.0F));
        }
        return false;
    }

    @Override
    public @Nullable PartEntity<?>[] getParts() {
        return this.parts;
    }


    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    public int getShieldAmount(){
        return this.entityData.get(SHIELD_AMOUNT);
    }

    public void setShieldAmount(int amount){
        this.entityData.set(SHIELD_AMOUNT,amount);
    }

    @Override
    public boolean isPowered() {
        return this.getShieldAmount()>0;
    }

    class AttackGoal extends MeleeAttackGoal {

        public AttackGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_) {
            super(p_25552_, p_25553_, p_25554_);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && RexChicken.this.recoveryTimer<=0 && RexChicken.this.stunnedTick<=0 && !RexChicken.this.isLaser() && RexChicken.this.prepareLaser<=0 && !RexChicken.this.isCharging() && RexChicken.this.prepareChargeTimer<=0;
        }


        @Override
        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.getTicksUntilNextAttack()<=0 && RexChicken.this.attackTimer<=0) {
                this.resetAttackCooldown();
                this.mob.getNavigation().stop();
                RexChicken.this.restTime = 0;
            }else  if(RexChicken.this.restTime<=0){
                if(!this.mob.level().isClientSide){
                    if(RexChicken.this.cooldownCharge<=0){
                        RexChicken.this.initCharge();
                    }else if(RexChicken.this.cooldownLaser<=0){
                        ((RexChicken)this.mob).initLaser();
                    }
                }
            }
        }


        @Override
        protected void resetAttackCooldown() {
            super.resetAttackCooldown();
            RexChicken.this.setAttacking(true);
            if(!RexChicken.this.level().isClientSide){
                RexChicken.this.playAttack();
            }
        }
    }


    static class ChargedGoal extends Goal {
        private final RexChicken rex;
        private final double speedModifier;
        private Vec3 targetDirection;
        public ChargedGoal(RexChicken p_37936_, double p_37937_) {
            this.rex = p_37936_;
            this.speedModifier = p_37937_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return this.rex.isCharging();
        }

        public void start() {
            super.start();
            if(this.rex.getTarget()!=null){
                this.targetDirection = this.rex.chargeDirection.normalize().add(triangle(0.0D, 0.0172275D * (double)0.1f, rex.level().random), triangle(0.0D, 0.0172275D * (double)0.1F, rex.level().random), triangle(0.0D, 0.0172275D * (double)0.1F, rex.level().random)).scale((double)this.speedModifier);
                this.rex.setDeltaMovement(this.targetDirection);
            }
        }

        public void tick() {
            if(this.targetDirection!=null){
                this.targetDirection = this.targetDirection.scale(1F);
                this.rex.setDeltaMovement(this.targetDirection);
            }
        }
        public double triangle(double p_216329_, double p_216330_, RandomSource random) {
            return p_216329_ + p_216330_ * (random.nextDouble() - random.nextDouble());
        }
    }
}
