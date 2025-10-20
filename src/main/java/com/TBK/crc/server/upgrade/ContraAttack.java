package com.TBK.crc.server.upgrade;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketHandlerPowers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ContraAttack extends PassivePart{
    public float damageActually = 0 ;
    public boolean contraAttack = false;
    public ContraAttack() {
        super("contra_attack", false);
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);
    }


    public float getMaxDamage(){
        return this.refinements.contains("counter_attack_attack") ? 30.0F : 20.0F;
    }
    @Override
    public void onHurt(MultiArmCapability multiArmCapability, LivingHurtEvent event) {
        if(event.getSource().getEntity()==null)return;
        float amount = event.getAmount();
        DamageSource source = event.getSource();
        if(!this.contraAttack){
            float damage = Math.min(this.damageActually + amount,20.0F);
            if(damage==20.0F){
                this.contraAttack = true;
            }
            this.damageActually = damage;
        }else {
            Entity sourceEntity = source.getEntity();
            Player player = multiArmCapability.getPlayer();
            if(sourceEntity instanceof LivingEntity living){
                for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(4),e->e!=player)){
                    entity.invulnerableTime = 0;
                    entity.hurt(living.damageSources().generic(),getMaxDamage());
                }

                if(!player.level().isClientSide){
                    PacketHandler.sendToAllTracking(new PacketHandlerPowers(6,player,player), living);
                }
                this.damageActually = 0.0F;
                this.contraAttack = false;
            }
        }

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
