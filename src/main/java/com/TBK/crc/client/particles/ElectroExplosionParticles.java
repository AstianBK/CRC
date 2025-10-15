package com.TBK.crc.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.Random;

public class ElectroExplosionParticles extends ExplodeParticle {
    private final SpriteSet sprites;

    protected ElectroExplosionParticles(ClientLevel p_108484_, double p_108485_, double p_108486_, double p_108487_, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet ) {
        super(p_108484_,p_108485_,p_108486_,p_108487_,xSpeed,ySpeed,zSpeed,spriteSet);
        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
        this.scale(new Random().nextFloat()*5+4.0F);
        this.lifetime=(int)(16.0D / ((double)this.random.nextFloat() * 0.8D + 0.2D)) + 2;
    }


    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ElectroExplosionParticles bk_particles=new ElectroExplosionParticles(world,x,y,z,xSpeed,ySpeed,zSpeed,this.spriteSet);
            bk_particles.pickSprite(this.spriteSet);
            return bk_particles;
        }
    }
}
