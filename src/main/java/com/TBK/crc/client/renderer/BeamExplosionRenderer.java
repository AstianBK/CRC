package com.TBK.crc.client.renderer;

import com.TBK.crc.server.entity.BeamExplosionEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;

public class BeamExplosionRenderer <T extends BeamExplosionEntity> extends NoopRenderer<T> {
    public BeamExplosionRenderer(EntityRendererProvider.Context p_174326_) {
        super(p_174326_);
    }
}
