package com.TBK.crc.server.multiarm;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

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
            if (player.level().getLightEmission(player.getOnPos())<3){
                return true;
            }
        }
        return super.hasEffect(multiArmCapability, effect);
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
