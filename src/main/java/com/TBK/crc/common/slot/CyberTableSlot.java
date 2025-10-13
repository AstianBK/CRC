package com.TBK.crc.common.slot;

import com.TBK.crc.common.menu.CyborgTableMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CyberTableSlot extends Slot {
    private boolean isDirty = false;
    private final CyborgTableMenu menu;
    public CyberTableSlot(CyborgTableMenu menu, Container p_40223_, int p_40224_, int p_40225_, int p_40226_) {
        super(p_40223_, p_40224_, p_40225_, p_40226_);
        this.menu = menu;
    }
    public void setDirty(){
        this.isDirty = true;
    }

    public boolean isDirty(){
        return isDirty;
    }
    public void clearDirty(){
        this.isDirty = false;
    }

    @Override
    public void onTake(Player p_150645_, ItemStack p_150646_) {
        super.onTake(p_150645_, p_150646_);
        this.menu.slotCarried = index;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (menu.getPlayerStore().store.getItem(index).getItem()==getItem().getItem()){
            this.clearDirty();
        }else {
            this.setDirty();
        }
    }

}
