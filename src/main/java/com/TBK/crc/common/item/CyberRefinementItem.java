package com.TBK.crc.common.item;

import com.TBK.crc.UpgradeableParts;
import com.TBK.crc.server.upgrade.Upgrade;

public class CyberRefinementItem extends ItemCyborg{
    public Upgrade skill;
    public String name;

    public CyberRefinementItem(Properties p_41383_, Upgrade skill, String nameRefinement) {
        super(p_41383_, UpgradeableParts.REFINEMENT,0);
        this.skill = skill;
        this.name = nameRefinement;
    }

    public Upgrade getSkill() {
        return skill;
    }
    public String getName(){
        return name;
    }
}
