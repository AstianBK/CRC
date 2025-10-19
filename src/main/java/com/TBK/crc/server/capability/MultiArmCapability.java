package com.TBK.crc.server.capability;

import com.TBK.crc.CRC;
import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.common.ForgeInputEvent;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.api.IMultiArmPlayer;
import com.TBK.crc.common.item.CyberImplantItem;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.server.entity.TeleportEntity;
import com.TBK.crc.server.manager.*;
import com.TBK.crc.server.multiarm.*;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.*;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MultiArmCapability implements IMultiArmPlayer {
    public MultiArmSkillAbstract lastUsingMultiArmSkillAbstract=MultiArmSkillAbstract.NONE;
    public boolean chickenEnemy = false;
    public int cooldownUse;
    Player player;
    Level level;
    int posSelectMultiArmSkillAbstract=0;
    int castingTimer=0;
    int castingClientTimer=0;
    int maxCastingClientTimer=0;
    public boolean hotbarActive = false;
    public int invokeTimer = 0;
    public int warningLevel = 0;
    public Entity catchEntity = null;
    public int timeShoot = 0;
    public int timeShoot0 = 0;
    public int stopAimingAnim = 0;
    public int stopAimingAnim0 = 0;
    public int levelCharge = 0;
    public MultiArmSkillsAbstracts skills = new MultiArmSkillsAbstracts(Util.getMapArmEmpty());
    public MultiArmSkillsAbstracts passives = new MultiArmSkillsAbstracts(Util.getMapEmpty());
    public ImplantStore implantStore = new ImplantStore();
    public EntityType<?>[] types = new EntityType[]{
            BKEntityType.DRONE_CHICKEN.get(),BKEntityType.BOOM_CHICKEN.get(),BKEntityType.COIL_CHICKEN.get()
    };
    public boolean dirty = false;
    public SkillPose pose = SkillPose.NONE;
    public static MultiArmCapability get(Player player){
        return CRCCapability.getEntityCap(player,MultiArmCapability.class);
    }
    public void setPosSelectMultiArmSkillAbstract(int pos){
        this.posSelectMultiArmSkillAbstract=pos;
    }

    public int getPosSelectMultiArmSkillAbstract(){
        return this.posSelectMultiArmSkillAbstract;
    }
    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player=player;
    }


    public boolean isCatchedTarget(Entity entity){
        return this.catchEntity == entity;
    }
    @Override
    public MultiArmSkillAbstract getSelectSkill() {
        return this.getHotBarSkill().get(this.posSelectMultiArmSkillAbstract);
    }
    @OnlyIn(Dist.CLIENT)
    public float getAnimShoot(float partialTick){
        return Mth.lerp(partialTick,this.timeShoot0,this.timeShoot) / 10.0F;
    }
    @OnlyIn(Dist.CLIENT)
    public float getStopAiming(float partialTick){
        return Mth.lerp(partialTick,this.stopAimingAnim0,this.stopAimingAnim) / 5.0F;
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public MultiArmSkillAbstract getSkillForHotBar(int pos) {
        return this.skills.get(pos);
    }

    @Override
    public MultiArmSkillAbstract getLastUsingSkill() {
        return this.lastUsingMultiArmSkillAbstract;
    }

    @Override
    public void setLastUsingSkill(MultiArmSkillAbstract power) {
        this.lastUsingMultiArmSkillAbstract=power;
    }

    public void syncNewPlayer(ServerPlayer player,MultiArmCapability cap,boolean wasDeath){
        PacketHandler.sendToPlayer(new PacketSyncPlayerData(cap.serializeNBT(),wasDeath),player);
    }
    public void copyNbt(CompoundTag tag){
        this.deserializeNBT(tag);
    }
    public void syncWarningLevel(CompoundTag tag){
        this.warningLevel = tag.getInt("warningLevel");
    }
    public void copyFrom(MultiArmCapability cap){
        this.skills = cap.skills;
        this.passives = cap.passives;
        this.implantStore = cap.implantStore;
        this.setPosSelectMultiArmSkillAbstract(cap.getPosSelectMultiArmSkillAbstract());
        this.dirty = true;
    }
    @Override
    public void tick(Player player) {
        if(this.cooldownUse>0){
            this.cooldownUse--;
        }
        if(this.chickenEnemy){
            if(this.invokeTimer<=0){
                for (int i = 0 ; i < this.warningLevel+1 ; i++){
                    TeleportEntity tp = new TeleportEntity(level,types[this.level.random.nextInt(0,3)],Util.findRandomSurfaceNear(this.player,10,level.random),player,false);
                    this.level.addFreshEntity(tp);
                }
                this.invokeTimer = 100;
                this.warningLevel = Math.min(this.warningLevel+1,2);
                if(!this.level.isClientSide){
                    CompoundTag nbt = new CompoundTag();
                    PacketHandler.sendToPlayer(new PacketSyncPlayerData(nbt,false), (ServerPlayer) player);
                }
            }else {
                this.invokeTimer--;
            }
        }
        if(player instanceof ServerPlayer){
            if(this.dirty){
                PacketHandler.sendToPlayer(new PacketSyncImplant(player.getId(),this.implantStore.store), (ServerPlayer) player);
                PacketHandler.sendToPlayer(new PacketSyncSkill(this.skills.upgrades,this.passives.upgrades), (ServerPlayer) player);
                this.dirty = false;
            }
            PacketHandler.sendToPlayer(new PacketSyncPosHotBar(this.posSelectMultiArmSkillAbstract), (ServerPlayer) player);
        }

        if(!this.getPassives().upgrades.isEmpty()){
            this.getPassives().upgrades.forEach((i, passive)->{
                passive.getSkillAbstract().tick(this);
            });
        }
        if(Util.hasMultiArm(this)){

            if(!this.skills.getSkills().isEmpty()){
                this.skills.getSkills().forEach(e->{
                    e.getSkillAbstract().tick(this);
                });
            }
            this.stopAimingAnim0 = this.stopAimingAnim;

            if(this.stopAimingAnim>0){
                this.stopAimingAnim--;
                if(this.stopAimingAnim==0){
                    this.pose = SkillPose.NONE;
                }
            }
            if(this.level.isClientSide){
                this.timeShoot0 = this.timeShoot;
                if(this.castingClientTimer>0){
                    this.castingClientTimer--;
                }
                if(this.timeShoot>0){
                    this.timeShoot--;
                }
            }else {
                if (this.isCasting()){
                    this.castingTimer--;
                }
            }
        }
    }
    


    public static boolean hasEffect(MobEffect effect,Player player){
        MultiArmCapability cap = MultiArmCapability.get(player);
        if (cap!=null){
            return cap.passives.upgrades.values().stream().anyMatch(e-> e.getSkillAbstract().hasEffect(cap,effect));
        }
        return false;
    }

    public static boolean canEffect(MobEffect effect,Player player){
        MultiArmCapability cap = MultiArmCapability.get(player);
        if (cap!=null){
            return cap.passives.upgrades.values().stream().allMatch(e-> e.getSkillAbstract().canEffect(cap,effect));
        }
        return false;
    }



    public boolean isCasting(){
        return this.castingTimer>0;
    }

    @Override
    public void onJoinGame(Player player, EntityJoinLevelEvent event) {
        this.dirty = true;
    }

    public void clearAbilityStore(){
        MultiArmSkillsAbstracts map = new MultiArmSkillsAbstracts(new HashMap<>());
        setSetHotbar(map);

        this.dirty = true;
    }

    public void clearForUpgradeStore(){
        passives = new MultiArmSkillsAbstracts(new HashMap<>());
        this.dirty = true;
    }

    public void clearForIndex(int index){
        MultiArmSkillsAbstracts map = new MultiArmSkillsAbstracts(passives.getUpgrades());

        map.upgrades.put(index,new MultiArmSkillAbstractInstance(MultiArmSkillAbstract.NONE,0));

        passives = map;
        this.dirty = true;
    }

    public void addNewPassive(MultiArmSkillAbstract skill,int index){
        MultiArmSkillsAbstracts map = new MultiArmSkillsAbstracts(Util.getMapEmpty());
        for (Map.Entry<Integer,MultiArmSkillAbstractInstance> entry : this.passives.getUpgrades().entrySet()){
            map.addMultiArmSkillAbstracts(entry.getKey(),entry.getValue().getSkillAbstract());
        }
        map.addMultiArmSkillAbstracts(index,skill);
        this.passives = map;
        this.dirty = true;
    }

    public void addNewAbility(MultiArmSkillAbstract skill){
        MultiArmSkillsAbstracts map = new MultiArmSkillsAbstracts(this.getHotBarSkill().upgrades);
        int indexForUpgrade = Util.getIndexForName(skill.name);
        if(indexForUpgrade !=-1){
            map.addMultiArmSkillAbstracts(indexForUpgrade,skill);
            setSetHotbar(map);
            setPosSelectMultiArmSkillAbstract(indexForUpgrade);
            this.dirty = true;
        }
    }

    public void setSetHotbar(MultiArmSkillsAbstracts skillAbstracts){
        this.skills = skillAbstracts;
    }

    @Override
    public void handledSkill(MultiArmSkillAbstract power) {
        power.startAbility(this);
    }

    @Override
    public void stopSkill(MultiArmSkillAbstract power) {
        this.setLastUsingSkill(MultiArmSkillAbstract.NONE);
        power.stopAbility(this);
    }


    @Override
    public boolean canUseSkill(MultiArmSkillAbstract skillAbstract) {
        return this.player.getMainHandItem().isEmpty() && !skillAbstract.name.equals("none");
    }

    @Override
    public MultiArmSkillsAbstracts getPassives() {
        return this.passives;
    }

    @Override
    public MultiArmSkillsAbstracts getHotBarSkill() {
        return this.skills;
    }


    @Override
    public void startCasting(Player player) {

        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketHandlerPowers(0,player, player), (ServerPlayer) player);
        }

        if(this.canUseSkill(this.getSelectSkill())){
            if(this.getSelectSkill().isCasting){
                this.startCasting(this.getSelectSkill(),this.player);
            }else {
                if(this.getSelectSkill().isCanReActive()){
                    MultiArmSkillAbstract power=this.getSelectSkill();
                    this.setLastUsingSkill(this.getSelectSkill());
                    this.handledSkill(power);
                }
            }
        }

    }

    public void stopCasting(Player player) {
        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketHandlerPowers(2,player, player), (ServerPlayer) player);
        }
        if (this.getLastUsingSkill() == this.getSelectSkill()){
            this.stopSkill(this.getSelectSkill());
        }
    }

    public void startCasting(MultiArmSkillAbstract power,Player player){
        this.setLastUsingSkill(this.getSelectSkill());
        this.handledSkill(power);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        tag.putBoolean("publicEnemy",this.chickenEnemy);
        this.skills.save(tag);
        this.implantStore.save(tag);
        CompoundTag passiveTag = new CompoundTag();
        this.passives.save(passiveTag);
        tag.put("passives",passiveTag);
        tag.putInt("select_power",this.posSelectMultiArmSkillAbstract);


        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.skills = new MultiArmSkillsAbstracts(nbt);
        this.passives = new MultiArmSkillsAbstracts(nbt.getCompound("passives"));
        this.posSelectMultiArmSkillAbstract=nbt.getInt("select_power");
        this.chickenEnemy=nbt.getBoolean("publicEnemy");
        this.implantStore = new ImplantStore(nbt);
    }

    public void init(Player player) {
        this.setPlayer(player);
        this.level=player.level();
    }


    public enum SkillPose{
        NONE,
        CHARGE_CLAWS,
        DASH_CLAWS,
        CHARGE_CANNON,
        STOP_AIMING;
    }

    public static class SkillPlayerProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<IMultiArmPlayer> instance = LazyOptional.of(MultiArmCapability::new);

        @NonNull
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CRCCapability.MULTIARM_CAPABILITY.orEmpty(cap,instance.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }
    }
}
