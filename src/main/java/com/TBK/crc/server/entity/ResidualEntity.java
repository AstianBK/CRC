package com.TBK.crc.server.entity;

import com.TBK.crc.CRC;
import com.TBK.crc.common.registry.BKEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;

import java.util.Optional;

public class ResidualEntity extends Entity {
    private static final EntityDataAccessor<Optional<BlockPos>> ORIGEN = SynchedEntityData.defineId(ResidualEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Optional<BlockPos>> END = SynchedEntityData.defineId(ResidualEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);


    public int delayTime = 0;
    public int delayTime0 = 0;
    public int residualTime = 0;
    public int residualTime0 = 0;
    public boolean clientDiscard = false;
    public ResidualEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public ResidualEntity(Level p_19871_, BlockPos end, BlockPos start,int delay) {
        super(BKEntityType.RESIDUAL.get(), p_19871_);
        this.setEnd(end);
        this.setOrigen(start);
        this.residualTime=20;
        this.residualTime0=20;
        this.delayTime = delay;
        this.delayTime0 = delay;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ORIGEN,Optional.empty());
        this.entityData.define(END,Optional.empty());

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }
    public float getDelayTime(float partialTick){
        return Mth.lerp(partialTick,this.delayTime0,this.delayTime);
    }
    public float getResidualTime(float partialTick){
        return Mth.lerp(partialTick,this.residualTime0,this.residualTime);
    }

    public BlockPos getOrigen() {
        return this.entityData.get(ORIGEN).orElse(null);
    }

    public BlockPos getEnd() {
        return this.entityData.get(END).orElse(null);
    }
    public void setEnd(BlockPos pos){
        this.entityData.set(END,Optional.of(pos));
    }
    public void setOrigen(BlockPos pos){
        this.entityData.set(ORIGEN,Optional.of(pos));
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket p_146866_) {
        super.recreateFromPacket(p_146866_);
    }

    @Override
    public void handleEntityEvent(byte p_19882_) {
        if (p_19882_ == 4){
            this.clientDiscard = true;
            this.residualTime = 20;
            this.residualTime0 = 20;
        }
        super.handleEntityEvent(p_19882_);

    }

    @Override
    public void tick() {
        super.tick();
        this.delayTime0 = this.delayTime;
        this.residualTime0 = this.residualTime;
        if(this.delayTime>0){
            this.delayTime--;
            if(this.delayTime==0){
                this.level().broadcastEntityEvent(this,(byte) 4);
            }
        }else {
            if(this.residualTime>0){
                this.residualTime--;
                if(this.residualTime==0){
                    discard();
                }
            }
        }

    }
}
