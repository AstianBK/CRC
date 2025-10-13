package com.TBK.crc.server.multiarm;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;

public class RabbitFeet extends PassivePart{
    public RabbitFeet() {
        super("rabbit_feet", false);
    }

    @Override
    public boolean hasEffect(MultiArmCapability multiArmCapability, MobEffect effect) {
        return effect == MobEffects.JUMP;
    }

    @Override
    public boolean canActive(MultiArmCapability multiArmCapability) {
        return false;
    }

    @Override
    public boolean handlerPassive(MultiArmCapability multiArmCapability, Entity source) {
        return false;
    }
}
