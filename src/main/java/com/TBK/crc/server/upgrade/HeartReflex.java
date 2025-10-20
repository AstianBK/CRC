package com.TBK.crc.server.upgrade;

import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKSounds;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.ResidualEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HeartReflex extends PassivePart{
    public HeartReflex() {
        super("heart_reflex", true);
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        //super.tick(multiArmCapability);
    }




    @Override
    public boolean canActive(MultiArmCapability multiArmCapability) {
        ItemStack stack = multiArmCapability.implantStore.getImplantForSkill(this);
        return stack!=null && !multiArmCapability.getPlayer().getCooldowns().isOnCooldown(stack.getItem());
    }

    @Override
    public boolean handlerPassive(MultiArmCapability multiArmCapability, Entity source) {
        startCooldown(multiArmCapability);
        Player player = multiArmCapability.getPlayer();
        Vec3 position = player.position();
        Vec3 delta = player.getDeltaMovement().multiply(1.0f,0.0f,1.0f);
        if(delta.length()<0.01D){
            delta = player.getViewVector(1.0F).multiply(1.0f,0.0f,1.0f);
        }
        Vec3 offSet = Vec3.ZERO;
        Vec3 prevOffSet = Vec3.ZERO;
        Vec3 teleportPos = position;
        BlockPos surface = player.blockPosition();
        boolean flag = false;
        int i = 0;
        while (!flag){
            BlockState state = player.level().getBlockState(surface.below());
            Vec3 deltaFinal = state.isAir() ? new Vec3(delta.x,-0.09f,delta.z) : delta;
            if(!Util.isSafePosition(multiArmCapability.getPlayer().level(), position.add(offSet))){
                teleportPos = position.add(prevOffSet);
                break;
            }
            if (!player.level().isClientSide){
                ResidualEntity residual = new ResidualEntity(multiArmCapability.getPlayer().level(), BlockPos.containing(teleportPos),BlockPos.containing(position),player,i, MultiArmCapability.SkillPose.NONE, (float) Math.atan2(deltaFinal.z,deltaFinal.x)*180.0F/Mth.PI, (float) Math.acos(deltaFinal.y)*180.0F/Mth.PI);
                residual.setPos(position.add(offSet));
                player.level().addFreshEntity(residual);
            }
            prevOffSet = offSet;
            offSet = offSet.add(deltaFinal);
            surface = player.level().getBlockState(surface.offset(BlockPos.containing(offSet))).isAir() ? surface.offset(BlockPos.containing(offSet)) : surface.above();
            if(i>10){
                teleportPos = position.add(offSet);
                flag = true;
            }
            i++;
        }


        multiArmCapability.getPlayer().hurtMarked = false;
        multiArmCapability.getPlayer().setHealth(1.0F);
        if(this.refinements.contains("heart_reflex")){
            multiArmCapability.getPlayer().addEffect(new MobEffectInstance(MobEffects.ABSORPTION,100,1));
        }
        multiArmCapability.getPlayer().teleportTo(teleportPos.x,teleportPos.y+1.0F,teleportPos.z);
        if(player.level().isClientSide){
            player.level().playLocalSound(BlockPos.containing(teleportPos),BKSounds.HEART_REFLEX.get(),SoundSource.PLAYERS,10.0F,1.0F,false);
        }
        return true;
    }
}
