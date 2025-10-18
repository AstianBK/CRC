package com.TBK.crc.client.model;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.crc.CRC;
import com.TBK.crc.client.animacion.BoomChickenAnim;
import com.TBK.crc.client.animacion.CyborgRobotChickenAnim;
import com.TBK.crc.server.entity.BoomChicken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class BoomChickenModel<T extends BoomChicken> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID , "boomchickenmodel"), "main");
	public static final ModelLayerLocation ARMOR_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID , "boomchickenarmor"), "main");

	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart head2;
	private final ModelPart head;
	private final ModelPart bill;
	private final ModelPart chin;
	private final ModelPart right_leg;
	private final ModelPart body;
	private final ModelPart bombs;
	private final ModelPart tnt;
	private final ModelPart left_wing;
	private final ModelPart right_wing;
	private final ModelPart left_leg;

	public BoomChickenModel(ModelPart root) {
		this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.head2 = this.main.getChild("head2");
		this.head = this.head2.getChild("head");
		this.bill = this.head2.getChild("bill");
		this.chin = this.head2.getChild("chin");
		this.right_leg = this.main.getChild("right_leg");
		this.body = this.main.getChild("body");
		this.bombs = this.body.getChild("bombs");
		this.tnt = this.bombs.getChild("tnt");
		this.left_wing = this.main.getChild("left_wing");
		this.right_wing = this.main.getChild("right_wing");
		this.left_leg = this.main.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(-2.0F, 19.0F, 1.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head2 = main.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, -4.0F));

		PartDefinition head = head2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, deformation), PartPose.offset(0.0F, -2.5F, -1.0F));

		PartDefinition bill = head2.addOrReplaceChild("bill", CubeListBuilder.create().texOffs(12, 0).addBox(-1.5F, 1.0F, -3.0F, 3.0F, 2.0F, 2.0F, deformation), PartPose.offset(0.0F, -4.0F, -1.0F));

		PartDefinition chin = head2.addOrReplaceChild("chin", CubeListBuilder.create().texOffs(14, 4).addBox(-0.5F, 3.0F, -1.5F, 1.0F, 2.0F, 2.0F, deformation), PartPose.offset(0.0F, -4.0F, -1.0F));

		PartDefinition right_leg = main.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(20, 1).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, deformation), PartPose.offset(0.5F, 2.0F, 0.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 7).addBox(-2.5F, -3.0F, -5.0F, 5.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(2.0F, -3.0F, -1.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition bombs = body.addOrReplaceChild("bombs", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.2625F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition body_r1 = bombs.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -1.5F, -1.0F, 0.0F, 3.0F, 2.0F, deformation), PartPose.offsetAndRotation(-3.7081F, -0.95F, -0.9783F, 0.3927F, 0.0F, -1.5708F));

		PartDefinition body_r2 = bombs.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -2.0F, -1.5F, 0.0F, 3.0F, 2.0F, deformation), PartPose.offsetAndRotation(-3.4375F, 1.05F, -0.325F, 0.3927F, 0.0F, -1.5708F));

		PartDefinition body_r3 = bombs.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(26, 12).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F,  deformation), PartPose.offsetAndRotation(-2.1625F, 1.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition body_r4 = bombs.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(30, 9).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, deformation), PartPose.offsetAndRotation(0.2625F, 1.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition tnt = bombs.addOrReplaceChild("tnt", CubeListBuilder.create(), PartPose.offset(0.2625F, -1.0F, 0.0F));

		PartDefinition body_r5 = tnt.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(30, 9).mirror().addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, deformation).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition body_r6 = tnt.addOrReplaceChild("body_r6", CubeListBuilder.create().texOffs(26, 12).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, deformation), PartPose.offsetAndRotation(-2.425F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition left_wing = main.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(18, 9).mirror().addBox(0.0F, -1.5F, 0.0F, 1.0F, 3.0F, 5.0F, deformation).mirror(false), PartPose.offset(4.5F, -0.5F, -4.0F));

		PartDefinition right_wing = main.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(18, 9).addBox(-1.0F, -1.5F, 0.0F, 1.0F, 3.0F, 5.0F, deformation), PartPose.offset(-0.5F, -0.5F, -4.0F));

		PartDefinition left_leg = main.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 1).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, deformation), PartPose.offset(3.5F, 2.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.idle, CyborgRobotChickenAnim.idle,ageInTicks,1.0F);

		this.animateWalk(BoomChickenAnim.move,limbSwing,limbSwingAmount,2.0F,2.5F);
		this.animate(entity.air, BoomChickenAnim.air,ageInTicks,1.0F);
		if(entity.standTimer>0){
			this.left_leg.getAllParts().forEach(ModelPart::resetPose);
			this.right_leg.getAllParts().forEach(ModelPart::resetPose);
		}
		this.animate(entity.stand, BoomChickenAnim.stand,ageInTicks,1.0F);

		if(entity.isLaunch()){
			this.truemain.xRot = (float) (Math.cos(0.0872665*ageInTicks)*5.0F);
		}else {
			this.truemain.resetPose();
		}
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