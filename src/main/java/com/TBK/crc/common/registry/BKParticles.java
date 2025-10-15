package com.TBK.crc.common.registry;

import com.TBK.crc.CRC;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BKParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CRC.MODID);

    public static final RegistryObject<SimpleParticleType> LIGHTNING_TRAIL_PARTICLES =
            PARTICLE_TYPES.register("lightning_trail", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> ELECTRO_EXPLOSION_PARTICLES =
            PARTICLE_TYPES.register("electro_explosion", () -> new SimpleParticleType(true));


}
