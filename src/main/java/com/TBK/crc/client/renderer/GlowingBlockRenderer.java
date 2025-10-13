package com.TBK.crc.client.renderer;

import com.TBK.crc.common.Util;
import com.TBK.crc.common.api.ICanGlowing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Calendar;

import static com.TBK.crc.common.registry.BKRenderType.GLOW_LINES;

public class GlowingBlockRenderer<T extends ChestBlockEntity> implements BlockEntityRenderer<T> {
    private static final String BOTTOM = "bottom";
    private static final String LID = "lid";
    private static final String LOCK = "lock";
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;
    private final ModelPart doubleLeftLid;
    private final ModelPart doubleLeftBottom;
    private final ModelPart doubleLeftLock;
    private final ModelPart doubleRightLid;
    private final ModelPart doubleRightBottom;
    private final ModelPart doubleRightLock;
    private boolean xmasTextures;
    public GlowingBlockRenderer(BlockEntityRendererProvider.Context p_173521_){
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
            this.xmasTextures = true;
        }

        ModelPart modelpart = p_173521_.bakeLayer(ModelLayers.CHEST);
        this.bottom = modelpart.getChild("bottom");
        this.lid = modelpart.getChild("lid");
        this.lock = modelpart.getChild("lock");
        ModelPart modelpart1 = p_173521_.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT);
        this.doubleLeftBottom = modelpart1.getChild("bottom");
        this.doubleLeftLid = modelpart1.getChild("lid");
        this.doubleLeftLock = modelpart1.getChild("lock");
        ModelPart modelpart2 = p_173521_.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT);
        this.doubleRightBottom = modelpart2.getChild("bottom");
        this.doubleRightLid = modelpart2.getChild("lid");
        this.doubleRightLock = modelpart2.getChild("lock");

    }


    public void render(T p_112363_, float p_112364_, PoseStack p_112365_, MultiBufferSource p_112366_, int p_112367_, int p_112368_) {
        Level level = p_112363_.getLevel();
        boolean flag = level != null;
        BlockState blockstate = flag ? p_112363_.getBlockState() : Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chesttype = blockstate.hasProperty(ChestBlock.TYPE) ? blockstate.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        Block block = blockstate.getBlock();
        if (block instanceof AbstractChestBlock<?> abstractchestblock) {
            boolean flag1 = chesttype != ChestType.SINGLE;
            p_112365_.pushPose();
            float f = blockstate.getValue(ChestBlock.FACING).toYRot();
            p_112365_.translate(0.5F, 0.5F, 0.5F);
            p_112365_.mulPose(Axis.YP.rotationDegrees(-f));
            p_112365_.translate(-0.5F, -0.5F, -0.5F);
            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> neighborcombineresult;
            if (flag) {
                neighborcombineresult = abstractchestblock.combine(blockstate, level, p_112363_.getBlockPos(), true);
            } else {
                neighborcombineresult = DoubleBlockCombiner.Combiner::acceptNone;
            }

            float f1 = neighborcombineresult.apply(ChestBlock.opennessCombiner(p_112363_)).get(p_112364_);
            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;
            int i = neighborcombineresult.apply(new BrightnessCombiner<>()).applyAsInt(p_112367_);
            Material material = this.getMaterial(p_112363_, chesttype);
            VertexConsumer vertexconsumer = material.buffer(p_112366_, RenderType::entityCutout);
            if (flag1) {
                if (chesttype == ChestType.LEFT) {
                    this.render(p_112365_, vertexconsumer, this.doubleLeftLid, this.doubleLeftLock, this.doubleLeftBottom, f1, i, p_112368_);
                } else {
                    this.render(p_112365_, vertexconsumer, this.doubleRightLid, this.doubleRightLock, this.doubleRightBottom, f1, i, p_112368_);
                }
            } else {
                this.render(p_112365_, vertexconsumer, this.lid, this.lock, this.bottom, f1, i, p_112368_);
            }

            if(level!=null){
                int fullBright = 15728880;

                boolean neverOpened = Util.isHackingChest(p_112363_.getBlockPos());
                if (neverOpened) {
                    draw(p_112366_, p_112365_, 0.05F, 0.3176F, 0.83F, 0.96F, fullBright,System.currentTimeMillis()); // color brillante magenta
                }
            }

            p_112365_.popPose();
        }
    }

    private void render(PoseStack p_112370_, VertexConsumer p_112371_, ModelPart p_112372_, ModelPart p_112373_, ModelPart p_112374_, float p_112375_, int p_112376_, int p_112377_) {
        p_112372_.xRot = -(p_112375_ * ((float)Math.PI / 2F));
        p_112373_.xRot = p_112372_.xRot;
        p_112372_.render(p_112370_, p_112371_, p_112376_, p_112377_);
        p_112373_.render(p_112370_, p_112371_, p_112376_, p_112377_);
        p_112374_.render(p_112370_, p_112371_, p_112376_, p_112377_);
    }

    protected Material getMaterial(T blockEntity, ChestType chestType) {
        return Sheets.chooseMaterial(blockEntity, chestType, this.xmasTextures);
    }

    private void putVertex(VertexConsumer vc, Matrix4f mat, Matrix3f normal, float x, float y, float z,
                           float r, float g, float b, float a, int packedLight) {
        vc.vertex(mat, x, y, z)
                .color((int)(r * 255), (int)(g * 255), (int)(b * 255), (int)(a * 255))
                .uv(0.0f, 0.0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normal, 0.0f, 1.0f, 0.0f)
                .endVertex();
    }

    private void draw(MultiBufferSource bufferSource, PoseStack ps, float expand, float r, float g, float b, int light, long time){

        float brightness = 0.5f + 0.5f * (float)Math.sin(time * 0.005);

        expand -= 0.01f*brightness ;
        float min = 0.0f - expand;
        float max = 1.0f + expand;

        PoseStack.Pose last = ps.last();
        Matrix4f mat = last.pose();
        Matrix3f normal = last.normal();

        VertexConsumer lines = bufferSource.getBuffer(GLOW_LINES);

        // 8 vértices del cubo
        float[][] v = new float[][]{
                {min, min, min}, {max, min, min}, {max, max, min}, {min, max, min},
                {min, min, max}, {max, min, max}, {max, max, max}, {min, max, max}
        };

        // aristas
        int[][] edges = new int[][]{
                {0,1},{1,2},{2,3},{3,0},
                {4,5},{5,6},{6,7},{7,4},
                {0,4},{1,5},{2,6},{3,7}
        };

        for (int[] e : edges) {
            float[] a = v[e[0]];
            float[] bV = v[e[1]];

            putVertex(lines, mat, normal, a[0], a[1], a[2], r* brightness, g* brightness, b* brightness, 1.0f, light);
            putVertex(lines, mat, normal, bV[0], bV[1], bV[2], r* brightness, g* brightness, b* brightness, 1.0f, light);
        }
    }
}
