package com.TBK.crc.common.screen;

import com.TBK.crc.CRC;
import com.TBK.crc.common.menu.ImplantMenu;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketHandlerPowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;


public class PortalScreen extends Screen {
    public Button acceptButton;
    public Button cancelButton;
    public Player player;
    public PortalScreen(Component component) {
        super(component);
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();
        CRC.LOGGER.debug("Se inicio el puto portal");
        this.acceptButton = new Button.Builder(Component.translatable("crc.portal.bottom_accept"),(s)->{
            PacketHandler.sendToServer(new PacketHandlerPowers(6,null,null));
        }).bounds(this.width / 2 - 100, this.height / 4 + 72 , 100, 20).build();
        this.cancelButton = new Button.Builder(Component.translatable("crc.portal.bottom_cancel"),(s)->{
            this.onClose();
        }).bounds(this.width / 2 - 100, this.height / 4 + 96 , 100, 20).build();
        this.addRenderableWidget(this.acceptButton);

        this.addRenderableWidget(this.cancelButton);
    }


    public void render(GuiGraphics p_283488_, int p_283551_, int p_283002_, float p_281981_) {
        p_283488_.fillGradient(0, 0, this.width, this.height, 0x80000000, 0x80000000);
        super.render(p_283488_, p_283551_, p_283002_, p_281981_);
        refreshButtons();
    }

    private void refreshButtons(){
        this.acceptButton.active = this.player.tickCount>20;
        this.cancelButton.active = this.player.tickCount>20;
    }

    @Override
    public boolean mouseClicked(double p_97748_, double p_97749_, int p_97750_) {
        if (this.acceptButton.mouseClicked(p_97748_, p_97749_, p_97750_)) {
            refreshButtons();
            return true;
        }else if(cancelButton.mouseClicked(p_97748_, p_97749_, p_97750_)){
            refreshButtons();
            return true;
        }
        return super.mouseClicked(p_97748_, p_97749_, p_97750_);
    }
}
