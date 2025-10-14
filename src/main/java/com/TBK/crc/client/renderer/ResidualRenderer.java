package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.layer.MultiarmLayer;
import com.TBK.crc.client.model.MultiArmModel;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.BeamExplosionEntity;
import com.TBK.crc.server.entity.ResidualEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ResidualRenderer<T extends ResidualEntity> extends NoopRenderer<T> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm.png");

    public PlayerModel model;
    public MultiArmModel armModel;
    public ResidualRenderer(EntityRendererProvider.Context p_174326_) {
        super(p_174326_);
        this.model = new PlayerModel(p_174326_.bakeLayer(ModelLayers.PLAYER),false);
        this.model.young = false;
        this.armModel = new MultiArmModel<>(p_174326_.bakeLayer(MultiArmModel.LAYER_LOCATION));
    }

    @Override
    public boolean shouldRender(T p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    @Override
    public void render(T p_114485_, float p_114486_, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int p_114490_) {
        AbstractClientPlayer player = (AbstractClientPlayer) p_114485_.getOwner();

        if(this.model==null || player==null)return;
        if(p_114485_.getOrigen()!=null){

            MultiArmCapability cap = MultiArmCapability.get(player);
            float alpha = p_114485_.getResidualTime(partialTick)/20.0F;
            stack.pushPose();
            stack.translate(0,1.5,0);
            stack.mulPose(Axis.XP.rotationDegrees(180.0F));

            if(cap!=null){
                this.setPose(p_114485_.getSkillPose(),stack,player);
            }
            model.setupAnim(player,50, 0.5F, p_114486_, 0.0F,0.0F);
            model.renderToBuffer(stack,bufferSource.getBuffer(RenderType.entityTranslucentEmissive(player.getSkinTextureLocation())),p_114490_, OverlayTexture.NO_OVERLAY,0.31f,0.83f,0.96f,alpha);
            if(cap!=null && Util.hasMultiArm(cap)){
                this.renderArm(cap,player,stack,bufferSource,p_114490_);
            }
            stack.popPose();
        }
        super.render(p_114485_, p_114486_, partialTick, stack, bufferSource, p_114490_);

    }
    public void renderArm(MultiArmCapability cap, Player owner, PoseStack stack,MultiBufferSource buffer,int light){
        stack.pushPose();
        selectPoseArm(owner,cap.getSelectSkill().name);
        armModel.root().copyFrom(model.rightArm);
        armModel.root().xScale=1.01F;
        armModel.root().yScale=1.01F;
        armModel.root().zScale=1.01F;
        armModel.selectArm(cap.getSelectSkill().name,cap.getSelectSkill(),owner.tickCount+Minecraft.getInstance().getPartialTick());
        armModel.renderToBuffer(stack, buffer.getBuffer(RenderType.entityTranslucent(TEXTURE)),light, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);
        stack.popPose();
    }
    private void selectPoseArm(Player entity,String name) {
        switch (name){
            case "cannon_arm","gancho_arm"->{
                model.rightArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
                model.poseRightArm(entity);
            }
        }
    }
    private void setPose (MultiArmCapability.SkillPose pose, PoseStack stack,Player player){
        switch (pose){
            case DASH_CLAWS -> {
                stack.translate(0,-0.5F,0);
                Vec3 direction = player.getDeltaMovement();
                float rotX = (float) Math.acos(direction.y);
                stack.mulPose(Axis.XP.rotationDegrees(-90-rotX));

            }
        }
    }
}
