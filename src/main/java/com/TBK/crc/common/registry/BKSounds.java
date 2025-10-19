package com.TBK.crc.common.registry;


import com.TBK.crc.CRC;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BKSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CRC.MODID);


    //ENTITY

    public static final RegistryObject<SoundEvent> CHICKEN_BOMB_LOOP =
            registerSoundEvent("chicken_bomb_loop");

    public static final RegistryObject<SoundEvent> CHICKEN_MINION_HURT =
            registerSoundEvent("chicken_minion_hurt");

    public static final RegistryObject<SoundEvent> REX_LASER_LOOP =
            registerSoundEvent("rex_laser_loop");

    public static final RegistryObject<SoundEvent> REX_LASER_SHOOT =
            registerSoundEvent("rex_laser_shoot");

    public static final RegistryObject<SoundEvent> REX_CANNON_SHOOT =
            registerSoundEvent("rex_cannon_shoot");

    public static final RegistryObject<SoundEvent> REX_START_CHARGE =
            registerSoundEvent("rex_start_charge");

    public static final RegistryObject<SoundEvent> REX_STEP1 =
            registerSoundEvent("rex_step1");

    public static final RegistryObject<SoundEvent> REX_STEP2 =
            registerSoundEvent("rex_step2");

    public static final RegistryObject<SoundEvent> REX_STOMP =
            registerSoundEvent("rex_stomp");

    //CYBER-IMPLANTS

    public static final RegistryObject<SoundEvent> HEART_REFLEX =
            registerSoundEvent("heart_reflex");

    public static final RegistryObject<SoundEvent> MULTIARM_CLAW_DASH =
            registerSoundEvent("multiarm_claw_dash");

    public static final RegistryObject<SoundEvent> MULTIARM_CANNON_CHARGING =
            registerSoundEvent("multiarm_cannon_charging");

    public static final RegistryObject<SoundEvent> MULTIARM_CANNON_CHARGED_SHOT =
            registerSoundEvent("multiarm_cannon_charged_shot");

    public static final RegistryObject<SoundEvent> MULTIARM_CANNON_NORMAL_SHOT =
            registerSoundEvent("multiarm_cannon_normal_shot");

    //BLOCKS

    public static final RegistryObject<SoundEvent> CYBORG_TABLE_IMPLANT =
            registerSoundEvent("cyborg_table_implant");



    public static RegistryObject<SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CRC.MODID, name)));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
