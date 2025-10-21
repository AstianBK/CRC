package com.TBK.crc.server.capability;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.api.IMultiArmPlayer;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.common.registry.BKSounds;
import com.TBK.crc.server.entity.TeleportEntity;
import com.TBK.crc.server.manager.*;
import com.TBK.crc.server.upgrade.*;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.*;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

import java.util.*;


public class MultiArmCapability implements IMultiArmPlayer {
    public Upgrade lastUsingUpgrade = Upgrade.NONE;
    public boolean chickenEnemy = false;
    public int cooldownUse;
    Player player;
    Level level;
    int posSelectUpgrade = 0;
    public boolean hotbarActive = false;
    public int invokeTimer = 0;
    public int warningLevel = 0;
    public Entity catchEntity = null;
    public int timeLevelWarning = 0;
    public int timeLevelWarning0 = 0;
    public int chickenAnimTime = 0;
    public int chickenAnimTime0 = 0;
    public boolean chickenSpoke = false;
    public boolean playChickenWarning = false;
    public int timeShoot = 0;
    public int timeShoot0 = 0;
    public int timeCharge = 0;
    public int timeCharge0 = 0;
    public int stopAimingAnim = 0;
    public int stopAimingAnim0 = 0;
    public int levelCharge = 0;
    public int wave = 0;
    public int energy = 10;
    public float[] chanceLeaderSpawn = new float[]{
            0.05F,0.5F,1.0F
    };
    public Upgrades skills = new Upgrades(Util.getMapArmEmpty());
    public Upgrades passives = new Upgrades(Util.getMapEmpty());
    public ImplantStore implantStore = new ImplantStore();
    public boolean dirty = false;
    public SkillPose pose = SkillPose.NONE;
    ServerBossEvent event =  (ServerBossEvent)(new ServerBossEvent(Component.translatable("chicken_attack.next_wave"), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS)).setPlayBossMusic(true).setCreateWorldFog(false);
    public static MultiArmCapability get(Player player){
        return CRCCapability.getEntityCap(player,MultiArmCapability.class);
    }
    public void setPosSelectUpgrade(int pos){
        this.posSelectUpgrade =pos;
    }

