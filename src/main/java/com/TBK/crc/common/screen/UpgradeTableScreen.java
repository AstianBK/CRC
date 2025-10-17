package com.TBK.crc.common.screen;

import com.TBK.crc.CRC;
import com.TBK.crc.common.menu.CyborgTableMenu;
import com.TBK.crc.common.menu.UpgradeTableMenu;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.GanchoArm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public class UpgradeTableScreen extends AbstractContainerScreen<UpgradeTableMenu> {

    public UpgradeTableScreen(UpgradeTableMenu containerMenu, Inventory inventory, Component component) {
        super(containerMenu, inventory, component);
        this.imageWidth = 175;
        this.imageHeight = 165;
    }
    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {

    }
    @Override
    protected void renderBg(GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        float ageInTick = this.menu.player.tickCount + p_97788_;
        int frame = (int) ((1.25F * ageInTick) % CRC.GUI_IMPLANT_STAGES.size());
        ResourceLocation location = CRC.GUI_IMPLANT_LOCATIONS.get(frame);
        p_283065_.blit(location, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        this.renderTooltip(p_283065_,p_97789_,p_97790_);
    }

    @Override
    public void render(GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_) {
        super.render(p_283479_, p_283661_, p_281248_, p_281886_);
        this.refreshButtons();
    }

    private void refreshButtons(){
        int i = (this.width - 147) / 2 - 86;
        int j = (this.height - 166) / 2;
    }
    @Override
    public boolean mouseClicked(double p_97748_, double p_97749_, int p_97750_) {
        return super.mouseClicked(p_97748_, p_97749_, p_97750_);
    }
}
