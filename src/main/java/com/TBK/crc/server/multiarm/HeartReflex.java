package com.TBK.crc.server.multiarm;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class HeartReflex extends PassivePart{
    public double oldX = 0.0F;
    public double oldY = 0.0F;
    public double oldZ = 0.0F;

    public double x = 0.0F;
    public double y = 0.0F;
    public double z = 0.0F;
    public int effectTimer0 = 0;
    public int effectTimer = 0;
    public HeartReflex() {
        super("heart_reflex", true);
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        //super.tick(multiArmCapability);
        if(multiArmCapability.getPlayer().level().isClientSide){
            //CRC.LOGGER.debug("Tick :"+this.effectTimer + " old :" +this.effectTimer0);
        }
        this.effectTimer0 = this.effectTimer;

        if (effectTimer>0){
            this.effectTimer--;
        }
    }




    public float getEffectTimer(float partialTick) {
        return Mth.lerp(partialTick,this.effectTimer0,this.effectTimer);
    }

    @Override
    public boolean canActive(MultiArmCapability multiArmCapability) {
        return !multiArmCapability.getCooldowns().isOnCooldown(this);
    }
    public Vec3 getPos(){
        return new Vec3(x,y,z);
    }
    public Vec3 getOldPos(){
        return new Vec3(oldX,oldY,oldZ);
    }
    @Override
    public boolean handlerPassive(MultiArmCapability multiArmCapability, Entity source) {
        Player player = multiArmCapability.getPlayer();
        Vec3 position = player.position();
        Vec3 delta = player.getDeltaMovement().multiply(1.0f,0.0f,1.0f);
        if(delta.length()<0.01D){
            delta = player.getViewVector(1.0F).multiply(1.0f,0.0f,1.0f);
        }
        Vec3 offSet = Vec3.ZERO;
        Vec3 teleportPos = position;
        boolean flag = false;
        int i = 0;
        while (!flag){
            offSet = offSet.add(delta);
            if(!Util.isSafePosition(multiArmCapability.getPlayer().level(), position.add(offSet))){
                teleportPos = position.add(offSet.subtract(delta));
                break;
            }
            if(i>10){
                teleportPos = position.add(offSet);
                flag = true;
            }
            i++;
        }
        if(multiArmCapability.getPlayer().level().isClientSide){
            CRC.LOGGER.debug("Client");
        }else {
            CRC.LOGGER.debug("Server");
        }
        this.oldX = position.x;
        this.oldY = position.y;
        this.oldZ = position.z;
        this.x = teleportPos.x;
        this.y = teleportPos.y;
        this.z = teleportPos.z;
        this.effectTimer = 20;
        this.effectTimer0 = 20;
        multiArmCapability.getPlayer().setHealth(1.0F);
        multiArmCapability.getPlayer().teleportTo(teleportPos.x,teleportPos.y,teleportPos.z);
        return true;
    }
}
