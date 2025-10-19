package com.TBK.crc.common.item;

import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.server.upgrade.Upgrade;

public class CyberUpgradeItem extends ItemCyborg{
    public Upgrade skill;

    public CyberUpgradeItem(Properties p_41383_, UpgradeableParts part, Upgrade skill, int tier) {
        super(p_41383_, part, tier);
        this.skill = skill;
    }

    public Upgrade getSkill() {
        return skill;
    }
}
