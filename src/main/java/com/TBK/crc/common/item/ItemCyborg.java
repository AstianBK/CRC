package com.TBK.crc.common.item;

import com.TBK.crc.UpgradeableParts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

public class ItemCyborg extends Item {
    public UpgradeableParts typePart;
    public int tier;
    public ItemCyborg(Properties p_41383_, UpgradeableParts part,int tier) {
        super(p_41383_);
        this.typePart = part;
        this.tier = tier;
    }

    public UpgradeableParts getTypePart() {
        return typePart;
    }

    public int getTier() {
        return tier;
    }

}
