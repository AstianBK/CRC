package com.TBK.crc.common.menu;

import com.TBK.crc.CRC;
import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.common.item.CyberImplantItem;
import com.TBK.crc.common.registry.BKContainers;
import com.TBK.crc.common.slot.CyberTableSlot;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.manager.ImplantStore;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketAddImplant;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CyborgTableMenu extends AbstractContainerMenu {

    public final SimpleContainer craftSlots;
    protected final Level level;
    public int id;
    public int slotCarried = -1;
    public ContainerData data;
    public Player player;
    public CyborgTableMenu(int i, Inventory inventory, FriendlyByteBuf buf) {
        this(i,inventory,new SimpleContainer(6),new SimpleContainerData(1),new SimpleContainer(6));
    }
    public CyborgTableMenu(int p_39356_, Inventory p_39357_, SimpleContainer container, ContainerData data,SimpleContainer skills) {
        super(BKContainers.TABLE_MENU.get(), p_39356_);
        this.data=data;
        this.id=data.get(0);
        this.craftSlots = container;
        this.player = p_39357_.player;
        this.addSlot(new CyberTableSlot(this,container, 0, 18,50){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.ARM;
            }
        });
        this.addSlot(new CyberTableSlot(this,container, 1, 36,16){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.EYE;
            }
        });
        this.addSlot(new CyberTableSlot(this,container, 2, 114,39){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.SYSTEMS;
            }
        });
        this.addSlot(new CyberTableSlot(this,container, 3, 132,39){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.SYSTEMS;
            }
        });
        this.addSlot(new CyberTableSlot(this,container, 4, 116,90){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.LEGS;
            }
        });
        this.addSlot(new CyberTableSlot(this,container, 5, 134,90){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.LEGS;
            }
        });

        for (int i = 0 ; i<6 ; i++){
            this.craftSlots.setItem(i,skills.getItem(i));
        }

        this.level = p_39357_.player.level();
        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(p_39357_, i1 + k * 9 + 9, 8 + i1 * 18, 165+ k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(p_39357_, l, 8 + l * 18, 223));
        }
        this.addDataSlots(data);
    }

    
    public ImplantStore getPlayerStore(){
        MultiArmCapability cap = MultiArmCapability.get(player);
        if (cap!=null){
            return cap.implantStore;
        }
        return null;
    }



    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38987_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }

    public void refreshPlayer(Player player) {
        for (int i = 0 ; i<6 ; i++){
            refreshSlot(i,player);
        }
    }

    public void refreshSlot(int i,Player player){
        MultiArmCapability cap = MultiArmCapability.get(player);
        if(cap!=null){
            ItemStack menuItem = this.craftSlots.getItem(i);
            ItemStack storeItem = this.getPlayerStore().store.getItem(i);
            if(menuItem.isEmpty() && !storeItem.isEmpty()){
                PacketHandler.sendToServer(new PacketAddImplant(ItemStack.EMPTY,i));
            }
            if((!menuItem.isEmpty() && storeItem.isEmpty() || menuItem.getItem()!=storeItem.getItem())){
                PacketHandler.sendToServer(new PacketAddImplant(menuItem,i));
            }
        }
    }


    @Override
    public void removed(Player p_38940_) {
        ItemStack itemstack = this.getCarried();
        if (!itemstack.isEmpty() && this.slotCarried != -1 && getSlot(this.slotCarried).getItem().isEmpty()){
            MultiArmCapability cap = MultiArmCapability.get(p_38940_);
            if (cap!=null){
                cap.implantStore.setImplant(itemstack,this.slotCarried);
                if(this.slotCarried == 0){
                    cap.clearAbilityStore();
                }else {
                    cap.clearForIndex(this.slotCarried);
                }
                if(!this.level.isClientSide){
                    cap.dirty = true;
                }
            }
        }else {
            this.refreshPlayer(p_38940_);
        }
        if (p_38940_ instanceof ServerPlayer){
            if (!itemstack.isEmpty()) {
                if (p_38940_.isAlive() && !((ServerPlayer)p_38940_).hasDisconnected()) {
                    p_38940_.getInventory().placeItemBackInInventory(itemstack);
                } else {
                    p_38940_.drop(itemstack, false);
                }
                this.setCarried(ItemStack.EMPTY);
            }
        }

        this.slotCarried = -1;
        this.craftSlots.clearContent();
    }
}
