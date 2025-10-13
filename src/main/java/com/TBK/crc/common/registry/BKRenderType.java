package com.TBK.crc.common.registry;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.OptionalDouble;

public class BKRenderType extends RenderType {
    public BKRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }


    public static RenderType getBeam(ResourceLocation location) {
        return RenderType.create("beam", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder()
                .setTextureState(new TextureStateShard(location, false, false))
                .setShaderState(RenderType.RENDERTYPE_ENERGY_SWIRL_SHADER)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOutputState(ITEM_ENTITY_TARGET)
                .createCompositeState(false));
    }
    public static final RenderType GLOW_LINES = RenderType.create("glow_lines",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(NO_CULL)
                    .createCompositeState(false));


}
