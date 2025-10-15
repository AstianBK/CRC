package com.TBK.crc.common.mixin;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingRendererMixin <T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(method = "setupRotations",at = @At("TAIL"))
    public void setSkillPose(T p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_, CallbackInfo ci){
        if(p_115317_ instanceof Player player){

            MultiArmCapability cap = MultiArmCapability.get(player);
            if(cap!=null){
                switch (cap.pose){
                    case DASH_CLAWS -> {
                        p_115318_.translate(0,1.0F,0);
                        Vec3 direction = player.getViewVector(1.0F);
                        float rotX = (float) Math.acos(direction.y);

                        p_115318_.mulPose(Axis.XP.rotationDegrees(-rotX*180.0F/Mth.PI));

                    }
                }
            }
        }
    }
}
