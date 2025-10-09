package com.TBK.crc.common;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class Util {
    //Map<String,MultiArmSkillAbstract> upgradeSkill= new HashMap<>();
    public static boolean hasMultiArm(MultiArmCapability cap){
        return !cap.skills.upgrades.isEmpty();
    }

    public static MultiArmSkillAbstract getTypeSkillForName(String name){
        switch (name){
            case "gancho_arm"->{
                return new GanchoArm();
            }
            case "cannon_arm"->{
                return new CannonArm();
            }
            case "claws_arm"->{
                return new SwordArm();
            }
            case "ultra_instict_hearth"->{
                return new UltraInstictHeart();
            }
        }
        return null;
    }

    public static int getIndexForName(String name){
        switch (name){
            case "gancho_arm"->{
                return 2;
            }
            case "cannon_arm"->{
                return 1;
            }
            case "claws_arm"->{
                return 0;
            }
        }
        return -1;
    }

    public static boolean isSafePosition(Level world, Vec3 pos) {
        BlockPos blockPos = BlockPos.containing(pos);

        BlockState floor = world.getBlockState(blockPos);
        boolean floorSafe = floor.isAir() || !floor.getCollisionShape(world, blockPos).isEmpty();

        BlockState head = world.getBlockState(blockPos.above());
        boolean headSafe = head.isAir() || head.getFluidState().isEmpty();

        BlockState feet = world.getBlockState(blockPos.below());
        boolean feetSafe = feet.getFluidState().isEmpty();

        return floorSafe && headSafe && feetSafe;
    }
}
