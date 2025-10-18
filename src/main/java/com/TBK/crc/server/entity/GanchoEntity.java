package com.TBK.crc.server.entity;

import com.TBK.crc.CRC;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.GanchoArm;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketHandlerPowers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
    protected void onHitEntity(EntityHitResult p_36757_) {
        if (p_36757_.getEntity()!=getOwner()){
            this.cachedID = p_36757_.getEntity().getId();
            this.cachedUUID = p_36757_.getEntity().getUUID();
            this.cachedTarget = p_36757_.getEntity();

            if(!this.level().isClientSide){
                if(this.getOwner() instanceof Player player){
                    MultiArmCapability cap = MultiArmCapability.get(player);
                    if(cap!=null && p_36757_.getEntity() instanceof LivingEntity){
                        cap.catchEntity = p_36757_.getEntity();
                        PacketHandler.sendToAllTracking(new PacketHandlerPowers(1,p_36757_.getEntity(),player), (LivingEntity) p_36757_.getEntity());
                    }
                }
            }
            discard();
            p_36757_.getEntity().hurt(damageSources().generic(),2.0F);
        }
    }



    @Override
    public void tick() {
        super.tick();
        Entity entity = this.getOwner();

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
                this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
            }

            ++this.clientSideReturnTridentTickCount;

        }
    }

    @Override
    protected boolean tryPickup(Player p_150121_) {
        this.discard();
        return false;
    }

    @Override
    public void handleEntityEvent(byte p_19882_) {
        if(p_19882_==4){
            if(this.getOwner() instanceof Player player){
                MultiArmCapability cap = MultiArmCapability.get(player);
                if(cap!=null){
                    MultiArmSkillAbstract skill = cap.getHotBarSkill().getForName("gancho_arm");
                    if(skill instanceof GanchoArm arm){
                        arm.hasGancho=true;
                    }
                }
            }
        }
        super.handleEntityEvent(p_19882_);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

}
