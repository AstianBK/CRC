package com.TBK.crc.server.network.messager;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.multiarm.HackerEye;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketHackerBlock implements Packet<PacketListener> {
    public final int id;
    public List<BlockPos> block;
    public PacketHackerBlock(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.block = buf.readList(FriendlyByteBuf::readBlockPos);
    }

    public PacketHackerBlock(int id, List<BlockPos> blockPosList) {
        this.id = id;
        this.block = blockPosList;
    }


    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeCollection(this.block,FriendlyByteBuf::writeBlockPos);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            assert context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;
            Player player = (Player) Minecraft.getInstance().level.getEntity(this.id);
            MultiArmCapability cap = MultiArmCapability.get(player);
            if(cap!=null){
                ((HackerEye)cap.passives.getForName("hacker_eye")).hackingBlock = this.block;
                CRC.LOGGER.debug("block :"+((HackerEye)cap.passives.getForName("hacker_eye")).hackingBlock);
            }

        });
        context.get().setPacketHandled(true);
    }



    @Override
    public void handle(PacketListener p_131342_) {

    }
}
