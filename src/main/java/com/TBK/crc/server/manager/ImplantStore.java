package com.TBK.crc.server.manager;

import com.TBK.crc.CRC;
import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketAddImplant;
import net.minecraft.nbt.CompoundTag;
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
        this.store.fromTag(nbt.getList("store",10));
    }

    public void fromStore(CompoundTag tag){

    }
    public void save(CompoundTag tag){
        tag.put("store",this.store.createTag());
    }
    public void setImplant(Level level, ItemStack implant, UpgradeableParts type){
        int index = getSlotForType(type);
        if(index!=-1){
            this.store.setItem(index,implant);
            if(level.isClientSide){
                PacketHandler.sendToServer(new PacketAddImplant(implant,index));
            }
        }
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
    public void setStoreForList(List<ItemStack> list){
        this.store.clearContent();
        list.forEach(e->{
            this.store.addItem(e);
        });
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
