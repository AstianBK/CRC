package com.TBK.crc.server.upgrade;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;

public class CoilFeet extends PassivePart{
    public CoilFeet() {
        super("coil_feet", false);
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

    public float getJumpBoost() {
        return this.refinements.contains("coil_feet") ? 2.0F : 1.5F;
    }
}
