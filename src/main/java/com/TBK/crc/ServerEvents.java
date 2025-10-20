package com.TBK.crc;


import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.server.entity.*;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CRC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerEvents {
    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event) {
        event.put(BKEntityType.REX_CHICKEN.get(), RexChicken.setAttributes());
        event.put(BKEntityType.CYBORG_ROBOT_CHICKEN.get(), CyborgRobotChicken.setAttributes());
        event.put(BKEntityType.BOOM_CHICKEN.get(), BoomChicken.setAttributes());
        event.put(BKEntityType.COIL_CHICKEN.get(), CoilChicken.setAttributes());
        event.put(BKEntityType.DRONE_CHICKEN.get(), DroneChicken.setAttributes());
        event.put(BKEntityType.PUNCH_CHICKEN.get(), PunchChicken.setAttributes());
    }
}
