package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.model.TheFuturePortalModel;
import com.TBK.crc.server.entity.BeamExplosionEntity;
import com.TBK.crc.server.entity.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;

import java.util.Map;

public class PortalRenderer<T extends PortalEntity> extends NoopRenderer<T> {
    public final ResourceLocation TEXTURE = new ResourceLocation(CRC.MODID,"textures/entity/portal/portal_entity.png");

    private static final ResourceLocation[] RING_FRAMES = new ResourceLocation[] {
            new ResourceLocation(CRC.MODID, "textures/entity/portal/portal_ring_0.png"),
            new ResourceLocation(CRC.MODID, "textures/entity/portal/portal_ring_1.png"),
            new ResourceLocation(CRC.MODID, "textures/entity/portal/portal_ring_2.png"),
            new ResourceLocation(CRC.MODID, "textures/entity/portal/portal_ring_3.png"),
            new ResourceLocation(CRC.MODID, "textures/entity/portal/portal_ring_4.png"),
            new ResourceLocation(CRC.MODID, "textures/entity/portal/portal_ring_5.png"),
            new ResourceLocation(CRC.MODID, "textures/entity/portal/portal_ring_6.png"),
            new ResourceLocation(CRC.MODID, "textures/entity/portal/portal_ring_7.png"),
            new ResourceLocation(CRC.MODID, "textures/entity/portal/portal_ring_8.png")
    };
    public TheFuturePortalModel model;
    public PortalRenderer(EntityRendererProvider.Context p_174326_) {
        super(p_174326_);
        this.model=new TheFuturePortalModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(TheFuturePortalModel.LAYER_LOCATION));
    }

    @Override
    public boolean shouldRender(T p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    @Override
    public void render(T p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
        super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
        p_115458_.pushPose();
        p_115458_.translate(0,0.5F,0.0F);
        p_115458_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        p_115458_.mulPose(Axis.ZP.rotationDegrees(180.0F));
        float ageInTick = p_115457_+p_115455_.tickCount;

        this.model.setupAnim(p_115455_,0,0,ageInTick,0,0);
        this.model.renderToBuffer(p_115458_,p_115459_.getBuffer(RenderType.entityTranslucent(TEXTURE)),p_115460_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);
        this.model.setupAnim(p_115455_,0,0,ageInTick,0,0);
        this.model.renderToBuffer(p_115458_,p_115459_.getBuffer(RenderType.eyes(TEXTURE)),p_115460_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);
        int frame = (int) ((0.25F * ageInTick) % RING_FRAMES.length);
        ResourceLocation location = RING_FRAMES[frame];
        this.model.setupAnim(p_115455_,0,0,ageInTick,0,0);
        this.model.renderToBuffer(p_115458_,p_115459_.getBuffer(RenderType.entityTranslucent(location)),p_115460_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);
        this.model.setupAnim(p_115455_,0,0,ageInTick,0,0);
        this.model.renderToBuffer(p_115458_,p_115459_.getBuffer(RenderType.eyes(location)),p_115460_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);

        p_115458_.popPose();
    }

}
