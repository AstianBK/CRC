package com.TBK.crc.server.upgrade;

import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketHackerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

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
            if(multiArmCapability.getPlayer().isShiftKeyDown() && multiArmCapability.getPlayer().onGround()){
                if(time == 0){
                    this.refreshTarget(multiArmCapability.getPlayer(),level);
                }
                time++;
            }else {
                if(!this.hackingBlock.isEmpty()){
                    clearTargets(multiArmCapability.getPlayer(),level);
                }
                time=0;
            }
        }
    }

    @Override
    public float[] getWindowsColor(MultiArmCapability multiArmCapability) {
        return multiArmCapability.getPlayer().isShiftKeyDown() && multiArmCapability.getPlayer().onGround() ? new float[]{0.0F,0.0F,1.0F,0.5F} : super.getWindowsColor(multiArmCapability);
    }
    public void refreshTarget (Player player,Level level){
        BlockPos origin = player.getOnPos();
        List<BlockPos> list = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(20,5,20),origin.offset(-20,-1,-20))){
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof ChestBlockEntity chest && chest.lootTable!=null){
                list.add(pos.immutable());
            }
        }
        hackingBlock = list;
        PacketHandler.sendToPlayer(new PacketHackerBlock(player.getId(),hackingBlock), (ServerPlayer) player);

    }

    public void clearTargets(Player player,Level level){
        hackingBlock = new ArrayList<>();
        PacketHandler.sendToPlayer(new PacketHackerBlock(player.getId(),hackingBlock), (ServerPlayer) player);
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
