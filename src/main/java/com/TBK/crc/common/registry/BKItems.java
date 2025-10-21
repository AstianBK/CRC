package com.TBK.crc.common.registry;

import com.TBK.crc.CRC;
import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.common.item.*;
import com.TBK.crc.server.upgrade.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BKItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CRC.MODID);

    //CYBER-COMPONENT

    public static final RegistryObject<Item> CYBORG_RESISTOR = ITEMS.register("cyborg_resistor",()->new CyberComponentItem(new Item.Properties()));
    public static final RegistryObject<Item> CYBORG_CAPACITOR = ITEMS.register("cyborg_capacitor",()->new CyberComponentItem(new Item.Properties()));
    public static final RegistryObject<Item> CYBORG_BONE = ITEMS.register("cyborg_bone",()->new CyberComponentItem(new Item.Properties()));
    public static final RegistryObject<Item> CYBORG_MICROCHIP = ITEMS.register("cyborg_microchip",()->new CyberComponentItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> QUANTUM_MICROPROCESSOR = ITEMS.register("quantum_microprocessor",()->new CyberComponentItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));


    //CYBER-IMPLANT

    public static final RegistryObject<Item> CYBORG_ARM = ITEMS.register("cyborg_arm",()->new CyberImplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), UpgradeableParts.ARM, Upgrade.NONE));
    public static final RegistryObject<Item> CYBORG_EYE = ITEMS.register("cyborg_eye",()->new CyberImplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), UpgradeableParts.EYE,new NightEye()));
    public static final RegistryObject<Item> CYBORG_EYE_HACKER = ITEMS.register("cyborg_eye_hacker",()->new CyberImplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), UpgradeableParts.EYE,new HackerEye()));
    public static final RegistryObject<Item> CYBORG_HEART = ITEMS.register("cyborg_heart",()->new CyberImplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), UpgradeableParts.SYSTEMS,new HeartReflex()));
    public static final RegistryObject<Item> CYBORG_THORACIC_CAGE = ITEMS.register("cyborg_thoracic_cage",()->new CyberImplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), UpgradeableParts.SYSTEMS,new ContraAttack()));
    public static final RegistryObject<Item> CYBORG_KNEES_SPIKED = ITEMS.register("cyborg_knees_spiked",()->new CyberImplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), UpgradeableParts.LEGS,new KneesSpiked()));
    public static final RegistryObject<Item> CYBORG_KNEES = ITEMS.register("cyborg_knees",()->new CyberImplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), UpgradeableParts.LEGS,new KneesHard()));
    public static final RegistryObject<Item> CYBORG_SYNTHARM = ITEMS.register("cyborg_syntharm",()->new CyberImplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), UpgradeableParts.ARM, Upgrade.NONE));
    public static final RegistryObject<Item> CYBORG_COIL_FEET= ITEMS.register("cyborg_coil_feet",()->new CyberImplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), UpgradeableParts.LEGS,new CoilFeet()));


    //CYBER-UPGRADE

    public static final RegistryObject<Item> UPGRADE_ARM_CANNON = ITEMS.register("upgrade_arm_cannon",()->new CyberUpgradeItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),UpgradeableParts.ARM,new CannonArm(),null));
    public static final RegistryObject<Item> UPGRADE_ARM_GANCHO = ITEMS.register("upgrade_arm_harpoon",()->new CyberUpgradeItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),UpgradeableParts.ARM,new GanchoArm(),null));
    public static final RegistryObject<Item> UPGRADE_ARM_CLAWS = ITEMS.register("upgrade_arm_claws",()->new CyberUpgradeItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),UpgradeableParts.ARM,new ClawsArm(),null));
    public static final RegistryObject<Item> UPGRADE_SYSTEM_HEART= ITEMS.register("upgrade_system_heart",()->new CyberRefinementItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),new HeartReflex(),"heart_reflex"));

    public static final RegistryObject<Item> UPGRADE_SYSTEM_THORACIC_CAGE= ITEMS.register("upgrade_system_thoracic_cage",()->new CyberRefinementItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),new ContraAttack(),"counter_attack"));

    public static final RegistryObject<Item> UPGRADE_LEGS_COIL_FEET= ITEMS.register("upgrade_legs_coil_feet",()->new CyberRefinementItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),new CoilFeet(),"coil_feet"));

    public static final RegistryObject<Item> UPGRADE_LEGS_KNEES= ITEMS.register("upgrade_legs_knees",()->new CyberRefinementItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),new KneesHard(),"knees_hard"));

    public static final RegistryObject<Item> UPGRADE_LEGS_KNEES_SPIKED= ITEMS.register("upgrade_legs_knees_spiked",()->new CyberRefinementItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),new KneesSpiked(),"knees_spiked"));


    //REGULAR-ITEM
    public static final RegistryObject<Item> SIGNAL_JAMMER = ITEMS.register("signal_jammer",()->new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> TANTALUM_INGOT = ITEMS.register("tantalum_ingot",()->new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> PORTAL_OPENER = ITEMS.register("portal_opener",()->new Item(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));
    public static final RegistryObject<Item> DANGER_INCREASER = ITEMS.register("danger_increaser",()->new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));


    //COSMETIC

    public static final RegistryObject<Item> UPGRADE_SKIN_JETSTREAM = ITEMS.register("upgrade_skin_jetstream",
            ()-> new CyberSkinItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),"jetstream"));

    public static final RegistryObject<Item> UPGRADE_SKIN_MILITARY = ITEMS.register("upgrade_skin_military",
            ()-> new CyberSkinItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),"military"));

    public static final RegistryObject<Item> UPGRADE_SKIN_CYBORG = ITEMS.register("upgrade_skin_cyborg",
            ()-> new CyberSkinItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),"none"));

    //SPAWN-EGGS

    public static final RegistryObject<Item> COIL_CHICKEN_SPAWN_EGG = ITEMS.register("coil_chicken_spawn_egg",
            () -> new ForgeSpawnEggItem(BKEntityType.COIL_CHICKEN,0xd8e1e6, 0x36b7ff,
                    new Item.Properties()));

    public static final RegistryObject<Item> BOOM_CHICKEN_SPAWN_EGG = ITEMS.register("boom_chicken_spawn_egg",
            () -> new ForgeSpawnEggItem(BKEntityType.BOOM_CHICKEN,0xd8e1e6, 0x36b7ff,
                    new Item.Properties()));

    public static final RegistryObject<Item> DRONE_CHICKEN_SPAWN_EGG = ITEMS.register("drone_chicken_spawn_egg",
            () -> new ForgeSpawnEggItem(BKEntityType.DRONE_CHICKEN,0xd8e1e6, 0x36b7ff,
                    new Item.Properties()));

    public static final RegistryObject<Item> PUNCH_CHICKEN_SPAWN_EGG = ITEMS.register("punch_chicken_spawn_egg",
            () -> new ForgeSpawnEggItem(BKEntityType.PUNCH_CHICKEN,0xd8e1e6, 0x36b7ff,
                    new Item.Properties()));


}