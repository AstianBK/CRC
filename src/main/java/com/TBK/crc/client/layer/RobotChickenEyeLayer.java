package com.TBK.crc.client.layer;

import com.TBK.crc.CRC;
import com.TBK.crc.server.entity.RobotChicken;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class RobotChickenEyeLayer<T extends RobotChicken,M extends HierarchicalModel<T>> extends EyesLayer<T,M> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CRC.MODID,"textures/entity/minion_chicken/minion_chicken_glowing.png");
    private final M model;

    public RobotChickenEyeLayer(RenderLayerParent<T, M> p_117346_, M model) {
        super(p_117346_);
        this.model = model;

    }


    @Override
    public RenderType renderType() {
        return RenderType.eyes(TEXTURE);
    }
}
