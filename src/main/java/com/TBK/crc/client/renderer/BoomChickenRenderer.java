package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.model.BoomChickenModel;
import com.TBK.crc.client.model.CyborgRobotChickenModel;
import com.TBK.crc.server.entity.BoomChicken;
import com.TBK.crc.server.entity.CyborgRobotChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class BoomChickenRenderer<T extends BoomChicken,M extends BoomChickenModel<T>> extends LivingEntityRenderer<T,M> {
    public BoomChickenRenderer(EntityRendererProvider.Context p_174289_) {
        super(p_174289_, (M) new BoomChickenModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BoomChickenModel.LAYER_LOCATION)), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return new ResourceLocation(CRC.MODID,"textures/entity/boom_chicken/boom_chicken.png");
    }
}