    public int getPosSelectUpgrade(){
        return this.posSelectUpgrade;
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
    public Upgrade getSelectSkill() {
        return this.getHotBarSkill().get(this.posSelectUpgrade);
    }
    @OnlyIn(Dist.CLIENT)
    public float getAnimLevelWarning(float partialTick){
        return Mth.lerp(partialTick,this.timeLevelWarning0,this.timeLevelWarning) / 10.0F;
    }
    @OnlyIn(Dist.CLIENT)
    public float getAnimShoot(float partialTick){
        return Mth.lerp(partialTick,this.timeShoot0,this.timeShoot) / 10.0F;
    }
    @OnlyIn(Dist.CLIENT)
    public float getAnimChargeClaw(float partialTick){
        return 1.0F-(Mth.lerp(partialTick,this.timeCharge0,this.timeCharge) / 10.0F);
    }
    @OnlyIn(Dist.CLIENT)
    public float getStopAiming(float partialTick){
        return Mth.lerp(partialTick,this.stopAimingAnim0,this.stopAimingAnim) / 5.0F;
    }

    @OnlyIn(Dist.CLIENT)
    public float getChickenTalkAnim(float partialTick){
        return Mth.lerp(partialTick,this.chickenAnimTime0,this.chickenAnimTime) / 30.0F;
    }
    @Override
    public Upgrade getSkillForHotBar(int pos) {
        return this.skills.get(pos);
    }

    @Override
    public Upgrade getLastUsingSkill() {
        return this.lastUsingUpgrade;
    }

    @Override
    public void setLastUsingSkill(Upgrade power) {
        this.lastUsingUpgrade = power;
    }

    public void syncNewPlayer(ServerPlayer player,MultiArmCapability cap,boolean wasDeath){
        PacketHandler.sendToPlayer(new PacketSyncPlayerData(cap.serializeNBT(),wasDeath,player.getId()),player);
    }
    public void copyNbt(CompoundTag tag){
        this.deserializeNBT(tag);
    }
    public void loadWarningLevel(CompoundTag tag){
        this.warningLevel = tag.getInt("warningLevel");
        this.wave = tag.getInt("wave");
        this.timeLevelWarning = tag.getInt("timeLevelWarning");
        this.timeLevelWarning0 = tag.getInt("timeLevelWarning");
        this.playChickenWarning = tag.getBoolean("playAnim");
        this.invokeTimer = tag.getInt("invokeTime");
    }
    public void copyFrom(MultiArmCapability cap){
        this.skills = cap.skills;
        this.passives = cap.passives;
        this.implantStore = cap.implantStore;
        this.setPosSelectUpgrade(cap.getPosSelectUpgrade());
        this.dirty = true;
    }
    public CompoundTag saveChickenEnemyData(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("warningLevel",this.warningLevel);
        tag.putInt("wave",this.wave);
        tag.putInt("timeLevelWarning",this.timeLevelWarning);
        tag.putBoolean("playAnim",this.playChickenWarning);
        tag.putInt("invokeTime",this.invokeTimer);
        return tag;
    }
    @Override
    public void tick(Player player) {

        if(player instanceof ServerPlayer serverPlayer){
            event.setVisible(this.chickenEnemy);
            if(!event.getPlayers().contains(serverPlayer)){
                event.addPlayer(serverPlayer);
            }
            Component component;
            if(this.warningLevel>2){
                component = Component.translatable("chicken_attack.infinite_wave");
            }else {
                component = Component.translatable("chicken_attack.wave").append(" : "+(this.wave+1));
            }
            event.setName(component);
            BossEvent.BossBarColor color = getColorForWarningLevel();
            event.setColor(color);
            event.setProgress(1.0F-(float) this.invokeTimer/1000.0F);
        }else {
            event.removeAllPlayers();
        }
        if(this.cooldownUse>0){
            this.cooldownUse--;
        }

        if(this.level.isClientSide){
            this.timeLevelWarning0 = this.timeLevelWarning;
            this.chickenAnimTime0 = this.chickenAnimTime;
            if(this.playChickenWarning){

                if(this.chickenAnimTime<=0){
                    if(this.chickenSpoke){
                        if(this.timeLevelWarning>0){
                            this.timeLevelWarning--;
                            if(this.timeLevelWarning == 0){
                                this.playChickenWarning = false;
                                this.chickenSpoke = false;
                            }
                        }
                    }else {
                        if(this.timeLevelWarning<10){
                            this.timeLevelWarning++;
                            if(this.timeLevelWarning==10){
                                this.chickenAnimTime = 30;
                                this.chickenAnimTime0 = 30;
                                this.level.playLocalSound(player.blockPosition().above(1), BKSounds.CHICKEN_CYBORG_SPEECH.get(), SoundSource.AMBIENT,10.0F,1.0F,false);
                            }
                        }
                    }
                }else {
                    this.chickenAnimTime--;
                    if(this.chickenAnimTime==10 || this.chickenAnimTime==20){
                        this.level.playLocalSound(player.blockPosition().above(1), BKSounds.CHICKEN_CYBORG_SPEECH.get(), SoundSource.AMBIENT,10.0F,1.0F,false);
                    }
                    if(this.chickenAnimTime == 0){
                        this.chickenSpoke = true;
                    }
                }
            }
        }
        if(this.chickenEnemy){
            if(this.invokeTimer<=0){
                if(warningLevel>0){
                    this.teleportChicken();
                }
                this.invokeTimer = 1000;
                this.wave = this.wave+1;
                if(this.wave>6 && this.warningLevel<3){
                    this.warningLevel = Math.min(this.warningLevel+1,3);
                    this.wave = 0;
                    this.playChickenWarning = true;
                }else {
                    this.playChickenWarning = false;
                }
                this.wave = Math.min(this.wave,6);
                if(!this.level.isClientSide){
                    PacketHandler.sendToPlayer(new PacketSyncPlayerData(saveChickenEnemyData(),false,player.getId()), (ServerPlayer) player);
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
            PacketHandler.sendToPlayer(new PacketSyncPosHotBar(this.posSelectUpgrade), (ServerPlayer) player);
        }

        if(!this.getPassives().upgrades.isEmpty()){
            this.getPassives().upgrades.forEach((i, passive)->{
                passive.getUpgrade().tick(this);
            });
        }


        if(Util.hasMultiArm(this)){

            if(!this.skills.getSkills().isEmpty()){
                this.skills.getSkills().forEach(e->{
                    e.getUpgrade().tick(this);
                });
            }

            this.stopAimingAnim0 = this.stopAimingAnim;
            this.timeCharge0 = this.timeCharge;
            if(this.pose == SkillPose.STOP_AIMING){
                if(this.stopAimingAnim>0){
                    this.stopAimingAnim--;
                    if(this.stopAimingAnim==0){
                        this.pose = SkillPose.NONE;
                    }
                }
            }

            if(this.level.isClientSide){
                this.timeShoot0 = this.timeShoot;
                if(this.timeShoot>0){
                    this.timeShoot--;
                }

                if(this.pose == SkillPose.CHARGE_CLAWS){
                    if(this.timeCharge>0){
                        this.timeCharge--;
                    }
                }
            }
        }
    }

    private BossEvent.BossBarColor getColorForWarningLevel() {
        switch (this.warningLevel){
            case 2->{
                return BossEvent.BossBarColor.YELLOW;
            }
            case 3->{
                return BossEvent.BossBarColor.RED;
            }
            default->{
                return BossEvent.BossBarColor.BLUE;
            }
        }
    }

    private void teleportChicken() {
        float chanceLeader = this.chanceLeaderSpawn[this.warningLevel-1];
        for (EntityTypeWaves wave : EntityTypeWaves.values()){
            int length = wave.waves[this.wave]>0 ? (wave.waves[this.wave] + this.level.random.nextInt(0,2)) : 0 ;
            boolean hasShield;
            if(warningLevel==2 || wave.type.equals(BKEntityType.PUNCH_CHICKEN.get())){
                hasShield = true;
            }else if((warningLevel>1) || this.wave == 6){
                hasShield = this.level.random.nextBoolean();
            }else {
                hasShield = false;
            }
            if(wave.type.equals(BKEntityType.PUNCH_CHICKEN.get())){
                length = this.level.random.nextFloat()<chanceLeader ? 1 : 0;
            }
            for (int i = 0 ; i < length ;i++){
                TeleportEntity tp = new TeleportEntity(level,wave.type, Util.findCaveSurfaceNearHeight(this.player,10,level.random),player,hasShield);
                this.level.addFreshEntity(tp);
            }
        }
    }


    public static float getJumpBoost(Player player){
        MultiArmCapability cap = MultiArmCapability.get(player);
        if (cap!=null){
            return cap.passives.getForName("coil_feet").name!="none" ? ((CoilFeet)cap.passives.getForName("coil_feet")).getJumpBoost() : 1.0F;
        }
        return 1.0F;
    }
    public static boolean hasEffect(MobEffect effect,Player player){
        MultiArmCapability cap = MultiArmCapability.get(player);
        if (cap!=null){
            return cap.passives.upgrades.values().stream().anyMatch(e-> e.getUpgrade().hasEffect(cap,effect));
        }
        return false;
    }

    public static boolean canEffect(MobEffect effect,Player player){
        MultiArmCapability cap = MultiArmCapability.get(player);
        if (cap!=null){
            return cap.passives.upgrades.values().stream().allMatch(e-> e.getUpgrade().canEffect(cap,effect));
        }
        return false;
    }


    @Override
    public void onJoinGame(Player player, EntityJoinLevelEvent event) {
        this.dirty = true;
    }

    public void clearAbilityStore(){
        Upgrades map = new Upgrades(new HashMap<>());
        setSetHotbar(map);

        this.dirty = true;
    }

    public void clearForUpgradeStore(){
        passives = new Upgrades(new HashMap<>());
        this.dirty = true;
    }

    public void clearForIndex(int index){
        Upgrades map = new Upgrades(passives.getUpgrades());

        map.upgrades.put(index,new UpgradeInstance(Upgrade.NONE,0));

        passives = map;
        this.dirty = true;
    }

    public void addNewPassive(Upgrade skill, int index){
        Upgrades map = new Upgrades(Util.getMapEmpty());
        for (Map.Entry<Integer, UpgradeInstance> entry : this.passives.getUpgrades().entrySet()){
            map.addMultiArmSkillAbstracts(entry.getKey(),entry.getValue().getUpgrade());
        }
        map.addMultiArmSkillAbstracts(index,skill);
        this.passives = map;
        this.dirty = true;
    }

    public void addNewAbility(Upgrade skill){
        Upgrades map = new Upgrades(this.getHotBarSkill().upgrades);
        int indexForUpgrade = Util.getIndexForName(skill.name);
        if(indexForUpgrade !=-1){
            map.addMultiArmSkillAbstracts(indexForUpgrade,skill);
            setSetHotbar(map);
            setPosSelectUpgrade(indexForUpgrade);
            this.dirty = true;
        }
    }

    public void setSetHotbar(Upgrades skillAbstracts){
        this.skills = skillAbstracts;
    }

    @Override
    public void handledSkill(Upgrade power) {
        power.startAbility(this);
    }

    @Override
    public void stopSkill(Upgrade power) {
        this.setLastUsingSkill(Upgrade.NONE);
        power.stopAbility(this);
    }


    @Override
    public boolean canUseSkill(Upgrade skillAbstract) {
        return this.player.getMainHandItem().isEmpty() && !skillAbstract.name.equals("none");
    }

    @Override
    public Upgrades getPassives() {
        return this.passives;
    }

    @Override
    public Upgrades getHotBarSkill() {
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
                    Upgrade power=this.getSelectSkill();
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

    public void startCasting(Upgrade power, Player player){
        this.setLastUsingSkill(this.getSelectSkill());
        this.handledSkill(power);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=saveChickenEnemyData();
        tag.putBoolean("publicEnemy",this.chickenEnemy);
        this.skills.save(tag);
        this.implantStore.save(tag);
        CompoundTag passiveTag = new CompoundTag();
        this.passives.save(passiveTag);
        tag.put("passives",passiveTag);
        tag.putInt("select_power",this.posSelectUpgrade);


        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.skills = new Upgrades(nbt);
        this.passives = new Upgrades(nbt.getCompound("passives"));
        this.posSelectUpgrade =nbt.getInt("select_power");
        this.chickenEnemy=nbt.getBoolean("publicEnemy");
        this.implantStore = new ImplantStore(nbt);
        this.warningLevel = nbt.getInt("warningLevel");
        this.wave = nbt.getInt("wave");
        this.timeLevelWarning = nbt.getInt("timeLevelWarning");
        this.timeLevelWarning0 = nbt.getInt("timeLevelWarning");

    }

    public void init(Player player) {
        this.setPlayer(player);
        this.level=player.level();
        this.playChickenWarning = false;
    }


    public enum SkillPose{
        NONE,
        CHARGE_CLAWS,
        DASH_CLAWS,
        CHARGE_CANNON,
        STOP_AIMING;
    }

    public enum EntityTypeWaves{
        CHICKEN_COIL(BKEntityType.COIL_CHICKEN.get(),new int[]{1,2,2,3,3,3,4}),
        CHICKEN_BOOM(BKEntityType.BOOM_CHICKEN.get(),new int[]{0,1,1,2,2,2,2}),
        CHICKEN_DRONE(BKEntityType.DRONE_CHICKEN.get(),new int[]{0,0,0,0,1,2,3}),
        CHICKEN_PUNCH(BKEntityType.PUNCH_CHICKEN.get(),new int[]{0,0,0,0,0,0,0});

        public final int[] waves;
        public final EntityType<?> type;
        EntityTypeWaves(EntityType<?> type,int[] waves){
            this.type = type;
            this.waves = waves;
        }
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
