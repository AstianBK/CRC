package com.TBK.crc.server.capability;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CRCCapability {
    public static final Capability<MultiArmCapability> MULTIARM_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    //public static final Capability<AnimationPlayerCapability> ANIMATION_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(MultiArmCapability.class);
        //event.register(AnimationPlayerCapability.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends MultiArmCapability> T getEntityCap(Entity entity, Class<T> type) {
        if (entity != null) {
            MultiArmCapability entitypatch = entity.getCapability(CRCCapability.MULTIARM_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }

}
