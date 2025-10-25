package com.TBK.crc.server.entity;

import com.TBK.crc.CRC;
import com.TBK.crc.common.Util;
import com.TBK.crc.common.registry.BKParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class BeamExplosionEntity extends Entity {
    public Map<BlockPos,Integer> crackingBlock = new HashMap<>();
    public int restoreCracking = 0;
    public boolean cracking = false;
    public LivingEntity owner;
    public BeamExplosionEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.owner = null;
    }
    public BeamExplosionEntity(EntityType<?> p_19870_, Level p_19871_,LivingEntity livingEntity) {
        super(p_19870_, p_19871_);
        this.owner = livingEntity;
    }

    public void startCracking(){
        if(!this.level().isClientSide){
            this.level().broadcastEntityEvent(this,(byte) 4);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.restoreCracking>0){
            this.restoreCracking--;
        }
        if(this.level().isClientSide){
            if(this.restoreCracking>0){
                if (this.restoreCracking%5==0){
                    //this.level().playLocalSound(this.getX(),this.getY(),this.getY(), SoundEvents.ROOTED_DIRT_BREAK,SoundSource.HOSTILE,10.0f,1.0f,false);
                    for (BlockPos pos : this.crackingBlock.keySet()){
                        Minecraft.getInstance().particleEngine.crack(pos, Direction.UP);
                    }
                }

                if (this.restoreCracking==1){
                    for (int i = 0 ; i<3 ; i++){
                        this.level().addParticle(BKParticles.ELECTRO_EXPLOSION_PARTICLES.get(),this.getX()+this.random.nextInt(-2,2),this.getY()+this.random.nextInt(0,2),this.getZ()+this.random.nextInt(-2,2),0.0F,0.0F,0.0F);
                    }
                    this.level().playLocalSound(this.getX(),this.getY(),this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS,4.0F,1.0f,false);
                }

            }
        }else {
            if(this.crackingBlock.isEmpty() && !this.cracking){
                this.startCracking();
                this.cracking = true;
                this.restoreCracking = 50;
            }else {
                if(this.restoreCracking == 0){
                    float f2 = 2 * 2.0F;
                    int k1 = Mth.floor(this.getX() - (double)f2 - 1.0D);
                    int l1 = Mth.floor(this.getX() + (double)f2 + 1.0D);
                    int i2 = Mth.floor(this.getY() - (double)f2 - 1.0D);
                    int i1 = Mth.floor(this.getY() + (double)f2 + 1.0D);
                    int j2 = Mth.floor(this.getZ() - (double)f2 - 1.0D);
                    int j1 = Mth.floor(this.getZ() + (double)f2 + 1.0D);
                    List<Entity> list = this.level().getEntities(this, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
                    Vec3 vec3 = new Vec3(this.getX(), this.getY(), this.getZ());

                    for(int k2 = 0; k2 < list.size(); ++k2) {
                        Entity entity = list.get(k2);
                        if (!entity.ignoreExplosion()) {
                            double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double)f2;
                            if (d12 <= 1.0D) {
                                double d5 = entity.getX() - this.getX();
                                double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.getY();
                                double d9 = entity.getZ() - this.getZ();
                                double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                                if (d13 != 0.0D) {
                                    double d14 = (double)getSeenPercent(vec3, entity);
                                    double d10 = (1.0D - d12) * d14;
                                    if(this.owner instanceof Mob mob){
                                        entity.hurt(Util.electricDamageMob((ServerLevel) this.level(),mob), (float)((int)((d10 * d10 + d10) / 2.0D * 20.0D * (double)f2 + 4.0D)));
                                    }else if(this.owner instanceof Player player){
                                        entity.hurt(Util.electricDamage((ServerLevel) this.level(), player), (float)((int)((d10 * d10 + d10) / 2.0D * 20.0D * (double)f2 + 4.0D)));
                                    }else {
                                        entity.hurt(damageSources().explosion(null,null), (float)((int)((d10 * d10 + d10) / 2.0D * 20.0D * (double)f2 + 4.0D)));
                                    }
                                }
                            }
                        }
                    }
                    discard();
                }
            }
        }
    }

    public static float getSeenPercent(Vec3 p_46065_, Entity p_46066_) {
        AABB aabb = p_46066_.getBoundingBox();
        double d0 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;

            for(double d5 = 0.0D; d5 <= 1.0D; d5 += d0) {
                for(double d6 = 0.0D; d6 <= 1.0D; d6 += d1) {
                    for(double d7 = 0.0D; d7 <= 1.0D; d7 += d2) {
                        double d8 = Mth.lerp(d5, aabb.minX, aabb.maxX);
                        double d9 = Mth.lerp(d6, aabb.minY, aabb.maxY);
                        double d10 = Mth.lerp(d7, aabb.minZ, aabb.maxZ);
                        Vec3 vec3 = new Vec3(d8 + d3, d9, d10 + d4);
                        if (p_46066_.level().clip(new ClipContext(vec3, p_46065_, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, p_46066_)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
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


    @Override
    public void handleEntityEvent(byte p_19882_) {
        if(p_19882_==4){
            Map<BlockPos,Integer> posSet = new HashMap<>();
            for (BlockPos pos2 : BlockPos.betweenClosed(this.getOnPos().offset(2,1,2),this.getOnPos().offset(-2,-1,-2))){
                if(!this.level().getBlockState(pos2).isAir()){
                    float entityHitDistance = Math.max((float) Math.sqrt((pos2.getZ() -this.getOnPos().getZ()) * (pos2.getZ() - this.getOnPos().getZ()) + (pos2.getX() - this.getOnPos().getX()) * (pos2.getX() - this.getOnPos().getX())),0);
                    if (entityHitDistance <= 2) {
                        Random random1 = new Random();
                        double distance = Math.ceil(entityHitDistance) + random1.nextFloat(0.0F,1.0F);
                        BlockPos.MutableBlockPos pos1 = pos2.mutable();
                        boolean canSummon=true;
                        for (int i = 0; i< Mth.ceil(distance); i++){
                            if(canSummon && !this.level().getBlockState(pos1.above()).isAir()){
                                canSummon=false;
                            }
                        }
                        if(canSummon){
                            distance = Mth.clamp((2.0F/entityHitDistance),0.0F,1.0f);
                            posSet.put(new BlockPos(pos2.getX(), pos2.getY(), pos2.getZ()), (int) (distance));
                        }
                    }
                }
            }
            this.crackingBlock = posSet;
            this.restoreCracking = 50;
        }
        super.handleEntityEvent(p_19882_);
    }
}
