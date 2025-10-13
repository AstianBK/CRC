package com.TBK.crc.server.entity;

import com.TBK.crc.common.registry.BKParticles;
import com.TBK.crc.server.StructureData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class PortalEntity extends Entity {
    public PortalEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, this::canTeleportEntity);
            if (result.getType() == HitResult.Type.MISS && this.isAlive()) {
                List<Entity> intersecting = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox(), this::canTeleportEntity);
                if (!intersecting.isEmpty())
                    this.onTeleport(new EntityHitResult(intersecting.get(0)));
            }
        }else {
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
    }

    private void onTeleport(EntityHitResult entityHitResult) {
        if(entityHitResult.getEntity() instanceof LivingEntity living){
            StructureData.get().getCyberChickenFight().teleport(living);
        }
    }


    private boolean canTeleportEntity(Entity entity) {
        return entity!=this;
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
