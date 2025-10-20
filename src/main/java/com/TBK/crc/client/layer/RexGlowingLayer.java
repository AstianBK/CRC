package com.TBK.crc.client.layer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.model.RexChickenModel;
import com.TBK.crc.server.entity.RexChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RexGlowingLayer<T extends RexChicken,M extends RexChickenModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation GLOWING = new ResourceLocation(CRC.MODID,"textures/entity/rex_chicken/rex_chicken_glowing.png");
    private final M model;

    public RexGlowingLayer(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
        this.model = (M) new RexChickenModel(Minecraft.getInstance().getEntityModels().bakeLayer(RexChickenModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, T p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        VertexConsumer consumer = p_117350_.getBuffer(RenderType.eyes(GLOWING));

        this.model.renderToBuffer(p_117349_,consumer,p_117351_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);
    }
}
