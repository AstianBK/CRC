package com.TBK.crc.server;

import com.TBK.crc.CRC;
import com.TBK.crc.server.fight.CyberChickenFight;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class StructureData extends SavedData {
	private CyberChickenFight realmManager;
	public CyberChickenFight getCyberChickenFight() {
		if (this.realmManager == null) {
			this.realmManager = new CyberChickenFight();
		}

		return this.realmManager;
	}

	public static StructureData get() {
		DimensionDataStorage manager = CRC.getServer().getLevel(Level.OVERWORLD).getDataStorage();

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
		created.realmManager = new CyberChickenFight(data);
		return created;
	}

}
