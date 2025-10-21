package com.TBK.crc.server;

import com.TBK.crc.CRC;
import com.TBK.crc.common.registry.BKDimension;
import com.TBK.crc.server.fight.CyberChickenFight;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class StructureData extends SavedData {
	private CyberChickenFight fightManager;
	public CyberChickenFight getCyberChickenFight() {
		if (this.fightManager == null) {
			this.fightManager = new CyberChickenFight();
		}

		return this.fightManager;
	}

	public static StructureData get() {
		DimensionDataStorage manager = CRC.getServer().getLevel(BKDimension.THE_FUTURE_LEVEL).getDataStorage();

		StructureData state = manager.computeIfAbsent(
				StructureData::load,
				StructureData::new,
				CRC.MODID
		);
		state.setDirty();
		return state;
	}

	@Override
	public CompoundTag save(CompoundTag data) {
		data.put("CyberChickenFight",this.getCyberChickenFight().serialise());
		return data;
	}


	public static StructureData load(CompoundTag data) {
		StructureData created = new StructureData();
		created.fightManager = new CyberChickenFight(data.getCompound("CyberChickenFight"));
		CRC.LOGGER.debug("data :"+data);
		return created;
	}

}
