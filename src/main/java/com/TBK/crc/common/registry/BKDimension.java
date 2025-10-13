package com.TBK.crc.common.registry;


import com.TBK.crc.CRC;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;


public class BKDimension {
    public final static ResourceLocation THE_FUTURE_ID = new ResourceLocation(CRC.MODID,"the_future");

    public static final ResourceKey<Level> THE_FUTURE_LEVEL = ResourceKey.create(Registries.DIMENSION, THE_FUTURE_ID);
    public static final ResourceKey<DimensionType> THE_FUTURE_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, THE_FUTURE_ID);

}
