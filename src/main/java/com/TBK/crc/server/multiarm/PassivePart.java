package com.TBK.crc.server.multiarm;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketHandlerPowers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public abstract class PassivePart extends MultiArmSkillAbstract{

    public boolean activable;
    public PassivePart(String name,boolean activable) {
        super(name, 200, false, false);
        this.activable = activable;
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);

        if (this.activable && canActive(multiArmCapability)){
            handlerPassive(multiArmCapability,multiArmCapability.getPlayer());
        }
    }

    public abstract boolean canActive(MultiArmCapability multiArmCapability);

    public abstract boolean handlerPassive(MultiArmCapability multiArmCapability,Entity source);

    public boolean onDie(MultiArmCapability multiArmCapability, Entity source){
        if(!multiArmCapability.getPlayer().level().isClientSide && source!=null){
            PacketHandler.sendToAllTracking(new PacketHandlerPowers(5,source,multiArmCapability.getPlayer()), (LivingEntity) source);
        }
        if(canActive(multiArmCapability)){
            this.startAbility(multiArmCapability);
            return handlerPassive(multiArmCapability,source);
        }

        return false;
    }

}
