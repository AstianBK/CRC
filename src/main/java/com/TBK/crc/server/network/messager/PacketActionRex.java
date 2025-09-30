package com.TBK.crc.server.network.messager;

import com.TBK.crc.CRC;
import com.TBK.crc.server.entity.RexChicken;
import com.TBK.crc.server.entity.RexPart;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketActionRex implements Packet<PacketListener> {
    private final int targetId;

    private final int action;
    private final int rexId;
    public PacketActionRex(FriendlyByteBuf buf) {
        this.targetId =buf.readInt();
        this.rexId = buf.readInt();
        this.action = buf.readInt();
    }

    public PacketActionRex(int rexId, int targetId, int action) {
        this.targetId = targetId;
        this.rexId = rexId;
        this.action = action;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.targetId);
        buf.writeInt(this.rexId);
        buf.writeInt(this.action);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            assert context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;
            handlerAnim();
        });
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void handlerAnim() {
        Minecraft mc=Minecraft.getInstance();
        assert mc.level!=null;
        Entity dragon=mc.level.getEntity(this.rexId);
        Entity target=mc.level.getEntity(this.targetId);
        if(dragon instanceof RexChicken ray && target instanceof LivingEntity){
            ray.setTarget((LivingEntity) target);
        }
        if(dragon instanceof RexChicken ray){
            target = ray.getPartForId(this.targetId);
            if(target instanceof RexPart<?> part){
                part.handleEntityEvent((byte) action);

            }
        }
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }
}
