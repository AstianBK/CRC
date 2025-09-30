package com.TBK.crc.client.layer;

import com.TBK.crc.client.model.RexChickenModel;
import com.TBK.crc.server.entity.RexChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

public class ShieldLayer<T extends RexChicken,M extends RexChickenModel<T>> extends EnergySwirlLayer<T, M> {
    private static final ResourceLocation WITHER_ARMOR_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final M model;

    public ShieldLayer(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
        this.model = (M) new RexChickenModel(Minecraft.getInstance().getEntityModels().bakeLayer(RexChickenModel.ARMOR_LOCATION));
    }

    protected float xOffset(float p_116683_) {
        return p_116683_ * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return WITHER_ARMOR_LOCATION;
    }

    protected EntityModel<T> model() {
        return this.model;
    }
}
