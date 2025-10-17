package com.TBK.crc.common;

import com.TBK.crc.common.registry.BKDimension;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.ElectroExplosionEntity;
import com.TBK.crc.server.entity.RexChicken;
import com.TBK.crc.server.manager.MultiArmSkillAbstractInstance;
import com.TBK.crc.server.multiarm.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.HashMap;
import java.util.List;
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

    public static void refreshHackingChest(Player player,Level level){
        MultiArmCapability cap = MultiArmCapability.get(player);
        if(cap!=null && cap.passives.hasMultiArmSkillAbstract("hacker_eye")){
            HackerEye eye = (HackerEye) cap.passives.getForName("hacker_eye");
            eye.refreshTarget(player,level);
        }
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
        upgrades.put("claws_arm", ClawsArm::new);
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
        boolean headSafe = head.isAir() && head.getFluidState().isEmpty();

        BlockState feet = world.getBlockState(blockPos.above());
        boolean feetSafe = feet.isAir()  && feet.getFluidState().isEmpty();

        return headSafe && feetSafe;
    }
    public static boolean isInFuture(LivingEntity entity) {
        return isInDimension(entity, BKDimension.THE_FUTURE_LEVEL);
    }

    public static boolean isInDimension(LivingEntity entity, ResourceKey<Level> key) {
        return key.equals(entity.level().dimension());
    }
    public static ElectroExplosionEntity createExplosion(Entity entity, Level level, BlockPos end, float radius){
        ElectroExplosionEntity explosion = new ElectroExplosionEntity(level,entity,end.getX(),end.getY(),end.getZ(),radius,false);
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(level, explosion)) return explosion;
        explosion.explode();
        explosion.finalizeExplosion(false);
        return explosion;
    }

    public static List<? extends RexChicken> getRexChickens(ServerLevel level) {
        return level.getEntities(BKEntityType.REX_CHICKEN.get(), LivingEntity::isAlive);
    }


}
