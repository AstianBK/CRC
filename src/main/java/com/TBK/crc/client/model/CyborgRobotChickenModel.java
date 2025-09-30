package com.TBK.crc.client.model;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.crc.CRC;
import com.TBK.crc.client.animacion.CyborgRobotChickenAnim;
import com.TBK.crc.server.entity.CyborgRobotChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CyborgRobotChickenModel<T extends CyborgRobotChicken> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID, "cyborgrobotchicken"), "main");
	private final ModelPart truemain;
	private final ModelPart Chicken;
	private final ModelPart head;
	private final ModelPart bill;
	private final ModelPart chin;
	private final ModelPart body;
	private final ModelPart jetpack;
	private final ModelPart left_wing;
	private final ModelPart lower;
	private final ModelPart fist;
	private final ModelPart right_wing;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public CyborgRobotChickenModel(ModelPart root) {
		this.truemain = root.getChild("truemain");
		this.Chicken = this.truemain.getChild("Chicken");
		this.head = this.Chicken.getChild("head");
		this.bill = this.head.getChild("bill");
		this.chin = this.head.getChild("chin");
		this.body = this.Chicken.getChild("body");
		this.jetpack = this.body.getChild("jetpack");
		this.left_wing = this.Chicken.getChild("left_wing");
		this.lower = this.left_wing.getChild("lower");
		this.fist = this.lower.getChild("fist");
		this.right_wing = this.Chicken.getChild("right_wing");
		this.left_leg = this.Chicken.getChild("left_leg");
		this.right_leg = this.Chicken.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Chicken = truemain.addOrReplaceChild("Chicken", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, -4.0F));

		PartDefinition head = Chicken.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 23).addBox(0.0F, -8.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(18, 10).addBox(-2.5F, -6.1F, -3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 10).addBox(-2.0F, -5.6F, -3.025F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bill = head.addOrReplaceChild("bill", CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chin = head.addOrReplaceChild("chin", CubeListBuilder.create().texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = Chicken.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition jetpack = body.addOrReplaceChild("jetpack", CubeListBuilder.create().texOffs(26, 27).addBox(1.0F, 2.225F, -0.9833F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 27).addBox(-4.0F, 2.225F, -0.9833F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 27).mirror().addBox(-4.0F, -4.0F, -0.9833F, 3.0F, 8.0F, 2.0F, new CubeDeformation(0.02F)).mirror(false)
		.texOffs(26, 31).addBox(-1.5F, -2.0F, -0.5333F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.02F))
		.texOffs(16, 27).addBox(1.0F, -4.0F, -0.9833F, 3.0F, 8.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 2.0F, 3.9833F));

		PartDefinition left_wing = Chicken.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(41, 12).addBox(0.0F, -1.0F, -1.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.002F)), PartPose.offset(3.0F, -1.0F, 2.5F));

		PartDefinition lower = left_wing.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(1.0F, 2.0679F, 0.0654F));

		PartDefinition left_wing_r1 = lower.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(41, 19).addBox(-1.0F, -2.0F, -1.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, -1.0F, -0.6981F, 0.0F, 0.0F));

		PartDefinition fist = lower.addOrReplaceChild("fist", CubeListBuilder.create(), PartPose.offset(0.0F, 4.1812F, -3.2498F));

		PartDefinition left_wing_r2 = fist.addOrReplaceChild("left_wing_r2", CubeListBuilder.create().texOffs(40, 6).addBox(-1.5F, 2.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.6812F, 2.2498F, -0.6981F, 0.0F, 0.0F));

		PartDefinition right_wing = Chicken.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, -2.0F, 0.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 1.0F, 0.0F, -0.2182F, 0.0F));

		PartDefinition left_leg = Chicken.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(35, 1).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 4.0F, 5.0F));

		PartDefinition right_leg = Chicken.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(38, 0).addBox(-1.0F, 2.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(50, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 4.0F, 5.0F));

		PartDefinition right_leg_r1 = right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(50, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.5F, -0.5F, 0.0F, 0.0F, 0.0F, -3.1416F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.idle,CyborgRobotChickenAnim.idle,ageInTicks,1.0F);

		this.animateWalk(CyborgRobotChickenAnim.walk,limbSwing,limbSwingAmount,2.0F,2.5F);
		this.animate(entity.attack,CyborgRobotChickenAnim.punch,ageInTicks,1.0F);
		this.animate(entity.charge,CyborgRobotChickenAnim.charge,ageInTicks,1.0F);

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		truemain.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return truemain;
	}

}