package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.layer.RobotChickenShieldLayer;
import com.TBK.crc.client.model.RobotChickenModel;
import com.TBK.crc.server.entity.PunchChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PunchChickenRenderer<T extends PunchChicken,M extends RobotChickenModel<T>> extends LivingEntityRenderer<T,M> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(CRC.MODID,"textures/entity/punch_chicken/punch_chicken.png");
    public PunchChickenRenderer(EntityRendererProvider.Context p_174289_) {
        super(p_174289_, (M) new RobotChickenModel(Minecraft.getInstance().getEntityModels().bakeLayer(RobotChickenModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new RobotChickenShieldLayer(this,new RobotChickenModel(Minecraft.getInstance().getEntityModels().bakeLayer(RobotChickenModel.ARMOR_LOCATION))));
        this.shadowRadius = 0.25F;

    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return TEXTURES;
    }
}
