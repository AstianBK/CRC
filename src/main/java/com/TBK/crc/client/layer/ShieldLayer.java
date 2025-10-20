package com.TBK.crc.client.layer;

import com.TBK.crc.client.model.RexChickenModel;
import com.TBK.crc.server.entity.RexChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

public class ShieldLayer<T extends RexChicken,M extends RexChickenModel<T>> extends EnergySwirlLayer<T, M> {
    private static final ResourceLocation WITHER_ARMOR_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final M model;

    public ShieldLayer(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
        this.model = (M) new RexChickenModel(Minecraft.getInstance().getEntityModels().bakeLayer(RexChickenModel.ARMOR_LOCATION));
    }
    public void render(PoseStack p_116970_, MultiBufferSource p_116971_, int p_116972_, T p_116973_, float p_116974_, float p_116975_, float p_116976_, float p_116977_, float p_116978_, float p_116979_) {
        if (p_116973_.isPowered()) {
            float f = (float)p_116973_.tickCount + p_116976_;
            EntityModel<T> entitymodel = this.model();
            entitymodel.prepareMobModel(p_116973_, p_116974_, p_116975_, p_116976_);
            this.getParentModel().copyPropertiesTo(entitymodel);
            VertexConsumer vertexconsumer = p_116971_.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(f) % 1.0F, f * 0.01F % 1.0F));
            entitymodel.setupAnim(p_116973_, p_116974_, p_116975_, p_116977_, p_116978_, p_116979_);
            float g = 1.0F;
            float b = 1.0F;
            if(p_116973_.hurtShield>0){
                g = 0;
                b = 0;
            }
            entitymodel.renderToBuffer(p_116970_, vertexconsumer, p_116972_, OverlayTexture.NO_OVERLAY, 0.5F, g, b, 1.0F);
        }
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
