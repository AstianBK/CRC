package com.TBK.crc.common.item;

import com.TBK.crc.CRC;
import com.TBK.crc.UpgradeableParts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;

public class CyberSkinItem extends ItemCyborg{
    public ResourceLocation location;
    public String name;
    public CyberSkinItem(Properties p_41383_,String name) {
        super(p_41383_, UpgradeableParts.ARM,0);
        location = new ResourceLocation(CRC.MODID,"textures/hand/cyborgarm_"+name+".png");
        this.name = name;
    }

    public static ResourceLocation getTextures(CompoundTag tag){
        if(tag.contains("skin")){
            return new ResourceLocation(tag.getString("skin"));
        }
        return null;
    }

    public static void addSkin(CompoundTag tag,CyberSkinItem skin){
        if(skin.name.equals("none")){
            tag.remove("skin");
        }else{
            tag.putString("skin",skin.location.toString());
        }
    }
}
