package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.server.entity.BeamExplosionEntity;
import com.TBK.crc.server.entity.ResidualEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ResidualRenderer<T extends ResidualEntity> extends NoopRenderer<T> {
    public PlayerModel model;
    public ResidualRenderer(EntityRendererProvider.Context p_174326_) {
        super(p_174326_);
        this.model = new PlayerModel(p_174326_.bakeLayer(ModelLayers.PLAYER),false);
        this.model.young = false;

    }

    @Override
    public boolean shouldRender(T p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    @Override
    public void render(T p_114485_, float p_114486_, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int p_114490_) {
        AbstractClientPlayer player = Minecraft.getInstance().player;

        if(this.model==null || player==null)return;
        if(p_114485_.getOrigen()!=null){
            float alpha = p_114485_.getResidualTime(partialTick)/20.0F;
            stack.pushPose();
            stack.translate(0,1.5,0);
            stack.mulPose(Axis.XP.rotationDegrees(180.0F));
            model.setupAnim(player,50, 0.5F, p_114486_, 0.0F,0.0F);
            model.renderToBuffer(stack,bufferSource.getBuffer(RenderType.entityTranslucentEmissive(player.getSkinTextureLocation())),p_114490_, OverlayTexture.NO_OVERLAY,0.31f,0.83f,0.96f,alpha);
            stack.popPose();
        }
        super.render(p_114485_, p_114486_, partialTick, stack, bufferSource, p_114490_);

    }
}
