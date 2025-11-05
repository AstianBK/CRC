package com.TBK.crc.common.registry;

import com.TBK.crc.CRC;
import com.TBK.crc.server.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BKEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CRC.MODID);

    public static final RegistryObject<EntityType<RexChicken>> REX_CHICKEN = ENTITY_TYPES
            .register("rex_chicken", () -> EntityType.Builder.<RexChicken>of(RexChicken::new, MobCategory.MISC).clientTrackingRange(10)
                    .fireImmune().sized(2F, 2F).build(CRC.MODID+ "rex_chicken"));

    public static final RegistryObject<EntityType<PortalEntity>> PORTAL = ENTITY_TYPES
            .register("portal", () -> EntityType.Builder.<PortalEntity>of(PortalEntity::new, MobCategory.MISC)
                    .fireImmune().sized(1.5F, 2.0F).build(CRC.MODID+ "portal"));

    public static final RegistryObject<EntityType<PunchChicken>> PUNCH_CHICKEN = ENTITY_TYPES
            .register("punch_chicken", () -> EntityType.Builder.<PunchChicken>of(PunchChicken::new, MobCategory.MISC)
                    .fireImmune().sized(1F, 0.8F).build(CRC.MODID+ "punch_chicken"));

    public static final RegistryObject<EntityType<CyborgRobotChicken>> CYBORG_ROBOT_CHICKEN = ENTITY_TYPES
            .register("cyborg_robot_chicken", () -> EntityType.Builder.<CyborgRobotChicken>of(CyborgRobotChicken::new, MobCategory.MISC).noSummon()
                    .fireImmune().sized(1F, 0.8F).build(CRC.MODID+ "cyborg_robot_chicken"));
    public static final RegistryObject<EntityType<BoomChicken>> BOOM_CHICKEN = ENTITY_TYPES
            .register("boom_chicken", () -> EntityType.Builder.<BoomChicken>of(BoomChicken::new, MobCategory.MISC)
                    .fireImmune().sized(1F, 0.8F).build(CRC.MODID+ "boom_chicken"));

    public static final RegistryObject<EntityType<CoilChicken>> COIL_CHICKEN = ENTITY_TYPES
            .register("coil_chicken", () -> EntityType.Builder.<CoilChicken>of(CoilChicken::new, MobCategory.MISC)
                    .fireImmune().sized(1F, 0.8F).build(CRC.MODID+ "coil_chicken"));

    public static final RegistryObject<EntityType<DroneChicken>> DRONE_CHICKEN = ENTITY_TYPES
            .register("drone_chicken", () -> EntityType.Builder.<DroneChicken>of(DroneChicken::new, MobCategory.MISC)
                    .fireImmune().sized(0.8F, 0.8F).build(CRC.MODID+ "drone_chicken"));

    public static final RegistryObject<EntityType<TeleportEntity>> TELEPORT = ENTITY_TYPES
            .register("teleport", () -> EntityType.Builder.<TeleportEntity>of(TeleportEntity::new, MobCategory.MISC).noSummon()
                    .fireImmune().sized(0.2F, 0.2F).build(CRC.MODID+ "teleport"));
    public static final RegistryObject<EntityType<GanchoEntity>> GANCHO = ENTITY_TYPES
            .register("gancho", () -> EntityType.Builder.<GanchoEntity>of(GanchoEntity::new, MobCategory.MISC)
                    .fireImmune().sized(0.2F, 0.2F).build(CRC.MODID+ "gancho"));

    public static final RegistryObject<EntityType<ElectroProjectile>> ELECTRO = ENTITY_TYPES
            .register("electro", () -> EntityType.Builder.<ElectroProjectile>of(ElectroProjectile::new, MobCategory.MISC)
                    .fireImmune().sized(0.2F, 0.2F).build(CRC.MODID+ "electro"));

    public static final RegistryObject<EntityType<BeamExplosionEntity>> CRACKING_BEAM = ENTITY_TYPES
            .register("cracking_beam", () -> EntityType.Builder.<BeamExplosionEntity>of(BeamExplosionEntity::new, MobCategory.MISC)
                    .fireImmune().sized(0.2F, 0.2F).build(CRC.MODID+ "cracking_beam"));

    public static final RegistryObject<EntityType<ResidualEntity>> RESIDUAL = ENTITY_TYPES
            .register("residual", () -> EntityType.Builder.<ResidualEntity>of(ResidualEntity::new, MobCategory.MISC)
                    .fireImmune().sized(0.6F, 1.95F).build(CRC.MODID+ "residual"));


}
