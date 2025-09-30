package com.TBK.crc.client;

import com.TBK.crc.CRC;
import com.TBK.crc.client.gui.MultiArmOverlay;
import com.TBK.crc.client.layer.GanchoLayer;
import com.TBK.crc.client.layer.MultiarmLayer;
import com.TBK.crc.client.model.*;
import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CRC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEvent {
    public static final CubeDeformation OUT_ARMOR_DEFORMATION = new CubeDeformation(2F);


    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BoomChickenModel.LAYER_LOCATION, BoomChickenModel::createBodyLayer);
        event.registerLayerDefinition(RexChickenModel.LAYER_LOCATION, Suppliers.ofInstance(RexChickenModel.createBodyLayer(CubeDeformation.NONE)) );
        event.registerLayerDefinition(RexChickenModel.ARMOR_LOCATION, Suppliers.ofInstance(RexChickenModel.createBodyLayer(OUT_ARMOR_DEFORMATION)));
        event.registerLayerDefinition(GanchoModel.LAYER_LOCATION, GanchoModel::createBodyLayer);

        event.registerLayerDefinition(CyborgRobotChickenModel.LAYER_LOCATION, CyborgRobotChickenModel::createBodyLayer);
        event.registerLayerDefinition(MultiArmModel.LAYER_LOCATION, MultiArmModel::createBodyLayer);
    }



    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.AddLayers event){
        event.getSkins().forEach(s -> {
            event.getSkin(s).addLayer(new MultiarmLayer(event.getSkin(s)));
            //event.getSkin(s).addLayer(new GanchoLayer(event.getSkin(s)));
            //event.getSkin(s).addLayer(new LivingProtectionLayer(event.getSkin(s)));
        });
        Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(s->{
            if(s instanceof LivingEntityRenderer<?,?> l){
               // l.addLayer(new GanchoLayer(l));
            }

        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerArmorRenderers(EntityRenderersEvent.AddLayers event){

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerGui(RegisterGuiOverlaysEvent event){
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "skill_hotbar",new MultiArmOverlay());

    }
}
