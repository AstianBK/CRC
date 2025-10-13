package com.TBK.crc.common.screen;

import com.TBK.crc.CRC;
import com.TBK.crc.common.menu.CyborgTableMenu;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketHandlerPowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class CyborgTableScreen extends AbstractContainerScreen<CyborgTableMenu> {

    private static final ResourceLocation CRAFTING_TABLE_LOCATION = new ResourceLocation(CRC.MODID, "textures/gui/cyborg_table_gui.png");
    public Button acceptButton;
    public CyborgTableScreen(CyborgTableMenu containerMenu, Inventory inventory, Component component) {
        super(containerMenu, inventory, component);
        this.imageWidth = 175;
        this.imageHeight = 246;
    }

    @Override
    protected void init() {
        super.init();
        this.acceptButton = new Button.Builder(Component.literal("VAMOS A PERDER"),(s)->{
            this.menu.refreshPlayer(menu.player);
            assert Minecraft.getInstance().player!=null;
            Minecraft.getInstance().player.closeContainer();
        }).bounds(this.leftPos + 45, (this.height / 2 - this.imageHeight / 2)  + 135, 100, 20).build();
        this.acceptButton.active = false;
        this.addRenderableWidget(this.acceptButton);
    }

    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {

    }

    @Override
    protected void renderBg(GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        p_283065_.blit(CRAFTING_TABLE_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        this.renderTooltip(p_283065_,p_97789_,p_97790_);
    }



    @Override
    public void render(GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_) {
        super.render(p_283479_, p_283661_, p_281248_, p_281886_);
        this.refreshButtons();
        this.acceptButton.render(p_283479_,p_283661_,p_281248_,p_281886_);
    }

    private void refreshButtons(){
        int i = (this.width - 147) / 2 - 86;
        int j = (this.height - 166) / 2;
        this.acceptButton.active = isDirty();
    }

    private boolean isDirty() {
        for (int i = 0 ; i<6 ; i++){
            if(this.menu.craftSlots.getItem(i).getItem()!=this.menu.getPlayerStore().store.getItem(i).getItem()){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double p_97748_, double p_97749_, int p_97750_) {
        if (this.acceptButton.mouseClicked(p_97748_, p_97749_, p_97750_)) {
            refreshButtons();
            return true;
        }
        return super.mouseClicked(p_97748_, p_97749_, p_97750_);
    }
}
