package com.TBK.crc.server.multiarm;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.ResidualEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ClawsArm extends MultiArmSkillAbstract{
    public boolean dash = false;
    public boolean charge = false;
    public int dashTime = 0;
    public ClawsArm() {
        super("claws_arm", 100, false, true);
    }

    @Override
    public boolean canActiveAbility(MultiArmCapability multiArmCapability) {
        //CRC.LOGGER.debug("Item"+multiArmCapability.implantStore.getImplantForSkill(this).getItem() + " en Cooldown :"+!multiArmCapability.getPlayer().getCooldowns().isOnCooldown(multiArmCapability.implantStore.getImplantForSkill(this).getItem()));
        return !multiArmCapability.getPlayer().getCooldowns().isOnCooldown(multiArmCapability.implantStore.getImplantForSkill(this).getItem());
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);
        Player player = multiArmCapability.getPlayer();
        if(this.charge){
            player.setDeltaMovement(player.getDeltaMovement().x*0.1F,player.getDeltaMovement().y,player.getDeltaMovement().z*0.1F);
        }
        if(this.dash){
            for (LivingEntity living : player.level().getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(1.5F), e->e!=player)){
                living.invulnerableTime = 0;
                living.hurt(player.damageSources().playerAttack(player),5.0F);
                living.invulnerableTime = 0;
            }
            if(!player.level().isClientSide){
                ResidualEntity residual = new ResidualEntity(multiArmCapability.getPlayer().level(), BlockPos.ZERO ,player.blockPosition(),player,1, MultiArmCapability.SkillPose.DASH_CLAWS);
                residual.setPos(player.position());
                player.level().addFreshEntity(residual);
            }
            if(this.dashTime > 0){
                this.dashTime--;
                if(this.dashTime == 0){
                    this.dash = false;
                    multiArmCapability.pose = MultiArmCapability.SkillPose.NONE;
                }
            }
        }
    }


    @Override
    public void startAbility(MultiArmCapability multiArmCapability) {
        super.startAbility(multiArmCapability);

        CRC.LOGGER.debug("Start Charge");
        if(!this.charge){
            multiArmCapability.pose = MultiArmCapability.SkillPose.CHARGE_CLAWS;
            this.charge = true;
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
    public void stopAbility(MultiArmCapability multiArmCapability) {
        super.stopAbility(multiArmCapability);
        CRC.LOGGER.debug("Stop Charge");
        if(this.charge){
            Player player = multiArmCapability.getPlayer();
            Vec3 view = player.getViewVector(1.0F).multiply(1.0F,player.onGround() ? 0.0F : 1.0F,1.0F).normalize().scale(4.0F);
            player.setDeltaMovement(view);
            player.hasImpulse = true;
            this.charge = false;
            this.dash = true;
            this.dashTime = 5;
            multiArmCapability.pose = MultiArmCapability.SkillPose.DASH_CLAWS;
        }
    }
}
