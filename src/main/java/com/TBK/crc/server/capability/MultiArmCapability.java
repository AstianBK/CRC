package com.TBK.crc.server.capability;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.api.IMultiArmPlayer;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.server.entity.TeleportEntity;
import com.TBK.crc.server.manager.*;
import com.TBK.crc.server.multiarm.*;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketAddSkill;
import com.TBK.crc.server.network.messager.PacketHandlerPowers;
import com.TBK.crc.server.network.messager.PacketSyncPosHotBar;
import com.TBK.crc.server.network.messager.PacketSyncSkill;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
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

import java.util.HashMap;
import java.util.Map;


public class MultiArmCapability implements IMultiArmPlayer {
    public MultiArmSkillAbstract lastUsingMultiArmSkillAbstract=MultiArmSkillAbstract.NONE;
    public boolean chickenEnemy = false;
    Player player;
    Level level;
    int posSelectMultiArmSkillAbstract=0;
    int castingTimer=0;
    int castingClientTimer=0;
    int maxCastingClientTimer=0;
    public boolean isTransform=false;
    public boolean hotbarActive = false;
    public int cooldownReUse = 0;
    public int invokeTimer = 0;
    public int warningLevel = 0;
    public Entity catchEntity = null;
    public PlayerCooldowns cooldowns=new PlayerCooldowns();
    public ActiveEffectDuration durationEffect=new ActiveEffectDuration();
    public MultiArmSkillsAbstracts skills = new MultiArmSkillsAbstracts(new HashMap<>());
    public ImplantStore implantStore = new ImplantStore();
    public EntityType<?>[] types = new EntityType[]{
            BKEntityType.CYBORG_ROBOT_CHICKEN.get(),BKEntityType.BOOM_CHICKEN.get()
    };
    public boolean dirty = false;
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

    public void setIsTransform(boolean isTransform){
        this.isTransform=isTransform;
    }


