package com.TBK.crc.common.mixin;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;


@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",at = @At("HEAD"))
    public void armLayer(T p_102866_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_, CallbackInfo ci){
        if (((Object)this) instanceof PlayerModel<?> model && p_102866_ instanceof Player player){
            MultiArmCapability cap = MultiArmCapability.get(player);
            if(cap!=null && Util.hasMultiArm(cap)){
                model.rightArm.visible = false;
            }
        }
    }

    @Inject(method = "poseRightArm",at = @At("HEAD"))
    public void setup(T p_102876_, CallbackInfo ci){
        if (((Object)this) instanceof PlayerModel<?> model && p_102876_ instanceof Player player){
            MultiArmCapability cap = MultiArmCapability.get(player);
            if(cap!=null && Util.hasMultiArm(cap)){
                switch (cap.pose){
                    case DASH_CLAWS -> {
                        model.rightArm.xRot = (float) (180.0F*(Math.PI/180.0F));
                    }
                    case CHARGE_CLAWS -> {
                        model.rightArm.xRot = (float) (-90.0F*(Math.PI/180.0F));
                    }
                }
                model.rightArm.visible = false;
                model.rightArm.xScale=0;
                model.rightArm.yScale=0;
                model.rightArm.zScale=0;
            }
        }
    }
}
