package com.TBK.crc.client.model;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.crc.CRC;
import com.TBK.crc.server.entity.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class TheFuturePortalModel<T extends PortalEntity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CRC.MODID, "portal"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart background;
	private final ModelPart portal1;
	private final ModelPart portal2;
	private final ModelPart portal3;
	private final ModelPart portal4;

	public TheFuturePortalModel(ModelPart root) {
		this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.background = this.main.getChild("background");
		this.portal1 = this.main.getChild("portal1");
		this.portal2 = this.main.getChild("portal2");
		this.portal3 = this.main.getChild("portal3");
		this.portal4 = this.main.getChild("portal4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition background = main.addOrReplaceChild("background", CubeListBuilder.create().texOffs(172, 0).addBox(-32.0F, -32.0F, 0.0F, 64.0F, 64.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -42.0F, 0.0F));

		PartDefinition portal1 = main.addOrReplaceChild("portal1", CubeListBuilder.create().texOffs(-1, 0).addBox(-32.0F, -32.0F, -2.0F, 64.0F, 64.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -43.0F, -4.0F));

		PartDefinition portal2 = main.addOrReplaceChild("portal2", CubeListBuilder.create().texOffs(0, 69).addBox(-25.0F, -25.0F, 0.0F, 50.0F, 50.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -43.0F, -4.0F));

		PartDefinition portal3 = main.addOrReplaceChild("portal3", CubeListBuilder.create().texOffs(178, 72).addBox(-25.0F, -25.0F, 0.0F, 50.0F, 50.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -41.0F, 2.0F));

		PartDefinition portal4 = main.addOrReplaceChild("portal4", CubeListBuilder.create().texOffs(177, 133).addBox(-25.0F, -25.0F, 0.0F, 50.0F, 50.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -40.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		truemain.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}