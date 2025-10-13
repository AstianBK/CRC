package com.TBK.crc.common;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.manager.MultiArmSkillAbstractInstance;
import com.TBK.crc.server.multiarm.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Util {
    public static Map<String, Supplier<MultiArmSkillAbstract>> upgrades= new HashMap<>();

    public static Map<Integer,MultiArmSkillAbstractInstance> getMapEmpty(){
        Map<Integer, MultiArmSkillAbstractInstance> map = new HashMap<>();
        map.put(0,new MultiArmSkillAbstractInstance(MultiArmSkillAbstract.NONE,0));
        map.put(1,new MultiArmSkillAbstractInstance(MultiArmSkillAbstract.NONE,0));
        map.put(2,new MultiArmSkillAbstractInstance(MultiArmSkillAbstract.NONE,0));
        map.put(3,new MultiArmSkillAbstractInstance(MultiArmSkillAbstract.NONE,0));
        map.put(4,new MultiArmSkillAbstractInstance(MultiArmSkillAbstract.NONE,0));
        map.put(5,new MultiArmSkillAbstractInstance(MultiArmSkillAbstract.NONE,0));
        return map;
    }
    public static boolean hasMultiArm(MultiArmCapability cap){
        return !cap.implantStore.getImplant(0).isEmpty();
    }

    public static boolean isHackingChest(BlockPos pos){
        Minecraft mc = Minecraft.getInstance();
        MultiArmCapability cap = MultiArmCapability.get(mc.player);
        if(cap!=null && cap.passives.hasMultiArmSkillAbstract("hacker_eye")){
            HackerEye eye = (HackerEye) cap.passives.getForName("hacker_eye");
            return eye.hackingBlock.contains(pos);
        }
        return false;
    }
    public static void initUpgrades(){
        upgrades.put("gancho_arm",GanchoArm::new);
        upgrades.put("cannon_arm",CannonArm::new);
        upgrades.put("claws_arm",SwordArm::new);
        upgrades.put("heart_reflex",HeartReflex::new);
        upgrades.put("contra_attack",ContraAttack::new);
        upgrades.put("night_eye",NightEye::new);
        upgrades.put("hacker_eye",HackerEye::new);
        upgrades.put("rabbit_feet",RabbitFeet::new);
        upgrades.put("knees_spiked",KneesSpiked::new);
        upgrades.put("knees_hard",KneesHard::new);

    }

    
    public static MultiArmSkillAbstract getTypeSkillForName(String name){
        if(upgrades.containsKey(name)){
            return upgrades.get(name).get();
        }
        return MultiArmSkillAbstract.NONE;
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


        BlockState head = world.getBlockState(blockPos.above(2));
        boolean headSafe = head.isAir() || head.getFluidState().isEmpty();

        BlockState feet = world.getBlockState(blockPos.above());
        boolean feetSafe = feet.isAir()  || feet.getFluidState().isEmpty();

        return headSafe && feetSafe;
    }
}
