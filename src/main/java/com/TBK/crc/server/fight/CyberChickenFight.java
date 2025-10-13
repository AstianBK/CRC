package com.TBK.crc.server.fight;

import com.TBK.crc.CRC;
import com.TBK.crc.common.registry.BKDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.*;

public class CyberChickenFight{
	private Structure structure;

	public CyberChickenFight() {}
	public CyberChickenFight(CompoundTag data) {
		this.deserialise(data);
	}

	public CompoundTag serialise() {
		CompoundTag data = new CompoundTag();

		data.put("Structure", this.getStructure().serialise());

		return data;
	}

	public void deserialise(CompoundTag data) {
		this.structure = new Structure(data.getCompound("Structure"));
	}


	public static ServerLevel getDimension() {
		return CRC.getServer().getLevel(BKDimension.THE_FUTURE_LEVEL);
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
		entity.teleportTo(getDimension(), vec.x	,vec.y,vec.z, new HashSet<>(),entity.getYRot(), entity.getXRot());
	}

	public static class Structure {
		private final ResourceLocation structure;
		private boolean isPlaced;
		private BlockPos centre;
		public Structure(ResourceLocation structure, @Nullable BlockPos centre) {
			this.structure = structure;
			this.isPlaced = false;
			this.centre = centre;
		}
		public Structure() {
			this(getDefaultStructure(), new BlockPos(0, 143, 0)); // default island structures
		}
		public Structure(CompoundTag data) {
			this.structure = new ResourceLocation(data.getString("Structure"));

			this.deserialise(data);
		}

		public CompoundTag serialise() {
			CompoundTag data = new CompoundTag();

			data.putString("Structure", this.structure.toString());

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

		private static ResourceLocation getDefaultStructure() {
			return new ResourceLocation(CRC.MODID, "temple");
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
			this.place(getDimension(), true);
		}

		private void place(ServerLevel level, boolean inform) {
			if(level==null)return;
			if (inform && level!=null) {
				for (ServerPlayer p : level.getServer().getPlayerList().getPlayers()) {
					p.sendSystemMessage(Component.literal("Please wait while the structures is placed..."));
				}

			}

			long start = System.currentTimeMillis();


			if (this.isPlaced()) {
				CRC.LOGGER.warn("Tried to place realm structures twice");
			}

			StructurePlaceSettings settings=new StructurePlaceSettings();
			this.placeComponent(start,level,-42,111,38,new ResourceLocation(CRC.MODID, "arena_derecha0"), settings);
			this.placeComponent(start,level,-42,111,0,new ResourceLocation(CRC.MODID, "arena_derecha1"), settings);
			this.placeComponent(start,level,-42,111,-42,new ResourceLocation(CRC.MODID, "arena_derecha2"), settings);
			this.placeComponent(start,level,31,111,-43,new ResourceLocation(CRC.MODID, "arena_izquierda0"), settings);
			this.placeComponent(start,level,31,111,0,new ResourceLocation(CRC.MODID, "arena_izquierda1"), settings);
			this.placeComponent(start,level,31,111,38,new ResourceLocation(CRC.MODID, "arena_izquierda2"), settings);
			this.placeComponent(start,level,0,111,38,new ResourceLocation(CRC.MODID, "arena_medio0"), settings);
			this.placeComponent(start,level,0,111,0,new ResourceLocation(CRC.MODID, "arena_medio1"), settings);
			this.placeComponent(start,level,0,111,-43,new ResourceLocation(CRC.MODID, "arena_medio2"), settings);


			//placeColumn(level);


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

			this.centre = new BlockPos(0, 112, 0);

			CRC.LOGGER.info("Placed " + this +"ms");
			return this.centre;
		}

		@Override
		public String toString() {
			return "RealmStructure{" +
					"structures=" + structure+
					", isPlaced=" + isPlaced +
					", centre=" + centre +
					'}';
		}
	}
}
