package com.TBK.crc.client.model;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.crc.CRC;
import com.TBK.crc.client.animacion.RexChickenAnim;
import com.TBK.crc.server.entity.RexChicken;
import com.TBK.crc.server.entity.RexPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RexChickenModel<T extends RexChicken> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID, "rexchickenmodel"), "main");
	public static final ModelLayerLocation ARMOR_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID, "armor"), "main");

	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart Body;
	private final ModelPart Head;
	private final ModelPart Mane;
	private final ModelPart HeadSection;
	private final ModelPart HeadSection2;
	private final ModelPart UpperJaw;
	private final ModelPart LowerJaw;
	private final ModelPart laser;
	private final ModelPart sectionlaser;
	private final ModelPart seat;
	private final ModelPart Chicken;
	private final ModelPart head2;
	private final ModelPart bill;
	private final ModelPart chin;
	private final ModelPart crest;
	private final ModelPart body2;
	private final ModelPart jetpack;
	private final ModelPart left_wing;
	private final ModelPart lower;
	private final ModelPart fist;
	private final ModelPart right_wing;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart Torso;
	private final ModelPart LeftLegJoint;
	private final ModelPart RightLegJoint;
	private final ModelPart Waist;
	private final ModelPart BackPack;
	private final ModelPart WeaponLeft;
	private final ModelPart ChickLauncher;
	private final ModelPart WeaponRight;
	private final ModelPart ChickLauncher2;
	private final ModelPart BackpackMiddle;
	private final ModelPart BackpakUpper;
	private final ModelPart LeftLeg;
	private final ModelPart UpperLeftLeg;
	private final ModelPart LowerLeftLeg;
	private final ModelPart FootSpike;
	private final ModelPart FootSpike2;
	private final ModelPart LeftLegHeel;
	private final ModelPart LeftFootWheels;
	private final ModelPart RightLeg;
	private final ModelPart UpperRightLeg;
	private final ModelPart LowerRightLeg;
	private final ModelPart FootSpike3;
	private final ModelPart FootSpike4;
	private final ModelPart RightFootWheels;
	private final ModelPart RightLegHeel;

	public RexChickenModel(ModelPart root) {
		this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.Body = this.main.getChild("Body");
		this.Head = this.Body.getChild("Head");
		this.Mane = this.Head.getChild("Mane");
		this.HeadSection = this.Mane.getChild("HeadSection");
		this.HeadSection2 = this.Mane.getChild("HeadSection2");
		this.UpperJaw = this.Head.getChild("UpperJaw");
		this.LowerJaw = this.Head.getChild("LowerJaw");
		this.laser = this.LowerJaw.getChild("laser");
		this.sectionlaser = this.laser.getChild("sectionlaser");
		this.seat = this.LowerJaw.getChild("seat");
		this.Chicken = this.Head.getChild("Chicken");
		this.head2 = this.Chicken.getChild("head2");
		this.bill = this.head2.getChild("bill");
		this.chin = this.head2.getChild("chin");
		this.crest = this.head2.getChild("crest");
		this.body2 = this.Chicken.getChild("body2");
		this.jetpack = this.body2.getChild("jetpack");
		this.left_wing = this.Chicken.getChild("left_wing");
		this.lower = this.left_wing.getChild("lower");
		this.fist = this.lower.getChild("fist");
		this.right_wing = this.Chicken.getChild("right_wing");
		this.left_leg = this.Chicken.getChild("left_leg");
		this.right_leg = this.Chicken.getChild("right_leg");
		this.Torso = this.Body.getChild("Torso");
		this.LeftLegJoint = this.Body.getChild("LeftLegJoint");
		this.RightLegJoint = this.Body.getChild("RightLegJoint");
		this.Waist = this.Body.getChild("Waist");
		this.BackPack = this.Body.getChild("BackPack");
		this.WeaponLeft = this.BackPack.getChild("WeaponLeft");
		this.ChickLauncher = this.WeaponLeft.getChild("ChickLauncher");
		this.WeaponRight = this.BackPack.getChild("WeaponRight");
		this.ChickLauncher2 = this.WeaponRight.getChild("ChickLauncher2");
		this.BackpackMiddle = this.BackPack.getChild("BackpackMiddle");
		this.BackpakUpper = this.BackPack.getChild("BackpakUpper");
		this.LeftLeg = this.main.getChild("LeftLeg");
		this.UpperLeftLeg = this.LeftLeg.getChild("UpperLeftLeg");
		this.LowerLeftLeg = this.LeftLeg.getChild("LowerLeftLeg");
		this.FootSpike = this.LowerLeftLeg.getChild("FootSpike");
		this.FootSpike2 = this.LowerLeftLeg.getChild("FootSpike2");
		this.LeftLegHeel = this.LowerLeftLeg.getChild("LeftLegHeel");
		this.LeftFootWheels = this.LowerLeftLeg.getChild("LeftFootWheels");
		this.RightLeg = this.main.getChild("RightLeg");
		this.UpperRightLeg = this.RightLeg.getChild("UpperRightLeg");
		this.LowerRightLeg = this.RightLeg.getChild("LowerRightLeg");
		this.FootSpike3 = this.LowerRightLeg.getChild("FootSpike3");
		this.FootSpike4 = this.LowerRightLeg.getChild("FootSpike4");
		this.RightFootWheels = this.LowerRightLeg.getChild("RightFootWheels");
		this.RightLegHeel = this.LowerRightLeg.getChild("RightLegHeel");
	}

	public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(-1.9473F, -65.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(1.9935F, -11.5F, 59.6197F));

		PartDefinition Body = main.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(-0.0462F, -53.5F, -29.6197F));

		PartDefinition Mane = Head.addOrReplaceChild("Mane", CubeListBuilder.create().texOffs(220, 236).addBox(-42.0F, -27.0F, 25.0F, 94.0F, 19.0F, 40.0F, new CubeDeformation(0.02F))
		.texOffs(414, 678).addBox(20.0F, -8.0F, 25.0F, 15.0F, 9.0F, 40.0F, deformation)
		.texOffs(409, 490).addBox(-25.0F, -8.0F, 25.0F, 14.0F, 9.0F, 40.0F, deformation)
		.texOffs(240, 704).addBox(-3.0F, -12.7591F, 25.9514F, 16.0F, 21.0F, 32.0F, deformation), PartPose.offsetAndRotation(-5.9473F, -3.1784F, -33.9939F, 0.0873F, 0.0F, 0.0F));

		PartDefinition HeadSection = Mane.addOrReplaceChild("HeadSection", CubeListBuilder.create(), PartPose.offset(45.8523F, -14.6527F, 45.0F));

		PartDefinition cube_r1 = HeadSection.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(530, 777).addBox(-8.5298F, -4.3535F, -5.2623F, 17.0F, 10.0F, 9.0F, deformation), PartPose.offsetAndRotation(4.6124F, 14.0473F, 12.1163F, 2.3562F, 0.0F, -0.3491F));

		PartDefinition cube_r2 = HeadSection.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(136, 459).addBox(-8.5F, -5.0F, -4.5F, 17.0F, 24.0F, 9.0F, deformation), PartPose.offsetAndRotation(5.3709F, 16.2183F, -6.2411F, 1.5708F, 0.0F, -0.3491F));

		PartDefinition cube_r3 = HeadSection.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(774, 765).addBox(-8.5F, -14.5F, 11.325F, 17.0F, 10.0F, 9.0F, deformation), PartPose.offsetAndRotation(10.9199F, 31.464F, -14.5652F, 0.9163F, 0.0F, -0.3491F));

		PartDefinition cube_r4 = HeadSection.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(584, 669).addBox(-10.0F, -9.5F, -20.0F, 20.0F, 25.0F, 40.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition HeadSection2 = Mane.addOrReplaceChild("HeadSection2", CubeListBuilder.create(), PartPose.offset(-33.9577F, -14.6527F, 45.0F));

		PartDefinition cube_r5 = HeadSection2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(530, 777).mirror().addBox(-8.4702F, -4.3535F, -5.2623F, 17.0F, 10.0F, 9.0F, deformation).mirror(false), PartPose.offsetAndRotation(-6.507F, 14.0473F, 12.1163F, 2.3562F, 0.0F, 0.3491F));

		PartDefinition cube_r6 = HeadSection2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(136, 459).mirror().addBox(-8.5F, -5.0F, -4.5F, 17.0F, 24.0F, 9.0F, deformation).mirror(false), PartPose.offsetAndRotation(-7.2655F, 16.2183F, -6.2411F, 1.5708F, 0.0F, 0.3491F));

		PartDefinition cube_r7 = HeadSection2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(774, 765).mirror().addBox(-8.5F, -14.5F, 11.325F, 17.0F, 10.0F, 9.0F, deformation).mirror(false), PartPose.offsetAndRotation(-12.8145F, 31.464F, -14.5652F, 0.9163F, 0.0F, 0.3491F));

		PartDefinition cube_r8 = HeadSection2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(584, 669).mirror().addBox(-10.0F, -9.5F, -20.0F, 20.0F, 25.0F, 40.0F, deformation).mirror(false), PartPose.offsetAndRotation(-1.8946F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition UpperJaw = Head.addOrReplaceChild("UpperJaw", CubeListBuilder.create().texOffs(594, 321).addBox(-20.5975F, -1.7964F, -48.8046F, 30.0F, 3.0F, 49.0F, deformation)
		.texOffs(0, 607).addBox(-20.5975F, -6.7964F, -47.8046F, 30.0F, 5.0F, 48.0F, new CubeDeformation(0.022F))
		.texOffs(488, 258).addBox(-20.5975F, -18.7964F, -21.8046F, 30.0F, 15.0F, 22.0F, new CubeDeformation(0.013F))
		.texOffs(336, 727).addBox(-20.5975F, -10.7964F, -46.8046F, 30.0F, 4.0F, 25.0F, new CubeDeformation(0.02F))
		.texOffs(740, 294).addBox(-20.5975F, -12.7964F, -39.8046F, 30.0F, 5.0F, 18.0F, deformation)
		.texOffs(744, 237).addBox(-20.5975F, -14.7964F, -33.8046F, 30.0F, 2.0F, 12.0F, deformation)
		.texOffs(312, 226).addBox(-20.5975F, -16.7964F, -27.8046F, 30.0F, 2.0F, 6.0F, deformation), PartPose.offsetAndRotation(4.6502F, -7.1534F, -9.3008F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r9 = UpperJaw.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(286, 578).addBox(-11.0F, -1.0F, -6.0F, 30.0F, 7.0F, 49.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-9.5975F, -11.693F, -41.0806F, 0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r10 = UpperJaw.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(734, 589).addBox(-11.0F, -1.0F, -13.0F, 30.0F, 2.0F, 12.0F, deformation), PartPose.offsetAndRotation(-9.5975F, -11.4955F, -45.8492F, 1.2654F, 0.0F, 0.0F));

		PartDefinition LowerJaw = Head.addOrReplaceChild("LowerJaw", CubeListBuilder.create().texOffs(240, 226).addBox(-13.4173F, 11.2057F, -56.6651F, 30.0F, 2.0F, 6.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.01F))
		.texOffs(570, 419).addBox(-13.4173F, 3.2057F, -50.6651F, 30.0F, 10.0F, 49.0F, new CubeDeformation(0.02F))
		.texOffs(384, 226).addBox(-13.4173F, -1.7943F, -13.6651F, 17.0F, 5.0F, 2.0F, deformation)
		.texOffs(764, 156).addBox(-4.4698F, 13.2433F, -29.037F, 11.0F, 10.0F, 20.0F, new CubeDeformation(0.25F)), PartPose.offset(-2.53F, -3.4167F, -6.2131F));

		PartDefinition cube_r11 = LowerJaw.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(869, 588).addBox(-9.0F, -11.0F, 24.0F, 16.0F, 6.0F, 5.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.002F))
		.texOffs(883, 570).mirror().addBox(7.0F, -11.0F, -1.0F, 6.0F, 6.0F, 30.0F, new CubeDeformation(0.002F)).mirror(false)
		.texOffs(866, 574).addBox(-8.996F, -11.0F, -1.0F, 16.0F, 6.0F, 6.0F, new CubeDeformation(0.002F))
		.texOffs(883, 570).addBox(-15.0F, -11.0F, -1.0F, 6.0F, 6.0F, 30.0F, new CubeDeformation(0.002F))
		.texOffs(464, 554).addBox(-16.0F, -5.0F, -3.0F, 30.0F, 6.0F, 51.0F, new CubeDeformation(0.0021F)), PartPose.offsetAndRotation(2.5827F, 6.388F, -50.2337F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r12 = LowerJaw.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(883, 559).mirror().addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 2.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.002F)).mirror(false), PartPose.offsetAndRotation(3.4733F, -6.2868F, -49.3743F, 0.0562F, 0.0668F, -0.8708F));

		PartDefinition cube_r13 = LowerJaw.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(883, 559).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 1.0F, 2.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.002F)), PartPose.offsetAndRotation(-0.4133F, -6.2868F, -49.3743F, 0.0562F, -0.0668F, 0.8708F));

		PartDefinition cube_r14 = LowerJaw.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(883, 563).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.002F)), PartPose.offsetAndRotation(-0.4133F, -4.7445F, -49.2F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r15 = LowerJaw.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(520, 353).addBox(-16.0F, -9.0F, -3.0F, 30.0F, 10.0F, 6.0F, deformation), PartPose.offsetAndRotation(2.5827F, 11.1541F, -53.5033F, -0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r16 = LowerJaw.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(416, 634).addBox(-16.0F, -1.5F, 6.0F, 5.0F, 10.0F, 9.0F, deformation)
		.texOffs(416, 634).addBox(9.0F, -1.5F, 6.0F, 5.0F, 10.0F, 9.0F, deformation), PartPose.offsetAndRotation(2.5827F, -0.0837F, -9.4853F, 0.1745F, 0.0F, 0.0F));

		PartDefinition Laser = LowerJaw.addOrReplaceChild("laser", CubeListBuilder.create().texOffs(240, 757).addBox(-4.5F, 9.2387F, -19.01F, 9.0F, 10.0F, 23.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offset(1.0302F, 22.9296F, -14.777F));

		PartDefinition LeftLegUpper_r1 = Laser.addOrReplaceChild("LeftLegUpper_r1", CubeListBuilder.create().texOffs(546, 720).addBox(-3.5F, -5.0F, -11.5F, 7.0F, 7.0F, 7.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(0.0F, 21.2716F, 7.572F, -0.8727F, 0.0F, 0.0F));

		PartDefinition sectionlaser = Laser.addOrReplaceChild("sectionlaser", CubeListBuilder.create().texOffs(304, 757).addBox(-3.5F, 0.2738F, -4.6544F, 7.0F, 15.0F, 8.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition seat = LowerJaw.addOrReplaceChild("seat", CubeListBuilder.create(), PartPose.offsetAndRotation(1.78F, -4.4279F, -33.3233F, -0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r17 = seat.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 19).addBox(-8.0F, -2.0F, 2.0F, 16.0F, 4.0F, 20.0F, deformation), PartPose.offsetAndRotation(0.0F, 7.4956F, 4.6244F, 1.6581F, 0.0F, 0.0F));

		PartDefinition cube_r18 = seat.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 50).addBox(-8.0F, -2.0F, -6.0F, 16.0F, 4.0F, 12.0F, deformation), PartPose.offsetAndRotation(0.0F, 4.4588F, -3.5785F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Chicken = Head.addOrReplaceChild("Chicken", CubeListBuilder.create(), PartPose.offset(0.0F, -10.35F, -50.0F));

		PartDefinition head2 = Chicken.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(150, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F, deformation)
		.texOffs(168, 10).addBox(-2.5F, -6.1F, -3.0F, 2.0F, 2.0F, 3.0F, deformation)
		.texOffs(175, 10).addBox(-2.0F, -5.6F, -3.025F, 1.0F, 1.0F, 2.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.925F, 1.05F, -0.0873F, 0.0F, 0.0F));

		PartDefinition bill = head2.addOrReplaceChild("bill", CubeListBuilder.create().texOffs(164, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chin = head2.addOrReplaceChild("chin", CubeListBuilder.create().texOffs(164, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition crest = head2.addOrReplaceChild("crest", CubeListBuilder.create(), PartPose.offset(0.5F, -4.3936F, 0.5731F));

		PartDefinition head_r1 = crest.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(150, 23).addBox(-0.5F, -2.9132F, -1.5076F, 1.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0436F, -0.4981F, 0.2618F, 0.0F, 0.0F));

		PartDefinition body2 = Chicken.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(150, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, deformation), PartPose.offsetAndRotation(0.0F, 2.075F, 4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition jetpack = body2.addOrReplaceChild("jetpack", CubeListBuilder.create().texOffs(176, 27).addBox(1.0F, 2.225F, -0.9833F, 3.0F, 1.0F, 2.0F, deformation)
		.texOffs(176, 27).addBox(-4.0F, 2.225F, -0.9833F, 3.0F, 1.0F, 2.0F, deformation)
		.texOffs(166, 27).mirror().addBox(-4.0F, -4.0F, -0.9833F, 3.0F, 8.0F, 2.0F, new CubeDeformation(0.02F)).mirror(false)
		.texOffs(176, 31).addBox(-1.5F, -2.0F, -0.5333F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.02F))
		.texOffs(166, 27).addBox(1.0F, -4.0F, -0.9833F, 3.0F, 8.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 2.0F, 3.9833F));

		PartDefinition left_wing = Chicken.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(191, 12).addBox(0.0F, -1.0F, -1.5F, 2.0F, 4.0F, 3.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.002F)), PartPose.offsetAndRotation(3.0F, -0.7F, 0.9F, -0.9421F, 0.3567F, -0.0931F));

		PartDefinition lower = left_wing.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(1.0F, 2.0679F, 0.0654F));

		PartDefinition left_wing_r1 = lower.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(191, 19).addBox(-1.0F, -2.0F, -1.5F, 2.0F, 4.0F, 3.0F, deformation), PartPose.offsetAndRotation(0.0F, 1.5F, -1.0F, -0.6981F, 0.0F, 0.0F));

		PartDefinition fist = lower.addOrReplaceChild("fist", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 4.1812F, -3.2498F, 0.0251F, 0.2388F, 0.1923F));

		PartDefinition left_wing_r2 = fist.addOrReplaceChild("left_wing_r2", CubeListBuilder.create().texOffs(190, 6).addBox(-1.6359F, 1.8219F, -1.5211F, 3.0F, 3.0F, 3.0F, deformation), PartPose.offsetAndRotation(0.0F, -2.6812F, 2.2498F, -0.6981F, 0.0F, 0.0F));

		PartDefinition right_wing = Chicken.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(174, 13).addBox(0.0F, -1.775F, 0.0F, 1.0F, 4.0F, 6.0F, deformation), PartPose.offsetAndRotation(-4.0F, 0.0F, 1.0F, 0.0F, -0.2182F, 0.0F));

		PartDefinition left_leg = Chicken.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(176, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, deformation)
		.texOffs(185, 1).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, deformation), PartPose.offsetAndRotation(1.0F, 4.0F, 5.0F, -1.2104F, -0.2457F, -0.0914F));

		PartDefinition right_leg = Chicken.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(188, 0).addBox(-1.0F, 2.0F, -3.0F, 3.0F, 3.0F, 3.0F, deformation)
		.texOffs(200, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, deformation), PartPose.offsetAndRotation(-2.0F, 4.0F, 5.0F, -1.1655F, 0.6162F, -0.0752F));

		PartDefinition right_leg_r1 = right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(200, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.5F, -0.5F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition Torso = Body.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(628, 734).addBox(-10.0F, -20.0967F, -40.412F, 20.0F, 18.0F, 18.0F, deformation), PartPose.offset(-1.046F, -9.4549F, 12.2578F));

		PartDefinition LeftLegUpper_r2 = Torso.addOrReplaceChild("LeftLegUpper_r2", CubeListBuilder.create().texOffs(728, 413).addBox(-10.0F, -25.5F, -17.5F, 16.0F, 4.0F, 32.0F, deformation), PartPose.offsetAndRotation(2.0F, -9.0137F, 40.3022F, 0.1309F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r3 = Torso.addOrReplaceChild("LeftLegUpper_r3", CubeListBuilder.create().texOffs(444, 611).addBox(-10.0F, -8.5F, -17.5F, 20.0F, 17.0F, 50.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.012F)), PartPose.offsetAndRotation(0.0F, -22.3327F, 41.6188F, 0.1309F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r4 = Torso.addOrReplaceChild("LeftLegUpper_r4", CubeListBuilder.create().texOffs(732, 95).addBox(-10.0F, -2.5F, -12.5F, 20.0F, 11.0F, 25.0F, deformation), PartPose.offsetAndRotation(0.0F, -33.106F, 77.1282F, 0.829F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r5 = Torso.addOrReplaceChild("LeftLegUpper_r5", CubeListBuilder.create().texOffs(446, 727).addBox(-6.0F, -8.0F, -31.05F, 16.0F, 5.0F, 34.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.01F))
		.texOffs(634, 204).addBox(-8.0F, -3.0F, -32.05F, 20.0F, 17.0F, 35.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(-2.0F, -24.2685F, 23.1944F, 0.7854F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r6 = Torso.addOrReplaceChild("LeftLegUpper_r6", CubeListBuilder.create().texOffs(744, 204).addBox(-6.0F, -8.0F, -13.05F, 16.0F, 5.0F, 28.0F, deformation)
		.texOffs(136, 535).addBox(-8.0F, -3.0F, -32.05F, 20.0F, 17.0F, 55.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(-2.0F, -16.2685F, -16.8056F, -0.7854F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r7 = Torso.addOrReplaceChild("LeftLegUpper_r7", CubeListBuilder.create().texOffs(704, 669).addBox(-10.0F, -15.5F, -27.5F, 20.0F, 24.0F, 28.0F, deformation), PartPose.offsetAndRotation(0.0F, -30.1246F, -20.605F, -0.2618F, 0.0F, 0.0F));

		PartDefinition LeftLegJoint = Body.addOrReplaceChild("LeftLegJoint", CubeListBuilder.create().texOffs(96, 745).addBox(-5.0F, -7.5F, -27.5F, 20.0F, 15.0F, 20.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F)), PartPose.offset(13.954F, 2.0F, 48.0F));

		PartDefinition RightLegJoint = Body.addOrReplaceChild("RightLegJoint", CubeListBuilder.create().texOffs(96, 745).mirror().addBox(-15.0F, -7.5F, -27.5F, 20.0F, 15.0F, 20.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)).mirror(false), PartPose.offset(-14.0464F, 2.0F, 48.0F));

		PartDefinition Waist = Body.addOrReplaceChild("Waist", CubeListBuilder.create().texOffs(336, 704).addBox(-18.046F, -14.0714F, -56.2788F, 34.0F, 15.0F, 5.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F))
		.texOffs(240, 118).addBox(-9.046F, 6.3266F, -57.8698F, 15.0F, 20.0F, 88.0F, new CubeDeformation(0.023F))
		.texOffs(0, 550).addBox(-11.046F, -11.5F, 19.5F, 20.0F, 19.0F, 35.0F, new CubeDeformation(0.023F))
		.texOffs(488, 181).addBox(-13.046F, -11.7234F, -29.0478F, 24.0F, 28.0F, 49.0F, new CubeDeformation(0.023F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r8 = Waist.addOrReplaceChild("LeftLegUpper_r8", CubeListBuilder.create().texOffs(0, 459).addBox(26.5F, -10.0F, -52.5F, 5.0F, 28.0F, 63.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F))
		.texOffs(0, 459).mirror().addBox(-2.5F, -10.0F, -52.5F, 5.0F, 28.0F, 63.0F, new CubeDeformation(0.023F)).mirror(false), PartPose.offsetAndRotation(-15.592F, -12.1617F, -4.4639F, 0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r9 = Waist.addOrReplaceChild("LeftLegUpper_r9", CubeListBuilder.create().texOffs(740, 256).addBox(-10.0F, -10.0F, -7.0F, 20.0F, 16.0F, 22.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-1.046F, 12.3858F, 31.463F, -1.3526F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r10 = Waist.addOrReplaceChild("LeftLegUpper_r10", CubeListBuilder.create().texOffs(734, 548).addBox(-10.0F, -7.5F, 3.5F, 20.0F, 20.0F, 21.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-1.046F, 1.7766F, 29.524F, -0.5236F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r11 = Waist.addOrReplaceChild("LeftLegUpper_r11", CubeListBuilder.create().texOffs(626, 548).addBox(-12.0F, -14.0F, -5.5F, 24.0F, 28.0F, 30.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-1.046F, 11.3266F, -45.3698F, 0.4363F, 0.0F, 0.0F));

		PartDefinition BackPack = Body.addOrReplaceChild("BackPack", CubeListBuilder.create().texOffs(220, 295).addBox(-16.0F, -20.5F, -51.0F, 32.0F, 17.0F, 60.0F, deformation), PartPose.offset(-1.046F, -43.6297F, 93.224F));

		PartDefinition WeaponLeft = BackPack.addOrReplaceChild("WeaponLeft", CubeListBuilder.create().texOffs(250, 86).addBox(-5.2272F, -9.9895F, -7.5F, 47.0F, 14.0F, 15.0F, deformation), PartPose.offset(34.0269F, -9.6684F, -10.9585F));

		PartDefinition LeftLegUpper_r12 = WeaponLeft.addOrReplaceChild("LeftLegUpper_r12", CubeListBuilder.create().texOffs(728, 449).addBox(-16.0F, -4.5F, -7.5F, 38.0F, 9.0F, 15.0F, deformation), PartPose.offsetAndRotation(16.7728F, 3.5105F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition ChickLauncher = WeaponLeft.addOrReplaceChild("ChickLauncher", CubeListBuilder.create().texOffs(404, 295).addBox(-21.7698F, -18.1432F, -25.7143F, 44.0F, 8.0F, 50.0F, deformation)
		.texOffs(0, 401).addBox(-21.7698F, 10.0068F, -25.7143F, 44.0F, 8.0F, 50.0F, deformation)
		.texOffs(592, 258).addBox(-18.0944F, -11.2956F, -15.7143F, 34.0F, 23.0F, 40.0F, deformation), PartPose.offsetAndRotation(41.0267F, 10.4983F, -16.7857F, 0.0F, 0.0F, 2.5307F));

		PartDefinition RightLegUpper_r1 = ChickLauncher.addOrReplaceChild("RightLegUpper_r1", CubeListBuilder.create().texOffs(286, 634).addBox(-17.0F, -10.0F, -25.0F, 15.0F, 20.0F, 50.0F, deformation), PartPose.offsetAndRotation(-10.5887F, -1.8956F, -0.7143F, 0.0F, 0.0F, 0.4363F));

		PartDefinition RightLegUpper_r2 = ChickLauncher.addOrReplaceChild("RightLegUpper_r2", CubeListBuilder.create().texOffs(634, 134).addBox(-7.5F, -10.0F, -25.0F, 15.0F, 20.0F, 50.0F, deformation), PartPose.offsetAndRotation(-19.1986F, 5.8764F, -0.7143F, 0.0F, 0.0F, -0.4363F));

		PartDefinition LeftLegUpper_r13 = ChickLauncher.addOrReplaceChild("LeftLegUpper_r13", CubeListBuilder.create().texOffs(622, 478).addBox(-7.5F, -10.0F, -25.0F, 15.0F, 20.0F, 50.0F, deformation), PartPose.offsetAndRotation(19.5156F, 5.8764F, -0.7143F, 0.0F, 0.0F, 0.4363F));

		PartDefinition LeftLegUpper_r14 = ChickLauncher.addOrReplaceChild("LeftLegUpper_r14", CubeListBuilder.create().texOffs(156, 607).addBox(2.0F, -10.0F, -25.0F, 15.0F, 20.0F, 50.0F, deformation), PartPose.offsetAndRotation(10.9056F, -1.8956F, -0.7143F, 0.0F, 0.0F, -0.4363F));

		PartDefinition WeaponRight = BackPack.addOrReplaceChild("WeaponRight", CubeListBuilder.create().texOffs(250, 86).mirror().addBox(-41.7728F, -9.9895F, -7.5F, 47.0F, 14.0F, 15.0F, deformation).mirror(false), PartPose.offset(-34.0273F, -9.6684F, -10.9585F));

		PartDefinition RightLegUpper_r3 = WeaponRight.addOrReplaceChild("RightLegUpper_r3", CubeListBuilder.create().texOffs(728, 449).mirror().addBox(-22.0F, -4.5F, -7.5F, 38.0F, 9.0F, 15.0F, deformation).mirror(false), PartPose.offsetAndRotation(-16.7728F, 3.5105F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition ChickLauncher2 = WeaponRight.addOrReplaceChild("ChickLauncher2", CubeListBuilder.create().texOffs(404, 295).mirror().addBox(-22.2302F, -18.1432F, -25.7143F, 44.0F, 8.0F, 50.0F, deformation).mirror(false)
		.texOffs(0, 401).mirror().addBox(-22.2302F, 10.0068F, -25.7143F, 44.0F, 8.0F, 50.0F, deformation).mirror(false)
		.texOffs(592, 258).mirror().addBox(-15.9056F, -11.2956F, -15.7143F, 34.0F, 23.0F, 40.0F, deformation).mirror(false), PartPose.offsetAndRotation(-41.0267F, 10.4983F, -16.7857F, 0.0F, 0.0F, -2.5307F));

		PartDefinition LeftLegUpper_r15 = ChickLauncher2.addOrReplaceChild("LeftLegUpper_r15", CubeListBuilder.create().texOffs(286, 634).mirror().addBox(2.0F, -10.0F, -25.0F, 15.0F, 20.0F, 50.0F, deformation).mirror(false), PartPose.offsetAndRotation(10.5887F, -1.8956F, -0.7143F, 0.0F, 0.0F, -0.4363F));

		PartDefinition LeftLegUpper_r16 = ChickLauncher2.addOrReplaceChild("LeftLegUpper_r16", CubeListBuilder.create().texOffs(634, 134).mirror().addBox(-7.5F, -10.0F, -25.0F, 15.0F, 20.0F, 50.0F, deformation).mirror(false), PartPose.offsetAndRotation(19.1986F, 5.8764F, -0.7143F, 0.0F, 0.0F, 0.4363F));

		PartDefinition RightLegUpper_r4 = ChickLauncher2.addOrReplaceChild("RightLegUpper_r4", CubeListBuilder.create().texOffs(622, 478).mirror().addBox(-7.5F, -10.0F, -25.0F, 15.0F, 20.0F, 50.0F, deformation).mirror(false), PartPose.offsetAndRotation(-19.5156F, 5.8764F, -0.7143F, 0.0F, 0.0F, -0.4363F));

		PartDefinition RightLegUpper_r5 = ChickLauncher2.addOrReplaceChild("RightLegUpper_r5", CubeListBuilder.create().texOffs(156, 607).mirror().addBox(-17.0F, -10.0F, -25.0F, 15.0F, 20.0F, 50.0F, deformation).mirror(false), PartPose.offsetAndRotation(-10.9056F, -1.8956F, -0.7143F, 0.0F, 0.0F, 0.4363F));

		PartDefinition BackpackMiddle = BackPack.addOrReplaceChild("BackpackMiddle", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.454F, -7.398F, 13.0584F, -0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r1 = BackpackMiddle.addOrReplaceChild("LeftLegLower_r1", CubeListBuilder.create().texOffs(732, 649).addBox(-15.5F, -5.4F, 0.05F, 32.0F, 10.0F, 10.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(0.0F, -14.5915F, 8.4049F, -0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r2 = BackpackMiddle.addOrReplaceChild("LeftLegLower_r2", CubeListBuilder.create().texOffs(450, 68).addBox(-15.5F, -8.0F, -21.0F, 32.0F, 4.0F, 11.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(0.0F, 7.4455F, 1.5989F, -2.1817F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r3 = BackpackMiddle.addOrReplaceChild("LeftLegLower_r3", CubeListBuilder.create().texOffs(752, 348).addBox(-15.5F, -2.0F, -5.0F, 32.0F, 4.0F, 10.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(0.0F, -10.1037F, 17.9891F, -1.5272F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r4 = BackpackMiddle.addOrReplaceChild("LeftLegLower_r4", CubeListBuilder.create().texOffs(204, 372).addBox(-13.0F, 26.5F, -25.5F, 32.0F, 5.0F, 63.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-2.5F, -8.837F, -33.4361F, 0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r5 = BackpackMiddle.addOrReplaceChild("LeftLegLower_r5", CubeListBuilder.create().texOffs(710, 373).addBox(-13.0F, -12.5F, 28.5F, 32.0F, 18.0F, 22.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-2.5F, 13.2066F, -29.7757F, 0.4363F, 0.0F, 0.0F));

		PartDefinition BackpakUpper = BackPack.addOrReplaceChild("BackpakUpper", CubeListBuilder.create().texOffs(394, 372).addBox(-64.5F, 3.261F, -84.9398F, 82.0F, 29.0F, 18.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(23.546F, -8.123F, 31.0584F, -0.4363F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r1 = BackpakUpper.addOrReplaceChild("RightLegLower_r1", CubeListBuilder.create().texOffs(628, 770).mirror().addBox(-12.5F, -2.0F, -5.0F, 20.0F, 4.0F, 10.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)).mirror(false)
		.texOffs(628, 770).addBox(39.5924F, -2.0F, -5.0F, 20.0F, 4.0F, 10.0F, new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-47.0924F, -10.1037F, 17.9891F, -1.5272F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r2 = BackpakUpper.addOrReplaceChild("RightLegLower_r2", CubeListBuilder.create().texOffs(0, 768).mirror().addBox(-12.5F, -8.0F, -21.0F, 20.0F, 4.0F, 11.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)).mirror(false)
		.texOffs(0, 768).addBox(39.5924F, -8.0F, -21.0F, 20.0F, 4.0F, 11.0F, new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-47.0924F, 7.4455F, 1.5989F, -2.1817F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r3 = BackpakUpper.addOrReplaceChild("RightLegLower_r3", CubeListBuilder.create().texOffs(450, 0).mirror().addBox(-15.0F, 26.5F, -25.5F, 20.0F, 5.0F, 63.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)).mirror(false)
		.texOffs(450, 0).addBox(37.0924F, 26.5F, -25.5F, 20.0F, 5.0F, 63.0F, new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-44.5924F, -8.837F, -33.4361F, 0.4363F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r4 = BackpakUpper.addOrReplaceChild("RightLegLower_r4", CubeListBuilder.create().texOffs(0, 118).mirror().addBox(-15.0F, -12.5F, -49.5F, 20.0F, 18.0F, 100.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)).mirror(false)
		.texOffs(0, 118).addBox(37.0924F, -12.5F, -49.5F, 20.0F, 18.0F, 100.0F, new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-44.5924F, 13.2066F, -29.7757F, 0.4363F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r5 = BackpakUpper.addOrReplaceChild("RightLegLower_r5", CubeListBuilder.create().texOffs(470, 766).mirror().addBox(-12.5F, -5.4F, 0.05F, 20.0F, 10.0F, 10.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)).mirror(false)
		.texOffs(470, 766).addBox(39.5924F, -5.4F, 0.05F, 20.0F, 10.0F, 10.0F, new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-47.0924F, -14.5915F, 8.4049F, -0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r6 = BackpakUpper.addOrReplaceChild("LeftLegLower_r6", CubeListBuilder.create().texOffs(404, 353).addBox(-27.0F, 8.5F, -10.0F, 54.0F, 9.0F, 4.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-22.5F, 5.4754F, -16.4472F, -2.5307F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r7 = BackpakUpper.addOrReplaceChild("LeftLegLower_r7", CubeListBuilder.create().texOffs(616, 0).addBox(-26.0F, -17.5F, -10.0F, 54.0F, 35.0F, 20.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-23.5F, -3.1155F, -34.8704F, -1.4835F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r8 = BackpakUpper.addOrReplaceChild("LeftLegLower_r8", CubeListBuilder.create().texOffs(0, 346).addBox(-41.0F, -20.5F, -9.0F, 82.0F, 35.0F, 20.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.023F)), PartPose.offsetAndRotation(-23.5F, 5.2779F, -67.9872F, -1.1345F, 0.0F, 0.0F));

		PartDefinition LeftLeg = main.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(28.5F, -1.7705F, 32.2014F));

		PartDefinition UpperLeftLeg = LeftLeg.addOrReplaceChild("UpperLeftLeg", CubeListBuilder.create().texOffs(464, 487).addBox(-12.4966F, 0.4743F, -20.6246F, 25.0F, 13.0F, 54.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.012F))
		.texOffs(586, 68).addBox(-10.4966F, 13.5458F, -41.1176F, 21.0F, 14.0F, 52.0F, new CubeDeformation(0.012F))
		.texOffs(0, 236).addBox(-12.5066F, -24.0421F, -21.32F, 25.0F, 25.0F, 85.0F, new CubeDeformation(0.023F))
		.texOffs(732, 606).addBox(7.4934F, -31.0421F, 27.68F, 5.0F, 7.0F, 36.0F, new CubeDeformation(0.023F))
		.texOffs(546, 734).addBox(-12.5066F, -31.0421F, 27.68F, 5.0F, 7.0F, 36.0F, new CubeDeformation(0.023F)), PartPose.offset(13.0066F, 13.3125F, -43.3813F));

		PartDefinition LeftLegUpper_r17 = UpperLeftLeg.addOrReplaceChild("LeftLegUpper_r17", CubeListBuilder.create().texOffs(0, 728).addBox(-10.5F, -6.5F, -12.5F, 21.0F, 13.0F, 27.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(0.012F)), PartPose.offsetAndRotation(0.0034F, 28.1719F, -27.0378F, 0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r18 = UpperLeftLeg.addOrReplaceChild("LeftLegUpper_r18", CubeListBuilder.create().texOffs(594, 373).addBox(-12.5F, -1.0F, -17.0F, 25.0F, 12.0F, 33.0F,deformation!=CubeDeformation.NONE ? deformation :  new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0164F, -3.0588F, 44.7114F, 0.3927F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r19 = UpperLeftLeg.addOrReplaceChild("LeftLegUpper_r19", CubeListBuilder.create().texOffs(130, 677).addBox(-12.5F, -67.0F, -15.0F, 25.0F, 38.0F, 30.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.021F)), PartPose.offsetAndRotation(-0.0066F, 6.2699F, 8.7217F, 1.5708F, 0.0F, 0.0F));

		PartDefinition LeftLegUpper_r20 = UpperLeftLeg.addOrReplaceChild("LeftLegUpper_r20", CubeListBuilder.create().texOffs(0, 660).addBox(-12.5F, -15.0F, -15.0F, 25.0F, 28.0F, 40.0F, deformation), PartPose.offsetAndRotation(-0.0066F, -0.6168F, -38.6767F, 0.3927F, 0.0F, 0.0F));

		PartDefinition LowerLeftLeg = LeftLeg.addOrReplaceChild("LowerLeftLeg", CubeListBuilder.create(), PartPose.offset(13.0F, 47.0F, -37.0F));

		PartDefinition LeftLegLower_r9 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r9", CubeListBuilder.create().texOffs(704, 721).addBox(-22.5F, -4.5F, -11.5F, 25.0F, 20.0F, 24.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F)), PartPose.offsetAndRotation(10.0F, 37.3576F, -82.1223F, 1.2654F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r10 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r10", CubeListBuilder.create().texOffs(704, 765).addBox(-12.5F, -5.4F, 0.05F, 25.0F, 10.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F)), PartPose.offsetAndRotation(0.0F, -13.3237F, 5.6859F, -0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r11 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r11", CubeListBuilder.create().texOffs(400, 766).addBox(-12.5F, -2.0F, -5.0F, 25.0F, 4.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(0.0F, -8.8359F, 15.2701F, -1.5272F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r12 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r12", CubeListBuilder.create().texOffs(764, 186).addBox(-12.5F, -8.0F, -21.0F, 25.0F, 4.0F, 11.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(0.0F, 8.7133F, -1.12F, -2.1817F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r13 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r13", CubeListBuilder.create().texOffs(732, 55).addBox(-7.5F, -7.5F, -17.5F, 15.0F, 10.0F, 30.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(0.0F, 12.2276F, 13.5124F, 0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r14 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r14", CubeListBuilder.create().texOffs(394, 419).addBox(-10.0F, 26.5F, -25.5F, 25.0F, 5.0F, 63.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(-2.5F, -7.5692F, -36.155F, 0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r15 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -12.5F, -49.5F, 25.0F, 18.0F, 100.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(-2.5F, 14.4745F, -32.4947F, 0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r16 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r16", CubeListBuilder.create().texOffs(764, 0).addBox(-2.5F, -4.5F, -9.5F, 5.0F, 20.0F, 22.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(524, 678).addBox(27.5F, -4.5F, -9.5F, 5.0F, 20.0F, 22.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(-15.0F, 39.8384F, -79.3554F, 1.2654F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r17 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r17", CubeListBuilder.create().texOffs(586, 134).addBox(-2.5F, -9.0F, -7.5F, 5.0F, 15.0F, 15.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(286, 535).addBox(27.5F, -9.0F, -7.5F, 5.0F, 15.0F, 15.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(-15.0F, 19.4253F, -8.3282F, 0.4363F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r18 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r18", CubeListBuilder.create().texOffs(136, 492).addBox(-2.5F, -9.0F, -7.5F, 5.0F, 18.0F, 15.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(446, 181).addBox(27.5F, -9.0F, -7.5F, 5.0F, 18.0F, 15.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(-15.0F, 10.0719F, -16.8989F, 1.0472F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r19 = LowerLeftLeg.addOrReplaceChild("LeftLegLower_r19", CubeListBuilder.create().texOffs(446, 86).addBox(-2.5F, -9.0F, -47.5F, 5.0F, 30.0F, 65.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(250, 0).addBox(2.5F, 10.0F, -47.5F, 25.0F, 11.0F, 75.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(188, 440).addBox(27.5F, -9.0F, -47.5F, 5.0F, 30.0F, 65.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(-15.0F, 14.6285F, -33.0195F, 0.4363F, 0.0F, 0.0F));

		PartDefinition FootSpike = LowerLeftLeg.addOrReplaceChild("FootSpike", CubeListBuilder.create().texOffs(536, 68).addBox(-2.0F, -36.4787F, 6.9667F, 5.0F, 5.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(110, 560).addBox(-2.0F, -36.4787F, 16.9667F, 5.0F, 8.0F, 5.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offset(-8.5F, 50.3217F, -94.784F));

		PartDefinition LeftLegLower_r20 = FootSpike.addOrReplaceChild("LeftLegLower_r20", CubeListBuilder.create().texOffs(446, 214).addBox(-23.5F, -5.5F, 1.5F, 10.0F, 9.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(752, 473).addBox(-22.5F, -4.5F, -7.5F, 8.0F, 8.0F, 25.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(18.5F, -15.3836F, 4.9881F, 1.2654F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r21 = FootSpike.addOrReplaceChild("LeftLegLower_r21", CubeListBuilder.create().texOffs(570, 478).addBox(-22.5F, -17.5F, -7.5F, 6.0F, 2.0F, 6.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(19.5F, -4.5012F, 19.6382F, 1.2654F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r22 = FootSpike.addOrReplaceChild("LeftLegLower_r22", CubeListBuilder.create().texOffs(422, 226).addBox(-22.5F, -19.5F, -7.5F, 6.0F, 4.0F, 6.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(19.5F, -4.8061F, 18.4295F, 1.2654F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r23 = FootSpike.addOrReplaceChild("LeftLegLower_r23", CubeListBuilder.create().texOffs(422, 366).addBox(-3.0F, -2.625F, -0.95F, 6.0F, 2.0F, 3.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(404, 366).addBox(-3.0F, -3.475F, 1.5F, 6.0F, 2.0F, 3.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(328, 476).addBox(-3.0F, -1.5F, -3.5F, 6.0F, 2.0F, 8.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(0.0F, 1.4246F, -0.4074F, 0.9599F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r24 = FootSpike.addOrReplaceChild("LeftLegLower_r24", CubeListBuilder.create().texOffs(136, 525).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 1.0F, 7.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.3963F, 0.0F, 0.0F));

		PartDefinition FootSpike2 = LowerLeftLeg.addOrReplaceChild("FootSpike2", CubeListBuilder.create().texOffs(586, 164).addBox(-2.0F, -36.4787F, 6.9667F, 5.0F, 5.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(286, 565).addBox(-2.0F, -36.4787F, 16.9667F, 5.0F, 8.0F, 5.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offset(7.5F, 50.3217F, -94.784F));

		PartDefinition LeftLegLower_r25 = FootSpike2.addOrReplaceChild("LeftLegLower_r25", CubeListBuilder.create().texOffs(240, 677).addBox(-23.5F, -5.5F, 1.5F, 10.0F, 9.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(752, 506).addBox(-22.5F, -4.5F, -7.5F, 8.0F, 8.0F, 25.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(18.5F, -15.3836F, 4.9881F, 1.2654F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r26 = FootSpike2.addOrReplaceChild("LeftLegLower_r26", CubeListBuilder.create().texOffs(110, 573).addBox(-22.5F, -17.5F, -7.5F, 6.0F, 2.0F, 6.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(19.5F, -4.5012F, 19.6382F, 1.2654F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r27 = FootSpike2.addOrReplaceChild("LeftLegLower_r27", CubeListBuilder.create().texOffs(110, 550).addBox(-22.5F, -19.5F, -7.5F, 6.0F, 4.0F, 6.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(19.5F, -4.8061F, 18.4295F, 1.2654F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r28 = FootSpike2.addOrReplaceChild("LeftLegLower_r28", CubeListBuilder.create().texOffs(458, 366).addBox(-3.0F, -2.625F, -0.95F, 6.0F, 2.0F, 3.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(440, 366).addBox(-3.0F, -3.475F, 1.5F, 6.0F, 2.0F, 3.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) )
		.texOffs(356, 476).addBox(-3.0F, -1.5F, -3.5F, 6.0F, 2.0F, 8.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(0.0F, 1.4246F, -0.4074F, 0.9599F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r29 = FootSpike2.addOrReplaceChild("LeftLegLower_r29", CubeListBuilder.create().texOffs(162, 525).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 1.0F, 7.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.3963F, 0.0F, 0.0F));

		PartDefinition LeftLegHeel = LowerLeftLeg.addOrReplaceChild("LeftLegHeel", CubeListBuilder.create().texOffs(328, 440).addBox(-9.4962F, -5.1135F, 3.0682F, 19.0F, 23.0F, 13.0F, deformation)
		.texOffs(374, 86).addBox(-9.4962F, 32.3093F, -6.0147F, 19.0F, 19.0F, 13.0F, deformation), PartPose.offset(0.0192F, 5.6905F, 22.4342F));

		PartDefinition LeftLegLower_r30 = LeftLegHeel.addOrReplaceChild("LeftLegLower_r30", CubeListBuilder.create().texOffs(752, 317).addBox(-9.5F, 6.5F, -14.5F, 19.0F, 9.0F, 22.0F, deformation), PartPose.offsetAndRotation(0.0038F, 18.1332F, -1.1492F, 1.1345F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r31 = LeftLegHeel.addOrReplaceChild("LeftLegLower_r31", CubeListBuilder.create().texOffs(176, 745).addBox(-9.5F, -11.5F, -5.5F, 19.0F, 23.0F, 13.0F, deformation), PartPose.offsetAndRotation(0.0038F, 21.0602F, -0.0183F, -0.0436F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r32 = LeftLegHeel.addOrReplaceChild("LeftLegLower_r32", CubeListBuilder.create().texOffs(336, 756).addBox(-9.5F, -7.5F, -5.5F, 19.0F, 22.0F, 13.0F, deformation), PartPose.offsetAndRotation(0.0038F, -1.4859F, 6.1012F, -0.4363F, 0.0F, 0.0F));

		PartDefinition LeftFootWheels = LowerLeftLeg.addOrReplaceChild("LeftFootWheels", CubeListBuilder.create().texOffs(584, 611).addBox(-12.5F, -2.1913F, -32.1567F, 25.0F, 9.0F, 49.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offset(0.0F, 50.2015F, -54.7213F));

		PartDefinition LeftLegLower_r33 = LeftFootWheels.addOrReplaceChild("LeftLegLower_r33", CubeListBuilder.create().texOffs(764, 131).addBox(-12.5F, -11.0F, -6.5F, 25.0F, 16.0F, 9.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(0.0F, 0.9233F, -24.4543F, -1.0472F, 0.0F, 0.0F));

		PartDefinition LeftLegLower_r34 = LeftFootWheels.addOrReplaceChild("LeftLegLower_r34", CubeListBuilder.create().texOffs(764, 131).addBox(-12.5F, -11.0F, -2.5F, 25.0F, 16.0F, 9.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ), PartPose.offsetAndRotation(0.0F, 0.9233F, 9.2548F, 1.0472F, 0.0F, 0.0F));

		PartDefinition RightLeg = main.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-28.5924F, -1.7705F, 32.2014F));

		PartDefinition UpperRightLeg = RightLeg.addOrReplaceChild("UpperRightLeg", CubeListBuilder.create().texOffs(464, 487).mirror().addBox(-12.5034F, 0.4743F, -20.6246F, 25.0F, 13.0F, 54.0F, new CubeDeformation(0.012F)).mirror(false)
		.texOffs(586, 68).mirror().addBox(-10.5034F, 13.5458F, -41.1176F, 21.0F, 14.0F, 52.0F, new CubeDeformation(0.012F)).mirror(false)
		.texOffs(0, 236).mirror().addBox(-12.4934F, -24.0421F, -21.32F, 25.0F, 25.0F, 85.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(732, 606).mirror().addBox(-12.4934F, -31.0421F, 27.68F, 5.0F, 7.0F, 36.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(546, 734).mirror().addBox(7.5066F, -31.0421F, 27.68F, 5.0F, 7.0F, 36.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offset(-13.0066F, 13.3125F, -43.3813F));

		PartDefinition RightLegUpper_r6 = UpperRightLeg.addOrReplaceChild("RightLegUpper_r6", CubeListBuilder.create().texOffs(0, 728).mirror().addBox(-10.5F, -6.5F, -12.5F, 21.0F, 13.0F, 27.0F, new CubeDeformation(0.012F)).mirror(false), PartPose.offsetAndRotation(-0.0034F, 28.1719F, -27.0378F, 0.4363F, 0.0F, 0.0F));

		PartDefinition RightLegUpper_r7 = UpperRightLeg.addOrReplaceChild("RightLegUpper_r7", CubeListBuilder.create().texOffs(594, 373).mirror().addBox(-12.5F, -1.0F, -17.0F, 25.0F, 12.0F, 33.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offsetAndRotation(-0.0164F, -3.0588F, 44.7114F, 0.3927F, 0.0F, 0.0F));

		PartDefinition RightLegUpper_r8 = UpperRightLeg.addOrReplaceChild("RightLegUpper_r8", CubeListBuilder.create().texOffs(130, 677).mirror().addBox(-12.5F, -67.0F, -15.0F, 25.0F, 38.0F, 30.0F, new CubeDeformation(0.021F)).mirror(false), PartPose.offsetAndRotation(0.0066F, 6.2699F, 8.7217F, 1.5708F, 0.0F, 0.0F));

		PartDefinition RightLegUpper_r9 = UpperRightLeg.addOrReplaceChild("RightLegUpper_r9", CubeListBuilder.create().texOffs(0, 660).mirror().addBox(-12.5F, -15.0F, -15.0F, 25.0F, 28.0F, 40.0F, deformation).mirror(false), PartPose.offsetAndRotation(0.0066F, -0.6168F, -38.6767F, 0.3927F, 0.0F, 0.0F));

		PartDefinition LowerRightLeg = RightLeg.addOrReplaceChild("LowerRightLeg", CubeListBuilder.create(), PartPose.offset(-13.0F, 47.0F, -37.0F));

		PartDefinition RightLegLower_r6 = LowerRightLeg.addOrReplaceChild("RightLegLower_r6", CubeListBuilder.create().texOffs(704, 721).mirror().addBox(-2.5F, -4.5F, -11.5F, 25.0F, 20.0F, 24.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(-10.0F, 37.3576F, -82.1223F, 1.2654F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r7 = LowerRightLeg.addOrReplaceChild("RightLegLower_r7", CubeListBuilder.create().texOffs(704, 765).mirror().addBox(-12.5F, -5.4F, 0.05F, 25.0F, 10.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, -13.3237F, 5.6859F, -0.4363F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r8 = LowerRightLeg.addOrReplaceChild("RightLegLower_r8", CubeListBuilder.create().texOffs(400, 766).mirror().addBox(-12.5F, -2.0F, -5.0F, 25.0F, 4.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, -8.8359F, 15.2701F, -1.5272F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r9 = LowerRightLeg.addOrReplaceChild("RightLegLower_r9", CubeListBuilder.create().texOffs(764, 186).mirror().addBox(-12.5F, -8.0F, -21.0F, 25.0F, 4.0F, 11.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, 8.7133F, -1.12F, -2.1817F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r10 = LowerRightLeg.addOrReplaceChild("RightLegLower_r10", CubeListBuilder.create().texOffs(732, 55).mirror().addBox(-7.5F, -7.5F, -17.5F, 15.0F, 10.0F, 30.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, 12.2276F, 13.5124F, 0.2182F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r11 = LowerRightLeg.addOrReplaceChild("RightLegLower_r11", CubeListBuilder.create().texOffs(394, 419).mirror().addBox(-15.0F, 26.5F, -25.5F, 25.0F, 5.0F, 63.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(2.5F, -7.5692F, -36.155F, 0.4363F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r12 = LowerRightLeg.addOrReplaceChild("RightLegLower_r12", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-15.0F, -12.5F, -49.5F, 25.0F, 18.0F, 100.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(2.5F, 14.4745F, -32.4947F, 0.4363F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r13 = LowerRightLeg.addOrReplaceChild("RightLegLower_r13", CubeListBuilder.create().texOffs(764, 0).mirror().addBox(-2.5F, -4.5F, -9.5F, 5.0F, 20.0F, 22.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(524, 678).mirror().addBox(-32.5F, -4.5F, -9.5F, 5.0F, 20.0F, 22.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(15.0F, 39.8384F, -79.3554F, 1.2654F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r14 = LowerRightLeg.addOrReplaceChild("RightLegLower_r14", CubeListBuilder.create().texOffs(586, 134).mirror().addBox(-2.5F, -9.0F, -7.5F, 5.0F, 15.0F, 15.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(286, 535).mirror().addBox(-32.5F, -9.0F, -7.5F, 5.0F, 15.0F, 15.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(15.0F, 19.4253F, -8.3282F, 0.4363F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r15 = LowerRightLeg.addOrReplaceChild("RightLegLower_r15", CubeListBuilder.create().texOffs(136, 492).mirror().addBox(-2.5F, -9.0F, -7.5F, 5.0F, 18.0F, 15.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(446, 181).mirror().addBox(-32.5F, -9.0F, -7.5F, 5.0F, 18.0F, 15.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(15.0F, 10.0719F, -16.8989F, 1.0472F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r16 = LowerRightLeg.addOrReplaceChild("RightLegLower_r16", CubeListBuilder.create().texOffs(446, 86).mirror().addBox(-2.5F, -9.0F, -47.5F, 5.0F, 30.0F, 65.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(250, 0).mirror().addBox(-27.5F, 10.0F, -47.5F, 25.0F, 11.0F, 75.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(188, 440).mirror().addBox(-32.5F, -9.0F, -47.5F, 5.0F, 30.0F, 65.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(15.0F, 14.6285F, -33.0195F, 0.4363F, 0.0F, 0.0F));

		PartDefinition FootSpike3 = LowerRightLeg.addOrReplaceChild("FootSpike3", CubeListBuilder.create().texOffs(536, 68).mirror().addBox(-3.0F, -36.4787F, 6.9667F, 5.0F, 5.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(110, 560).mirror().addBox(-3.0F, -36.4787F, 16.9667F, 5.0F, 8.0F, 5.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offset(8.5F, 50.3217F, -94.784F));

		PartDefinition RightLegLower_r17 = FootSpike3.addOrReplaceChild("RightLegLower_r17", CubeListBuilder.create().texOffs(446, 214).mirror().addBox(13.5F, -5.5F, 1.5F, 10.0F, 9.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(752, 473).mirror().addBox(14.5F, -4.5F, -7.5F, 8.0F, 8.0F, 25.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(-18.5F, -15.3836F, 4.9881F, 1.2654F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r18 = FootSpike3.addOrReplaceChild("RightLegLower_r18", CubeListBuilder.create().texOffs(570, 478).mirror().addBox(16.5F, -17.5F, -7.5F, 6.0F, 2.0F, 6.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(-19.5F, -4.5012F, 19.6382F, 1.2654F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r19 = FootSpike3.addOrReplaceChild("RightLegLower_r19", CubeListBuilder.create().texOffs(422, 226).mirror().addBox(16.5F, -19.5F, -7.5F, 6.0F, 4.0F, 6.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(-19.5F, -4.8061F, 18.4295F, 1.2654F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r20 = FootSpike3.addOrReplaceChild("RightLegLower_r20", CubeListBuilder.create().texOffs(422, 366).mirror().addBox(-3.0F, -2.625F, -0.95F, 6.0F, 2.0F, 3.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(404, 366).mirror().addBox(-3.0F, -3.475F, 1.5F, 6.0F, 2.0F, 3.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(328, 476).mirror().addBox(-3.0F, -1.5F, -3.5F, 6.0F, 2.0F, 8.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, 1.4246F, -0.4074F, 0.9599F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r21 = FootSpike3.addOrReplaceChild("RightLegLower_r21", CubeListBuilder.create().texOffs(136, 525).mirror().addBox(-3.0F, -3.0F, -4.0F, 6.0F, 1.0F, 7.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.3963F, 0.0F, 0.0F));

		PartDefinition FootSpike4 = LowerRightLeg.addOrReplaceChild("FootSpike4", CubeListBuilder.create().texOffs(586, 164).mirror().addBox(-3.0F, -36.4787F, 6.9667F, 5.0F, 5.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(286, 565).mirror().addBox(-3.0F, -36.4787F, 16.9667F, 5.0F, 8.0F, 5.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offset(-7.5F, 50.3217F, -94.784F));

		PartDefinition RightLegLower_r22 = FootSpike4.addOrReplaceChild("RightLegLower_r22", CubeListBuilder.create().texOffs(240, 677).mirror().addBox(13.5F, -5.5F, 1.5F, 10.0F, 9.0F, 10.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(752, 506).mirror().addBox(14.5F, -4.5F, -7.5F, 8.0F, 8.0F, 25.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(-18.5F, -15.3836F, 4.9881F, 1.2654F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r23 = FootSpike4.addOrReplaceChild("RightLegLower_r23", CubeListBuilder.create().texOffs(110, 573).mirror().addBox(16.5F, -17.5F, -7.5F, 6.0F, 2.0F, 6.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(-19.5F, -4.5012F, 19.6382F, 1.2654F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r24 = FootSpike4.addOrReplaceChild("RightLegLower_r24", CubeListBuilder.create().texOffs(110, 550).mirror().addBox(16.5F, -19.5F, -7.5F, 6.0F, 4.0F, 6.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(-19.5F, -4.8061F, 18.4295F, 1.2654F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r25 = FootSpike4.addOrReplaceChild("RightLegLower_r25", CubeListBuilder.create().texOffs(458, 366).mirror().addBox(-3.0F, -2.625F, -0.95F, 6.0F, 2.0F, 3.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(440, 366).mirror().addBox(-3.0F, -3.475F, 1.5F, 6.0F, 2.0F, 3.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false)
		.texOffs(356, 476).mirror().addBox(-3.0F, -1.5F, -3.5F, 6.0F, 2.0F, 8.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, 1.4246F, -0.4074F, 0.9599F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r26 = FootSpike4.addOrReplaceChild("RightLegLower_r26", CubeListBuilder.create().texOffs(162, 525).mirror().addBox(-3.0F, -3.0F, -4.0F, 6.0F, 1.0F, 7.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.3963F, 0.0F, 0.0F));

		PartDefinition RightFootWheels = LowerRightLeg.addOrReplaceChild("RightFootWheels", CubeListBuilder.create().texOffs(584, 611).mirror().addBox(-12.5F, -2.1913F, -32.1567F, 25.0F, 9.0F, 49.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offset(0.0F, 50.2015F, -54.7213F));

		PartDefinition RightLegLower_r27 = RightFootWheels.addOrReplaceChild("RightLegLower_r27", CubeListBuilder.create().texOffs(764, 131).mirror().addBox(-12.5F, -11.0F, -6.5F, 25.0F, 16.0F, 9.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, 0.9233F, -24.4543F, -1.0472F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r28 = RightFootWheels.addOrReplaceChild("RightLegLower_r28", CubeListBuilder.create().texOffs(764, 131).mirror().addBox(-12.5F, -11.0F, -2.5F, 25.0F, 16.0F, 9.0F, deformation!=CubeDeformation.NONE ? deformation : new CubeDeformation(0.023F) ).mirror(false), PartPose.offsetAndRotation(0.0F, 0.9233F, 9.2548F, 1.0472F, 0.0F, 0.0F));

		PartDefinition RightLegHeel = LowerRightLeg.addOrReplaceChild("RightLegHeel", CubeListBuilder.create().texOffs(328, 440).mirror().addBox(-9.5038F, -5.1135F, 3.0682F, 19.0F, 23.0F, 13.0F, deformation).mirror(false)
		.texOffs(374, 86).mirror().addBox(-9.5038F, 32.3093F, -6.0147F, 19.0F, 19.0F, 13.0F, deformation).mirror(false), PartPose.offset(-0.0192F, 5.6905F, 22.4342F));

		PartDefinition RightLegLower_r29 = RightLegHeel.addOrReplaceChild("RightLegLower_r29", CubeListBuilder.create().texOffs(752, 317).mirror().addBox(-9.5F, 6.5F, -14.5F, 19.0F, 9.0F, 22.0F, deformation).mirror(false), PartPose.offsetAndRotation(-0.0038F, 18.1332F, -1.1492F, 1.1345F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r30 = RightLegHeel.addOrReplaceChild("RightLegLower_r30", CubeListBuilder.create().texOffs(176, 745).mirror().addBox(-9.5F, -11.5F, -5.5F, 19.0F, 23.0F, 13.0F, deformation).mirror(false), PartPose.offsetAndRotation(-0.0038F, 21.0602F, -0.0183F, -0.0436F, 0.0F, 0.0F));

		PartDefinition RightLegLower_r31 = RightLegHeel.addOrReplaceChild("RightLegLower_r31", CubeListBuilder.create().texOffs(336, 756).mirror().addBox(-9.5F, -7.5F, -5.5F, 19.0F, 22.0F, 13.0F, deformation).mirror(false), PartPose.offsetAndRotation(-0.0038F, -1.4859F, 6.1012F, -0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 1024, 1024);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		AnimationDefinition[] towerAnim = new AnimationDefinition[]{
				RexChickenAnim.cannonrightshoot,RexChickenAnim.cannonleftshoot,
				RexChickenAnim.prepareshootright,RexChickenAnim.prepareshootleft
				,RexChickenAnim.cannonrightbroken, RexChickenAnim.cannonleftbroken
		};
		this.animate(entity.idle, RexChickenAnim.idlebody,ageInTicks,1.0F);
		this.animate(entity.idle, RexChickenAnim.idlelegs,ageInTicks,1.0F);
		this.right_leg.getAllParts().forEach(ModelPart::resetPose);
		this.left_leg.getAllParts().forEach(ModelPart::resetPose);
		if(!entity.isCharging()){
			this.animateWalk(RexChickenAnim.walkbody,limbSwing,limbSwingAmount,2.0F,2.5F);
			this.animateWalk(RexChickenAnim.walklegs,limbSwing,limbSwingAmount,2.0F,2.5F);
		}
		this.animate(entity.stunned,RexChickenAnim.stunned,ageInTicks,1.0F);
		this.animate(entity.attack,RexChickenAnim.stomp,ageInTicks,1.0F);
		this.animate(entity.charge,RexChickenAnim.charge,ageInTicks,1.0F);
		this.animate(entity.prepareCharge,RexChickenAnim.startcharge,ageInTicks,1.0F);
		this.animate(entity.death,RexChickenAnim.deathstart,ageInTicks,1.0F);
		this.animate(entity.idleDeath,RexChickenAnim.deathidle,ageInTicks,1.0F);
		int i = 0;
		this.ChickLauncher.getAllParts().forEach(ModelPart::resetPose);
		this.ChickLauncher2.getAllParts().forEach(ModelPart::resetPose);

		for(RexPart<?> part :entity.towers){
			int index = i;
			this.animate(part.towerShoot,towerAnim[index],ageInTicks,1.0F);
			index=i+2;
			this.animate(part.prepareShoot,towerAnim[index],ageInTicks,1.0F);
			if(i==1){
				this.ChickLauncher.visible = !part.isBreaking();
			}else {
				this.ChickLauncher2.visible = !part.isBreaking();
			}
			i++;
		}
		float partialTick = ageInTicks-entity.tickCount;
		float f0 = Mth.lerp(partialTick,entity.rotHeadY0,entity.rotHeadY);
		float f1 = Mth.lerp(partialTick,entity.rotHeadX0,entity.rotHeadX);
		laser.yRot = (float) (f0*Math.PI/180.0F);
		laser.xRot = (float) (f1*Math.PI/180.0F);
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