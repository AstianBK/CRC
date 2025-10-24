package com.TBK.crc.server.entity;

import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.common.registry.BKSounds;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.upgrade.GanchoArm;
import com.TBK.crc.server.upgrade.Upgrade;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketHandlerPowers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.UUID;

public class GanchoEntity extends AbstractArrow {
    public int cachedID = -1;
    public UUID cachedUUID;
    public Entity cachedTarget = null;
    public boolean isBack = false;
    public int clientSideReturnTridentTickCount = 0;
    public boolean hit = false;
    public GanchoEntity(EntityType<? extends AbstractArrow> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public GanchoEntity(Level p_37249_, Entity owner,boolean isBack) {
        super(BKEntityType.GANCHO.get(), p_37249_);
        this.setOwner(owner);
        this.isBack = isBack;
    }
    public GanchoEntity(Level p_37249_, Entity owner) {
        super(BKEntityType.GANCHO.get(), p_37249_);
        this.setOwner(owner);
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        this.hit=true;
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        if (p_36757_.getEntity()!=getOwner()){
            if(p_36757_.getEntity() instanceof RexChicken || p_36757_.getEntity() instanceof PartEntity<?> || p_36757_.getEntity() instanceof EnderDragon){
                 this.isBack = true;
                 if(!this.level().isClientSide){
                     this.level().broadcastEntityEvent(this,(byte) 5);
                 }
            }else {
                this.cachedID = p_36757_.getEntity().getId();
                this.cachedUUID = p_36757_.getEntity().getUUID();
                this.cachedTarget = p_36757_.getEntity();

                if(!this.level().isClientSide){
                    if(this.getOwner() instanceof Player player){
                        MultiArmCapability cap = MultiArmCapability.get(player);
                        if(cap!=null){
                            if(p_36757_.getEntity() instanceof LivingEntity){
                                cap.catchEntity = p_36757_.getEntity();
                                PacketHandler.sendToAllTracking(new PacketHandlerPowers(1,p_36757_.getEntity(),player), (LivingEntity) p_36757_.getEntity());
                            }else if(p_36757_.getEntity() instanceof PartEntity<?> partEntity){
                                cap.catchEntity = partEntity.getParent();
                                PacketHandler.sendToAllTracking(new PacketHandlerPowers(1,p_36757_.getEntity(),player), (LivingEntity)partEntity.getParent());
                            }
                        }
                    }
                }
                discard();
                p_36757_.getEntity().hurt(damageSources().generic(),2.0F);

            }
        }
    }



    @Override
    public void tick() {
        super.tick();
        Entity entity = this.getOwner();

        if(entity==null){
            this.discard();
            return;
        }
        if(isBack){
            this.setNoPhysics(true);
            Vec3 vec3 = entity.getEyePosition().subtract(this.position());
            this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.05, this.getZ());
            if (this.level().isClientSide) {
                this.yOld = this.getY();
            }

            double d0 = 0.6D;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
            if (this.clientSideReturnTridentTickCount == 0) {
                entity.playSound(BKSounds.MULTIARM_HARPOON_COMEBACK.get(), 10.0F, 1.0F);
            }

            ++this.clientSideReturnTridentTickCount;
        }
    }

    @Override
    protected boolean tryPickup(Player p_150121_) {
        if(this.isBack){
            if(this.getOwner() instanceof Player player){
                MultiArmCapability cap = MultiArmCapability.get(player);
                if(cap!=null){
                    Upgrade skill = cap.getHotBarSkill().getForName("gancho_arm");
                    if(skill instanceof GanchoArm arm){
                        arm.hasGancho=true;
                        this.level().broadcastEntityEvent(this,(byte) 4);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte p_19882_) {
        if(p_19882_==4){
            if(this.getOwner() instanceof Player player){
                MultiArmCapability cap = MultiArmCapability.get(player);
                if(cap!=null){
                    Upgrade skill = cap.getHotBarSkill().getForName("gancho_arm");
                    if(skill instanceof GanchoArm arm){
                        arm.hasGancho=true;
                    }
                }
            }
        }else if(p_19882_==5){
            this.isBack = true;
        }
        super.handleEntityEvent(p_19882_);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

}
