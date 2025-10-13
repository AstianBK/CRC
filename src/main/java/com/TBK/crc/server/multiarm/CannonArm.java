package com.TBK.crc.server.multiarm;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.ElectroProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CannonArm extends MultiArmSkillAbstract{
    public int castingId;
    public CannonArm() {
        super("cannon_arm", 100, false, true);
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);
        Entity entity=multiArmCapability.getPlayer().level().getEntity(this.castingId);
        if(multiArmCapability.getCastingTimer()%10==0){
            if(entity instanceof ElectroProjectile projectile){
                multiArmCapability.getPlayer().level().playSound(null,multiArmCapability.getPlayer(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.PLAYERS,1.0F,1.0F);
                if(!multiArmCapability.getPlayer().level().isClientSide){
                    projectile.level().broadcastEntityEvent(projectile,(byte) 4);
                }

            }
        }

        if(!multiArmCapability.getPlayer().level().isClientSide){
            if(entity instanceof  ElectroProjectile projectile){
                projectile.setPos(this.getPos(multiArmCapability.getPlayer().getEyePosition(),multiArmCapability.getPlayer()));
                reRot(projectile,multiArmCapability.getPlayer().getXRot(),multiArmCapability.getPlayer().getYRot(),1.0F,1.0F);
                projectile.level().broadcastEntityEvent(projectile,(byte) 4);
            }
        }
    }

    @Override
    public void startAbility(MultiArmCapability multiArmCapability) {
        ElectroProjectile orb = new ElectroProjectile(multiArmCapability.getPlayer().level(),multiArmCapability.getPlayer(),0);
        orb.setPos(this.getPos(multiArmCapability.getPlayer().getEyePosition(),multiArmCapability.getPlayer()));
        reRot(orb,0.0F,multiArmCapability.getPlayer().getYRot(),0.0F,1.0F,1.0F);

        multiArmCapability.getPlayer().level().addFreshEntity(orb);
        this.castingId = orb.getId();
    }

    public void reRot(ElectroProjectile projectile,float x, float y, float vel, float miss){
        float f = -Mth.sin(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((x) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        reRot(projectile,f,f1,f2,miss,vel);
    }

    public void reRot(ElectroProjectile projectile,double x, double y, double z, float vel, float miss) {
        Vec3 vec3 = (new Vec3(x, y, z)).normalize().add(projectile.level().random.triangle(0.0D, 0.0172275D * (double) miss), projectile.level().random.triangle(0.0D, 0.0172275D * (double) miss), projectile.level().random.triangle(0.0D, 0.0172275D * (double) miss)).scale((double) vel);
        double d0 = vec3.horizontalDistance();
        projectile.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        projectile.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        projectile.yRotO = projectile.getYRot();
        projectile.xRotO = projectile.getXRot();
    }

    @Override
    public void stopAbility(MultiArmCapability multiArmCapability) {
        super.stopAbility(multiArmCapability);
        Entity entity=multiArmCapability.getPlayer().level().getEntity(this.castingId);
        if(entity instanceof ElectroProjectile orb){
            orb.shootFromRotation(multiArmCapability.getPlayer(),multiArmCapability.getPlayer().getXRot(),multiArmCapability.getPlayer().getYRot(), 0.0F, 1.0F, 1.0F);
            this.castingId = -1;
        }
    }


    public Vec3 getPos(Vec3 initialVec, Player player){
        float f1 = player.getYHeadRot() * Mth.DEG_TO_RAD;
        float f2 = Mth.sin(f1);
        float f3 = Mth.cos(f1);
        float f5 = player.getViewXRot(1.0F);
        float f4 = (float) ((f5+90.0F) * (double) Mth.DEG_TO_RAD);
        if(f5<0.0F){
            f5 =-f5;
        }
        float f6 =Mth.lerp(f5/90.0F,1.5F,0.0F);
        f4 = Mth.cos(f4);
        f2 = f2*f6;
        f3 = f3*f6;
        return initialVec.add(-f2,f4-0.35F,f3);
    }
}
