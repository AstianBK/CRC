package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.layer.RobotChickenShieldLayer;
import com.TBK.crc.client.model.BoomChickenModel;
import com.TBK.crc.client.model.CyborgRobotChickenModel;
import com.TBK.crc.client.model.DroneChickenModel;
import com.TBK.crc.server.entity.BoomChicken;
import com.TBK.crc.server.entity.CyborgRobotChicken;
import com.TBK.crc.server.entity.DroneChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class DroneChickenRenderer<T extends DroneChicken,M extends DroneChickenModel<T>> extends LivingEntityRenderer<T,M> {

    public DroneChickenRenderer(EntityRendererProvider.Context p_174289_) {
        super(p_174289_, (M) new DroneChickenModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DroneChickenModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new RobotChickenShieldLayer(this,new DroneChickenModel(Minecraft.getInstance().getEntityModels().bakeLayer(DroneChickenModel.ARMOR_LOCATION))));
        this.shadowRadius = 0.25F;
    }
    @Override
    protected boolean shouldShowName(T p_115333_) {
        return false;
    }
    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return new ResourceLocation(CRC.MODID,"textures/entity/minion_chicken/minion_chicken.png");
    }
}
