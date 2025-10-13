package com.TBK.crc.server.multiarm;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketHackerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class HackerEye extends PassivePart{
    public List<BlockPos> hackingBlock = new ArrayList<>();
    public int time = 0;
    public HackerEye() {
        super("hacker_eye", false);
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);

        Level level = multiArmCapability.getPlayer().level();
        if(!level.isClientSide){
            if(multiArmCapability.getPlayer().isShiftKeyDown()){
                if(time == 0){
                    BlockPos origin = multiArmCapability.getPlayer().getOnPos();
                    List<BlockPos> list = new ArrayList<>();
                    for (BlockPos pos : BlockPos.betweenClosed(origin.offset(20,5,20),origin.offset(-20,-1,-20))){
                        BlockEntity entity = level.getBlockEntity(pos);
                        if(entity instanceof ChestBlockEntity chest && chest.lootTable!=null){
                            list.add(pos.immutable());
                        }
                    }
                    hackingBlock = list;
                    CRC.LOGGER.debug("Localizado :"+hackingBlock);
                    PacketHandler.sendToPlayer(new PacketHackerBlock(multiArmCapability.getPlayer().getId(),hackingBlock), (ServerPlayer) multiArmCapability.getPlayer());
                }
                time++;
            }else {
                time=0;
            }

        }
    }

    @Override
    public boolean canActive(MultiArmCapability multiArmCapability) {
        return true;
    }

    @Override
    public boolean handlerPassive(MultiArmCapability multiArmCapability, Entity source) {
        return false;
    }
}
