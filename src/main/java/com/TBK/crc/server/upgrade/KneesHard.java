package com.TBK.crc.server.upgrade;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class KneesHard extends PassivePart{
    public KneesHard() {
        super("knees_hard",false);
    }

    @Override
    public boolean canActive(MultiArmCapability multiArmCapability) {
        return false;
    }

    @Override
    public boolean handlerPassive(MultiArmCapability multiArmCapability, Entity source) {
        return false;
    }

    @Override
    public void onHurt(MultiArmCapability multiArmCapability, LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_FALL)){
            event.setAmount(event.getAmount()*0.7F);
        }
    }
}
