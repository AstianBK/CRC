package com.TBK.crc.server.entity;

import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.common.registry.BKParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DroneChicken extends RobotChicken {
    public AnimationState idle = new AnimationState();
    public AnimationState air = new AnimationState();
    public AnimationState stand = new AnimationState();
    public int idleAnimationTimeout = 0;
    public int standTimer = 0;
    public DroneChicken(Level p_21684_) {
        super(BKEntityType.DRONE_CHICKEN.get(), p_21684_);
    }

    public DroneChicken(EntityType<DroneChicken> type, Level level) {
        super(type,level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }


    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));

        this.goalSelector.addGoal(2,new DroneAttack(this,0.75F,6.0F,3,7));
        this.goalSelector.addGoal(4,new DroneFlyGoal(this,0.20F,4,10));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }



    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 45.D)
                .add(Attributes.MOVEMENT_SPEED, 0.3d)
                .build();
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


    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide){
            this.setupAnimationStates();
        }
    }
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
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
            //this.setIsLaunch(false);
        }else if(p_21375_ == 8){
            for (int i = 0 ; i<3 ; i++){
                this.level().addParticle(BKParticles.ELECTRO_EXPLOSION_PARTICLES.get(),this.getX()+this.random.nextInt(-2,2),this.getY()+this.random.nextInt(0,2),this.getZ()+this.random.nextInt(-2,2),0.0F,0.0F,0.0F);
            }
            this.level().playLocalSound(this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL,20.0F,1.0f,false);
        }
        super.handleEntityEvent(p_21375_);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if(!this.level().isClientSide){
            Util.createExplosion(this,this.level(),p_21372_.blockPosition(),5.0F);
        }else {
            this.level().broadcastEntityEvent(this,(byte) 8);
        }
        return super.doHurtTarget(p_21372_);
    }

    static class DroneAttack extends Goal{
        private final DroneChicken drone;
        private final double speed;
        private final double circleRadius;
        private final Level world;
        private double circlingAngle;
        private final int minAltitude;
        private final int maxAltitude;
        private Vec3 circlingPosition;
        private int attackCooldown=0;
        public boolean meleeAttack=false;
        public boolean rot=false;

        public DroneAttack(DroneChicken drone, double speed, double circleRadius, int minAltitude, int maxAltitude) {
            this.drone = drone;
            this.speed = speed;
            this.circleRadius = circleRadius;
            this.minAltitude = minAltitude;
            this.maxAltitude = maxAltitude;
            this.world = drone.level();
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.circlingAngle = 0.0;
        }

        public boolean canUse() {
            LivingEntity potentialTarget = this.drone.getTarget();
            return potentialTarget != null && potentialTarget.isAlive() ;
        }

        public void start() {
            this.circlingPosition = null;
            this.circlingAngle = 0.0;
            this.attackCooldown = 0;
            this.resetAmount();
        }

        public void resetAmount(){
            this.rot=this.world.random.nextBoolean();
        }

        public boolean canContinueToUse() {
            LivingEntity livingEntity = this.drone.getTarget();
            if (livingEntity instanceof Player playerEntity) {
                if (playerEntity.isCreative() || playerEntity.isInvulnerable()) {
                    return false;
                }
            }

            return livingEntity != null && livingEntity.isAlive();
        }

        public void stop() {
            this.circlingPosition = null;
        }

        public void tick() {
            LivingEntity target = this.drone.getTarget();
            if (target != null) {
                double distanceToTarget = this.drone.distanceToSqr(target.getX(), target.getY(), target.getZ());
                this.circlingAngle += this.rot ? 0.05F : -0.05F;

                Vec3 direction;
                if (!this.meleeAttack) {
                    double offsetX = Math.cos(this.circlingAngle) * this.circleRadius;
                    double offsetZ = Math.sin(this.circlingAngle) * this.circleRadius;
                    double heightOffset = this.minAltitude + 2.0D;
                    this.circlingPosition = new Vec3(target.getX() + offsetX, target.getY() + heightOffset, target.getZ() + offsetZ);

                    // Dirección deseada hacia la posición de círculo
                    direction = this.circlingPosition.subtract(this.drone.position()).normalize().scale(this.speed);

                    // Movimiento actual
                    Vec3 currentMotion = this.drone.getDeltaMovement();

                    // Interpolación (lerp): mezcla entre movimiento actual y el nuevo, suaviza el cambio
                    double smoothFactor = 0.1D; // cuanto más bajo, más suave (0.05-0.2 suele ir bien)
                    Vec3 smoothedMotion = currentMotion.add(direction.subtract(currentMotion).scale(smoothFactor));

                    this.drone.setDeltaMovement(smoothedMotion);
                }

                this.rotateTowardsTarget(target);

                if (this.attackCooldown >= 20 && !this.meleeAttack && distanceToTarget < 64.0F) {
                    ElectroProjectile electro = new ElectroProjectile(this.world, this.drone, 0);
                    electro.setPos(this.drone.position());
                    electro.shoot(
                            target.getX() - drone.getX(),
                            target.getY() + target.getBbWidth() / 2 - drone.getY(),
                            target.getZ() - drone.getZ(),
                            1.0F, 0.1F
                    );
                    this.world.addFreshEntity(electro);
                    this.attackCooldown = 0;
                } else {
                    if (this.meleeAttack) {
                        // suavizado también para el movimiento cuerpo a cuerpo
                        Vec3 desiredMotion = target.position().subtract(this.drone.position()).normalize().scale(0.5F);
                        Vec3 currentMotion = this.drone.getDeltaMovement();
                        Vec3 smoothedMotion = currentMotion.add(desiredMotion.subtract(currentMotion).scale(0.3D));
                        this.drone.setDeltaMovement(smoothedMotion);

                        if (distanceToTarget < 9.0F) {
                            this.drone.doHurtTarget(target);
                        }
                    }
                }

                this.attackCooldown = Math.min(this.attackCooldown + 1, 20);
            }

        }
        public boolean requiresUpdateEveryTick() {
            return true;
        }


        private void rotateTowardsTarget(LivingEntity target) {
            Vec3 direction = drone.getDeltaMovement();
            double dx = direction.x;
            double dy = direction.y;
            double dz = direction.z;
            double targetYaw = Math.toDegrees(Math.atan2(dz, dx)) - 90.0;
            double pitch = -Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
            this.drone.setYRot(this.lerpRotation(this.drone.getYRot(), (float)targetYaw, 30.0F));
            this.drone.setXRot((float)pitch);

        }

        private float lerpRotation(float currentYaw, float targetYaw, float maxTurnSpeed) {
            float deltaYaw = Mth.wrapDegrees(targetYaw - currentYaw);

            float clampedDelta = Mth.clamp(deltaYaw, -maxTurnSpeed, maxTurnSpeed);

            return currentYaw + clampedDelta;
        }



        private double calculateHeightOffset(LivingEntity target) {
            double currentAltitude = this.drone.getY();
            double targetAltitude = target.getY();
            double targetHeight = targetAltitude + (double)this.minAltitude + Math.random() * (double)(this.maxAltitude - this.minAltitude);
            return targetHeight - currentAltitude;
        }
    }
    public static class DroneFlyGoal extends Goal {
        private final DroneChicken drone;
        private final double speed;
        private final int minAltitude;
        private final int maxAltitude;
        private Vec3 targetPos;
        private final double targetThreshold = 1.5;
        private int chargeTime=0;
        private int idleTime = 0;
        private boolean isIdle = false;
        private int oldY =0;

        public DroneFlyGoal(DroneChicken drone, double speed, int minAltitude, int maxAltitude) {
            this.drone = drone;
            this.speed = speed;
            this.minAltitude = minAltitude;
            this.maxAltitude = maxAltitude;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return this.drone.isAlive() && !this.isIdle;
        }

        // Método principal de vuelo del drone
        public void start() {
            if (!this.isIdle) {
                if (this.targetPos != null) {
                    if (this.drone.position().distanceTo(this.targetPos) < targetThreshold) {
                        this.targetPos = getRandomAirPosition();
                        if (this.targetPos == null || this.drone.getRandom().nextFloat() < 0.4) {
                            this.enterIdleMode();
                            return;
                        }
                    }
                }

                if (this.targetPos != null) {
                    Vec3 direction = this.targetPos.subtract(this.drone.position()).normalize().scale(this.speed);
                    this.drone.setDeltaMovement(direction);
                    this.oldY = this.drone.blockPosition().getY();
                    this.drone.getLookControl().setLookAt(this.targetPos.x, this.targetPos.y, this.targetPos.z);
                    this.rotateTowardsTarget();
                }
            }
        }

        @Override
        public void stop() {
            super.stop();
        }

        public boolean canContinueToUse() {
            if (this.isIdle) {
                return this.idleTime > 0;
            } else {
                if (!this.drone.isAlive()) {
                    return this.targetPos != null && !(this.drone.position().distanceTo(this.targetPos) >= targetThreshold);
                }else return !this.drone.isAggressive();
            }
        }

        public void tick() {
            if (this.isIdle) {
                --this.idleTime;
                this.drone.setDeltaMovement(Vec3.ZERO);
                if (this.idleTime <= 0) {
                    this.isIdle = false;
                }
            } else {
                if (this.drone.onGround()) {
                    this.targetPos = getRandomAirPosition();
                }

                if(this.targetPos != null && this.oldY==this.drone.blockPosition().getY()){
                    this.chargeTime+=20;
                }
                if(this.chargeTime>100){
                    this.chargeTime=0;
                    this.targetPos= getRandomAirPosition();
                }

                if (this.targetPos != null && this.drone.position().distanceTo(this.targetPos) < targetThreshold) {
                    this.targetPos = getRandomAirPosition();
                }

                if (this.targetPos != null) {
                    Vec3 direction = this.targetPos.subtract(this.drone.position()).normalize().scale(this.speed);
                    this.drone.setDeltaMovement(direction);
                    this.oldY = this.drone.blockPosition().getY();
                    this.drone.getLookControl().setLookAt(this.targetPos.x, this.targetPos.y, this.targetPos.z);
                    this.rotateTowardsTarget();
                }
            }
        }

        // Método que pone a la drone en modo espera
        private void enterIdleMode() {
            this.idleTime = this.drone.getRandom().nextInt(41) + 20;
            this.isIdle = true;
            this.drone.setDeltaMovement(Vec3.ZERO);
        }

        private Vec3 getRandomAirPosition() {
            RandomSource random = this.drone.getRandom();
            Level world = this.drone.level(); // class_1937
            BlockPos currentPos = this.drone.blockPosition(); // class_2338
            int groundHeight = world.getHeight(Heightmap.Types.WORLD_SURFACE, currentPos.getX(), currentPos.getZ());
            int altitudeRange = this.maxAltitude - this.minAltitude;

            Vec3 targetPos = null;
            for (int i = 0; i < 10; ++i) {
                double newY = groundHeight + this.minAltitude + random.nextDouble() * altitudeRange;
                double x = this.drone.getX() + (random.nextDouble() * 20.0 - 10.0);
                double z = this.drone.getZ() + (random.nextDouble() * 20.0 - 10.0);
                Vec3 targetPosAux = new Vec3(x, newY, z);
                BlockPos targetBlockPos = BlockPos.containing(targetPosAux);
                if (this.isValidFlyPosition(world, targetBlockPos)) {
                    targetPos=targetPosAux;
                }
            }

            return targetPos;
        }

        private boolean isValidFlyPosition(Level world, BlockPos pos) {
            if (world.isEmptyBlock(pos) && world.isEmptyBlock(pos.above())) {
                BlockPos belowPos = pos.below();
                if (world.getBlockState(belowPos).getBlock().getDescriptionId().contains("leaves")) {
                    return false;
                } else {
                    for (BlockPos adjacentPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                        if (world.getBlockState(adjacentPos).getBlock().getDescriptionId().contains("leaves")) {
                            return false;
                        }
                    }
                    return true;
                }
            } else {
                return false;
            }
        }

        // Método que rota la drone hacia el objetivo
        private void rotateTowardsTarget() {
            Vec3 currentPosition = this.drone.position();
            Vec3 directionToTarget = this.targetPos.subtract(currentPosition).normalize();
            double yaw = Math.toDegrees(Math.atan2(directionToTarget.z, directionToTarget.x)) - 90.0;
            double pitch = -Math.toDegrees(Math.atan2(directionToTarget.y, Math.sqrt(directionToTarget.x * directionToTarget.x + directionToTarget.z * directionToTarget.z)));
            this.drone.setYRot((float)yaw);
            this.drone.setXRot((float)pitch);
        }

    }
}
