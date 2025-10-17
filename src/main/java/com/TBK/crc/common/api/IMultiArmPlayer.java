package com.TBK.crc.common.api;

import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import com.TBK.crc.server.multiarm.MultiArmSkillsAbstracts;
import com.TBK.crc.server.multiarm.PassivePart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import java.util.Map;

public interface IMultiArmPlayer extends INBTSerializable<CompoundTag> {
    Player getPlayer();
    void setPlayer(Player player);
    MultiArmSkillAbstract getSelectSkill();
    MultiArmSkillAbstract getSkillForHotBar(int pos);

    MultiArmSkillAbstract getLastUsingSkill();
    void setLastUsingSkill(MultiArmSkillAbstract power);
    void tick(Player player);
    void onJoinGame(Player player, EntityJoinLevelEvent event);
    void handledSkill(MultiArmSkillAbstract power);
    public void stopSkill(MultiArmSkillAbstract power);
    boolean canUseSkill(MultiArmSkillAbstract skillAbstract);
    MultiArmSkillsAbstracts getPassives();
    MultiArmSkillsAbstracts getHotBarSkill();

    void startCasting(Player player);

}
