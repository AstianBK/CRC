package com.TBK.crc.client.model;// Made with Blockbench 5.0.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.crc.CRC;
import com.TBK.crc.server.entity.CoilChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CoilChickenModel<T extends CoilChicken> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID, "chicken_coil"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart head2;
	private final ModelPart head;
	private final ModelPart bill;
	private final ModelPart chin;
	private final ModelPart body;
	private final ModelPart coil;
	private final ModelPart left_wing;
	private final ModelPart right_wing;

	public CoilChickenModel(ModelPart root) {
		this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.head2 = this.main.getChild("head2");
		this.head = this.head2.getChild("head");
		this.bill = this.head2.getChild("bill");
		this.chin = this.head2.getChild("chin");
		this.body = this.main.getChild("body");
		this.coil = this.main.getChild("coil");
		this.left_wing = this.main.getChild("left_wing");
		this.right_wing = this.main.getChild("right_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(-2.0F, 17.0F, 1.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(2.0F, 7.0F, -1.0F));

		PartDefinition head2 = main.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offset(0.0F, -11.475F, -3.0F));

		PartDefinition head = head2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 17).addBox(-0.5F, -3.675F, -0.95F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -1.0F));

		PartDefinition bill = head2.addOrReplaceChild("bill", CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 1.0F, -3.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -1.0F));

		PartDefinition chin = head2.addOrReplaceChild("chin", CubeListBuilder.create().texOffs(14, 4).addBox(-0.5F, 3.0F, -1.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -1.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 7).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.475F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition head_r1 = body.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(14, 20).mirror().addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.475F, -0.375F, 4.8F, -1.5708F, -0.2618F, 0.0F));

		PartDefinition head_r2 = body.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(14, 20).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.475F, -0.375F, 4.8F, -1.5708F, 0.2618F, 0.0F));

		PartDefinition coil = main.addOrReplaceChild("coil", CubeListBuilder.create().texOffs(27, 6).addBox(-3.0F, -3.0F, -0.25F, 6.0F, 6.0F, 11.0F, new CubeDeformation(-0.75F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition left_wing = main.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(18, 9).mirror().addBox(0.0F, -1.5F, 0.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.5F, -11.975F, -3.0F));

		PartDefinition right_wing = main.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(18, 9).addBox(-1.0F, -1.5F, 0.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -11.975F, -3.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		truemain.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.truemain;
	}
}