package com.TBK.crc.common.api;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import com.TBK.crc.server.multiarm.MultiArmSkillsAbstracts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import java.util.List;
import java.util.Map;

public interface IMultiArmPlayer extends INBTSerializable<CompoundTag> {
    Player getPlayer();
    void setPlayer(Player player);
    MultiArmSkillAbstract getSelectSkill();
    MultiArmSkillAbstract getSkillForHotBar(int pos);
    int getCooldownSkill();
    int getCastingTimer();
    int getStartTime();
    boolean lastUsingSkill();
    MultiArmSkillAbstract getLastUsingSkill();
    void setLastUsingSkill(MultiArmSkillAbstract power);
    void tick(Player player);
    void onJoinGame(Player player, EntityJoinLevelEvent event);
    void handledSkill(MultiArmSkillAbstract power);
    public void stopSkill(MultiArmSkillAbstract power);
    void handledPassive(Player player,MultiArmSkillAbstract power);
    boolean canUseSkill(MultiArmSkillAbstract skillAbstract);
    Map<Integer,MultiArmSkillAbstract> getPassives();
    MultiArmSkillsAbstracts getHotBarSkill();
    void syncSkill(Player player);
    void upSkill();
    void downSkill();
    void startCasting(Player player);

}
