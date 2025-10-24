package com.TBK.crc.server.upgrade;

import com.TBK.crc.common.registry.BKSounds;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.GanchoEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GanchoArm extends Upgrade {
    public static final int MAX_RANGE = 22;
    public boolean hasGancho = true;
    public int ganchoId = -1;
    public float largoActual = 0.0F;
    public boolean atraction = false;
    public GanchoArm() {
        super("gancho_arm", 100, true, false);
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);
        Entity gancho = multiArmCapability.getPlayer().level().getEntity(this.ganchoId);
        if (gancho instanceof GanchoEntity ganchoEntity) {
            Player player = multiArmCapability.getPlayer();
            if (ganchoEntity.hit && gancho.distanceTo(player) > MAX_RANGE*1.5F) {
                multiArmCapability.getPlayer().level().playLocalSound(multiArmCapability.getPlayer().blockPosition(),getStopSound(), SoundSource.PLAYERS, getVolumeStartAbility(),1.0f,false);

                ganchoEntity.isBack = true;
                ganchoEntity.hit = false;
                ganchoEntity.level().broadcastEntityEvent(gancho,(byte) 5);
            } else {
                if (!ganchoEntity.isBack && gancho.distanceTo(player) > MAX_RANGE) {
                    multiArmCapability.getPlayer().level().playLocalSound(multiArmCapability.getPlayer().blockPosition(),getStopSound(), SoundSource.PLAYERS, getVolumeStartAbility(),1.0f,false);

                    ganchoEntity.isBack = true;
                    ganchoEntity.level().broadcastEntityEvent(gancho,(byte) 5);
                }
            }
        }
    }

    @Override
    public void swapArm(MultiArmCapability multiArmCapability, Upgrade otherArm) {
        hasGancho = true;
        if(ganchoId!=-1){
            Entity gancho = multiArmCapability.getPlayer().level().getEntity(this.ganchoId);
            if(gancho instanceof GanchoEntity){
                gancho.discard();

            }
            this.ganchoId = -1;
        }
    }

    @Override
    public SoundEvent getStartSound() {
        return BKSounds.MULTIARM_HARPOON_SHOOT.get();
    }

    @Override
    public SoundEvent getStopSound() {
        return BKSounds.MULTIARM_HARPOON_COMEBACK.get();
    }

    @Override
    public void stopAbility(MultiArmCapability multiArmCapability) {

    }

    @Override
    public void startAbility(MultiArmCapability multiArmCapability) {
        if(hasGancho){
            super.startAbility(multiArmCapability);
            GanchoEntity gancho = new GanchoEntity(multiArmCapability.getPlayer().level(),multiArmCapability.getPlayer());
            gancho.setPos(multiArmCapability.getPlayer().getX(),multiArmCapability.getPlayer().getEyeY()-0.1f,multiArmCapability.getPlayer().getZ());
            gancho.shootFromRotation(multiArmCapability.getPlayer(),multiArmCapability.getPlayer().getXRot(),multiArmCapability.getPlayer().yHeadRot,0.0F,3.0F,1.0F);
            multiArmCapability.getPlayer().level().addFreshEntity(gancho);
            this.hasGancho = false;
            this.ganchoId = gancho.getId();
            this.largoActual = 0.0F;
            this.atraction = false;
        }else {
            multiArmCapability.getPlayer().fallDistance = 0.0F;
            Entity gancho = multiArmCapability.getPlayer().level().getEntity(this.ganchoId);
            if(gancho instanceof GanchoEntity ganchoEntity){
                if(!ganchoEntity.isBack ){
                    if (!multiArmCapability.getPlayer().isShiftKeyDown()) {
                        if (ganchoEntity.hit) {
                            Vec3 direction = gancho.position().subtract(multiArmCapability.getPlayer().position());
                            Vec3 pushDirection = direction.normalize().scale(3*Math.min(direction.length()/6.0F,1.0F));
                            multiArmCapability.getPlayer().setDeltaMovement(pushDirection.x, Math.min(pushDirection.y, 1.5F), pushDirection.z);
                            multiArmCapability.getPlayer().hurtMarked = true;
                        }
                    }
                    ganchoEntity.isBack = true;
                    ganchoEntity.level().broadcastEntityEvent(gancho,(byte) 5);
                }


            }else {
                if(multiArmCapability.catchEntity!=null){
                    Vec3 direction = multiArmCapability.getPlayer().position().subtract(multiArmCapability.getPlayer().position());
                    Vec3 pushDirection = direction.normalize().scale(3*Math.min(direction.length()/6.0F,1.0F));

                    multiArmCapability.catchEntity.setDeltaMovement(pushDirection.x,Math.min(pushDirection.y,1.5F),pushDirection.z);
                    multiArmCapability.catchEntity.hurtMarked=true;

                    GanchoEntity gancho1 = new GanchoEntity(multiArmCapability.getPlayer().level(),multiArmCapability.getPlayer(),true);
                    gancho1.setPos(multiArmCapability.catchEntity.getX(),multiArmCapability.catchEntity.getEyeY()-0.1f,multiArmCapability.catchEntity.getZ());
                    multiArmCapability.getPlayer().level().addFreshEntity(gancho1);
                }
            }

            multiArmCapability.catchEntity=null;
        }
    }


    @Override
    public void save(CompoundTag nbt) {
        super.save(nbt);
        nbt.putBoolean("has_gancho",hasGancho);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.hasGancho = nbt.getBoolean("has_gancho");
    }
}
