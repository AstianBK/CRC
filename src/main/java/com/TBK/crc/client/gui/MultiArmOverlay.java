package com.TBK.crc.client.gui;

import com.TBK.crc.CRC;
import com.TBK.crc.common.ForgeInputEvent;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
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
    private final Minecraft mc = Minecraft.getInstance();

    @Override
    public void render(ForgeGui gui, @NotNull GuiGraphics graphics, float partialTicks, int width, int height) {
        if (this.mc.player != null) {
            Player player = this.mc.player;
            MultiArmCapability cap=MultiArmCapability.get(player);
            if(cap!=null){
                int i = width / 2 -140;

                graphics.pose().pushPose();
                if(cap.hotbarActive){
                    for(int i1 = 0; i1 < cap.skills.powers.size(); ++i1) {
                        MultiArmSkillAbstract skillAbstract=cap.getHotBarSkill().get(i1);
                        int j1 =  i + 101 + i1 * 20;
                        int k1 = height - 58;
                        if(!skillAbstract.equals(MultiArmSkillAbstract.NONE)){
                            graphics.pose().pushPose();
                            graphics.blit(getIconsForSkill(skillAbstract), j1, k1 , 0,0,16, 16, 16, 16);
                            graphics.pose().popPose();

                            graphics.pose().pushPose();
                            float f = cap.getCooldowns().getCooldownPercent(skillAbstract);
                            if (f > 0.0F) {
                                int i2 = k1 + Mth.floor(16.0F * (1.0F-f));
                                int j2= i2 + Mth.floor(16.0F * f);
                                graphics.fill(RenderType.guiOverlay(), j1, i2, j1 + 16,  j2+5, Integer.MAX_VALUE);
                            }
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

    private ResourceLocation getIconsForSkill(MultiArmSkillAbstract skillAbstract) {
        return new ResourceLocation(CRC.MODID,"textures/gui/skill/"+skillAbstract.name+"_icon.png");
    }
}
