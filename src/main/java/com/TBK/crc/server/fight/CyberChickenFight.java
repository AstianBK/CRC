package com.TBK.crc.server.fight;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKDimension;
import com.TBK.crc.common.registry.BKEntityType;
import com.TBK.crc.server.entity.PortalEntity;
import com.TBK.crc.server.entity.RexChicken;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.*;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class CyberChickenFight{
	private Structure structure;
	private boolean prevCyberChickenDefeat = false;
	private boolean cyberChickenDefeat = false;
	private boolean needsStateScanning = true;
	private int chickenId = -1;
	private int ticksChickenSeen = 0;
	private UUID chickenUUID = null;
	private ServerLevel level;
	private final BlockPos origin;
	private final Predicate<Entity> validPlayer;

	public ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(Component.translatable("entity.crc.rex_chicken"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)).setPlayBossMusic(true).setCreateWorldFog(false);
	private int ticksSinceLastPlayerScan = 0;

	public CyberChickenFight() {
		this.level = CRC.getServer().getLevel(BKDimension.THE_FUTURE_LEVEL);
		this.origin = new BlockPos(0,116,0);
		this.validPlayer = EntitySelector.ENTITY_STILL_ALIVE.and(EntitySelector.withinDistance((double)origin.getX(), (double)(128 + origin.getY()), (double)origin.getZ(), 192.0D));
		this.structure = new Structure();
	}
	public CyberChickenFight(CompoundTag data) {
		this.deserialise(data);
		this.level = CRC.getServer().getLevel(BKDimension.THE_FUTURE_LEVEL);
		this.origin = new BlockPos(0,140,0);
		this.validPlayer = EntitySelector.ENTITY_STILL_ALIVE.and(EntitySelector.withinDistance((double)origin.getX(), (double)(128 + origin.getY()), (double)origin.getZ(), 192.0D));

	}
	public void setDragonKilled(RexChicken p_64086_) {
		if (p_64086_.getUUID().equals(this.chickenUUID)) {
			this.bossEvent.setProgress(0.0F);
			this.bossEvent.setVisible(false);
			this.spawnExitPortal(true);

			this.prevCyberChickenDefeat = true;
			this.cyberChickenDefeat = true;
		}

	}
	public void tick(){
		this.bossEvent.setVisible(!cyberChickenDefeat);
		if (++this.ticksSinceLastPlayerScan >= 20) {
			this.updatePlayers();
			this.ticksSinceLastPlayerScan = 0;
		}
		if (!this.bossEvent.getPlayers().isEmpty()) {
			this.level.getChunkSource().addRegionTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
			boolean flag = this.isArenaLoaded();
			if (this.needsStateScanning && flag) {
				this.scanState();
				this.needsStateScanning = false;
			}


			if (!this.cyberChickenDefeat) {
				if ((this.chickenUUID == null || ++this.ticksChickenSeen >= 1200) && flag) {
					this.findOrCreateChicken();
					this.ticksChickenSeen = 0;
				}

			}
		} else {
			this.level.getChunkSource().removeRegionTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
		}
	}

	private boolean isArenaLoaded() {
		ChunkPos chunkpos = new ChunkPos(this.origin);

		for(int i = -8 + chunkpos.x; i <= 8 + chunkpos.x; ++i) {
			for(int j = 8 + chunkpos.z; j <= 8 + chunkpos.z; ++j) {
				ChunkAccess chunkaccess = this.level.getChunk(i, j, ChunkStatus.FULL, false);
				if (!(chunkaccess instanceof LevelChunk)) {
					return false;
				}

				FullChunkStatus fullchunkstatus = ((LevelChunk)chunkaccess).getFullStatus();
				if (!fullchunkstatus.isOrAfter(FullChunkStatus.BLOCK_TICKING)) {
					return false;
				}
			}
		}

		return true;
	}
	private void findOrCreateChicken() {
		List<? extends RexChicken> list = Util.getRexChickens(level);
		if (list.isEmpty()) {
			this.createNewRex();
		} else {
			this.chickenUUID = list.get(0).getUUID();
		}
	}

	@javax.annotation.Nullable
	private RexChicken createNewRex() {
		this.level.getChunkAt(new BlockPos(this.origin.getX(), 128 + this.origin.getY(), this.origin.getZ()));
		RexChicken rex = BKEntityType.REX_CHICKEN.get().create(this.level);
		if (rex != null) {
			rex.moveTo((double)this.origin.getX(), (double)(this.origin.getY()), (double)this.origin.getZ(), this.level.random.nextFloat() * 360.0F, 0.0F);
			rex.setShieldAmount(50);
			this.level.addFreshEntity(rex);
			this.chickenUUID = rex.getUUID();
		}

		return rex;
	}

	private void updatePlayers() {
		Set<ServerPlayer> set = Sets.newHashSet();

		for(ServerPlayer serverplayer : this.level.getPlayers(this.validPlayer)) {
			this.bossEvent.addPlayer(serverplayer);
			set.add(serverplayer);
		}

		Set<ServerPlayer> set1 = Sets.newHashSet(this.bossEvent.getPlayers());
		set1.removeAll(set);

		for(ServerPlayer serverplayer1 : set1) {
			this.bossEvent.removePlayer(serverplayer1);
		}
	}

	public void updateDragon(RexChicken p_64097_) {
		if (p_64097_.getUUID().equals(this.chickenUUID)) {
			this.bossEvent.setProgress(p_64097_.getHealth() / p_64097_.getMaxHealth());
			this.ticksChickenSeen = 0;
			if (p_64097_.hasCustomName()) {
				this.bossEvent.setName(p_64097_.getDisplayName());
			}
		}

	}


	private void scanState() {
		List<? extends RexChicken> list = Util.getRexChickens(level);
		if (list.isEmpty()) {
			this.cyberChickenDefeat = true;
		} else {
			RexChicken enderdragon = list.get(0);
			this.chickenUUID = enderdragon.getUUID();

			this.cyberChickenDefeat = false;
		}

		if (!this.prevCyberChickenDefeat && this.cyberChickenDefeat) {
			this.cyberChickenDefeat = false;
		}

	}

	private void spawnExitPortal(boolean p_64094_) {
		List<PortalEntity> portals = level.getEntitiesOfClass(PortalEntity.class,new AABB(this.origin).inflate(10.0F));
		if (portals.isEmpty()){
			PortalEntity portal = new PortalEntity(BKEntityType.PORTAL.get(),level);
			portal.setPos(this.origin.getCenter());
			level.addFreshEntity(portal);
		}
	}
	public CompoundTag serialise() {
		CompoundTag data = new CompoundTag();

		data.put("Structure", this.structure.serialise());
		data.putBoolean("prevChickenDefeat",this.prevCyberChickenDefeat);
		if(!this.prevCyberChickenDefeat && this.chickenUUID!=null){
			data.putUUID("chickenUUID",this.chickenUUID);
		}
		return data;
	}

	private boolean hasActiveExitPortal() {

		return false;
	}
	public void deserialise(CompoundTag data) {

		this.structure = new Structure(data.getCompound("Structure"));
		if(!data.getBoolean("prevChickenDefeat")){
			if (data.contains("chickenUUID")){
				this.chickenUUID = data.getUUID("chickenUUID");
			}
			this.prevCyberChickenDefeat = false;
			this.cyberChickenDefeat = false;
		}else {
			this.prevCyberChickenDefeat = true;
			this.cyberChickenDefeat = true;
		}
	}


	public static ServerLevel getDimension() {
		return CRC.getServer().getLevel(BKDimension.THE_FUTURE_LEVEL);
	}

	public static ServerLevel getDimensionReturn() {
		return CRC.getServer().getLevel(Level.OVERWORLD);
	}

	public Structure getStructure() {
		if (this.structure == null) {
			CRC.LOGGER.warn("Missing realm structures! Creating..");
			this.structure = new Structure();
		}

		return this.structure;
	}

	public void teleport(LivingEntity entity) {
		Vec3 vec = getStructure().getCentre().getCenter();
		if(Util.isInFuture(entity)){
			entity.teleportTo(getDimensionReturn(), vec.x,vec.y,vec.z, new HashSet<>(),entity.getYRot(), entity.getXRot());
		}else {
			entity.teleportTo(getDimension(), vec.x	,vec.y,vec.z, new HashSet<>(),entity.getYRot(), entity.getXRot());
		}
	}

	public static class Structure {
		private boolean isPlaced;
		private BlockPos centre;
		public Structure(@Nullable BlockPos centre) {
			this.isPlaced = false;
			this.centre = centre;
		}
		public Structure() {
			this(new BlockPos(0, 143, 0));
		}
		public Structure(CompoundTag data) {
			this.deserialise(data);
		}

		public CompoundTag serialise() {
			CompoundTag data = new CompoundTag();

			data.putBoolean("isPlaced", this.isPlaced);
			if (this.centre != null)
				data.put("Centre", NbtUtils.writeBlockPos(this.centre));

			return data;
		}

		public void deserialise(CompoundTag data) {
			this.isPlaced = data.getBoolean("isPlaced");

			if (data.contains("Centre")) {
				this.centre = NbtUtils.readBlockPos(data.getCompound("Centre"));
			}
		}

		public boolean isPlaced() {
			return this.isPlaced;
		}


		private Optional<StructureTemplate> findStructure(ResourceLocation structure) {
			return CRC.getServer().getStructureManager().get(structure);
		}

		/**
		 * places the structures if it is not already placed
		 */
		public void verify() {
			if (!this.isPlaced()) {
				this.place();
			}
		}

		private void place() {
			this.place(getDimension());
		}

		private void place(ServerLevel level) {
			if(level==null)return;
			for (ServerPlayer p : level.getServer().getPlayerList().getPlayers()) {
				p.sendSystemMessage(Component.literal("Please wait while the structures is placed..."));
			}

			long start = System.currentTimeMillis();


			if (this.isPlaced()) {
				CRC.LOGGER.warn("Tried to place realm structures twice");
			}

			StructurePlaceSettings settings=new StructurePlaceSettings();
			this.placeComponent(start,level,-42,111,38,new ResourceLocation(CRC.MODID, "arena_derecha0"), settings);
			this.placeComponent(start,level,-42,111,0,new ResourceLocation(CRC.MODID, "arena_derecha1"), settings);
			this.placeComponent(start,level,-42,111,-42,new ResourceLocation(CRC.MODID, "arena_derecha2"), settings);
			this.placeComponent(start,level,31,111,-42,new ResourceLocation(CRC.MODID, "arena_izquierda0"), settings);
			this.placeComponent(start,level,31,111,0,new ResourceLocation(CRC.MODID, "arena_izquierda1"), settings);
			this.placeComponent(start,level,31,111,38,new ResourceLocation(CRC.MODID, "arena_izquierda2"), settings);
			this.placeComponent(start,level,0,111,38,new ResourceLocation(CRC.MODID, "arena_medio0"), settings);
			this.placeComponent(start,level,0,111,0,new ResourceLocation(CRC.MODID, "arena_medio1"), settings);
			this.placeComponent(start,level,0,111,-42,new ResourceLocation(CRC.MODID, "arena_medio2"), settings);



			this.isPlaced = true;
		}

		public void placeComponent(long start,ServerLevel level,int addX,int height,int addZ,ResourceLocation location,StructurePlaceSettings settings){
			StructureTemplate component = this.findStructure(location).orElse(null);

			if (component == null) {
				CRC.LOGGER.error("Could not find realm component :" + location.toString());
				return;
			}
			Vec3i size = component.getSize();
			BlockPos offset = new BlockPos(-size.getX() / 2+addX, height, -size.getZ() / 2 +addZ);


			component.placeInWorld(
					level,
					offset,
					offset,
					settings,
					level.getRandom(),
					Block.UPDATE_KNOWN_SHAPE
			);
			CRC.LOGGER.info("Placed " + this + " at " + offset + " in " + (System.currentTimeMillis() - start) + "ms");
		}


		public BlockPos getCentre() {
			this.verify();

			if (this.centre != null) return this.centre;

			this.centre = new BlockPos(0, 114, 0);

			CRC.LOGGER.info("Placed " + this +"ms");
			return this.centre;
		}

		@Override
		public String toString() {
			return "RealmStructure{" +
					", isPlaced=" + isPlaced +
					", centre=" + centre +
					'}';
		}
	}
}