    public boolean isCatchedTarget(Entity entity){
        return this.catchEntity == entity;
    }
    @Override
    public MultiArmSkillAbstract getSelectSkill() {
        return this.getHotBarSkill().get(this.posSelectMultiArmSkillAbstract);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public MultiArmSkillAbstract getSkillForHotBar(int pos) {
        return this.skills.get(pos);
    }

    @Override
    public int getCooldownSkill() {
        return 0;
    }

    @Override
    public int getCastingTimer() {
        return this.castingTimer;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getCastingClientTimer() {
        return this.castingClientTimer;
    }

    @OnlyIn(Dist.CLIENT)
    public int getMaxCastingClientTimer() {
        return this.maxCastingClientTimer;
    }

    @Override
    public int getStartTime() {
        return 0;
    }

    @Override
    public boolean lastUsingSkill() {
        return false;
    }

    @Override
    public MultiArmSkillAbstract getLastUsingSkill() {
        return this.lastUsingMultiArmSkillAbstract;
    }

    @Override
    public void setLastUsingSkill(MultiArmSkillAbstract power) {
        this.lastUsingMultiArmSkillAbstract=power;
    }

    @Override
    public void tick(Player player) {
        if(this.chickenEnemy){
            if(this.invokeTimer<=0){

                for (int i = 0 ; i < this.warningLevel ; i++){
                    TeleportEntity tp = new TeleportEntity(level,types[this.level.random.nextInt(0,2)],findRandomSurfaceNear(this.player,10,level.random),player);
                    this.level.addFreshEntity(tp);
                }
                this.invokeTimer = 100;
                this.warningLevel = Math.min(this.warningLevel+1,5);
            }else {
                this.invokeTimer--;
            }
        }
        if(Util.hasMultiArm(this)){

            if (this.cooldownReUse>0){
                this.cooldownReUse--;
            }
            if(player instanceof ServerPlayer){
                if(this.dirty){
                    PacketHandler.sendToPlayer(new PacketSyncSkill(this.skills.powers), (ServerPlayer) player);
                    this.dirty = false;
                }
                if (this.cooldowns.hasCooldownsActive()){
                    this.cooldowns.tick(1);
                }
                if(this.durationEffect.hasDurationsActive()){
                    this.durationEffect.tick(1);
                }
                PacketHandler.sendToPlayer(new PacketSyncPosHotBar(this.posSelectMultiArmSkillAbstract), (ServerPlayer) player);
            }else if(this.level.isClientSide){
                if (this.cooldowns.hasCooldownsActive()){
                    this.cooldowns.tick(1);
                }

            }
            if(!this.skills.getSkills().isEmpty()){
                this.skills.getSkills().forEach(e->{
                    if(this.durationEffect.hasDurationForSkill(e.getSkillAbstract()) || e.getSkillAbstract().canReactive){
                        e.getSkillAbstract().tick(this);
                        this.durationEffect.decrementDurationCount(e.getSkillAbstract());
                    }
                });
            }
            /*if(!this.passives.getSkills().isEmpty()){
                this.passives.getSkills().forEach(e->{
                    e.getMultiArmSkillAbstract().tick(this);
                });
            }*/
            if(this.level.isClientSide){
                if(this.castingClientTimer>0){
                    this.castingClientTimer--;
                }
            }else {
                if (this.isCasting()){
                    this.castingTimer--;
                }
            }
        }
    }

    public static BlockPos findRandomSurfaceNear(Entity entity, int radius, RandomSource random) {
        Level level = entity.level();
        BlockPos origin = entity.blockPosition();

        for (int i = 0; i < 20; i++) {
            int dx = random.nextInt(-radius, radius + 1);
            int dz = random.nextInt(-radius, radius + 1);

            BlockPos pos = origin.offset(dx, 0, dz);

            int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
            BlockPos surfacePos = new BlockPos(pos.getX(), surfaceY, pos.getZ());

            if (level.isEmptyBlock(surfacePos) && level.isEmptyBlock(surfacePos.above())) {
                return surfacePos;
            }
        }
        return origin;
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
        if(this.level.isClientSide){
            PacketHandler.sendToServer(new PacketHandlerPowers(3,player, player));
        }
    }
    public void addNewAbility(MultiArmSkillAbstract skill){
        MultiArmSkillsAbstracts map = new MultiArmSkillsAbstracts(new HashMap<>());
        int indexForUpgrade = Util.getIndexForName(skill.name);
        if(indexForUpgrade !=-1){
            map.addMultiArmSkillAbstracts(indexForUpgrade,skill);
            setSetHotbar(map);
            setPosSelectMultiArmSkillAbstract(indexForUpgrade);
            if(this.level.isClientSide){
                PacketHandler.sendToServer(new PacketAddSkill(skill.name,indexForUpgrade));
            }
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
        this.castingClientTimer = 0;
        this.castingTimer = 0;
        this.maxCastingClientTimer = 0;
        this.syncSkill(this.getPlayer());
    }

    @Override
    public void handledPassive(Player player, MultiArmSkillAbstract power) {

    }

    @Override
    public boolean canUseSkill(MultiArmSkillAbstract skillAbstract) {
        return !this.getCooldowns().isOnCooldown(skillAbstract) && !this.isCasting() && this.player.getMainHandItem().isEmpty();
    }

    @Override
    public Map<Integer, MultiArmSkillAbstract> getPassives() {
        return null;
    }

    @Override
    public MultiArmSkillsAbstracts getHotBarSkill() {
        return this.skills;
    }


    @Override
    public void syncSkill(Player player) {
        if(player instanceof ServerPlayer serverPlayer){
            this.cooldowns.syncToPlayer(serverPlayer);
            this.durationEffect.syncAllToPlayer();

        }
    }
    public void syncPos(int pos,Player player){
        PacketHandler.sendToAllTracking(new PacketSyncPosHotBar(pos),player);
    }

    @Override
    public void upSkill() {

    }

    @Override
    public void downSkill() {

    }

    @Override
    public void startCasting(Player player) {
        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketHandlerPowers(0,player, player), (ServerPlayer) player);
        }

        if(this.canUseSkill(this.getSelectSkill())){

            boolean skillActive=this.durationEffect.hasDurationForSkill(this.getSelectSkill());
            if(this.getSelectSkill().isCasting){

                this.startCasting(this.getSelectSkill(),this.player);
                this.castingTimer = 20;
                this.castingClientTimer = 20;
                this.maxCastingClientTimer = 20;
            }else {
                if((!skillActive || this.getSelectSkill().isCanReActive())){
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
        boolean skillActive=this.durationEffect.hasDurationForSkill(this.getLastUsingSkill());
        if (this.isCasting()){
            if(skillActive){
                MultiArmSkillAbstract skill=this.getLastUsingSkill();
                DurationInstance instance=this.durationEffect.getDurationInstance(skill.name);
                this.removeActiveEffect(instance,DurationResult.CLICKUP);
            }
        }
    }

    public void startCasting(MultiArmSkillAbstract power,Player player){
        DurationInstance instance=new DurationInstance(power.name,0,20,200);
        this.addActiveEffect(instance,player);
        this.setLastUsingSkill(this.getSelectSkill());
        this.handledSkill(power);
    }

    public void removeActiveEffect(DurationInstance instance){
        this.durationEffect.removeDuration(instance, DurationResult.TIMEOUT,true);
    }

    public void removeActiveEffect(DurationInstance instance,DurationResult result){
        this.durationEffect.removeDuration(instance,result,true);
    }

    public void addActiveEffect(DurationInstance instance, Player player){
        this.durationEffect.addDuration(instance,this);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        tag.putBoolean("publicEnemy",this.chickenEnemy);
        this.skills.save(tag);
        CRC.LOGGER.debug("implant :"+this.implantStore);
        this.implantStore.save(tag);
        tag.putInt("select_power",this.posSelectMultiArmSkillAbstract);
        if(this.cooldowns.hasCooldownsActive()){
            tag.put("cooldowns",this.cooldowns.saveNBTData());
        }
        if(this.durationEffect.hasDurationsActive()){
            tag.put("activeEffect",this.durationEffect.saveNBTData());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.skills = new MultiArmSkillsAbstracts(nbt);
        this.posSelectMultiArmSkillAbstract=nbt.getInt("select_power");
        this.chickenEnemy=nbt.getBoolean("publicEnemy");
        CRC.LOGGER.debug("nbt :"+nbt);
        this.implantStore = new ImplantStore(nbt);
        CRC.LOGGER.debug("store :"+this.implantStore);
        if(nbt.contains("cooldowns")){
            ListTag listTag=nbt.getList("cooldowns",10);
            this.cooldowns.loadNBTData(listTag);
        }
        if(nbt.contains("activeEffect")){
            ListTag listTag=nbt.getList("activeEffect",10);
            this.durationEffect.loadNBTData(listTag);
        }
    }

    public void init(Player player) {
        this.setPlayer(player);
        this.level=player.level();
        if(player instanceof ServerPlayer serverPlayer){
            this.durationEffect=new ActiveEffectDuration(serverPlayer);
        }
    }

    public PlayerCooldowns getCooldowns() {
        return this.cooldowns;
    }
    public ActiveEffectDuration getActiveEffectDuration(){
        return this.durationEffect;
    }
    public void setActiveEffectDuration(ActiveEffectDuration activeEffectDuration){
        this.durationEffect=activeEffectDuration;
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
