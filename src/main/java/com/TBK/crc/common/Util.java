package com.TBK.crc.common;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.CannonArm;
import com.TBK.crc.server.multiarm.GanchoArm;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import com.TBK.crc.server.multiarm.SwordArm;

public class Util {
    public static boolean hasMultiArm(MultiArmCapability cap){
        return !cap.skills.powers.isEmpty();
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
}
