package com.TBK.crc.common.mixin;

import com.TBK.crc.common.registry.BKSounds;
import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.sounds.SoundSource;
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
    public void modifyJump (CallbackInfoReturnable<Float> cir){
        if((Object)this instanceof Player player){
            if(MultiArmCapability.hasEffect(MobEffects.JUMP,player)){
                float original = cir.getReturnValue();
                cir.setReturnValue(original*MultiArmCapability.getJumpBoost(player));
            }
        }
    }

    @Inject(method = "jumpFromGround",at = @At("HEAD"),cancellable = true)
    public void onJump (CallbackInfo ci){
        if((Object)this instanceof Player player){
            if(MultiArmCapability.hasEffect(MobEffects.JUMP,player)){
                player.level().playSound(null,player.blockPosition(), player.level().random.nextBoolean() ? BKSounds.CHICKEN_COIL_MOVE_1.get() : BKSounds.CHICKEN_COIL_MOVE_2.get() , SoundSource.PLAYERS);
            }
        }
    }
}
