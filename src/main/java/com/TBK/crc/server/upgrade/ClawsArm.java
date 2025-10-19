package com.TBK.crc.server.upgrade;

import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKSounds;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.ResidualEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ClawsArm extends Upgrade {
    public boolean dash = false;
    public boolean charge = false;
    public int dashTime = 0;
    public ClawsArm() {
        super("claws_arm", 175, false, true);
    }


    @Override
    public boolean canActiveAbility(MultiArmCapability multiArmCapability) {
        return !multiArmCapability.getPlayer().getCooldowns().isOnCooldown(multiArmCapability.implantStore.getArmForSkill(this).getItem());
    }

    @Override
    public void swapArm(MultiArmCapability multiArmCapability, Upgrade otherArm) {
        this.charge = false;
        this.dash = false;
        this.dashTime = 0;
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);

        Player player = multiArmCapability.getPlayer();

        if(this.charge){
            player.setDeltaMovement(player.getDeltaMovement().x*0.05F,player.getDeltaMovement().y,player.getDeltaMovement().z*0.05F);
        }
        if(this.dash){
            player.fallDistance = 0.0F;
            player.setDeltaMovement(player.getDeltaMovement().multiply(1.0F,0.89F,1.0F));
            if(!player.level().isClientSide){
                for (LivingEntity living : player.level().getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(1.5F), e->e!=player)){
                    living.invulnerableTime = 0;
                    living.hurt(Util.electricDamage((ServerLevel) player.level(),player),5.0F);
                    living.invulnerableTime = 0;
                }
            }
            if(this.dashTime > 0){
                this.dashTime--;
                if(this.dashTime<4){
                    if(!player.level().isClientSide){
                        Vec3 delta = player.getDeltaMovement();
                        ResidualEntity residual = new ResidualEntity(multiArmCapability.getPlayer().level(), BlockPos.ZERO ,player.blockPosition(),player,1, MultiArmCapability.SkillPose.DASH_CLAWS, (float) Math.atan2(delta.z,delta.x) * 180.0F / Mth.PI,(float) Math.acos(delta.y)* 180.0F / Mth.PI);
                        residual.setPos(player.position());
                        player.level().addFreshEntity(residual);
                    }
                }
                if(this.dashTime == 0){
                    this.dash = false;
                    multiArmCapability.pose = MultiArmCapability.SkillPose.NONE;
                    player.setDeltaMovement(Vec3.ZERO);
                }
            }
        }
    }

    public void startCooldown(MultiArmCapability multiArmCapability){
        ItemStack stack = multiArmCapability.implantStore.getArmForSkill(this);
        if(stack!=null){
            multiArmCapability.getPlayer().getCooldowns().addCooldown(stack.getItem(),this.cd);
        }
    }
    @Override
    public void startAbility(MultiArmCapability multiArmCapability) {
        super.startAbility(multiArmCapability);
        if(canActiveAbility(multiArmCapability)){
            this.startCooldown(multiArmCapability);
            if(!this.charge){
                multiArmCapability.pose = MultiArmCapability.SkillPose.CHARGE_CLAWS;
                multiArmCapability.timeCharge = 10;
                multiArmCapability.timeCharge0 = 10;
                this.charge = true;
            }
        }
    }


    @Override
    public void onAttack(MultiArmCapability multiArmCapability, LivingHurtEvent event) {
        if(event.getSource().is(Util.ELECTRIC_DAMAGE_MOB) || event.getSource().is(Util.ELECTRIC_DAMAGE_PLAYER))return;

        event.getEntity().invulnerableTime = 0;

        event.getEntity().hurt(Util.electricDamage((ServerLevel) multiArmCapability.getPlayer().level(),multiArmCapability.getPlayer()),10.0F * multiArmCapability.getPlayer().getAttackStrengthScale(0.5F));
        event.setCanceled(true);
    }



    @Override
    public SoundEvent getStartSound() {
        return super.getStartSound();
    }

    @Override
    public SoundEvent getStopSound() {
        return BKSounds.MULTIARM_CLAW_DASH.get();
    }
    @Override
    public float getVolumeStopAbility() {
        return 15.0f;
    }

    @Override
    public void stopAbility(MultiArmCapability multiArmCapability) {
        super.stopAbility(multiArmCapability);
        if(this.charge){
            Player player = multiArmCapability.getPlayer();
            Vec3 view = player.getViewVector(1.0F);
            view = view.multiply(1.0F,player.onGround() || view.y>0.0F  ? 0.0F : 1.0F,1.0F);
            player.setDeltaMovement(view.scale(4.0F));
            player.hasImpulse = true;
            this.charge = false;
            this.dash = true;
            this.dashTime = 5;
            multiArmCapability.pose = MultiArmCapability.SkillPose.DASH_CLAWS;
            if(!player.level().isClientSide){
                ResidualEntity residual = new ResidualEntity(multiArmCapability.getPlayer().level(), BlockPos.ZERO ,player.blockPosition(),player,1, MultiArmCapability.SkillPose.CHARGE_CLAWS, (float) Math.atan2(view.z,view.x) * 180.0F / Mth.PI,(float) Math.acos(view.y)* 180.0F / Mth.PI);
                residual.setPos(player.position());
                player.level().addFreshEntity(residual);
            }
        }
    }
}
