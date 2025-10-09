package com.TBK.crc.common.menu;

import com.TBK.crc.CRC;
import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.common.item.CyberImplantItem;
import com.TBK.crc.common.registry.BKContainers;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.manager.ImplantStore;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import net.minecraft.network.FriendlyByteBuf;
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
    public ContainerData data;
    public Player player;
    public boolean initialize = false;
    public CyborgTableMenu(int i, Inventory inventory, FriendlyByteBuf buf) {
        this(i,inventory,new SimpleContainer(6),new SimpleContainerData(1),new SimpleContainer(6));
    }
    public CyborgTableMenu(int p_39356_, Inventory p_39357_, SimpleContainer container, ContainerData data,SimpleContainer skills) {
        super(BKContainers.POTION_MENU.get(), p_39356_);
        this.data=data;
        this.id=data.get(0);
        this.craftSlots = container;
        this.player = p_39357_.player;
        this.addSlot(new Slot(container, 0, 18,50){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.ARM;
            }

            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                super.onTake(p_150645_, p_150646_);
                CyborgTableMenu.this.upgradePlayer(false);
            }
        });
        this.addSlot(new Slot(container, 1, 36,16){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.EYE;
            }
            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                super.onTake(p_150645_, p_150646_);
                CyborgTableMenu.this.upgradePlayer(false);
            }
        });
        this.addSlot(new Slot(container, 2, 114,39){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.SYSTEMS;
            }
            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                super.onTake(p_150645_, p_150646_);
                CyborgTableMenu.this.upgradePlayer(false);
            }
        });
        this.addSlot(new Slot(container, 3, 132,39){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.SYSTEMS;
            }
            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                super.onTake(p_150645_, p_150646_);
                CyborgTableMenu.this.upgradePlayer(false);
            }
        });
        this.addSlot(new Slot(container, 4, 116,90){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.LEGS;
            }
            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                super.onTake(p_150645_, p_150646_);
                CyborgTableMenu.this.upgradePlayer(false);
            }
        });
        this.addSlot(new Slot(container, 5, 134,90){
            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return p_40231_.getItem() instanceof CyberImplantItem implant && implant.typePart == UpgradeableParts.LEGS;
            }
            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                super.onTake(p_150645_, p_150646_);
                CyborgTableMenu.this.upgradePlayer(false);
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

    public void upgradePlayer(boolean operar) {
        MultiArmCapability cap = MultiArmCapability.get(player);
        if(cap!=null){
            for (int i = 0 ; i<6 ; i++){
                ItemStack menuItem = this.craftSlots.getItem(i);
                ItemStack storeItem = this.getPlayerStore().store.getItem(i);

                if(menuItem.isEmpty() && !storeItem.isEmpty() && storeItem.getItem() instanceof CyberImplantItem implantStore){
                    cap.implantStore.setImplant(player.level(),ItemStack.EMPTY,implantStore.typePart);
                    if(implantStore.typePart==UpgradeableParts.ARM){
                        cap.clearAbilityStore();
                    }else {
                        cap.clearForUpgradeStore();
                    }
                }
                if((!menuItem.isEmpty() && storeItem.isEmpty() || menuItem.getItem()!=storeItem.getItem()) && (menuItem.getItem() instanceof CyberImplantItem implantMenu)){
                    cap.implantStore.setImplant(player.level(), menuItem.copy(),implantMenu.typePart);
                    if(implantMenu.typePart==UpgradeableParts.ARM){
                        for (MultiArmSkillAbstract upgrade : CyberImplantItem.getUpgrade(menuItem.getOrCreateTag())){
                            if(upgrade != MultiArmSkillAbstract.NONE){
                                cap.addNewAbility(upgrade);
                                CRC.LOGGER.debug("Se agrego una abilidad nueva");
                            }
                        }
                    }else {
                        for (MultiArmSkillAbstract upgrade : CyberImplantItem.getUpgrade(menuItem.getOrCreateTag())){
                            if(upgrade != MultiArmSkillAbstract.NONE){
                                cap.addNewPassive(upgrade);
                                CRC.LOGGER.debug("Se agrego una pasiva nueva");
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removed(Player p_38940_) {
        super.removed(p_38940_);
        this.craftSlots.clearContent();
    }
}
