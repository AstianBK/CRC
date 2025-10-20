package com.TBK.crc.common;

import com.TBK.crc.CRC;
import com.TBK.crc.common.registry.BKDimension;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.common.registry.BKItems;
import com.TBK.crc.common.registry.BKParticles;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.ElectroExplosionEntity;
import com.TBK.crc.server.entity.RexChicken;
import com.TBK.crc.server.manager.UpgradeInstance;
import com.TBK.crc.server.upgrade.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class Util {
    public static Map<String, Supplier<Upgrade>> upgrades= new HashMap<>();
    public static ResourceKey<DamageType> ELECTRIC_DAMAGE_MOB = ResourceKey.create(Registries.DAMAGE_TYPE
            , new ResourceLocation(CRC.MODID, "electric_damage_mob"));

    public static ResourceKey<DamageType> ELECTRIC_DAMAGE_PLAYER = ResourceKey.create(Registries.DAMAGE_TYPE
            , new ResourceLocation(CRC.MODID, "electric_damage_player"));
    public static BlockPos findCaveSurfaceNearHeight(Entity entity, int radius, RandomSource random) {
        Level level = entity.level();
        BlockPos origin = entity.blockPosition();
        int targetY = origin.getY();

        BlockPos closestPos = origin;
        int closestDifference = Integer.MAX_VALUE;

        for (int i = 0; i < 100; i++) {
            int dx = random.nextInt(-radius, radius + 1);
            int dy = random.nextInt(-radius, radius + 1);
            int dz = random.nextInt(-radius, radius + 1);

            BlockPos pos = origin.offset(dx, dy, dz);

            if (level.isEmptyBlock(pos) && level.isEmptyBlock(pos.above()) && level.isEmptyBlock(pos.below())) {
                int diff = Math.abs(pos.getY() - targetY);
                if (diff < closestDifference) {
                    closestDifference = diff;
                    closestPos = pos;

                    if (diff == 0) return closestPos;
                }
            }
        }

        return closestPos;
    }
    public static Map<Integer, UpgradeInstance> getMapEmpty(){
        Map<Integer, UpgradeInstance> map = new HashMap<>();
        map.put(0,new UpgradeInstance(Upgrade.NONE,0));
        map.put(1,new UpgradeInstance(Upgrade.NONE,0));
        map.put(2,new UpgradeInstance(Upgrade.NONE,0));
        map.put(3,new UpgradeInstance(Upgrade.NONE,0));
        map.put(4,new UpgradeInstance(Upgrade.NONE,0));
        map.put(5,new UpgradeInstance(Upgrade.NONE,0));
        return map;
    }

    public static void spawnChargedParticle(Vec3 entity){
        Random random = new Random();
        Minecraft mc=Minecraft.getInstance();
        if(mc.level!=null){
            for (int i = 0 ; i<2 ; i++){
                double box = 0.75F;
                double d0=random.nextDouble(-box, box);
                double d1=random.nextDouble(0.0d, 0.75F);
                double d2=random.nextDouble(-box, box);
                double xp = entity.x + d0;
                double yp = entity.y + d1;
                double zp = entity.z + d2;
                Particle particle = mc.particleEngine.createParticle(BKParticles.LIGHTNING_SPARK_PARTICLES.get(),xp,yp,zp,-d0*0.1F,-d1*0.1F,-d2*0.1F);
                if(particle !=null){
                    particle.scale(0.5F);
                }
            }
        }
    }
    public static Map<Integer, UpgradeInstance> getMapArmEmpty(){
        Map<Integer, UpgradeInstance> map = new HashMap<>();
        map.put(0,new UpgradeInstance(Upgrade.NONE,0));
        map.put(1,new UpgradeInstance(Upgrade.NONE,0));
        map.put(2,new UpgradeInstance(Upgrade.NONE,0));
        map.put(3,new UpgradeInstance(Upgrade.NONE,0));
        return map;
    }
    public static boolean hasMultiArm(MultiArmCapability cap){
        return !cap.implantStore.getImplant(0).isEmpty();
    }
    public static boolean hasSyntharm(MultiArmCapability cap){
        return cap.implantStore.getImplant(0).is(BKItems.CYBORG_SYNTHARM.get());
    }

    public static DamageSource electricDamage(ServerLevel level,Player player) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(ELECTRIC_DAMAGE_PLAYER),player);
    }

    public static DamageSource electricDamageMob(ServerLevel level,@NotNull Mob mob) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(ELECTRIC_DAMAGE_MOB),mob);
    }

    public static float getElapsedSeconds(AnimationDefinition p_232317_, long p_232318_) {
        float f = (float)p_232318_ / 1000.0F;
        return p_232317_.looping() ? f % p_232317_.lengthInSeconds() : f;
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
        upgrades.put("coil_feet", CoilFeet::new);
        upgrades.put("knees_spiked",KneesSpiked::new);
        upgrades.put("knees_hard",KneesHard::new);

    }

    
    public static Upgrade getTypeSkillForName(String name){
        if(upgrades.containsKey(name)){
            return upgrades.get(name).get();
        }
        return Upgrade.NONE;
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
