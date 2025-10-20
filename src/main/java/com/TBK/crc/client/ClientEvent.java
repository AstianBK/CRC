package com.TBK.crc.client;

import com.TBK.crc.CRC;
import com.TBK.crc.client.gui.MultiArmOverlay;
import com.TBK.crc.client.layer.MultiarmLayer;
import com.TBK.crc.client.model.*;
import com.TBK.crc.client.particles.ElectroExplosionParticles;
import com.TBK.crc.client.particles.LightningSparkParticles;
import com.TBK.crc.client.particles.LightningTrailParticles;
import com.TBK.crc.common.registry.BKContainers;
import com.TBK.crc.common.registry.BKParticles;
import com.TBK.crc.common.screen.CyborgTableScreen;
import com.TBK.crc.common.screen.ImplantScreen;
import com.TBK.crc.common.screen.UpgradeTableScreen;
import com.google.common.base.Suppliers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CRC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEvent {
    public static final CubeDeformation OUT_ARMOR_DEFORMATION = new CubeDeformation(2F);

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(BKContainers.TABLE_MENU.get(), CyborgTableScreen::new);
        MenuScreens.register(BKContainers.UPGRADE_MENU.get(), UpgradeTableScreen::new);
        MenuScreens.register(BKContainers.IMPLANT_MENU.get(), ImplantScreen::new);
    }
    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RobotChickenModel.LAYER_LOCATION, Suppliers.ofInstance(RobotChickenModel.createBodyLayer(CubeDeformation.NONE)));
        event.registerLayerDefinition(RobotChickenModel.ARMOR_LOCATION, Suppliers.ofInstance(RobotChickenModel.createBodyLayer(OUT_ARMOR_DEFORMATION)));

        event.registerLayerDefinition(BoomChickenModel.LAYER_LOCATION, Suppliers.ofInstance(BoomChickenModel.createBodyLayer(CubeDeformation.NONE)));
        event.registerLayerDefinition(BoomChickenModel.ARMOR_LOCATION, Suppliers.ofInstance(BoomChickenModel.createBodyLayer(OUT_ARMOR_DEFORMATION)));

        event.registerLayerDefinition(RexChickenModel.LAYER_LOCATION, Suppliers.ofInstance(RexChickenModel.createBodyLayer(CubeDeformation.NONE)) );
        event.registerLayerDefinition(RexChickenModel.ARMOR_LOCATION, Suppliers.ofInstance(RexChickenModel.createBodyLayer(OUT_ARMOR_DEFORMATION)));

        event.registerLayerDefinition(CoilChickenModel.LAYER_LOCATION, Suppliers.ofInstance(CoilChickenModel.createBodyLayer(CubeDeformation.NONE)));
        event.registerLayerDefinition(CoilChickenModel.ARMOR_LOCATION, Suppliers.ofInstance(CoilChickenModel.createBodyLayer(OUT_ARMOR_DEFORMATION)));

        event.registerLayerDefinition(DroneChickenModel.LAYER_LOCATION, Suppliers.ofInstance(DroneChickenModel.createBodyLayer(CubeDeformation.NONE)));
        event.registerLayerDefinition(DroneChickenModel.ARMOR_LOCATION, Suppliers.ofInstance(DroneChickenModel.createBodyLayer(OUT_ARMOR_DEFORMATION)));

        event.registerLayerDefinition(TheFuturePortalModel.LAYER_LOCATION, TheFuturePortalModel::createBodyLayer);
        event.registerLayerDefinition(CyborgRobotChickenModel.LAYER_LOCATION, CyborgRobotChickenModel::createBodyLayer);
        event.registerLayerDefinition(MultiArmModel.LAYER_LOCATION, MultiArmModel::createBodyLayer);
        event.registerLayerDefinition(GanchoModel.LAYER_LOCATION, GanchoModel::createBodyLayer);

    }



    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.AddLayers event){
        event.getSkins().forEach(s -> {
            event.getSkin(s).addLayer(new MultiarmLayer(event.getSkin(s),event.getContext().getItemInHandRenderer()));
            //event.getSkin(s).addLayer(new PassiveLayer(event.getSkin(s)));
            //event.getSkin(s).addLayer(new GanchoLayer(event.getSkin(s)));
            //event.getSkin(s).addLayer(new LivingProtectionLayer(event.getSkin(s)));
        });

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        if(BKParticles.LIGHTNING_TRAIL_PARTICLES.isPresent()){
            event.registerSpriteSet(BKParticles.LIGHTNING_TRAIL_PARTICLES.get(), LightningTrailParticles.Factory::new);
        }
        if(BKParticles.ELECTRO_EXPLOSION_PARTICLES.isPresent()){
            event.registerSpriteSet(BKParticles.ELECTRO_EXPLOSION_PARTICLES.get(), ElectroExplosionParticles.Factory::new);
        }
        if(BKParticles.LIGHTNING_SPARK_PARTICLES.isPresent()){
            event.registerSpriteSet(BKParticles.LIGHTNING_SPARK_PARTICLES.get(), LightningSparkParticles.Factory::new);
        }
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
