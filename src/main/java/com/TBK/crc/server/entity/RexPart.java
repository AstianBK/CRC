package com.TBK.crc.server.entity;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.common.registry.BKSounds;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketActionRex;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkHooks;

public class RexPart<T extends RexChicken> extends PartEntity<T> {
    public static final EntityDataAccessor<Boolean> BREAKING =
            SynchedEntityData.defineId(RexPart.class, EntityDataSerializers.BOOLEAN);
    public final T parentMob;
    public final String name;
    private EntityDimensions size;
    public boolean isTower;
    public AnimationState prepareShoot = new AnimationState();
    public AnimationState towerShoot = new AnimationState();
    public AnimationState towerBreak = new AnimationState();
    public int hits = 0;
    public int prepareShootTimer = 0;
    public int nextShootTimer = 0;
    public int maxTimer = 0;
    public int explosionTimer = 0;

    public RexPart(T parent, String name, float sizeX, float sizeZ) {
        super(parent);
        this.parentMob = parent;
        this.name = name;
        this.size = EntityDimensions.scalable(sizeX,sizeZ);
        this.setSize(this.size);
        this.isTower = false;
    }
    public RexPart(T parent, String name, float sizeX, float sizeZ,boolean isTower) {
        super(parent);
        this.parentMob = parent;
        this.name = name;
        this.size = EntityDimensions.scalable(sizeX,sizeZ);
        this.setSize(this.size);
        this.isTower = isTower;
        if(isTower){
            this.hits = 5;
        }
    }

    @Override
    public void tick() {
        if(!this.level().isClientSide){
            if(this.isTower && this.parentMob.getTarget()!=null && !this.isBreaking()){
                if(this.level().getEntitiesOfClass(BoomChicken.class,this.parentMob.getBoundingBox().inflate(100.0F)).size()<6){
                    if(this.prepareShootTimer>0){
                        this.prepareShootTimer--;
                        if(this.prepareShootTimer<=0){
                            this.shoot();
                            this.launchBoomChicken(this.parentMob.getTarget(),this.parentMob.getTarget().blockPosition());
                        }
                    }
                    if(this.nextShootTimer<=this.maxTimer){
                        this.nextShootTimer++;
                        if(this.nextShootTimer>this.maxTimer){
                            this.startShoot();
                        }
                    }
                }
            }
        }else {
            if(this.isTower){
                this.towerBreak.animateWhen(this.isBreaking(),this.parentMob.tickCount);
            }
            if(this.explosionTimer>0){
                float f = (this.random.nextFloat() - 0.5F) * 8.0F;
                float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
                float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
                this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + (double)f, this.getY() + 2.0D + (double)f1, this.getZ() + (double)f2, 0.0D, 0.0D, 0.0D);
                this.explosionTimer--;
            }
        }
    }

    protected void setSize(EntityDimensions size) {
        this.size = size;
        this.refreshDimensions();
    }

    protected void defineSynchedData() {
        this.entityData.define(BREAKING,false);
    }

    protected void readAdditionalSaveData(CompoundTag p_31025_) {
        this.setBreaking(p_31025_.getBoolean("isBreaking"));
    }

    protected void addAdditionalSaveData(CompoundTag p_31028_) {
        p_31028_.putBoolean("isBreaking",this.isBreaking());
    }


    public boolean isPickable() {
        return true;
    }



    public boolean hurt(DamageSource p_31020_, float p_31021_) {
        if(!this.parentMob.isPowered()){
            if(this.isTower){
                this.hits--;
                if(this.hits<=0){
                    this.destroy();
                }
            }else if(this.parentMob.stunnedTick>0 && this.name.equals("head")){
                float healtCurrent = this.parentMob.getHealth()-20.0F;
                if(healtCurrent<=0){
                    this.parentMob.setHealth(0.0F);
                    this.parentMob.die(p_31020_);
                    return true;
                }else {
                    this.parentMob.setHealth(healtCurrent);
                }
                this.parentMob.regenerationShieldTimer=0;
                this.parentMob.setShieldAmount(50);
                this.parentMob.stunnedTick = 0;
                if(!this.level().isClientSide){
                    this.parentMob.recoveryTimer=21;
                    this.parentMob.level().broadcastEntityEvent(this.parentMob,(byte) 17);
                    this.callReinforcement(p_31020_.getEntity());
                }
                return true;
            }
        }
        return this.parentMob.hurt(p_31020_,p_31021_);
    }

    public void callReinforcement(Entity entity){
        if (entity instanceof LivingEntity target){
            for (int i = 0 ; i<3 ; i++){
                TeleportEntity teleport = new TeleportEntity(this.level(), BKEntityType.DRONE_CHICKEN.get(), Util.findCaveSurfaceNearHeight(entity,3,this.random),target,true);
                this.level().addFreshEntity(teleport);
            }
        }
    }


    public boolean is(Entity p_31031_) {
        return this == p_31031_ || this.parentMob.is(p_31031_);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void shoot(){
        if(!this.level().isClientSide){
            PacketHandler.sendToAllTracking(new PacketActionRex(this.parentMob.getId(),this.getId(),4),this.parentMob);
            this.nextShootTimer = 0;
            this.maxTimer = 100 + level().random.nextInt(200);
            this.prepareShootTimer = 0;
        }
    }

    public void startShoot(){
        if(!this.level().isClientSide){
            this.prepareShootTimer = 21;
            PacketHandler.sendToAllTracking(new PacketActionRex(this.parentMob.getId(),this.getId(),8),this.parentMob);
        }
    }
    public void destroy(){
        if(!this.level().isClientSide){
            this.setBreaking(true);
            this.parentMob.stunnedTick=300;
            this.parentMob.level().broadcastEntityEvent(this.parentMob,(byte) 12);
            PacketHandler.sendToAllTracking(new PacketActionRex(this.parentMob.getId(),this.getId(),12),this.parentMob);
        }
    }
    public void launchBoomChicken(LivingEntity living, BlockPos targetPos){
        BoomChicken missile = new BoomChicken(this.level());
        missile.setTarget(living);
        missile.setIsLaunch(true);
        missile.setPos(this.getX(),this.getY() + 3.0d,this.getZ());
        missile.calculateRotPosition(missile.blockPosition(),targetPos);
        missile.setDeltaMovement(missile.calculateJumpVelocity(missile.blockPosition(),living.blockPosition()));
        this.level().addFreshEntity(missile);
    }
    public boolean isBreaking(){
        return this.entityData.get(BREAKING);
    }
    public void setBreaking(boolean isBreaking){
        this.entityData.set(BREAKING,isBreaking);
    }

    @Override
    public void handleEntityEvent(byte p_19882_) {
        if(p_19882_==4){
            this.prepareShoot.stop();
            this.towerShoot.start(this.parentMob.tickCount);
            this.level().playLocalSound(this.getX(),this.getY(),this.getZ(), BKSounds.REX_CANNON_SHOOT.get(), SoundSource.HOSTILE,3.0F,1.0F,false);

        }else if(p_19882_==8){
            this.towerShoot.stop();
            this.prepareShoot.start(this.parentMob.tickCount);
        }else if(p_19882_==12){
            this.prepareShoot.stop();
            this.towerShoot.stop();
            this.towerBreak.start(this.parentMob.tickCount);
            this.setBreaking(true);
            this.explosionTimer = 10;
            this.level().playLocalSound(this.getX(),this.getY(),this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE,4.0F,1.0F,false);
        }
        super.handleEntityEvent(p_19882_);
    }

    public EntityDimensions getDimensions(Pose p_19975_) {
        return this.size;
    }
}