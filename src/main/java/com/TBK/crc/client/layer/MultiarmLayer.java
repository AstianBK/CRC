package com.TBK.crc.client.layer;

import com.TBK.crc.CRC;
import com.TBK.crc.ModBusEvent;
import com.TBK.crc.client.model.MultiArmModel;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.item.CyberSkinItem;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MultiarmLayer <T extends LivingEntity,M extends HumanoidModel<T>> extends RenderLayer<T,M> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm.png");
    private static final ResourceLocation GLOWING = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_glowing.png");

    public MultiArmModel<?> multiarm;
    private final ItemInHandRenderer itemInHandRenderer;

    public MultiarmLayer(RenderLayerParent<T, M> p_117346_, ItemInHandRenderer itemInHandRenderer) {
        super(p_117346_);
        this.multiarm = new MultiArmModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(MultiArmModel.LAYER_LOCATION));
        this.itemInHandRenderer = itemInHandRenderer;

    }

    @Override
    public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, T p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        if(multiarm!=null){
            MultiArmCapability cap = MultiArmCapability.get((Player) p_117352_);
            if (cap!=null && Util.hasMultiArm(cap)){
                p_117349_.pushPose();
                selectPoseArm(p_117352_,cap.getSelectSkill().name);
                multiarm.root().copyFrom(getParentModel().rightArm);
                multiarm.root().xScale=1.01F;
                multiarm.root().yScale=1.01F;
                multiarm.root().zScale=1.01F;
                multiarm.selectArm((Player)p_117352_,cap.getSelectSkill().name,cap.getSelectSkill(),p_117352_.tickCount+Minecraft.getInstance().getPartialTick());
                if(!p_117352_.getMainHandItem().isEmpty()){
                    p_117349_.pushPose();
                    renderItem(p_117349_,p_117350_,p_117351_,p_117352_);
                    p_117349_.popPose();
                }
                ResourceLocation location = CyberSkinItem.getTextures(cap.implantStore.getImplant(0).getOrCreateTag());
                if(location==null){
                    location = TEXTURE;
                }
                multiarm.renderToBuffer(p_117349_, p_117350_.getBuffer(RenderType.entityTranslucent(location)),p_117351_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);
                multiarm.renderToBuffer(p_117349_, p_117350_.getBuffer(RenderType.eyes(GLOWING)),p_117351_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);
                if(cap.levelCharge>0){
                    ResourceLocation texture = ModBusEvent.PULSING[cap.levelCharge];
                    multiarm.renderToBuffer(p_117349_, p_117350_.getBuffer(RenderType.entityTranslucentEmissive(texture)), p_117351_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                }
                p_117349_.popPose();
            }
        }
    }

    private void selectPoseArm(T entity,String name) {
        switch (name){
            case "cannon_arm","gancho_arm"->{
                getParentModel().rightArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
                getParentModel().poseRightArm(entity);
            }
        }
    }

    protected void renderArmWithItem(LivingEntity p_270884_, ItemStack p_270379_, ItemDisplayContext p_270607_, HumanoidArm p_270324_, PoseStack p_270124_, MultiBufferSource p_270414_, int p_270295_) {
        if (p_270379_.is(Items.SPYGLASS) && p_270884_.getUseItem() == p_270379_ && p_270884_.swingTime == 0) {
            this.renderArmWithSpyglass(p_270884_, p_270379_, p_270324_, p_270124_, p_270414_, p_270295_);
        } else {
            if (!p_270379_.isEmpty()) {
                p_270124_.pushPose();
                this.translateToHand(p_270124_);
                p_270124_.mulPose(Axis.XP.rotationDegrees(-90.0F));
                p_270124_.mulPose(Axis.YP.rotationDegrees(180.0F));
                boolean flag = p_270324_ == HumanoidArm.LEFT;
                p_270124_.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
                this.itemInHandRenderer.renderItem(p_270884_, p_270379_, p_270607_, flag, p_270124_, p_270414_, p_270295_);
                p_270124_.popPose();
            }
        }

    }

    public void translateToHand(PoseStack p_102855_) {
        this.multiarm.root().translateAndRotate(p_102855_);
    }
    private void renderArmWithSpyglass(LivingEntity p_174518_, ItemStack p_174519_, HumanoidArm p_174520_, PoseStack p_174521_, MultiBufferSource p_174522_, int p_174523_) {
        p_174521_.pushPose();
        ModelPart modelpart = this.getParentModel().getHead();
        float f = modelpart.xRot;
        modelpart.xRot = Mth.clamp(modelpart.xRot, (-(float)Math.PI / 6F), ((float)Math.PI / 2F));
        modelpart.translateAndRotate(p_174521_);
        modelpart.xRot = f;
        CustomHeadLayer.translateToHead(p_174521_, false);
        boolean flag = p_174520_ == HumanoidArm.LEFT;
        p_174521_.translate((flag ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);
        this.itemInHandRenderer.renderItem(p_174518_, p_174519_, ItemDisplayContext.HEAD, false, p_174521_, p_174522_, p_174523_);
        p_174521_.popPose();
    }


    public void renderItem(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_) {
        boolean flag = p_117207_.getMainArm() == HumanoidArm.RIGHT;
        ItemStack itemstack = flag ? p_117207_.getOffhandItem() : p_117207_.getMainHandItem();
        ItemStack itemstack1 = flag ? p_117207_.getMainHandItem() : p_117207_.getOffhandItem();
        if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
            p_117204_.pushPose();
            if (this.getParentModel().young) {
                float f = 0.5F;
                p_117204_.translate(0.0F, 0.75F, 0.0F);
                p_117204_.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderArmWithItem(p_117207_, itemstack1, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, p_117204_, p_117205_, p_117206_);
            this.renderArmWithItem(p_117207_, itemstack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, p_117204_, p_117205_, p_117206_);
            p_117204_.popPose();
        }
    }

    
}
