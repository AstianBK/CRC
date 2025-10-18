package com.TBK.crc.client.model;

import com.TBK.crc.CRC;
import com.TBK.crc.client.animacion.ChickenDroneAnim;
import com.TBK.crc.server.entity.DroneChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class DroneChickenModel<T extends DroneChicken> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID, "chicken_drone"), "main");
    public static final ModelLayerLocation ARMOR_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID, "chicken_drone_armor"), "main");

    private final ModelPart truemain;
    private final ModelPart main;
    private final ModelPart head2;
    private final ModelPart head;
    private final ModelPart bill;
    private final ModelPart chin;
    private final ModelPart body;
    private final ModelPart left_wing;
    private final ModelPart right_wing;

    public DroneChickenModel(ModelPart root) {
        this.truemain = root.getChild("truemain");
        this.main = this.truemain.getChild("main");
        this.head2 = this.main.getChild("head2");
        this.head = this.head2.getChild("head");
        this.bill = this.head2.getChild("bill");
        this.chin = this.head2.getChild("chin");
        this.body = this.main.getChild("body");
        this.left_wing = this.main.getChild("left_wing");
        this.right_wing = this.main.getChild("right_wing");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(-2.0F, 17.0F, 1.0F));

        PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(2.0F, 7.0F, -1.0F));

        PartDefinition head2 = main.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offset(0.0F, -9.475F, -3.0F));

        PartDefinition head = head2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, deformation), PartPose.offset(0.0F, -2.5F, -1.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(12, 17).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 4.0F, 3.0F, deformation), PartPose.offsetAndRotation(0.0F, -2.0F, 3.5F, 1.5708F, 0.0F, 0.0F));

        PartDefinition bill = head2.addOrReplaceChild("bill", CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 1.0F, -3.0F, 3.0F, 2.0F, 2.0F, deformation), PartPose.offset(0.0F, -4.0F, -1.0F));

        PartDefinition chin = head2.addOrReplaceChild("chin", CubeListBuilder.create().texOffs(14, 4).addBox(-0.5F, 3.0F, -1.5F, 1.0F, 2.0F, 2.0F, deformation), PartPose.offset(0.0F, -4.0F, -1.0F));

        PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 7).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(0.0F, -9.475F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition left_wing = main.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offset(2.5F, -11.975F, -3.0F));

        PartDefinition left_wing_r1 = left_wing.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(50, 0).mirror().addBox(-1.5F, -1.5F, -2.5F, 2.0F, 7.0F, 5.0F, deformation).mirror(false), PartPose.offsetAndRotation(1.5F, -1.0F, 2.5F, 0.0F, 0.0F, -1.5708F));

        PartDefinition right_wing = main.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offset(-2.5F, -11.975F, -3.0F));

        PartDefinition right_wing_r1 = right_wing.addOrReplaceChild("right_wing_r1", CubeListBuilder.create().texOffs(50, 0).addBox(-0.5F, -1.5F, 0.0F, 2.0F, 7.0F, 5.0F, deformation), PartPose.offsetAndRotation(-1.5F, -1.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.idle, ChickenDroneAnim.idle,ageInTicks,1.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0,0.25,0);
        truemain.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    @Override
    public ModelPart root() {
        return this.truemain;
    }


}

