package com.TBK.crc.common.block;

import com.TBK.crc.common.block_entity.CyborgTableEntity;
import com.TBK.crc.common.menu.CyborgTableMenu;
import com.TBK.crc.server.capability.MultiArmCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class CyborgTableBlock extends BedBlock {

    public CyborgTableBlock() {
        super(net.minecraft.world.item.DyeColor.BLACK,
                Properties.of()
                        .mapColor(MapColor.COLOR_BLACK).strength(2.0f).noOcclusion().pushReaction(PushReaction.DESTROY)
        );

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH).setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false));
    }





    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CyborgTableEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        if (!level.isClientSide) {
            start(pos,player);
            player.playSound(SoundEvents.WOOD_PLACE, 1.0F, 0.8F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void start(BlockPos p_21141_,Player player) {
        MultiArmCapability cap = MultiArmCapability.get(player);
        if (cap==null)return;
        if (player.isPassenger()) {
            player.stopRiding();
        }
        if(player.level().isClientSide){
            Minecraft.getInstance().setScreen(null);
        }
        if(!player.level().isClientSide){
            NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider((id, inventory, player1)->{

                return new CyborgTableMenu(id,inventory,new SimpleContainer(6),new SimpleContainerData(1),cap.implantStore.store);
            },Component.literal("")),p_21141_);
        }


        player.setPose(Pose.SLEEPING);
        player.setPosToBed(p_21141_);
        player.setSleepingPos(p_21141_);
        player.setDeltaMovement(Vec3.ZERO);
        player.hasImpulse = true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        return super.getStateForPlacement(context);
    }
}
