package com.TBK.crc.client.layer;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.PassivePart;
import com.TBK.crc.server.multiarm.UltraInstictHeart;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PassiveLayer <T extends Player,M extends HumanoidModel<T>> extends RenderLayer<T,M> {
    public PassiveLayer(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T player, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }
}
