package com.TBK.crc.client.layer;

import com.TBK.crc.CRC;
import com.TBK.crc.client.model.MultiArmModel;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.CannonArm;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

public class MultiarmLayer <T extends LivingEntity,M extends HumanoidModel<T>> extends RenderLayer<T,M> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm.png");
    private static final ResourceLocation GLOWING = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_glowing.png");

    private static final ResourceLocation PULSING = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_pulsating_3.png");

    public MultiArmModel<?> multiarm;
    private final DrawSelector<T, M> drawSelector;
    public MultiarmLayer(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
        this.multiarm = new MultiArmModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(MultiArmModel.LAYER_LOCATION));
        this.drawSelector = new DrawSelector<T, M>() {
            @Override
            public List<ModelPart> getPartsToDraw(MultiArmModel<?> multiarm) {
                return multiarm.getRoot();
            }
        };
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
                multiarm.renderToBuffer(p_117349_, p_117350_.getBuffer(RenderType.entityTranslucent(TEXTURE)),p_117351_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);
                multiarm.renderToBuffer(p_117349_, p_117350_.getBuffer(RenderType.eyes(GLOWING)),p_117351_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,1.0f);
                this.onlyDrawSelectedParts();
                multiarm.renderToBuffer(p_117349_,p_117350_.getBuffer(RenderType.entityTranslucentEmissive(PULSING)),p_117351_, OverlayTexture.NO_OVERLAY,1.0f,1.0f,1.0f,cap.getPulsingAnim(p_117356_)/30.0F);
                this.resetDrawForAllParts();
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

    private void onlyDrawSelectedParts() {
        List<ModelPart> list = this.drawSelector.getPartsToDraw(multiarm);
        multiarm.root().getAllParts().forEach((p_234918_) -> {
            p_234918_.skipDraw = true;
        });
        list.forEach(this::skipDraw);
    }

    public void skipDraw(ModelPart part){
        part.skipDraw = false;
        for (ModelPart part1 : part.getAllParts().toList()){
            if(part1!=part){
                this.skipDraw(part1);
            }
        }
    }

    private void resetDrawForAllParts() {
        multiarm.root().getAllParts().forEach((p_234913_) -> {
            p_234913_.skipDraw = false;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public interface DrawSelector<T extends LivingEntity, M extends EntityModel<T>> {
        List<ModelPart> getPartsToDraw(MultiArmModel<?> multiarm);
    }
}
