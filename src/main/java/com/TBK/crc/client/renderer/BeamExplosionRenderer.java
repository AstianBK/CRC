package com.TBK.crc.client.renderer;

import com.TBK.crc.CRC;
import com.TBK.crc.server.entity.BeamExplosionEntity;
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
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.checkerframework.checker.units.qual.A;
import org.joml.Matrix4f;

import java.util.Map;

public class BeamExplosionRenderer <T extends BeamExplosionEntity> extends NoopRenderer<T> {
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);

    public BeamExplosionRenderer(EntityRendererProvider.Context p_174326_) {
        super(p_174326_);
    }

    @Override
    public boolean shouldRender(T p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    @Override
    public void render(T p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
        super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
        for (Map.Entry<BlockPos,Integer> entry : p_115455_.crackingBlock.entrySet()){
            p_115458_.pushPose();
            float progressMining = 1.0F - ((float)p_115455_.restoreCracking-entry.getValue())/50.0F;
            if(progressMining>=0.0F){
                BlockPos miningPos = entry.getKey();
                double d0 = Mth.lerp(p_115457_,p_115455_.xOld,p_115455_.getX());
                double d1 = Mth.lerp(p_115457_,p_115455_.yOld,p_115455_.getY());
                double d2 = Mth.lerp(p_115457_,p_115455_.zo,p_115455_.getZ());


                p_115458_.translate((double) miningPos.getX() - d0, (double) miningPos.getY() - d1, (double) miningPos.getZ() - d2);
                PoseStack.Pose posestack$pose = p_115458_.last();
                int progress = (int) Math.round((CRC.DESTROY_TYPES.size() - 1) * (float) Mth.clamp(progressMining, 0F, 1.0F));

                VertexConsumer vertexconsumer1 = new SheetedDecalTextureGenerator(Minecraft.getInstance().renderBuffers().crumblingBufferSource().getBuffer(CRC.DESTROY_TYPES.get(progress)), posestack$pose.pose(), posestack$pose.normal(), 1.0F);

                net.minecraftforge.client.model.data.ModelData modelData = p_115455_.level().getModelDataManager().getAt(miningPos);
                Minecraft.getInstance().getBlockRenderer().renderBreakingTexture(p_115455_.level().getBlockState(miningPos), miningPos, p_115455_.level(), p_115458_, vertexconsumer1, modelData == null ? net.minecraftforge.client.model.data.ModelData.EMPTY : modelData);
            }
            p_115458_.popPose();
        }
        float f5 = 1.0F-((float)p_115455_.restoreCracking/50.0F);
        float f7 = Math.min(f5 > 0.65F ? (f5 - 0.65F) / 0.45F : 0.0F, 1.0F);
        float secondCirclePercent = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
        RandomSource randomsource = RandomSource.create(432L);
        VertexConsumer vertexconsumer2 = p_115459_.getBuffer(RenderType.lightning());
        p_115458_.pushPose();
        renderLight(p_115458_,vertexconsumer2,0,0,0,f7,0);
        //renderLight(p_115458_,vertexconsumer2, (float) CRC.x, (float) CRC.y, (float) CRC.z,f5,20.0F);
        p_115458_.popPose();
    }

    private void renderLight(PoseStack stack,VertexConsumer vertexConsumer,float x,float y,float z,float percent,float rotX){
        stack.translate(x,y,z);
        for(int i = 0; (float)i < 1 + 3 * percent; ++i) {
            stack.mulPose(Axis.XP.rotationDegrees(30));
            stack.mulPose(Axis.YP.rotationDegrees(15+15*i));
            float f3 =  percent * 5.0F;
            float f4 =  0.5F;
            Matrix4f matrix4f = stack.last().pose();
            int j = (int)(255.0F * (1.0F - percent));
            vertex01(vertexConsumer, matrix4f, j);
            vertex2(vertexConsumer, matrix4f, f3, f4);
            vertex3(vertexConsumer, matrix4f, f3, f4);
            vertex01(vertexConsumer, matrix4f, j);
            vertex3(vertexConsumer, matrix4f, f3, f4);
            vertex4(vertexConsumer, matrix4f, f3, f4);
            vertex01(vertexConsumer, matrix4f, j);
            vertex4(vertexConsumer, matrix4f, f3, f4);
            vertex2(vertexConsumer, matrix4f, f3, f4);
        }
    }
    private static void vertex01(VertexConsumer p_254498_, Matrix4f p_253891_, int p_254278_) {
        p_254498_.vertex(p_253891_, 0.0F, 0.0F, 0.0F).color(79, 206, 245, p_254278_).endVertex();
    }

    private static void vertex2(VertexConsumer p_253956_, Matrix4f p_254053_, float p_253704_, float p_253701_) {
        p_253956_.vertex(p_254053_, -HALF_SQRT_3 * p_253701_, p_253704_, -0.5F * p_253701_).color(79, 206, 245, 0).endVertex();
    }

    private static void vertex3(VertexConsumer p_253850_, Matrix4f p_254379_, float p_253729_, float p_254030_) {
        p_253850_.vertex(p_254379_, HALF_SQRT_3 * p_254030_, p_253729_, -0.5F * p_254030_).color(79, 206, 245, 0).endVertex();
    }

    private static void vertex4(VertexConsumer p_254184_, Matrix4f p_254082_, float p_253649_, float p_253694_) {
        p_254184_.vertex(p_254082_, 0.0F, p_253649_, p_253694_).color(79, 206, 255 , 0).endVertex();
    }
}
