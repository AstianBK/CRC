package com.TBK.crc.server.entity;

import com.TBK.crc.CRC;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.server.capability.MultiArmCapability;
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
    public int timeRecharge = 0;
    public ElectroProjectile(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
        this.timeRecharge = 20;
    }
    public ElectroProjectile(Level p_36722_, Player player,int time) {
        super(BKEntityType.ELECTRO.get(), p_36722_);
        this.setOwner(player);
        this.timeRecharge = time;
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (result.getType() == HitResult.Type.MISS && this.isAlive()) {
                List<Entity> intersecting = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox(), this::canHitEntity);
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
        return (1.0F-(this.timeRecharge/20.0F));
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        if(p_36757_.getEntity() instanceof LivingEntity){
            p_36757_.getEntity().hurt(damageSources().generic(), (float) this.getBaseDamage());
        }
    }

    @Override
    public double getBaseDamage() {
        return super.getBaseDamage() + 20 * getScale();
    }


    @Override
    public void handleEntityEvent(byte p_19882_) {
        if(p_19882_==4){
            if(this.getOwner() instanceof Player player){
                MultiArmCapability cap = MultiArmCapability.get(player);
                this.timeRecharge = cap.getCastingClientTimer();
            }
        }
        super.handleEntityEvent(p_19882_);
    }


    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
}
