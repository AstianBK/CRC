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
            .register("rex_chicken", () -> EntityType.Builder.<RexChicken>of(RexChicken::new, MobCategory.MISC)
                    .fireImmune().sized(2F, 2F).build(CRC.MODID+ "rex_chicken"));

    public static final RegistryObject<EntityType<CyborgRobotChicken>> CYBORG_ROBOT_CHICKEN = ENTITY_TYPES
            .register("cyborg_robot_chicken", () -> EntityType.Builder.<CyborgRobotChicken>of(CyborgRobotChicken::new, MobCategory.MISC)
                    .fireImmune().sized(1F, 0.8F).build(CRC.MODID+ "cyborg_robot_chicken"));
    public static final RegistryObject<EntityType<BoomChicken>> BOOM_CHICKEN = ENTITY_TYPES
            .register("boom_chicken", () -> EntityType.Builder.<BoomChicken>of(BoomChicken::new, MobCategory.MISC)
                    .fireImmune().sized(1F, 0.8F).build(CRC.MODID+ "boom_chicken"));

    public static final RegistryObject<EntityType<TeleportEntity>> TELEPORT = ENTITY_TYPES
            .register("teleport", () -> EntityType.Builder.<TeleportEntity>of(TeleportEntity::new, MobCategory.MISC)
                    .fireImmune().sized(0.2F, 0.2F).build(CRC.MODID+ "teleport"));
    public static final RegistryObject<EntityType<GanchoEntity>> GANCHO = ENTITY_TYPES
            .register("gancho", () -> EntityType.Builder.<GanchoEntity>of(GanchoEntity::new, MobCategory.MISC)
                    .fireImmune().sized(0.2F, 0.2F).build(CRC.MODID+ "gancho"));


}
