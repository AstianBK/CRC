package com.TBK.crc.client.model;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.upgrade.GanchoArm;
import com.TBK.crc.server.upgrade.Upgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class MultiArmModel<T extends Player> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID, "cyborgarm"), "main");
	private final ModelPart truemain;
	private final ModelPart mainbase;
	private final ModelPart ShoulderGuard;
	private final ModelPart UpperTube;
	private final ModelPart LowerTube;
	private final ModelPart UpperInnerArm;
	private final ModelPart LowerInnerArm;
	private final ModelPart HandGuard;
	private final ModelPart mainnone;
	private final ModelPart mainharpoon;
	private final ModelPart harpoonbase;
	private final ModelPart harpoon;
	private final ModelPart maincannon;
	private final ModelPart cannonbase;
	private final ModelPart addons;
	private final ModelPart cannon;
	private final ModelPart mainmelee;
	private final ModelPart basemelee;
	private final ModelPart meleeclaw;
	private final ModelPart bone;
	private final ModelPart energyclaws;

	public MultiArmModel(ModelPart root) {
		this.truemain = root.getChild("truemain");
		this.mainbase = this.truemain.getChild("mainbase");
		this.ShoulderGuard = this.mainbase.getChild("ShoulderGuard");
		this.UpperTube = this.mainbase.getChild("UpperTube");
		this.LowerTube = this.mainbase.getChild("LowerTube");
		this.UpperInnerArm = this.mainbase.getChild("UpperInnerArm");
		this.LowerInnerArm = this.mainbase.getChild("LowerInnerArm");
		this.HandGuard = this.mainbase.getChild("HandGuard");
		this.mainnone = this.truemain.getChild("mainnone");
		this.mainharpoon = this.truemain.getChild("mainharpoon");
		this.harpoonbase = this.mainharpoon.getChild("harpoonbase");
		this.harpoon = this.mainharpoon.getChild("harpoon");
		this.maincannon = this.truemain.getChild("maincannon");
		this.cannonbase = this.maincannon.getChild("cannonbase");
		this.addons = this.maincannon.getChild("addons");
		this.cannon = this.maincannon.getChild("cannon");
		this.mainmelee = this.truemain.getChild("mainmelee");
		this.basemelee = this.mainmelee.getChild("basemelee");
		this.meleeclaw = this.mainmelee.getChild("meleeclaw");
		this.bone = this.meleeclaw.getChild("bone");
		this.energyclaws = this.meleeclaw.getChild("energyclaws");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition mainbase = truemain.addOrReplaceChild("mainbase", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition ShoulderGuard = mainbase.addOrReplaceChild("ShoulderGuard", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -25.0F, -2.5F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(38, 16).addBox(-11.375F, -21.0F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 22.0F, 0.0F));

		PartDefinition cube_r1 = ShoulderGuard.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(2, 49).addBox(-1.125F, -1.25F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.22F)), PartPose.offsetAndRotation(-5.4119F, -29.6755F, 0.4779F, -0.583F, 0.6956F, -0.3999F));

		PartDefinition cube_r2 = ShoulderGuard.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(2, 49).addBox(-1.125F, -1.25F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.22F)), PartPose.offsetAndRotation(-8.4869F, -28.0255F, 0.2779F, -0.4363F, 0.0F, 0.0F));

		PartDefinition cube_r3 = ShoulderGuard.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(2, 49).addBox(-1.125F, -1.25F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.22F)), PartPose.offsetAndRotation(-12.4869F, -25.6755F, -0.3471F, -0.0163F, 0.2812F, -0.5063F));

		PartDefinition cube_r4 = ShoulderGuard.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(12, 45).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.4883F, -25.5084F, -0.2979F, -0.0128F, -0.1096F, -0.4665F));

		PartDefinition cube_r5 = ShoulderGuard.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 45).addBox(-0.725F, -2.575F, -0.3F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.2704F, -22.6226F, -0.3012F, 0.0926F, -0.076F, -0.6836F));

		PartDefinition cube_r6 = ShoulderGuard.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(8, 45).addBox(-0.6F, -1.0F, -0.35F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.525F, -27.9327F, -0.0011F, -0.4784F, -0.0403F, -0.0774F));

		PartDefinition cube_r7 = ShoulderGuard.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 45).addBox(-3.025F, -1.375F, -0.125F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-6.0F, -26.0F, -1.0F, -0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r8 = ShoulderGuard.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(4, 45).addBox(-0.65F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.4F, -29.3434F, 0.5045F, -0.6429F, 0.469F, -0.4762F));

		PartDefinition cube_r9 = ShoulderGuard.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 45).addBox(-0.025F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-6.0F, -26.0F, -0.625F, -0.3054F, 0.0F, 0.0F));

		PartDefinition UpperTube = mainbase.addOrReplaceChild("UpperTube", CubeListBuilder.create(), PartPose.offset(5.0F, 22.0F, 0.0F));

		PartDefinition cube_r10 = UpperTube.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(51, 7).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.175F, -21.9955F, 2.9486F, -1.2654F, 0.0F, 0.0F));

		PartDefinition cube_r11 = UpperTube.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(34, 2).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-5.15F, -23.3508F, 2.476F, -1.0036F, 0.0F, 0.0F));

		PartDefinition LowerTube = mainbase.addOrReplaceChild("LowerTube", CubeListBuilder.create(), PartPose.offset(5.0F, 22.0F, 0.0F));

		PartDefinition cube_r12 = LowerTube.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(35, 7).addBox(-0.5F, -0.5F, -0.75F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.175F, -17.1532F, 3.0433F, -1.8762F, 0.0F, 0.0F));

		PartDefinition cube_r13 = LowerTube.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(43, 7).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.175F, -19.2385F, 3.3116F, -1.6144F, 0.0F, 0.0F));

		PartDefinition cube_r14 = LowerTube.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(34, 2).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-5.225F, -15.5423F, 2.3056F, -1.0908F, 0.0F, -3.1416F));

		PartDefinition UpperInnerArm = mainbase.addOrReplaceChild("UpperInnerArm", CubeListBuilder.create().texOffs(22, 4).addBox(-8.0F, -20.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 22.0F, 0.0F));

		PartDefinition LowerInnerArm = mainbase.addOrReplaceChild("LowerInnerArm", CubeListBuilder.create().texOffs(20, 14).addBox(-8.0F, -13.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 22.0F, 0.0F));

		PartDefinition HandGuard = mainbase.addOrReplaceChild("HandGuard", CubeListBuilder.create().texOffs(38, 16).addBox(-2.375F, 4.0F, -1.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(-1.0F, 0.0F, -2.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 4.0F, -0.5F));

		PartDefinition mainnone = truemain.addOrReplaceChild("mainnone", CubeListBuilder.create().texOffs(0, 21).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0015F)), PartPose.offset(-1.0F, 10.0F, 0.0F));

		PartDefinition mainharpoon = truemain.addOrReplaceChild("mainharpoon", CubeListBuilder.create(), PartPose.offset(-1.0F, 11.0F, 0.0F));

		PartDefinition harpoonbase = mainharpoon.addOrReplaceChild("harpoonbase", CubeListBuilder.create().texOffs(16, 22).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0015F)), PartPose.offset(0.0015F, 1.0015F, -0.0015F));

		PartDefinition harpoon = mainharpoon.addOrReplaceChild("harpoon", CubeListBuilder.create().texOffs(28, 12).addBox(0.0F, -4.5F, -4.0F, 0.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.5015F, 0.0F));

		PartDefinition harpoon2_r1 = harpoon.addOrReplaceChild("harpoon2_r1", CubeListBuilder.create().texOffs(28, 12).addBox(0.0F, -4.5F, -4.0F, 0.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition maincannon = truemain.addOrReplaceChild("maincannon", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 11.0F, 0.0F, -3.1416F, 0.4363F, 3.1416F));

		PartDefinition cannonbase = maincannon.addOrReplaceChild("cannonbase", CubeListBuilder.create().texOffs(29, 30).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0015F)), PartPose.offset(0.0015F, 1.0015F, -0.0015F));

		PartDefinition addons = maincannon.addOrReplaceChild("addons", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0795F, -0.3265F));

		PartDefinition basecannon_r1 = addons.addOrReplaceChild("basecannon_r1", CubeListBuilder.create().texOffs(41, 30).mirror().addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.55F)).mirror(false), PartPose.offsetAndRotation(2.0235F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0036F));

		PartDefinition basecannon_r2 = addons.addOrReplaceChild("basecannon_r2", CubeListBuilder.create().texOffs(41, 30).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.55F)), PartPose.offsetAndRotation(-2.0235F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0036F));

		PartDefinition cannon = maincannon.addOrReplaceChild("cannon", CubeListBuilder.create().texOffs(17, 27).addBox(-6.9985F, -8.9955F, -2.0015F, 2.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(5, 32).addBox(-6.9985F, -0.9955F, -2.0015F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 11.0F, 0.0F));

		PartDefinition mainmelee = truemain.addOrReplaceChild("mainmelee", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 11.0F, 0.0F, 0.0F, 1.1345F, 0.0F));

		PartDefinition basemelee = mainmelee.addOrReplaceChild("basemelee", CubeListBuilder.create().texOffs(29, 35).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0015F, 1.0015F, -0.0015F));

		PartDefinition meleeclaw = mainmelee.addOrReplaceChild("meleeclaw", CubeListBuilder.create(), PartPose.offset(0.0012F, 1.9314F, -0.3186F));

		PartDefinition basemelee_r1 = meleeclaw.addOrReplaceChild("basemelee_r1", CubeListBuilder.create().texOffs(53, 40).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0012F, 1.0084F, 0.2784F, -0.4363F, 0.0F, 0.0F));

		PartDefinition bone = meleeclaw.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(41, 40).addBox(-0.5F, -2.0F, -3.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(41, 35).addBox(-3.5F, -5.0F, -3.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(47, 40).addBox(-2.0F, -2.0F, -3.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(41, 40).addBox(-3.5F, -2.0F, -3.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5003F, 5.0701F, 2.3171F));

		PartDefinition energyclaws = meleeclaw.addOrReplaceChild("energyclaws", CubeListBuilder.create().texOffs(53, 31).addBox(0.0F, -3.0F, -1.4917F, 0.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(53, 31).addBox(-1.5F, -3.0F, -1.4917F, 0.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(53, 31).addBox(1.5F, -3.0F, -1.5167F, 0.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0003F, 6.0701F, 0.3338F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
	public void selectArm(Player player, String name, Upgrade arm, float ageInTicks){
		if(player.getMainHandItem().isEmpty()){
			switch (name) {
				case "claws_arm" -> {
					maincannon.visible = false;
					mainharpoon.visible = false;
					mainmelee.visible = true;
				}
				case "cannon_arm" -> {
					maincannon.visible = true;
					mainharpoon.visible = false;
					mainmelee.visible = false;
				}
				case "gancho_arm" -> {
					maincannon.visible = false;
					mainharpoon.visible = true;
					mainmelee.visible = false;
					harpoon.visible = ((GanchoArm) arm).hasGancho;
				}
				default -> {
					maincannon.visible = false;
					mainharpoon.visible = false;
					mainmelee.visible = false;
				}
			}
		}else {
			maincannon.visible=false;
			mainharpoon.visible=false;
			mainmelee.visible=false;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		truemain.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.truemain;
	}

	public List<ModelPart> getRoot() {
		return List.of(this.truemain);

	}

	public void setPoseArmInGuiForPose(MultiArmCapability cap,PoseStack poseStack,float partialTicks) {
		switch (cap.pose){
			case CHARGE_CANNON -> {
				float percent = cap.getAnimShoot(partialTicks);
				poseStack.mulPose(Axis.YP.rotationDegrees((float) -29.0F));

				poseStack.mulPose(Axis.XP.rotationDegrees((float) -29.0F) );
				poseStack.mulPose(Axis.ZP.rotationDegrees((float) -13.0F));
				poseStack.translate(0,-0.1F*percent,0);
			}
			case STOP_AIMING -> {
				float percent = cap.getStopAiming(partialTicks);
				poseStack.mulPose(Axis.YP.rotationDegrees((float) -29.0F * percent));

				poseStack.mulPose(Axis.XP.rotationDegrees((float) -29.0F * percent));
				poseStack.mulPose(Axis.ZP.rotationDegrees((float) -13.0F * percent));
			}
			case CHARGE_CLAWS -> {
				float percent = cap.getAnimChargeClaw(partialTicks);
				float sin = Mth.sin(cap.getPlayer().tickCount*0.15F)*0.025F;
				float cos = Mth.cos(cap.getPlayer().tickCount*0.15F)*0.025F;
				poseStack.translate(sin,-0.4F*percent,cos);
			}
		}
	}
}