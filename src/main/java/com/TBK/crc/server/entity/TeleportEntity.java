package com.TBK.crc.server.entity;

import com.TBK.crc.common.registry.BKEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class TeleportEntity extends Entity {
    public int teleportTimerOld;
    public int teleportTimer;
    public int generationTimer;
    public int generationTimerOld;
    public EntityType<?> type;
    public BlockPos blockTeleport;
    public LivingEntity target = null;
    public boolean shield = false;
    public TeleportEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.type = BKEntityType.CYBORG_ROBOT_CHICKEN.get();
        this.blockTeleport = null;
        teleportTimer = 25;
        teleportTimerOld = 25;
        generationTimer = 50;
        generationTimerOld = 50;
    }

    public TeleportEntity(Level p_19871_,EntityType<?> entity,BlockPos pos,LivingEntity target,boolean shield) {
        super(BKEntityType.TELEPORT.get(), p_19871_);
        this.target = target;
        this.type = entity;
        this.blockTeleport = pos;
        teleportTimer = 25;
        teleportTimerOld = 25;
        generationTimer = 50;
        generationTimerOld = 50;
        this.teleportTo(pos.getX(),pos.getY(),pos.getZ());
        this.shield = shield;
    }

    @Override
    public void tick() {
        this.teleportTimerOld = this.teleportTimer;
        this.generationTimerOld = this.generationTimer;
        super.tick();
        if(this.generationTimer>0){
            this.generationTimer--;
            if(generationTimer<=0){
                this.summon();
            }
        }else{
            if(this.teleportTimer>0){
                this.teleportTimer--;
                if(this.teleportTimer<=0){
                    this.discard();
                }
            }
        }
    }
    public float getGenerationPorcent(float partialTick){
        return (Mth.lerp(partialTick,this.generationTimer,this.generationTimerOld) / 50.0F);
    }
    public float getTeleportPorcent(float partialTick){
        return Mth.lerp(partialTick,this.teleportTimer,this.teleportTimerOld) / 25.0F;
    }

    public void summon(){
        if(!this.level().isClientSide){
            Entity entity = this.type.create(this.level());
            if(entity==null)return;
            if(entity instanceof RobotChicken){
                ((RobotChicken) entity).setShield(this.shield);
            }
            if(this.blockTeleport!=null){
                entity.teleportTo(this.blockTeleport.getX(),this.blockTeleport.getY()+1.0F,this.blockTeleport.getZ());
            }else {
                entity.teleportTo(this.getOnPos().getX(),this.getOnPos().getY()+1.0F,this.getOnPos().getZ());
            }
            if(this.target!=null && entity instanceof Mob){
                ((Mob) entity).setTarget(this.target);
            }
            this.level().addFreshEntity(entity);
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }
}
