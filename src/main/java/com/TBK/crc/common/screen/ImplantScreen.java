package com.TBK.crc.common.screen;

import com.TBK.crc.CRC;
import com.TBK.crc.common.menu.ImplantMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)

public class ImplantScreen extends AbstractContainerScreen<ImplantMenu> {

    private static final ResourceLocation LOCATION = new ResourceLocation(CRC.MODID, "textures/gui/implant_gui_player.png");
    public ImplantScreen(ImplantMenu containerMenu, Inventory inventory, Component component) {
        super(containerMenu, inventory, component);
        this.imageWidth = 175;
        this.imageHeight = 160;
    }

    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {

    }

    @Override
    protected void renderBg(GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;

        p_283065_.blit(LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        this.renderTooltip(p_283065_,p_97789_,p_97790_);
    }

    @Override
    public boolean keyPressed(int p_97765_, int p_97766_, int p_97767_) {
        if(p_97765_ == 86){
            this.onClose();
        }
        return super.keyPressed(p_97765_, p_97766_, p_97767_);
    }
}
