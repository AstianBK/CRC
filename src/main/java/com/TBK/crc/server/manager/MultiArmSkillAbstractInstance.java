package com.TBK.crc.server.manager;

import com.TBK.crc.server.multiarm.MultiArmSkillAbstract;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Objects;

public class MultiArmSkillAbstractInstance implements Comparable<MultiArmSkillAbstractInstance> {
    public static final MultiArmSkillAbstractInstance EMPTY = new MultiArmSkillAbstractInstance(MultiArmSkillAbstract.NONE, 0, false);
    private MutableComponent displayName;
    protected final MultiArmSkillAbstract power;
    protected final int spellLevel;
    protected final boolean locked;

    public MultiArmSkillAbstractInstance(MultiArmSkillAbstract spell, int level, boolean locked) {
        this.power = Objects.requireNonNull(spell);
        this.spellLevel = level;
        this.locked = locked;
    }

    public MultiArmSkillAbstractInstance(MultiArmSkillAbstract spell, int level) {
        this(spell, level, false);
    }



    public MultiArmSkillAbstract getSkillAbstract() {
        return power == null ? MultiArmSkillAbstract.NONE : power;
    }

    public void write(FriendlyByteBuf buf){
        MultiArmSkillAbstract skill=this.getSkillAbstract();
        buf.writeUtf(skill.name);
        buf.writeInt(skill.cd);
        /*buf.writeInt(skill.castingDuration);
        buf.writeInt(skill.lauchTime);
        buf.writeBoolean(skill.instantUse);
        buf.writeBoolean(skill.isTransform);
        buf.writeBoolean(skill.isCasting);
        buf.writeBoolean(skill.isPassive);
        buf.writeInt(skill.costBloodBase);
        buf.writeInt(skill.duration);*/
        buf.writeInt(this.getLevel());
    }
    public MultiArmSkillAbstractInstance read(FriendlyByteBuf buf){
        return new MultiArmSkillAbstractInstance(new MultiArmSkillAbstract(buf),buf.readInt());
    }

    public int getLevel() {
        return spellLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof MultiArmSkillAbstractInstance other) {
            return this.power.equals(other.power) && this.spellLevel == other.spellLevel;
        }

        return false;
    }

    public int hashCode() {
        return 31 * this.power.hashCode() + this.spellLevel;
    }
    public int compareTo(MultiArmSkillAbstractInstance other) {
        int i = this.power.name.compareTo(other.power.name);
        if (i == 0) {
            i = Integer.compare(this.spellLevel, other.spellLevel);
        }
        return i;
    }
}
