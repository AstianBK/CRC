package com.TBK.crc.common.menu;

import com.TBK.crc.common.registry.BKContainers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ImplantMenu extends AbstractContainerMenu {

    public Player player;
    public ImplantMenu(int i, Inventory inventory, FriendlyByteBuf buf) {
        this(i,inventory,new SimpleContainer(6));
    }
    public ImplantMenu(int p_39356_, Inventory p_39357_, SimpleContainer container) {
        super(BKContainers.IMPLANT_MENU.get(), p_39356_);

        this.player = p_39357_.player;
        this.addSlot(new Slot(container, 0, 18,50){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return false;
            }

            @Override
            public boolean mayPickup(Player p_40228_) {
                return false;
            }
        });
        this.addSlot(new Slot(container, 1, 36,16){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return false;
            }

            @Override
            public boolean mayPickup(Player p_40228_) {
                return false;
            }
        });
        this.addSlot(new Slot(container, 2, 114,39){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return false;
            }

            @Override
            public boolean mayPickup(Player p_40228_) {
                return false;
            }
        });
        this.addSlot(new Slot(container, 3, 132,39){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return false;
            }

            @Override
            public boolean mayPickup(Player p_40228_) {
                return false;
            }
        });
        this.addSlot(new Slot(container, 4, 116,90){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return false;
            }

            @Override
            public boolean mayPickup(Player p_40228_) {
                return false;
            }
        });
        this.addSlot(new Slot(container, 5, 134,90){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return false;
            }

            @Override
            public boolean mayPickup(Player p_40228_) {
                return false;
            }
        });

    }

    @Override
    protected void clearContainer(Player p_150412_, Container p_150413_) {
    }

    @Override
    public void removed(Player p_38940_) {
        super.removed(p_38940_);
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38987_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }

}
