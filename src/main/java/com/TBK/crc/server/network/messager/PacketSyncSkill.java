package com.TBK.crc.server.network.messager;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.manager.UpgradeInstance;
import com.TBK.crc.server.upgrade.Upgrade;
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
    private final Map<Integer, UpgradeInstance> upgradesPassive;

    private final Map<Integer, UpgradeInstance> upgrades;


    public PacketSyncSkill(FriendlyByteBuf buf) {
        this.upgrades = buf.readMap(PacketSyncSkill::readUpgradeIndex, PacketSyncSkill::readUpgradeInstance);
        this.upgradesPassive = buf.readMap(PacketSyncSkill::readUpgradeIndex, PacketSyncSkill::readUpgradeInstance);
    }
    public PacketSyncSkill(Map<Integer, UpgradeInstance> upgrade, Map<Integer, UpgradeInstance> passives) {
        this.upgrades = upgrade;
        this.upgradesPassive = passives;
    }
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeMap(upgrades, PacketSyncSkill::writePowerId, PacketSyncSkill::writeCoolDownInstance);
        buf.writeMap(upgradesPassive, PacketSyncSkill::writePowerId, PacketSyncSkill::writeCoolDownInstance);
    }


    public static Integer readUpgradeIndex(FriendlyByteBuf buffer) {
        return buffer.readInt();
    }

    public static UpgradeInstance readUpgradeInstance(FriendlyByteBuf buffer) {
        String name = buffer.readUtf();
        Upgrade upgrade=Util.getTypeSkillForName(name);
        upgrade.refinements = buffer.readList(FriendlyByteBuf::readUtf);
        return new UpgradeInstance(upgrade, 0);
    }

    public static void writePowerId(FriendlyByteBuf buf, Integer powerId) {
        buf.writeInt(powerId);
    }

    public static void writeCoolDownInstance(FriendlyByteBuf buf, UpgradeInstance cooldownInstance) {
        buf.writeUtf(cooldownInstance.getUpgrade().name);
        buf.writeCollection(cooldownInstance.getUpgrade().refinements,FriendlyByteBuf::writeUtf);
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
            upgrades.addMultiArmSkillAbstracts(k,v.getUpgrade());
        });

        this.upgradesPassive.forEach((k, v) -> {
            passives.addMultiArmSkillAbstracts(k,v.getUpgrade());
        });
    }


    @Override
    public void handle(PacketListener p_131342_) {

    }
}
