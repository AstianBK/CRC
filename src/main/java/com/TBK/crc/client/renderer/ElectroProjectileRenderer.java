package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.model.GanchoModel;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.ElectroProjectile;
import com.TBK.crc.server.entity.GanchoEntity;
import com.TBK.crc.server.multiarm.CannonArm;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ElectroProjectileRenderer<T extends ElectroProjectile> extends NoopRenderer<T> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CRC.MODID,"textures/entity/lightning_ball/lightning_ball_projectile.png");
    public final EntityRenderDispatcher entityRenderDispatcher;
    public ElectroProjectileRenderer(EntityRendererProvider.Context p_174326_) {
        super(p_174326_);
        this.entityRenderDispatcher=p_174326_.getEntityRenderDispatcher();
    }

    @Override
    public void render(T p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {
        super.render(p_114485_, p_114486_, p_114487_, p_114488_, p_114489_, p_114490_);
        if(p_114485_.getOwner() instanceof Player){
            MultiArmCapability cap = MultiArmCapability.get((Player) p_114485_.getOwner());
            if(cap!=null){
                if(cap.getCastingClientTimer()>0){
                    p_114488_.pushPose();
                    //CRC.LOGGER.debug("Desplacamiento :"+desplazamiento+ " Rot :"+p_117355_);
                    p_114488_.mulPose(this.entityRenderDispatcher.cameraOrientation());
                    p_114488_.mulPose(Axis.XP.rotationDegrees(90.0F));
                    p_114488_.translate(CRC.x,CRC.y,CRC.z);
                    float porcentajeDeCasteo = 1.0F - (float) cap.getCastingClientTimer() / cap.getMaxCastingClientTimer();
                    this.draw(p_114488_.last(),p_114485_,p_114489_,p_114490_,0.25f+1.25F*porcentajeDeCasteo,0);
                    p_114488_.popPose();
                }else {
                    p_114488_.pushPose();
                    p_114488_.mulPose(this.entityRenderDispatcher.cameraOrientation());
                    p_114488_.mulPose(Axis.XP.rotationDegrees(90.0F));
                    p_114488_.translate(CRC.x,CRC.y,CRC.z);

                    float porcentajeDeCasteo = 1.0F - (float) p_114485_.timeRecharge / 20;
                    this.draw(p_114488_.last(),p_114485_,p_114489_,p_114490_,0.25f+1.25F*porcentajeDeCasteo,0);
                    p_114488_.popPose();
                }
            }
        }
    }

    private void draw(PoseStack.Pose pose, T entity, MultiBufferSource bufferSource, int light, float width, int offset) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.dragonExplosionAlpha(TEXTURE));
        float halfWidth = width * 0.5f;
        consumer.vertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).color(255, 255, 255, 255).uv(0f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(25728640).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -0.1f, -halfWidth).color(255, 255, 255, 255).uv(1f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(25728640).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -0.1f, halfWidth).color(255, 255, 255, 255).uv(1f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(215728640).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, -halfWidth, -0.1f, halfWidth).color(255, 255, 255, 255).uv(0f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(25728640).normal(normalMatrix, 0f, 1f, 0f).endVertex();
    }

}
