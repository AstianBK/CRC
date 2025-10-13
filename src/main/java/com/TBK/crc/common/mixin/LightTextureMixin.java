package com.TBK.crc.common.mixin;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffects;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightTexture.class)
public class LightTextureMixin {
    @Shadow @Final private Minecraft minecraft;
    @ModifyVariable(method = "updateLightTexture", at = @At(value = "STORE"), ordinal = 7)
    private float modifyBrightness(float original) {
        return MultiArmCapability.hasEffect(MobEffects.NIGHT_VISION,this.minecraft.player) ? 1.0F : original;
    }
}
