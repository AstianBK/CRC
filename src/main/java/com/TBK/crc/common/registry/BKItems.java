package com.TBK.crc.common.registry;

import com.TBK.crc.CRC;
import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.common.item.CyberComponentItem;
import com.TBK.crc.common.item.CyberImplantItem;
import com.TBK.crc.common.item.CyberUpgradeItem;
import com.TBK.crc.server.multiarm.CannonArm;
import com.TBK.crc.server.multiarm.GanchoArm;
import com.TBK.crc.server.multiarm.SwordArm;
import com.TBK.crc.server.multiarm.UltraInstictHeart;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BKItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CRC.MODID);

    //CYBER-COMPONENT
    public static final RegistryObject<Item> CYBORG_ARM = ITEMS.register("cyborg_arm",()->new CyberImplantItem(new Item.Properties(), UpgradeableParts.ARM));
    public static final RegistryObject<Item> CYBORG_RESISTOR = ITEMS.register("cyborg_resistor",()->new CyberComponentItem(new Item.Properties()));
    public static final RegistryObject<Item> CYBORG_CAPACITOR = ITEMS.register("cyborg_capacitor",()->new CyberComponentItem(new Item.Properties()));
    public static final RegistryObject<Item> CYBORG_MICROCHIP = ITEMS.register("cyborg_microchip",()->new CyberComponentItem(new Item.Properties()));
    public static final RegistryObject<Item> CYBORG_BONE = ITEMS.register("cyborg_bone",()->new CyberComponentItem(new Item.Properties()));





    //CYBER-IMPLANT
    public static final RegistryObject<Item> CYBORG_EYE = ITEMS.register("cyborg_eye",()->new CyberImplantItem(new Item.Properties(), UpgradeableParts.EYE));
    public static final RegistryObject<Item> CYBORG_EYE_HACKER = ITEMS.register("cyborg_eye_hacker",()->new CyberImplantItem(new Item.Properties(), UpgradeableParts.EYE));
    public static final RegistryObject<Item> CYBORG_HEART = ITEMS.register("cyborg_heart",()->new CyberImplantItem(new Item.Properties(), UpgradeableParts.SYSTEMS));
    public static final RegistryObject<Item> CYBORG_THORACIC_CAGE = ITEMS.register("cyborg_thoracic_cage",()->new CyberImplantItem(new Item.Properties(), UpgradeableParts.SYSTEMS));
    public static final RegistryObject<Item> CYBORG_KNEES = ITEMS.register("cyborg_knees",()->new CyberImplantItem(new Item.Properties(), UpgradeableParts.LEGS));
    public static final RegistryObject<Item> CYBORG_SYNTHARM = ITEMS.register("cyborg_syntharm",()->new CyberImplantItem(new Item.Properties(), UpgradeableParts.ARM));
    public static final RegistryObject<Item> CYBORG_COIL_FEET= ITEMS.register("cyborg_coil_feet",()->new CyberImplantItem(new Item.Properties(), UpgradeableParts.LEGS));

    public static final RegistryObject<Item> UPGRADE_ULTRA_INSTICT= ITEMS.register("upgrade_ultra_instict",()->new CyberUpgradeItem(new Item.Properties(),UpgradeableParts.SYSTEMS,new UltraInstictHeart(),0));

    //CYBER-UPGRADE
    public static final RegistryObject<Item> UPGRADE_ARM_CANNON = ITEMS.register("upgrade_arm_cannon",()->new CyberUpgradeItem(new Item.Properties(),UpgradeableParts.ARM,new CannonArm(),0));
    public static final RegistryObject<Item> UPGRADE_ARM_GANCHO = ITEMS.register("upgrade_arm_harpoon",()->new CyberUpgradeItem(new Item.Properties(),UpgradeableParts.ARM,new GanchoArm(),0));
    public static final RegistryObject<Item> UPGRADE_ARM_CLAWS = ITEMS.register("upgrade_arm_claws",()->new CyberUpgradeItem(new Item.Properties(),UpgradeableParts.ARM,new SwordArm(),0));

    //REGULAR-ITEM
    public static final RegistryObject<Item> SIGNAL_JAMMER = ITEMS.register("signal_jammer",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> TANTALUM_INGOT = ITEMS.register("tantalum_ingot",()->new Item(new Item.Properties()));


}