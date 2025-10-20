package com.TBK.crc.client.gui;

import com.TBK.crc.CRC;
import com.TBK.crc.common.ForgeInputEvent;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.upgrade.Upgrade;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MultiArmOverlay implements IGuiOverlay {
    protected static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(CRC.MODID,"textures/gui/skill_hotbar.png");
    protected static final ResourceLocation[] FRAMES = new ResourceLocation[]{
            new ResourceLocation(CRC.MODID,"textures/gui/cyborg_chicken_talk_0.png"),
            new ResourceLocation(CRC.MODID,"textures/gui/cyborg_chicken_talk_1.png"),
            new ResourceLocation(CRC.MODID,"textures/gui/cyborg_chicken_talk_2.png"),
            new ResourceLocation(CRC.MODID,"textures/gui/cyborg_chicken_talk_3.png")
    };
    protected static final ResourceLocation STATIC_FRAME = new ResourceLocation(CRC.MODID,"textures/gui/cyborg_chicken_talk_static.png");

    private final Minecraft mc = Minecraft.getInstance();
    @Override
    public void render(ForgeGui gui, @NotNull GuiGraphics graphics, float partialTicks, int width, int height) {
        if (this.mc.player != null) {
            Player player = this.mc.player;
            MultiArmCapability cap=MultiArmCapability.get(player);
            if (cap != null) {
                graphics.pose().pushPose();
                float ageInTick = player.tickCount + partialTicks;

                if(cap.passives.hasMultiArmSkillAbstract("night_eye") || cap.passives.hasMultiArmSkillAbstract("hacker_eye") ){
                    Upgrade passive = cap.passives.get(1);
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    float[] color = passive.getWindowsColor(cap);
                    if(color.length>0){
                        RenderSystem.setShaderColor(color[0],color[1],color[2],color[3]);
                        if(passive.name.equals("night_eye")){
                            int frame = (int) ((1.25F * ageInTick) % CRC.NIGHT_VISION_STAGES.size());
                            ResourceLocation location = CRC.NIGHT_VISION_LOCATIONS.get(frame);

                            RenderSystem.disableDepthTest();
                            RenderSystem.depthMask(false);
                            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                            graphics.blit(location, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
                            RenderSystem.depthMask(true);
                            RenderSystem.enableDepthTest();
                            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                        }
                        int colorAlpha = passive.name.equals("night_eye") ? 0x4000FF00 : 0x400000FF;
                        graphics.fill(0, 0, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), colorAlpha);
                    }

                    RenderSystem.disableBlend();
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                    RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
                }
                graphics.pose().pushPose();
                if(cap.warningLevel>0){
                    int i = width / 2 -140;
                    int j1 =  i + 101;
                    int k1 = height - 58 ;
                    float percent = cap.getAnimLevelWarning(partialTicks);
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    float xExtra = cap.chickenSpoke ? Mth.lerp(percent,129,-40) : Mth.lerp(percent,30,-40);
                    float yExtra = cap.chickenSpoke ? Mth.lerp(percent,13,-155) : Mth.lerp(percent,-76,-155);
                    float centerX = (j1 + xExtra);
                    float centerY = (k1 + yExtra);

                    ResourceLocation location = cap.playChickenWarning ? STATIC_FRAME : (new ResourceLocation(CRC.MODID,"textures/mobeffect/location_tracking_"+(cap.warningLevel-1)+".png"));
                    if(cap.getChickenTalkAnim(partialTicks)>0.0F){
                        graphics.pose().translate(centerX, centerY, 0);
                        graphics.pose().mulPose(Axis.XP.rotationDegrees(Mth.sin((ageInTick *0.5F)) * 30.0F ));
                        graphics.pose().mulPose(Axis.YP.rotationDegrees(Mth.sin((ageInTick *0.9F)) * 15.0F ));
                        graphics.pose().translate(-centerX, -centerY, 0);
                        location = FRAMES[(int) ((ageInTick)%4)];
                    }
                    graphics.blit(location, (int) centerX, (int) centerY, 0,0, Mth.ceil(18 + 128*percent),  Mth.ceil(18 + 128*percent), Mth.ceil(18 + 128*percent) ,  Mth.ceil(18 + 128*percent));
                    RenderSystem.disableBlend();
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                    RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
                }
                graphics.pose().popPose();
                graphics.pose().popPose();

                if(Util.hasMultiArm(cap)){
                    int i = width / 2 - 140;

                    graphics.pose().pushPose();

                    if(cap.hotbarActive){
                        for(int i1 = 0; i1 < cap.skills.upgrades.size(); ++i1) {
                            Upgrade skillAbstract=cap.getHotBarSkill().get(i1);
                            int j1 =  i + 101 + i1 * 20;
                            int k1 = height - 58;
                            if(!skillAbstract.equals(Upgrade.NONE)){
                                graphics.pose().pushPose();
                                graphics.blit(getIconsForSkill(skillAbstract), j1, k1 , 0,0,16, 16, 16, 16);
                                graphics.pose().popPose();
                            }
                        }
                        graphics.blit(WIDGETS_LOCATION, (int) (i +66), (int) (height-72), 0, 0, 146, 32,256,256);
                        graphics.blit(WIDGETS_LOCATION, i + 96 + ForgeInputEvent.selectActual * 20, height - 60 , 20, 32, 24, 21
                                ,256,256);
                    }else {
                        graphics.blit(WIDGETS_LOCATION, i + 132, height - 74 , 0, 34, 18, 32
                                ,256,256);
                    }
                    graphics.pose().popPose();
                }
            }

        }
    }

    private ResourceLocation getIconsForSkill(Upgrade skillAbstract) {
        return new ResourceLocation(CRC.MODID,"textures/gui/skill/"+skillAbstract.name+"_icon.png");
    }
}
