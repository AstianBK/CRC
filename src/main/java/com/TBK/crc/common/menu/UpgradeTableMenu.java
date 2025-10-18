package com.TBK.crc.common.menu;

import com.TBK.crc.CRC;
import com.TBK.crc.common.item.CyberComponentItem;
import com.TBK.crc.common.item.CyberImplantItem;
import com.TBK.crc.common.item.CyberSkinItem;
import com.TBK.crc.common.item.CyberUpgradeItem;
import com.TBK.crc.common.registry.BKContainers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class UpgradeTableMenu extends AbstractContainerMenu {

    public final SimpleContainer craftSlots;

    protected final Level level;
    public int id;
    public ContainerData data;
    public Player player;
    public UpgradeTableMenu(int i, Inventory inventory, FriendlyByteBuf buf) {
        this(i,inventory,new SimpleContainer(3),new SimpleContainerData(1));
    }
    public UpgradeTableMenu(int p_39356_, Inventory p_39357_, SimpleContainer container, ContainerData data) {
        super(BKContainers.UPGRADE_MENU.get(), p_39356_);
        this.data=data;
        this.id=data.get(0);
        this.craftSlots = container;
        this.player = p_39357_.player;
        this.addSlot(new Slot(container, 0, 76,35){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberUpgradeItem || p_40231_.getItem() instanceof CyberSkinItem;
            }
        });
        this.addSlot(new Slot(container, 1, 27,35){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem;
            }
        });
        this.addSlot(new Slot(container, 2, 134,35){
            @Override
            public boolean mayPickup(Player p_40228_) {
                return !this.getItem().isEmpty();
            }

            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                super.onTake(p_150645_, p_150646_);
                UpgradeTableMenu.this.craftSlots.setItem(0,ItemStack.EMPTY);
                UpgradeTableMenu.this.craftSlots.setItem(1,ItemStack.EMPTY);
            }
        });

        this.level = p_39357_.player.level();
        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(p_39357_, i1 + k * 9 + 9, 8 + i1 * 18, 84+ k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(p_39357_, l, 8 + l * 18, 142));
        }
        this.addDataSlots(data);
        this.addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu menu, int i, ItemStack itemStack) {
                if(level.isClientSide)return;
                ItemStack base = menu.getSlot(1).getItem().copy();
                ItemStack upgrade = menu.getSlot(0).getItem().copy();

                if(!upgrade.isEmpty() && !base.isEmpty()){
                    if(upgrade.getItem() instanceof CyberUpgradeItem upgradeItem){
                        if(CyberImplantItem.canAddUpgrade(base.getOrCreateTag(),upgradeItem.getSkill())){
                            CyberImplantItem.addUpgrade(base.getOrCreateTag(),upgradeItem.getSkill());
                        }
                    }
                    if(upgrade.getItem() instanceof CyberSkinItem skinItem){
                        CyberSkinItem.addSkin(base.getOrCreateTag(),skinItem);
                    }
                    UpgradeTableMenu.this.craftSlots.setItem(2,base);
                }else {
                    UpgradeTableMenu.this.craftSlots.setItem(2,ItemStack.EMPTY);
                }
            }

            @Override
            public void dataChanged(AbstractContainerMenu abstractContainerMenu, int i, int i1) {}
        });
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
