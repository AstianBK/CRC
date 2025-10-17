package com.TBK.crc.common.mixin;

import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "getJumpPower",at = @At("RETURN"),cancellable = true)
    public void onJump (CallbackInfoReturnable<Float> cir){
        if((Object)this instanceof Player player){
            if(MultiArmCapability.hasEffect(MobEffects.JUMP,player)){
                float original = cir.getReturnValue();
                cir.setReturnValue(original*1.5F);
            }
        }
    }
}
