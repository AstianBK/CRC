package com.TBK.crc;

import com.TBK.crc.client.renderer.*;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.block.CyborgTableBlock;
import com.TBK.crc.common.registry.*;
import com.TBK.crc.server.capability.CRCCapability;
import com.TBK.crc.server.network.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static final List<ResourceLocation> DESTROY_STAGES = IntStream.range(0, 10).mapToObj((p_119253_) -> {
        return new ResourceLocation(MODID,"groundeffect/destroy_stage_" + p_119253_);
    }).collect(Collectors.toList());
    public static final List<ResourceLocation> BREAKING_LOCATIONS = DESTROY_STAGES.stream().map((p_119371_) -> {
        return new ResourceLocation(MODID,"textures/" + p_119371_.getPath() + ".png");
    }).collect(Collectors.toList());
    public static final List<RenderType> DESTROY_TYPES = BREAKING_LOCATIONS.stream().map(RenderType::crumbling).collect(Collectors.toList());

    public static final List<ResourceLocation> GUI_STAGES = IntStream.range(0, 155).mapToObj((p_119253_) -> {
        return new ResourceLocation(MODID,"gui/cyborg_table/cyborg_table_gui_" + p_119253_);
    }).collect(Collectors.toList());
    public static final List<ResourceLocation> GUI_LOCATIONS = GUI_STAGES.stream().map((p_119371_) -> {
        return new ResourceLocation(MODID,"textures/" + p_119371_.getPath() + ".png");
    }).collect(Collectors.toList());
    public static final List<ResourceLocation> GUI_IMPLANT_STAGES = IntStream.range(0, 73).mapToObj((p_119253_) -> {
        return new ResourceLocation(MODID,"gui/upgrade_table/cyborg_upgrade_table_gui_" + p_119253_);
    }).collect(Collectors.toList());
    public static final List<ResourceLocation> GUI_IMPLANT_LOCATIONS = GUI_IMPLANT_STAGES.stream().map((p_119371_) -> {
        return new ResourceLocation(MODID,"textures/" + p_119371_.getPath() + ".png");
    }).collect(Collectors.toList());

    public static final List<ResourceLocation> NIGHT_VISION_STAGES = IntStream.range(0, 95).mapToObj((p_119253_) -> {
        return new ResourceLocation(MODID,"gui/window_effect/night_eye_overlay_" + p_119253_);
    }).collect(Collectors.toList());
    public static final List<ResourceLocation> NIGHT_VISION_LOCATIONS = NIGHT_VISION_STAGES.stream().map((p_119371_) -> {
        return new ResourceLocation(MODID,"textures/" + p_119371_.getPath() + ".png");
    }).collect(Collectors.toList());
    public CRC()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        BKEntityType.ENTITY_TYPES.register(modEventBus);
        BKParticles.PARTICLE_TYPES.register(modEventBus);
        BKItems.ITEMS.register(modEventBus);
        BKBlocks.BLOCKS.register(modEventBus);
        BKCreativeModeTab.TABS.register(modEventBus);
        BKContainers.CONTAINERS.register(modEventBus);
        BKSounds.register(modEventBus);
        PacketHandler.registerMessages();
        modEventBus.addListener(CRCCapability::registerCapabilities);
        Util.initUpgrades();

        MinecraftForge.EVENT_BUS.addListener(this::sleepLocationCheck);
        MinecraftForge.EVENT_BUS.addListener(this::sleepTimeCheck);
        MinecraftForge.EVENT_BUS.addListener(this::sleepTimeFinish);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            modEventBus.addListener(this::registerRenderers);
        });
    }

    public void startServer(ServerStartedEvent event){
    }

    public void sleepLocationCheck(@NotNull SleepingLocationCheckEvent event) {
        if (event.getEntity().level().getBlockState(event.getSleepingLocation()).getBlock() instanceof CyborgTableBlock) {
            event.setResult(Event.Result.ALLOW);
        }
    }
    public void sleepTimeCheck(@NotNull SleepingTimeCheckEvent event) {
        event.getSleepingLocation().ifPresent((blockPos -> {
            if (event.getEntity().level().getBlockState(blockPos).getBlock() instanceof CyborgTableBlock) {
                event.setResult(Event.Result.ALLOW);
            }}));

    }

    public void sleepTimeFinish(@NotNull SleepFinishedTimeEvent event) {
        if (event.getLevel() instanceof ServerLevel) {
            boolean sleepingInCoffin = event.getLevel().players().stream().anyMatch(player -> {
                Optional<BlockPos> pos = player.getSleepingPos();
                return pos.isPresent() && event.getLevel().getBlockState(pos.get()).getBlock() instanceof CyborgTableBlock;
            });
            if (sleepingInCoffin) {
                event.setTimeAddition(0);
            }
        }
    }


    public static MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @OnlyIn(Dist.CLIENT)
    public void registerRenderers(FMLCommonSetupEvent event){
        EntityRenderers.register(BKEntityType.TELEPORT.get(), TeleportRenderer::new);
        EntityRenderers.register(BKEntityType.PORTAL.get(), PortalRenderer::new);
        EntityRenderers.register(BKEntityType.REX_CHICKEN.get(), RexChickenRenderer::new);
        EntityRenderers.register(BKEntityType.CRACKING_BEAM.get(), BeamExplosionRenderer::new);

        EntityRenderers.register(BKEntityType.GANCHO.get(), GanchoRenderer::new);
        EntityRenderers.register(BKEntityType.ELECTRO.get(), ElectroProjectileRenderer::new);
        EntityRenderers.register(BKEntityType.RESIDUAL.get(), ResidualRenderer::new);

        EntityRenderers.register(BKEntityType.BOOM_CHICKEN.get(), BoomChickenRenderer::new);
        EntityRenderers.register(BKEntityType.COIL_CHICKEN.get(), CoilChickenRenderer::new);
        EntityRenderers.register(BKEntityType.DRONE_CHICKEN.get(), DroneChickenRenderer::new);
        EntityRenderers.register(BKEntityType.PUNCH_CHICKEN.get(), PunchChickenRenderer::new);


        EntityRenderers.register(BKEntityType.CYBORG_ROBOT_CHICKEN.get(), CyborgRobotChickenRenderer::new);

        BlockEntityRenderers.register(BlockEntityType.CHEST,GlowingBlockRenderer::new);

    }
}
