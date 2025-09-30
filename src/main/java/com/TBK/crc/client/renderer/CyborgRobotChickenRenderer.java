package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.model.CyborgRobotChickenModel;
import com.TBK.crc.server.entity.CyborgRobotChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class CyborgRobotChickenRenderer<T extends CyborgRobotChicken,M extends CyborgRobotChickenModel<T>> extends LivingEntityRenderer<T,M> {
    public CyborgRobotChickenRenderer(EntityRendererProvider.Context p_174289_) {
        super(p_174289_, (M) new CyborgRobotChickenModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CyborgRobotChickenModel.LAYER_LOCATION)), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return new ResourceLocation(CRC.MODID,"textures/entity/cyborg_robot_chicken/cyborg_robot_chicken.png");
    }
}
