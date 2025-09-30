package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.layer.ShieldLayer;
import com.TBK.crc.client.model.CyborgRobotChickenModel;
import com.TBK.crc.client.model.RexChickenModel;
import com.TBK.crc.common.registry.BKRenderType;
import com.TBK.crc.server.entity.CyborgRobotChicken;
import com.TBK.crc.server.entity.RexChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RexChickenRenderer<T extends RexChicken,M extends RexChickenModel<T>> extends LivingEntityRenderer<T,M> {
    private static final ResourceLocation BEAM_INNER_LOCATION = new ResourceLocation(CRC.MODID,"textures/entity/rex_chicken/beam/rex_beam_inner.png");
    private static final ResourceLocation BEAM_OUTER_LOCATION = new ResourceLocation(CRC.MODID,"textures/entity/rex_chicken/beam/rex_beam_outer.png");

    public RexChickenRenderer(EntityRendererProvider.Context p_174289_) {
        super(p_174289_, (M) new RexChickenModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(RexChickenModel.LAYER_LOCATION)), 1.0F);
        this.addLayer(new ShieldLayer<>(this));
    }

    @Override
    public void render(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_) {
        super.render(p_115308_, p_115309_, p_115310_, p_115311_, p_115312_, p_115313_);
        if(p_115308_.isLaser()){
            render(p_115311_,p_115312_,p_115308_,p_115309_);
        }
    }

    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, T jellyfish, float pPartialTicks) {

        float ageInTicks = jellyfish.tickCount + pPartialTicks;
        float shakeByX = (float) Math.sin(ageInTicks * 4F) * 0.075F;
        float shakeByY = (float) Math.sin(ageInTicks * 4F + 1.2F) * 0.075F;
        float shakeByZ = (float) Math.sin(ageInTicks * 4F + 2.4F) * 0.075F;
        Vec3 beamOrigin = jellyfish.getHeadPos(pPartialTicks);
        Vec3 rawBeamPosition = jellyfish.getBeamDirection();
        float length = (float) rawBeamPosition.length();
        Vec3 vec3 = rawBeamPosition.normalize();
        float xRot = (float) Math.acos(vec3.y);
        float yRot = (float) Math.atan2(vec3.z, vec3.x);

        Vec3 offSet = beamOrigin.subtract(jellyfish.position());
        pMatrixStack.pushPose();

        pMatrixStack.translate(
                shakeByX+offSet.x,
                shakeByY+offSet.y-1.0F,
                shakeByZ+offSet.z
        );
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(((Mth.PI / 2F) - yRot) * Mth.RAD_TO_DEG));
        pMatrixStack.mulPose(Axis.XP.rotationDegrees((-(Mth.PI / 2F) + xRot) * Mth.RAD_TO_DEG));
        pMatrixStack.mulPose(Axis.ZP.rotationDegrees(45));


        renderBeamInner(jellyfish,pMatrixStack,pBuffer,pPartialTicks,1.5F,length);
        renderBeamOuter(jellyfish,pMatrixStack,pBuffer,pPartialTicks,1.75F,length);

        pMatrixStack.popPose();
    }

    private void renderBeamInner(T entity, PoseStack poseStack, MultiBufferSource source, float partialTicks, float width, float length) {
        poseStack.pushPose();
        int vertices = 4;
        VertexConsumer vertexconsumer = source.getBuffer(BKRenderType.getBeam(BEAM_INNER_LOCATION));
        float speed = 0.5F;
        float startAlpha = 1.0F;
        float endAlpha = 1.0F;

        float v = ((float) entity.tickCount + partialTicks) * -0.25F * speed;
        float v1 = v + length * (0.5F);
        float f4 = -width;
        float f5 = 0;
        float f6 = 0.0F;
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        for (int j = 0; j <= vertices; ++j) {
            Matrix3f matrix3f = posestack$pose.normal();
            float f7 = Mth.cos((float) Math.PI + (float) j * ((float) Math.PI * 2F) / (float) vertices) * width;
            float f8 = Mth.sin((float) Math.PI + (float) j * ((float) Math.PI * 2F) / (float) vertices) * width;
            float f9 = (float) j + 1;
            vertexconsumer.vertex(matrix4f, f4 * 0.55F, f5 * 0.55F, 0.0F).color(1.0F, 1.0F, 1.0F, startAlpha).uv(f6, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f4, f5, length).color(1.0F, 1.0F, 1.0F, endAlpha).uv(f6, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7, f8, length).color(1.0F, 1.0F, 1.0F, endAlpha).uv(f9, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7 * 0.55F, f8 * 0.55F, 0.0F).color(1.0F, 1.0F, 1.0F, startAlpha).uv(f9, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            f4 = f7;
            f5 = f8;
            f6 = f9;
        }
        poseStack.popPose();
    }

    private void renderBeamOuter(T entity, PoseStack poseStack, MultiBufferSource source, float partialTicks, float width, float length) {
        poseStack.pushPose();
        int vertices = 8;
        VertexConsumer vertexconsumer= source.getBuffer(BKRenderType.getBeam(BEAM_OUTER_LOCATION));
        float speed = 1F;
        float startAlpha = 1.0F;
        float endAlpha = 0.0F;

        float v = ((float) entity.tickCount + partialTicks) * -0.25F * speed;
        float v1 = v + length * ( 0.15F);
        float f4 = -width;
        float f5 = 0;
        float f6 = 0.0F;
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        for (int j = 0; j <= vertices; ++j) {
            Matrix3f matrix3f = posestack$pose.normal();
            float f7 = Mth.cos((float) Math.PI + (float) j * ((float) Math.PI * 2F) / (float) vertices) * width;
            float f8 = Mth.sin((float) Math.PI + (float) j * ((float) Math.PI * 2F) / (float) vertices) * width;
            float f9 = (float) j + 1;
            vertexconsumer.vertex(matrix4f, f4 * 0.55F, f5 * 0.55F, 0.0F).color(1.0F, 1.0F, 1.0F, startAlpha).uv(f6, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f4, f5, length).color(1.0F, 1.0F, 1.0F, endAlpha).uv(f6, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7, f8, length).color(1.0F, 1.0F, 1.0F, endAlpha).uv(f9, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7 * 0.55F, f8 * 0.55F, 0.0F).color(1.0F, 1.0F, 1.0F, startAlpha).uv(f9, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            f4 = f7;
            f5 = f8;
            f6 = f9;
        }
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return new ResourceLocation(CRC.MODID,"textures/entity/rex_chicken/rex_chicken.png");
    }
}
