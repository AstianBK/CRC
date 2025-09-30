package com.TBK.crc.server.multiarm;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.GanchoEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class GanchoArm extends MultiArmSkillAbstract{
    public boolean hasGancho = true;
    public int ganchoId = -1;
    public GanchoArm() {
        super("gancho_arm", 100, true);
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);
        Entity gancho = multiArmCapability.getPlayer().level().getEntity(this.ganchoId);
        if(gancho instanceof GanchoEntity ganchoEntity && !ganchoEntity.isBack && gancho.distanceTo(multiArmCapability.getPlayer())>20){
            ganchoEntity.isBack = true;
        }
    }

    @Override
    public void startAbility(MultiArmCapability multiArmCapability) {
        if (multiArmCapability.getPlayer().getMainHandItem().isEmpty()){
            if(hasGancho){
                GanchoEntity gancho = new GanchoEntity(multiArmCapability.getPlayer().level(),multiArmCapability.getPlayer());
                gancho.setPos(multiArmCapability.getPlayer().getX(),multiArmCapability.getPlayer().getEyeY()-0.1f,multiArmCapability.getPlayer().getZ());
                gancho.shootFromRotation(multiArmCapability.getPlayer(),multiArmCapability.getPlayer().getXRot(),multiArmCapability.getPlayer().yHeadRot,0.0F,3.0F,1.0F);
                multiArmCapability.getPlayer().level().addFreshEntity(gancho);
                this.hasGancho = false;
                this.ganchoId = gancho.getId();
            }else {
                Entity gancho = multiArmCapability.getPlayer().level().getEntity(this.ganchoId);
                if(gancho instanceof GanchoEntity ganchoEntity){
                    if(!ganchoEntity.isBack ){
                        if(ganchoEntity.hit){
                            Vec3 pushDirection = gancho.position().subtract(multiArmCapability.getPlayer().position()).normalize().scale(3);
                            multiArmCapability.getPlayer().setDeltaMovement(pushDirection.x,Math.min(pushDirection.y,1.5F),pushDirection.z);
                            multiArmCapability.getPlayer().hurtMarked=true;
                        }
                        ganchoEntity.isBack = true;
                    }
                }else {
                    if(multiArmCapability.catchEntity!=null){
                        Vec3 pushDirection = multiArmCapability.getPlayer().position().subtract(multiArmCapability.catchEntity.position()).normalize().scale(3);
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
    }


    @Override
    public void save(CompoundTag nbt) {
        nbt.putBoolean("has_gancho",hasGancho);
    }
}
