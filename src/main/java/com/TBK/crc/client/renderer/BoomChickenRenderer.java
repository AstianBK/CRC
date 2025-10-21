package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.layer.RobotChickenShieldLayer;
import com.TBK.crc.client.model.BoomChickenModel;
import com.TBK.crc.client.model.CyborgRobotChickenModel;
import com.TBK.crc.server.entity.BoomChicken;
import com.TBK.crc.server.entity.CyborgRobotChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BoomChickenRenderer<T extends BoomChicken,M extends BoomChickenModel<T>> extends LivingEntityRenderer<T,M> {
    public BoomChickenRenderer(EntityRendererProvider.Context p_174289_) {
        super(p_174289_, (M) new BoomChickenModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BoomChickenModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new RobotChickenShieldLayer(this,new BoomChickenModel(Minecraft.getInstance().getEntityModels().bakeLayer(BoomChickenModel.ARMOR_LOCATION))));
        this.shadowRadius = 0.25F;

    }

    @Override
    protected void renderNameTag(T p_114498_, Component p_114499_, PoseStack p_114500_, MultiBufferSource p_114501_, int p_114502_) {
        super.renderNameTag(p_114498_, p_114499_, p_114500_, p_114501_, p_114502_);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return new ResourceLocation(CRC.MODID,"textures/entity/minion_chicken/minion_chicken.png");
    }
}
