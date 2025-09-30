package com.TBK.crc;

import com.TBK.crc.client.renderer.*;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.server.entity.GanchoEntity;
import com.TBK.crc.server.entity.RexChicken;
import com.TBK.crc.server.network.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CRC.MODID)
public class CRC
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "crc";
    public static double x=0;
    public static double y=0;
    public static double z=0;
    public static double xq=0;
    public static double yq=0;
    public static double zq=0;
    public static final Logger LOGGER = LogUtils.getLogger();

    public CRC()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        BKEntityType.ENTITY_TYPES.register(modEventBus);
        PacketHandler.registerMessages();
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            modEventBus.addListener(this::registerRenderers);
        });
    }
    @OnlyIn(Dist.CLIENT)
    public void registerRenderers(FMLCommonSetupEvent event){
        EntityRenderers.register(BKEntityType.TELEPORT.get(), TeleportRenderer::new);
        EntityRenderers.register(BKEntityType.REX_CHICKEN.get(), RexChickenRenderer::new);
        EntityRenderers.register(BKEntityType.GANCHO.get(), GanchoRenderer::new);

        EntityRenderers.register(BKEntityType.BOOM_CHICKEN.get(), BoomChickenRenderer::new);

        EntityRenderers.register(BKEntityType.CYBORG_ROBOT_CHICKEN.get(), CyborgRobotChickenRenderer::new);
    }
}
