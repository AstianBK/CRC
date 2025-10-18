package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.layer.RobotChickenShieldLayer;
import com.TBK.crc.client.model.BoomChickenModel;
import com.TBK.crc.client.model.CoilChickenModel;
import com.TBK.crc.client.model.CyborgRobotChickenModel;
import com.TBK.crc.server.entity.BoomChicken;
import com.TBK.crc.server.entity.CoilChicken;
import com.TBK.crc.server.entity.CyborgRobotChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class CoilChickenRenderer<T extends CoilChicken,M extends CoilChickenModel<T>> extends LivingEntityRenderer<T,M> {

    public CoilChickenRenderer(EntityRendererProvider.Context p_174289_) {
        super(p_174289_, (M) new CoilChickenModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(CoilChickenModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new RobotChickenShieldLayer(this,new CoilChickenModel(Minecraft.getInstance().getEntityModels().bakeLayer(CoilChickenModel.ARMOR_LOCATION))));

    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return new ResourceLocation(CRC.MODID,"textures/entity/minion_chicken/minion_chicken.png");
    }
}
