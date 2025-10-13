package com.TBK.crc.server.network.messager;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.menu.CyborgTableMenu;
import com.TBK.crc.common.screen.CyborgTableScreen;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.manager.MultiArmSkillAbstractInstance;
import com.TBK.crc.server.multiarm.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAddSkill implements Packet<PacketListener> {

    public final String name;
    public final int index;
    public final int id;
    public PacketAddSkill(FriendlyByteBuf buf) {
        this.name = buf.readUtf();
        this.index = buf.readInt();
        this.id = buf.readInt();
    }

    public PacketAddSkill(String name,int index,int id) {
        this.name = name;
        this.index = index;
        this.id = id;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
        buf.writeInt(this.index);
        buf.writeInt(this.id);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            assert context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER;
            MultiArmCapability cap = MultiArmCapability.get(context.get().getSender());

            if(cap!=null){
                switch (this.id){
                    case 0 -> {
                        cap.getHotBarSkill().addMultiArmSkillAbstracts(this.index, Util.getTypeSkillForName(this.name));
                    }
                    case 1 -> {
                        cap.passives.addMultiArmSkillAbstracts(this.index,Util.getTypeSkillForName(this.name));
                    }

                }
            }
        });
        context.get().setPacketHandled(true);
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }
}
