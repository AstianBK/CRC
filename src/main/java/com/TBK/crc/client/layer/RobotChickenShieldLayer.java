package com.TBK.crc.client.layer;

import com.TBK.crc.client.model.RexChickenModel;
import com.TBK.crc.server.entity.RexChicken;
import com.TBK.crc.server.entity.RobotChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class RobotChickenShieldLayer<T extends RobotChicken,M extends HierarchicalModel<T>> extends EnergySwirlLayer<T, M> {
    private static final ResourceLocation WITHER_ARMOR_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final M model;

    public RobotChickenShieldLayer(RenderLayerParent<T, M> p_117346_,M model) {
        super(p_117346_);
        this.model = model;
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
