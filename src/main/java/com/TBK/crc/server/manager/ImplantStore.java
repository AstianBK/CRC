package com.TBK.crc.server.manager;

import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.common.item.CyberImplantItem;
import com.TBK.crc.server.upgrade.Upgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ImplantStore{
    public SimpleContainer store;
    public ImplantStore(){
        this.store = new SimpleContainer(6);
    }
    public ImplantStore(CompoundTag nbt){
        this.store = new SimpleContainer(6);
        fromTag(this.store,nbt.getList("store",10));
    }

    public void fromTag(SimpleContainer store, ListTag tags){
        store.clearContent();

        for(int i = 0; i < tags.size(); ++i) {
            ItemStack itemstack = ItemStack.of(tags.getCompound(i));
            if (!itemstack.isEmpty()) {
                store.setItem(tags.getCompound(i).getInt("index"),itemstack);
            }
        }

    }
    public void save(CompoundTag tag){
        tag.put("store",createTag());
    }

    public ListTag createTag() {
        ListTag listtag = new ListTag();

        for(int i = 0; i < this.store.getContainerSize(); ++i) {
            ItemStack itemstack = this.store.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag nbt = new CompoundTag();
                nbt.putInt("index",i);
                listtag.add(itemstack.save(nbt));
            }
        }

        return listtag;
    }

    public void setImplant(ItemStack implant, int index){
        if(index!=-1){
            this.store.setItem(index,implant);
        }
    }
    public ItemStack getImplant(int index){
        return this.store.getItem(index);
    }

    public List<ItemStack> getItems(){
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0 ; i<this.store.getContainerSize() ; i++){
            list.add(this.store.getItem(i));
        }
        return list;
    }
    public ItemStack getImplantForSkill(Upgrade skill){
        return getItems().stream().filter(e->e.getItem() instanceof CyberImplantItem item && (item.skill.name.equals(skill.name) || CyberImplantItem.getUpgrade(item,e.getOrCreateTag()).contains(skill))).findFirst().orElse(null);
    }

    public ItemStack getArmForSkill(Upgrade skill){
        return getImplant(0).getItem() instanceof CyberImplantItem implantItem && CyberImplantItem.getUpgrade(implantItem,getImplant(0).getOrCreateTag()).stream().anyMatch(e->e.name.equals(skill.name)) ? getImplant(0) : null;
    }
    public void setStoreForList(List<ItemStack> list){
        this.store.clearContent();
        for (int i = 0 ; i<list.size() ; i++){
            this.store.setItem(i,list.get(i));
        }
    }
    private int getSlotForType(UpgradeableParts type) {
        switch (type){
            case ARM -> {
                return 0;
            }
            case EYE -> {
                return 1;
            }
            case SYSTEMS -> {
                return this.store.getItem(2).isEmpty() ? 2 : 3;
            }
            case LEGS -> {
                return this.store.getItem(4).isEmpty() ? 4 : 5;
            }
        }
        return -1;
    }



}
