package com.TBK.crc.server.entity;

import com.TBK.crc.CRC;
import com.TBK.crc.common.registry.BKParticles;
import com.TBK.crc.common.screen.PortalScreen;
import com.TBK.crc.server.StructureData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PortalEntity extends Entity {
    public AnimationState idle = new AnimationState();
    public int idleTimer = 0;
    public PortalEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if(this.tickCount % (15 + this.random.nextInt(0,5)) == 0){
                for(int i = 0; i < 4; ++i) {

                    double d0 = (double)this.getX() + this.random.nextDouble();
                    double d1 = (double)this.getY() + this.random.nextDouble() * 2.0D;
                    double d2 = (double)this.getZ() + this.random.nextDouble();
                    double d3 = ((double)this.random.nextFloat() - 0.5D) * 0.5D;
                    double d4 = ((double)this.random.nextFloat() - 0.5D) * 0.5D;
                    double d5 = ((double)this.random.nextFloat() - 0.5D) * 0.5D;

                    this.level().addParticle(BKParticles.LIGHTNING_TRAIL_PARTICLES.get(), d0, d1, d2, d3, d4, d5);
                }
            }

            if(this.idleTimer<=0){
                this.idleTimer = 80;
                this.idle.start(this.tickCount);
            }else {
                this.idleTimer--;
            }
        }
    }


    @Override
    public InteractionResult interactAt(Player p_19980_, Vec3 p_19981_, InteractionHand p_19982_) {
        if(!this.level().isClientSide){
            this.level().broadcastEntityEvent(this,(byte) 4);
        }
        return super.interactAt(p_19980_, p_19981_, p_19982_);
    }
    public boolean isPickable() {
        return true;
    }

    public static void onTeleport(Entity entity) {
        if(entity instanceof LivingEntity living){
            StructureData.get().getCyberChickenFight().teleport(living);
        }
    }


    @Override
    public void handleEntityEvent(byte p_19882_) {
        if(p_19882_ == 4){
            Minecraft.getInstance().setScreen(new PortalScreen( Component.nullToEmpty("")));
        }
        super.handleEntityEvent(p_19882_);
    }


    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }


    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }
}
