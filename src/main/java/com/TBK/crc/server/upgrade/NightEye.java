package com.TBK.crc.server.upgrade;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;

public class NightEye extends PassivePart{
    public NightEye() {
        super("night_eye", false);
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);

    }

    @Override
    public boolean hasEffect(MultiArmCapability multiArmCapability, MobEffect effect) {
        if(effect == MobEffects.NIGHT_VISION){
            Player player = multiArmCapability.getPlayer();
            if(player.level().getBrightness(LightLayer.SKY,player.getOnPos().above())>0){
                return false;
            }
            if(player.level().getBrightness(LightLayer.BLOCK,player.getOnPos().above())>0){
                return false;
            }
            for (BlockPos pos : BlockPos.betweenClosed(player.getOnPos().offset(1,3,1), player.getOnPos().offset(1,0,1))){
                if (player.level().getBrightness(LightLayer.BLOCK,pos)>0){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public float[] getWindowsColor(MultiArmCapability multiArmCapability) {
        return hasEffect(multiArmCapability,MobEffects.NIGHT_VISION) ? new float[]{0.0F,1.0F,0.0F,0.5F} : new float[]{};
    }

    @Override
    public boolean canActive(MultiArmCapability multiArmCapability) {
        return true;
    }

    @Override
    public boolean handlerPassive(MultiArmCapability multiArmCapability, Entity source) {
        return false;
    }
}
