package com.TBK.crc.server.manager;

import com.TBK.crc.server.upgrade.Upgrade;

public class UpgradeInstance {
    protected final Upgrade power;
    protected final int spellLevel;

    public UpgradeInstance(Upgrade spell, int level) {
        this.power = spell;
        this.spellLevel = level;
    }



    public Upgrade getUpgrade() {
        return power == null ? Upgrade.NONE : power;
    }

}
