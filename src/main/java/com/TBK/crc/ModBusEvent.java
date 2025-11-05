package com.TBK.crc;

import com.TBK.crc.client.layer.MultiarmLayer;
import com.TBK.crc.client.model.MultiArmModel;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.item.CyberSkinItem;
import com.TBK.crc.common.registry.BKDimension;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.common.registry.BKItems;
import com.TBK.crc.server.StructureData;
import com.TBK.crc.server.capability.CRCCapability;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.PortalEntity;
import com.TBK.crc.server.manager.UpgradeInstance;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketSyncPlayerData;
import com.TBK.crc.server.upgrade.PassivePart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber()
public class ModBusEvent {

    private static final ResourceLocation TEXTURE = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm.png");
    private static final ResourceLocation GLOWING = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_glowing.png");

    public static final ResourceLocation[] PULSING = {
            new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_pulsating_0.png"),
            new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_pulsating_1.png"),
            new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_pulsating_2.png"),
            new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_pulsating_3.png")
    };

    public static ResourceLocation getPulsingTextureForSkin(int levelCharge, String skinName){
        return new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_pulsating_"+levelCharge+"_"+skinName+".png");
    }

    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickItem event) {

        if(event.getItemStack().is(BKItems.DANGER_INCREASER.get())){
            MultiArmCapability cap = MultiArmCapability.get(event.getEntity());

            if(cap!=null && cap.warningLevel<3){
                if(!event.getLevel().isClientSide){
                    if(cap.chickenEnemy){
                        cap.warningLevel++;
                        cap.wave = 0;
                        PacketHandler.sendToPlayer(new PacketSyncPlayerData(cap.saveChickenEnemyData(),false,event.getEntity().getId()), (ServerPlayer) event.getEntity());
                    }
                }else {
                    event.getLevel().playLocalSound(event.getPos().above(), SoundEvents.ILLUSIONER_PREPARE_BLINDNESS, SoundSource.BLOCKS,5.0F,1.0F,false);
                }
                if(!event.getEntity().getAbilities().instabuild && cap.chickenEnemy){
                    event.getItemStack().shrink(1);
                }
            }
        }

        if(event.getItemStack().is(BKItems.SIGNAL_JAMMER.get())){
            MultiArmCapability cap = MultiArmCapability.get(event.getEntity());

            if(cap!=null){
                if(!event.getLevel().isClientSide){
                    if(cap.chickenEnemy){
                        cap.chickenEnemy = false;
                        cap.warningLevel = 0;
                        cap.wave = 0;
                        cap.timeLevelWarning = 0;
                        cap.playChickenWarning = false;
                        cap.invokeTimer = 400;
                        PacketHandler.sendToPlayer(new PacketSyncPlayerData(cap.saveChickenEnemyData(),false,event.getEntity().getId()), (ServerPlayer) event.getEntity());
                    }
                }else {
                    event.getLevel().playLocalSound(event.getPos().above(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS,5.0F,1.0F,false);
                }
                if(!event.getEntity().getAbilities().instabuild && cap.chickenEnemy){
                    event.getItemStack().shrink(1);
                }
            }
        }
        if(event.getItemStack().is(BKItems.PORTAL_OPENER.get()) && !Util.isInFuture(event.getEntity())){

            if(!event.getLevel().isClientSide()){
                PortalEntity portal = new PortalEntity(BKEntityType.PORTAL.get(),event.getLevel());
                portal.setPos(event.getPos().above().getCenter());
                event.getLevel().addFreshEntity(portal);
            }else {
                event.getLevel().playLocalSound(event.getPos().above(), SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS,5.0F,1.0F,false);
            }

            if(!event.getEntity().getAbilities().instabuild){
                event.getItemStack().shrink(1);
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
                CompoundTag tag =cap.implantStore.getImplant(0).getOrCreateTag();
                float[] colors = CyberSkinItem.getColors(tag);
                if(colors==null){
                    colors=new float[]{0.3176F, 0.819F,0.96F};
                }

                renderLeash(entity,partialTick,stack,bufferSource,player,event.getPackedLight(),colors);
            }
        }
    }

    private static void renderLeash(LivingEntity p_115462_, float p_115463_, PoseStack p_115464_, MultiBufferSource p_115465_, LivingEntity p_115466_, int packedLight, float[] colors) {
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
            addVertexPair(p_115462_,vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, colors);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(p_115462_,vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, colors);
        }

        p_115464_.popPose();
    }


    private static void addVertexPair(LivingEntity p_115462_, VertexConsumer p_174308_, Matrix4f p_254405_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, float[] p_174322_) {
        float f = (float)p_174321_ / 24.0F;
        int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.pack(i, j);
        float f1 = p_174321_ == (p_115462_.tickCount%25) ? 1F : 0.7F;
        float f2 = p_174322_[0] * f1;
        float f3 = p_174322_[1] * f1;
        float f4 = p_174322_[2] * f1;

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

            PoseStack poseStack = event.getPoseStack();
            float partialTicks=Minecraft.getInstance().getPartialTick();
            MultiArmModel<Player> modelLeft=new MultiArmModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(MultiArmModel.LAYER_LOCATION));

            ResourceLocation location = CyberSkinItem.getTextures(cap.implantStore.getImplant(0).getOrCreateTag());
            if(location==null){
                location = TEXTURE;
            }

            RenderType renderType = RenderType.entityTranslucent(location);
            String skinName = CyberSkinItem.getName(cap.implantStore.getImplant(0).getOrCreateTag());

            modelLeft.setPoseArmInGuiForPose(cap,poseStack,partialTicks);
            modelLeft.selectArm(event.getPlayer(),cap.getSelectSkill().name,cap.getSelectSkill(),event.getPlayer().tickCount+partialTicks);
            modelLeft.setupAnim(event.getPlayer(),0.0F,0.0F,partialTicks+event.getPlayer().tickCount,0.0F,0.0F);
            modelLeft.renderToBuffer(event.getPoseStack(), event.getMultiBufferSource().getBuffer(renderType), event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            modelLeft.setupAnim(event.getPlayer(),0.0F,0.0F,partialTicks+event.getPlayer().tickCount,0.0F,0.0F);
            modelLeft.renderToBuffer(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.eyes(skinName==null ? GLOWING : MultiarmLayer.getGlowingTexture(skinName))), event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            if(cap.levelCharge>0){
                ResourceLocation texture = ModBusEvent.PULSING[cap.levelCharge];
                if(skinName!=null){
                    texture = ModBusEvent.getPulsingTextureForSkin(cap.levelCharge,skinName);
                }
                modelLeft.renderToBuffer(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.entityTranslucentEmissive(texture)), event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        LivingEntity entity = event.getEntity();
        if(entity instanceof Chicken chicken && chicken.isBaby()){
            if(event.getSource().getEntity() instanceof Player player){
                MultiArmCapability cap = MultiArmCapability.get(player);
                if(cap!=null){
                    if(!cap.chickenEnemy){
                        cap.chickenEnemy=true;
                        cap.timeLevelWarning = 0;
                        cap.timeLevelWarning0 = 0;
                        cap.wave = 0;
                        cap.warningLevel = 1;
                        cap.playChickenWarning = true;
                        cap.invokeTimer = 400;
                        PacketHandler.sendToPlayer(new PacketSyncPlayerData(cap.saveChickenEnemyData(),false,event.getSource().getEntity().getId()), (ServerPlayer) event.getSource().getEntity());
                    }
                }
            }
        }
        if(entity instanceof Player player){
            MultiArmCapability cap = MultiArmCapability.get(player);
            if(cap!=null){
                boolean flag = false;
                for (UpgradeInstance instance : cap.getPassives().getSkills()){
                    if(instance.getUpgrade() instanceof PassivePart passive){
                        flag = flag || passive.onDie(cap,event.getSource().getEntity());
                    }
                }

                if(!flag){
                    cap.chickenEnemy=false;
                    cap.timeLevelWarning = 0;
                    cap.timeLevelWarning0 = 0;
                    cap.wave = 0;
                    cap.warningLevel = 0;
                    PacketHandler.sendToPlayer(new PacketSyncPlayerData(cap.saveChickenEnemyData(),false,player.getId()), (ServerPlayer) player);
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
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if(event.getEntity().level().isClientSide)return;
        if (!event.isWasDeath()) return;

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        MultiArmCapability oldCap = CRCCapability.getEntityCap(oldPlayer, MultiArmCapability.class);
        if(oldCap!=null){
            MultiArmCapability cap = CRCCapability.getEntityCap(newPlayer, MultiArmCapability.class);
            cap.init(newPlayer);
            cap.copyFrom(oldCap);

            cap.syncNewPlayer((ServerPlayer) newPlayer,oldCap,true);
        }
        oldPlayer.invalidateCaps();
    }

    @SubscribeEvent
    public static void onTick(TickEvent event){
        if(event.phase == TickEvent.Phase.END && event.side== LogicalSide.SERVER && CRC.getServer().getLevel(BKDimension.THE_FUTURE_LEVEL)!=null){
            StructureData.get().getCyberChickenFight().tick();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player player){
            MultiArmCapability oldCap = CRCCapability.getEntityCap(event.getObject(), MultiArmCapability.class);

            if (oldCap == null) {
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

    @SubscribeEvent
    public static void onCheckSpawn(MobSpawnEvent.SpawnPlacementCheck event) {
        if(event.getLevel() instanceof ServerLevel serverLevel){
            if (serverLevel.dimension() == BKDimension.THE_FUTURE_LEVEL) {
                event.setResult(Event.Result.DENY);
            }
        }

    }

    @SubscribeEvent
    public static void useBlock(PlayerContainerEvent.Open event){
        if(!event.getEntity().level().isClientSide){
            if(event.getContainer() instanceof ChestMenu){
                CRC.getServer().getPlayerList().getPlayers().forEach(e->{
                    Util.refreshHackingChest(e,e.level());
                });
            }
        }
    }


    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event){
        if (event.getEntity() instanceof Player player){
            MultiArmCapability cap = MultiArmCapability.get(player);
            if(cap!=null){
                cap.passives.upgrades.forEach((i,passive)->{
                    passive.getUpgrade().onHurt(cap, event);
                });
                cap.passives.upgrades.forEach((i,passive)->{
                    cap.getSelectSkill().onHurt(cap, event);
                });
            }
        }
        if(event.getSource().getEntity() instanceof Player player){
            MultiArmCapability cap = MultiArmCapability.get(player);
            if(cap!=null){
                cap.passives.upgrades.forEach((i,passive)->{
                    passive.getUpgrade().onAttack(cap,event);
                });
                if(cap.getSelectSkill().name.equals("claws_arm") && player.getMainHandItem().isEmpty()){
                    cap.getSelectSkill().onAttack(cap,event);
                }
            }
        }
    }
}
