package com.TBK.crc.common.item;

import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;

public class CyberUpgradeItem extends ItemCyborg{
    public MultiArmSkillAbstract skill;

    public CyberUpgradeItem(Properties p_41383_, UpgradeableParts part, MultiArmSkillAbstract skill, int tier) {
        super(p_41383_, part, tier);
        this.skill = skill;
    }

    public MultiArmSkillAbstract getSkill() {
        return skill;
    }
}
