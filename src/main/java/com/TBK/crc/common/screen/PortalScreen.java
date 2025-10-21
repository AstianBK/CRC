package com.TBK.crc.common.screen;

import com.TBK.crc.CRC;
import com.TBK.crc.common.menu.ImplantMenu;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.PortalEntity;
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
    public PortalEntity portal;
    public PortalScreen(PortalEntity entity) {
        super(Component.translatable("entity.portal.menu"));
        this.player = Minecraft.getInstance().player;
        this.portal = entity;
    }

    @Override
    protected void init() {
        super.init();

        this.acceptButton = new Button.Builder(Component.translatable("crc.portal.bottom_accept"),(s)->{
            if(portal!=null){
                MultiArmCapability cap = MultiArmCapability.get(player);
                if(cap!=null){
                    cap.chickenEnemy = false;
                    cap.warningLevel = 0;
                    cap.wave = 0;
                }
                portal.discard();
                PacketHandler.sendToServer(new PacketHandlerPowers(6,null,null));
            }
            onClose();
        }).bounds(this.width / 2 - 100, this.height / 4 + 72 , 200, 20).build();
        this.cancelButton = new Button.Builder(Component.translatable("crc.portal.bottom_cancel"),(s)->{
            this.onClose();
        }).bounds(this.width / 2 - 100, this.height / 4 + 96 , 200, 20).build();

        this.addRenderableWidget(this.acceptButton);

        this.addRenderableWidget(this.cancelButton);
    }


    public void render(GuiGraphics p_283488_, int p_283551_, int p_283002_, float p_281981_) {
        p_283488_.fillGradient(0, 0, this.width, this.height, 0x80000000, 0x80000000);
        p_283488_.pose().pushPose();
        p_283488_.drawCenteredString(this.font, this.title, this.width / 2, 70, 16777215);
        p_283488_.pose().popPose();
        super.render(p_283488_, p_283551_, p_283002_, p_281981_);
        refreshButtons();
    }

    private void refreshButtons(){
        this.acceptButton.active = this.player.tickCount>20;
        this.cancelButton.active = this.player.tickCount>20;
        if(!this.portal.isAlive()){
            this.onClose();
        }
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
