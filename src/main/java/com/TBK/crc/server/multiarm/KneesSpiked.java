package com.TBK.crc.server.multiarm;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class KneesSpiked extends PassivePart{
    public KneesSpiked() {
        super("knees_spiked",false);
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
    public void tick(MultiArmCapability multiArmCapability) {
        Player player = multiArmCapability.getPlayer();
        if(!player.onGround()){
            for (LivingEntity living : player.level().getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(1.5F),e->e!=player)){
                living.hurt(player.damageSources().playerAttack(player),5.0F);
            }
        }
    }
}
