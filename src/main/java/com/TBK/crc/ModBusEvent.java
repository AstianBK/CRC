package com.TBK.crc;

import com.TBK.crc.client.model.GanchoModel;
import com.TBK.crc.client.model.MultiArmModel;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.block.CyborgTableBlock;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.server.capability.CRCCapability;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.CyborgRobotChicken;
import com.TBK.crc.server.manager.MultiArmSkillAbstractInstance;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import com.TBK.crc.server.multiarm.PassivePart;
import com.TBK.crc.server.multiarm.UltraInstictHeart;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketHandlerPowers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber()
public class ModBusEvent {

    private static final ResourceLocation TEXTURE = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm.png");

    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickItem event) {
        if(event.getLevel().isClientSide){
            if(event.getItemStack().is(Items.STICK)){
                if(event.getEntity().isShiftKeyDown()){
                    CRC.xq-=0.1D;

                }else {
                    CRC.x-=0.1D;
                }
                CRC.LOGGER.debug("X :" + CRC.x);
            }

            if(event.getItemStack().is(Items.BLAZE_ROD)){
                if(event.getEntity().isShiftKeyDown()){
                }else {
                    CRC.y-=0.1D;
                }
                CRC.LOGGER.debug("Y :" + CRC.y);
            }

            if(event.getItemStack().is(Items.HEART_OF_THE_SEA)){
                if(event.getEntity().isShiftKeyDown()){
                    CRC.xq+=0.1D;
                }else {
                    CRC.x+=0.1D;
                }
                CRC.LOGGER.debug("XQ :" + CRC.xq);
            }
            if(event.getItemStack().is(Items.GOLD_INGOT)){
                CRC.y+=0.1D;
            }

            if(event.getItemStack().is(Items.NETHERITE_INGOT)){

                CRC.z-=0.1D;
            }
            if(event.getItemStack().is(Items.SADDLE)){
                CRC.z+=0.1D;
            }
            CRC.LOGGER.debug("X :" + CRC.x + " Y :"+CRC.y+
                    " Z :" + CRC.z + " XQ :"+CRC.xq+
                    " YQ :" + CRC.yq + " ZQ :"+CRC.zq);
        }

        if (event.getItemStack().is(Items.PRISMARINE_SHARD)){
            MultiArmCapability cap = MultiArmCapability.get(event.getEntity());
            if (cap!=null){
                cap.clearAbilityStore();
                cap.clearForUpgradeStore();
            }

        }
    }
    @SubscribeEvent
    public static void renderPreEvent(RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event){
        if(event.getRenderer().getModel() instanceof MultiArmModel model){
            model.root().visible = false;
        }
        AbstractClientPlayer player = Minecraft.getInstance().player;
        MultiArmCapability cap = MultiArmCapability.get(player);
        
        if(cap!=null){
            PoseStack stack = event.getPoseStack();
            MultiBufferSource bufferSource = event.getMultiBufferSource();
            float partialTick = event.getPartialTick();
            LivingEntity entity = event.getEntity();
            if (cap.isCatchedTarget(entity)){
                renderLeash(entity,partialTick,stack,bufferSource,player,event.getPackedLight());
            }
            if(event.getEntity() instanceof Player){
                if(event.getRenderer() instanceof PlayerRenderer renderer && cap.getPassives().hasMultiArmSkillAbstract("ultra_instict_hearth") && cap.getPassives().getForName("ultra_instict_hearth") instanceof UltraInstictHeart instictHeart){
                    if(instictHeart.getEffectTimer(partialTick)>0.0F){
                        stack.pushPose();
                        float alpha = instictHeart.getEffectTimer(partialTick)/20.0F;
                        Vec3 renderingAt = new Vec3(Mth.lerp(partialTick, player.xo, player.getX()), Mth.lerp(partialTick, player.yo, player.getY()), Mth.lerp(partialTick, player.zo, player.getZ()));
                        Vec3 pos=instictHeart.getPos();
                        Vec3 end=instictHeart.getOldPos();
                        Vec3 delta = end.subtract(pos);
                        double distance = end.length();
                        delta = delta.normalize();
                        Vec3 backPos=pos.add(delta.x,delta.y+2,delta.z);
                        for(int i=0;i<distance;i++){
                            Vec3 move=backPos.subtract(renderingAt);
                            Vec3 moveOri=pos.subtract(renderingAt);
                            Vec3 lastPos=moveOri.subtract(move);
                            if(lastPos.z<0){
                                lastPos= lastPos.multiply(1.0f,1.0f,-1.0f);
                            }
                            if(lastPos.x<0){
                                lastPos=lastPos.multiply(-1.0f,1.0f,1.0f);
                            }
                            stack.translate(move.x,move.y,move.z);
                            renderer.getModel().copyPropertiesTo(renderer.getModel());
                            renderer.getModel().setupAnim(player,0.0F, 0.0F, entity.tickCount+partialTick, 0.0F,0.0F);
                            renderer.getModel().renderToBuffer(stack,bufferSource.getBuffer(RenderType.entityTranslucentEmissive(renderer.getTextureLocation(player))),event.getPackedLight(), OverlayTexture.NO_OVERLAY,0.0f,0.0f,0.1f,alpha);
                            pos=backPos;
                            backPos=backPos.add(delta.x,delta.y+2,delta.z);
                        }
                        stack.popPose();
                    }

                }
            }
        }
    }

    private static void renderLeash(LivingEntity p_115462_, float p_115463_, PoseStack p_115464_, MultiBufferSource p_115465_, LivingEntity p_115466_,int packedLight) {
        p_115464_.pushPose();

        Vec3 vec3 = p_115466_.getRopeHoldPosition(p_115463_);
        double d0 = (double)(Mth.lerp(p_115463_, p_115462_.getYRot(), p_115462_.yRotO) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = new Vec3(0.0D, p_115462_.getBbHeight()/2.0D, 0.0D);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp((double)p_115463_, p_115462_.xo, p_115462_.getX()) + d1;
        double d4 = Mth.lerp((double)p_115463_, p_115462_.yo, p_115462_.getY()) + vec31.y;
        double d5 = Mth.lerp((double)p_115463_, p_115462_.zo, p_115462_.getZ()) + d2;
        p_115464_.translate(d1, vec31.y, d2);
        float f = (float)(vec3.x - d3);
        float f1 = (float)(vec3.y - d4);
        float f2 = (float)(vec3.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = p_115465_.getBuffer(RenderType.leash());
        Matrix4f matrix4f = p_115464_.last().pose();
        float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = BlockPos.containing(p_115462_.getEyePosition(p_115463_));
        BlockPos blockpos1 = BlockPos.containing(p_115466_.getEyePosition(p_115463_));
        int i = getBlockLightLevelForEntity(p_115462_,blockpos1);
        int j = getBlockLightLevelForEntity(p_115466_, blockpos1);
        int k = p_115462_.level().getBrightness(LightLayer.SKY, blockpos);
        int l = p_115462_.level().getBrightness(LightLayer.SKY, blockpos1);

        for(int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(p_115462_,vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(p_115462_,vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
        }

        p_115464_.popPose();
    }

    private static void addVertexPair(LivingEntity p_115462_,VertexConsumer p_174308_, Matrix4f p_254405_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
        float f = (float)p_174321_ / 24.0F;
        int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.pack(i, j);
        float f1 = p_174321_ == (p_115462_.tickCount%25) ? 1F : 0.7F;
        float f2 = 0.3176F * f1;
        float f3 = 0.819F * f1;
        float f4 = 0.96F * f1;

        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        p_174308_.vertex(p_254405_, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        p_174308_.vertex(p_254405_, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    protected static int getBlockLightLevelForEntity(Entity p_114496_, BlockPos p_114497_) {
        return p_114496_.isOnFire() ? 15 : p_114496_.level().getBrightness(LightLayer.BLOCK, p_114497_);
    }
    @SubscribeEvent
    public static void renderHand(RenderArmEvent event) {
        MultiArmCapability cap = MultiArmCapability.get(event.getPlayer());
        if(cap!=null && Util.hasMultiArm(cap)){
            boolean flag = true;
            float f = flag ? 1.0F : -1.0F;

            float partialTicks=Minecraft.getInstance().getPartialTick();
            MultiArmModel<Player> modelLeft=new MultiArmModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(MultiArmModel.LAYER_LOCATION));

            RenderType renderType = RenderType.entityTranslucent(TEXTURE);

            modelLeft.selectArm(cap.getSelectSkill().name,cap.getSelectSkill(),event.getPlayer().tickCount+partialTicks);
            modelLeft.setupAnim(event.getPlayer(),0.0F,0.0F,partialTicks+event.getPlayer().tickCount,0.0F,0.0F);
            modelLeft.renderToBuffer(event.getPoseStack(), event.getMultiBufferSource().getBuffer(renderType), event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        LivingEntity entity = event.getEntity();
        if(entity instanceof Chicken){
            if(event.getSource().getEntity() instanceof Player player){
                MultiArmCapability cap = MultiArmCapability.get(player);
                if(cap!=null){
                    cap.chickenEnemy=true;
                }
            }
        }
        if(entity instanceof Player player){
            MultiArmCapability cap = MultiArmCapability.get(player);
            if(cap!=null){
                boolean flag = false;
                for (MultiArmSkillAbstractInstance instance : cap.getPassives().getSkills()){
                    flag = flag || ((PassivePart)instance.getSkillAbstract()).onDie(cap,event.getSource().getEntity());
                }
                event.setCanceled(flag);
            }
        }
    }
    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event){
        if(event.getEntity() instanceof Player){
            MultiArmCapability cap = CRCCapability.getEntityCap(event.getEntity(), MultiArmCapability.class);

            if(cap!=null && event.getEntity().isAlive()){
                cap.tick((Player) event.getEntity());
            }
        }
    }
    @SubscribeEvent
    public void sleepTimeCheck(@NotNull SleepingLocationCheckEvent event) {
        if (event.getEntity().level().getBlockState(event.getSleepingLocation()).getBlock() instanceof CyborgTableBlock) {
            event.setResult(Event.Result.ALLOW);
        }
    }
    @SubscribeEvent
    public void sleepTimeCheck(@NotNull SleepingTimeCheckEvent event) {
        event.getSleepingLocation().ifPresent((blockPos -> {
            if (event.getEntity().level().getBlockState(blockPos).getBlock() instanceof CyborgTableBlock) {
                event.setResult(Event.Result.ALLOW);
            }}));
    }

    @SubscribeEvent
    public void sleepTimeFinish(@NotNull SleepFinishedTimeEvent event) {
        if (event.getLevel() instanceof ServerLevel) {
            boolean sleepingInCoffin = event.getLevel().players().stream().anyMatch(player -> {
                Optional<BlockPos> pos = player.getSleepingPos();
                return pos.isPresent() && event.getLevel().getBlockState(pos.get()).getBlock() instanceof CyborgTableBlock;
            });
            if (sleepingInCoffin) {
                event.setTimeAddition(1000);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player player){
            MultiArmCapability oldVamp = CRCCapability.getEntityCap(event.getObject(), MultiArmCapability.class);

            if (oldVamp == null) {
                MultiArmCapability.SkillPlayerProvider prov = new MultiArmCapability.SkillPlayerProvider();
                MultiArmCapability cap=prov.getCapability(CRCCapability.MULTIARM_CAPABILITY).orElse(null);
                cap.init(player);
                event.addCapability(new ResourceLocation(CRC.MODID, "multi_arm_cap"), prov);
            }
            
        }
    }

    @SubscribeEvent
    public static void onJoinGame(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player){
            MultiArmCapability cap = CRCCapability.getEntityCap(event.getEntity(), MultiArmCapability.class);
            if(cap!=null){
                cap.onJoinGame((Player) event.getEntity(),event);
            }
        }
    }
}
