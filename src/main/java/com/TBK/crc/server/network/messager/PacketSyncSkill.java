package com.TBK.crc.server.network.messager;

import com.TBK.crc.common.Util;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.manager.MultiArmSkillAbstractInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class PacketSyncSkill implements Packet<PacketListener> {
    private final Map<Integer, MultiArmSkillAbstractInstance> upgradesPassive;

    private final Map<Integer, MultiArmSkillAbstractInstance> upgrades;


    public PacketSyncSkill(FriendlyByteBuf buf) {
        this.upgrades = buf.readMap(PacketSyncSkill::readPowerID, PacketSyncSkill::readCoolDownInstance);
        this.upgradesPassive = buf.readMap(PacketSyncSkill::readPowerID, PacketSyncSkill::readCoolDownInstance);
    }
    public PacketSyncSkill(Map<Integer, MultiArmSkillAbstractInstance> upgrade,Map<Integer, MultiArmSkillAbstractInstance> passives) {
        this.upgrades = upgrade;
        this.upgradesPassive = passives;
    }
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeMap(upgrades, PacketSyncSkill::writePowerId, PacketSyncSkill::writeCoolDownInstance);
        buf.writeMap(upgradesPassive, PacketSyncSkill::writePowerId, PacketSyncSkill::writeCoolDownInstance);
    }


    public static Integer readPowerID(FriendlyByteBuf buffer) {
        return buffer.readInt();
    }

    public static MultiArmSkillAbstractInstance readCoolDownInstance(FriendlyByteBuf buffer) {
        String name = buffer.readUtf();
        return new MultiArmSkillAbstractInstance(Util.getTypeSkillForName(name), 0);
    }

    public static void writePowerId(FriendlyByteBuf buf, Integer powerId) {
        buf.writeInt(powerId);
    }

    public static void writeCoolDownInstance(FriendlyByteBuf buf, MultiArmSkillAbstractInstance cooldownInstance) {
        buf.writeUtf(cooldownInstance.getSkillAbstract().name);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(this::sync);
        supplier.get().setPacketHandled(true);

    }
    @OnlyIn(Dist.CLIENT)
    public void sync(){
        Minecraft minecraft =Minecraft.getInstance();
        Player player= minecraft.player;
        MultiArmCapability cap = MultiArmCapability.get(player);
        var upgrades = cap.skills;
        var passives = cap.passives;
        upgrades.upgrades.clear();
        passives.upgrades.clear();
        this.upgrades.forEach((k, v) -> {
            upgrades.addMultiArmSkillAbstracts(k,v.getSkillAbstract());
        });
        this.upgradesPassive.forEach((k, v) -> {
            passives.addMultiArmSkillAbstracts(k,v.getSkillAbstract());
        });
    }


    @Override
    public void handle(PacketListener p_131342_) {

    }
}
