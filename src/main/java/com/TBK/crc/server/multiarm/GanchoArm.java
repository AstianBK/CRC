package com.TBK.crc.server.multiarm;

import com.TBK.crc.CRC;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.entity.GanchoEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GanchoArm extends MultiArmSkillAbstract{
    public static final int MAX_RANGE = 20;
    public boolean hasGancho = true;
    public int ganchoId = -1;
    public float largoActual = 0.0F;
    public boolean atraction = false;
    public GanchoArm() {
        super("gancho_arm", 100, true, false);
    }

    @Override
    public void tick(MultiArmCapability multiArmCapability) {
        super.tick(multiArmCapability);
        Entity gancho = multiArmCapability.getPlayer().level().getEntity(this.ganchoId);
        if (gancho instanceof GanchoEntity ganchoEntity) {
            Player player = multiArmCapability.getPlayer();
            Vec3 playerPos = player.position();
            Vec3 ganchoPos = ganchoEntity.position();
            Vec3 toGancho = ganchoPos.subtract(playerPos);
            double distance = toGancho.length();

            if (largoActual <= 0) {
                largoActual = MAX_RANGE;
            }
            if (ganchoEntity.hit) {

                Vec3 direction = toGancho.normalize();
                Vec3 velocity = player.getDeltaMovement();

                // === CONFIGURACIONES ===
                double damping = 0.96;         // fricción del aire
                double gravityReduction = 0.04; // para un balanceo más suave
                double tensionForce = 0.25;     // fuerza de la cuerda al tensarse
                double shortenSpeed = 0.55;     // qué tan rápido se acorta con shift
                double minLength = 1.0;         // distancia mínima al gancho

                // === ACORTAR CUERDA CON SHIFT ===
                if (player.isShiftKeyDown()) {
                    if(!this.atraction){
                        largoActual = (float) Math.min(distance,MAX_RANGE);
                        CRC.LOGGER.debug("Largo actual :"+largoActual);
                        this.atraction = true;
                    }
                    largoActual = (float) Math.max(minLength, largoActual - shortenSpeed);
                }

                // === FÍSICA DEL BALANCEO ===
                velocity = velocity.scale(damping);
                velocity = velocity.add(0, gravityReduction, 0);

                // === SI SUPERA EL LARGO DE LA CUERDA ===
                if (distance > largoActual) {
                    double excess = distance - largoActual;

                    // Aplica una fuerza de tensión proporcional a cuánto se pasó
                    Vec3 tension = direction.scale(excess * tensionForce);

                    // Evita que el jugador siga alejándose
                    double awaySpeed = velocity.dot(direction);
                    if (awaySpeed < 0) {
                        velocity = velocity.subtract(direction.scale(awaySpeed));
                    }

                    velocity = velocity.add(tension);
                }

                player.setDeltaMovement(velocity);

            } else {
                if (!ganchoEntity.isBack && gancho.distanceTo(player) > MAX_RANGE) {
                    ganchoEntity.isBack = true;
                }
            }
        }
    }

    @Override
    public void startAbility(MultiArmCapability multiArmCapability) {
        if(hasGancho){
            GanchoEntity gancho = new GanchoEntity(multiArmCapability.getPlayer().level(),multiArmCapability.getPlayer());
            gancho.setPos(multiArmCapability.getPlayer().getX(),multiArmCapability.getPlayer().getEyeY()-0.1f,multiArmCapability.getPlayer().getZ());
            gancho.shootFromRotation(multiArmCapability.getPlayer(),multiArmCapability.getPlayer().getXRot(),multiArmCapability.getPlayer().yHeadRot,0.0F,3.0F,1.0F);
            multiArmCapability.getPlayer().level().addFreshEntity(gancho);
            this.hasGancho = false;
            this.ganchoId = gancho.getId();
            this.largoActual = 0.0F;
            this.atraction = false;
        }else {
            Entity gancho = multiArmCapability.getPlayer().level().getEntity(this.ganchoId);
            if(gancho instanceof GanchoEntity ganchoEntity){
                if(!ganchoEntity.isBack ){
                    if(ganchoEntity.hit){
                        Vec3 pushDirection = gancho.position().subtract(multiArmCapability.getPlayer().position()).normalize().scale(3);
                        multiArmCapability.getPlayer().setDeltaMovement(pushDirection.x,Math.min(pushDirection.y,1.5F),pushDirection.z);
                        multiArmCapability.getPlayer().hurtMarked=true;
                    }
                    ganchoEntity.isBack = true;
                }
            }else {
                if(multiArmCapability.catchEntity!=null){
                    Vec3 pushDirection = multiArmCapability.getPlayer().position().subtract(multiArmCapability.catchEntity.position()).normalize().scale(3);
                    multiArmCapability.catchEntity.setDeltaMovement(pushDirection.x,Math.min(pushDirection.y,1.5F),pushDirection.z);
                    multiArmCapability.catchEntity.hurtMarked=true;

                    GanchoEntity gancho1 = new GanchoEntity(multiArmCapability.getPlayer().level(),multiArmCapability.getPlayer(),true);
                    gancho1.setPos(multiArmCapability.catchEntity.getX(),multiArmCapability.catchEntity.getEyeY()-0.1f,multiArmCapability.catchEntity.getZ());
                    multiArmCapability.getPlayer().level().addFreshEntity(gancho1);
                }
            }
            multiArmCapability.catchEntity=null;
        }
    }


    @Override
    public void save(CompoundTag nbt) {
        super.save(nbt);
        nbt.putBoolean("has_gancho",hasGancho);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.hasGancho = nbt.getBoolean("has_gancho");
    }
}
