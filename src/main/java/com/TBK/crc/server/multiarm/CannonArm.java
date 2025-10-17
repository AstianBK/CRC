package com.TBK.crc.server.multiarm;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.ElectroProjectile;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CannonArm extends MultiArmSkillAbstract{

    public int stopAiming = 0;
    public int chargeTime = 0;
    public boolean charge = false;
    public CannonArm() {
        super("cannon_arm", 100, false, true);
    }

    @Override
    public void swapArm(MultiArmCapability multiArmCapability, MultiArmSkillAbstract otherArm) {
        this.charge = false;
        this.chargeTime = 0;
        this.stopAiming = 0;
        multiArmCapability.stopAimingAnim = 0;
        multiArmCapability.stopAimingAnim0 = 0;
        multiArmCapability.levelCharge = 0;
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);

        if(charge){
            multiArmCapability.getPlayer().setDeltaMovement(multiArmCapability.getPlayer().getDeltaMovement().multiply(0.96F,1.0F,0.96F));
            this.chargeTime++;
            this.stopAiming = 20;
            if(this.chargeTime<71){
                if(multiArmCapability.levelCharge>0 && (this.chargeTime-10)%20==0){
                    multiArmCapability.levelCharge = Math.min(multiArmCapability.levelCharge+1, 3);
                }
                if(multiArmCapability.levelCharge==0 && this.chargeTime>10){
                    multiArmCapability.levelCharge=1;
                }
            }
        }else {
            if(this.stopAiming > 0 ){
                this.stopAiming--;
                if(this.stopAiming == 0){
                    multiArmCapability.pose = MultiArmCapability.SkillPose.STOP_AIMING;
                    multiArmCapability.stopAimingAnim = 5;
                    multiArmCapability.stopAimingAnim0 = 5;
                }
            }
        }

    }

    @Override
    public SoundEvent getStartSound() {
        return super.getStartSound();
    }

    @Override
    public SoundEvent getStopSound() {
        return super.getStopSound();
    }


    @Override
    public void startAbility(MultiArmCapability multiArmCapability) {
        super.startAbility(multiArmCapability);
        multiArmCapability.pose =  MultiArmCapability.SkillPose.CHARGE_CANNON;
        this.charge = true;
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
        multiArmCapability.timeShoot = 10;
        multiArmCapability.timeShoot0 = 10;
        this.stopAiming = 20;
        this.charge = false;
        multiArmCapability.levelCharge = 0;
        ElectroProjectile orb = new ElectroProjectile(multiArmCapability.getPlayer().level(),multiArmCapability.getPlayer(),this.chargeTime);
        orb.shootFromRotation(multiArmCapability.getPlayer(),multiArmCapability.getPlayer().getXRot(),multiArmCapability.getPlayer().getYRot(), 0.0F, 1.0F, 1.0F);
        orb.setPos(this.getPos(multiArmCapability.getPlayer().getEyePosition(),multiArmCapability.getPlayer()));

        multiArmCapability.getPlayer().level().addFreshEntity(orb);
        this.chargeTime = 0;
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
