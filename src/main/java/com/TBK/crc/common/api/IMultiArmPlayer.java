package com.TBK.crc.common.api;

import com.TBK.crc.server.upgrade.Upgrade;
import com.TBK.crc.server.upgrade.Upgrades;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

public interface IMultiArmPlayer extends INBTSerializable<CompoundTag> {
    Player getPlayer();
    void setPlayer(Player player);
    Upgrade getSelectSkill();
    Upgrade getSkillForHotBar(int pos);

    Upgrade getLastUsingSkill();
    void setLastUsingSkill(Upgrade power);
    void tick(Player player);
    void onJoinGame(Player player, EntityJoinLevelEvent event);
    void handledSkill(Upgrade power);
    public void stopSkill(Upgrade power);
    boolean canUseSkill(Upgrade skillAbstract);
    Upgrades getPassives();
    Upgrades getHotBarSkill();

    void startCasting(Player player);

}
