package dev.yurisuika.blossom.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class FallingPetalsParticle extends TextureSheetParticle {

    public float rotSpeed;
    public final float particleRandom;
    public final float spinAcceleration;

    public FallingPetalsParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.setSprite(spriteSet.get(random.nextInt(12), 12));
        this.rotSpeed = (float) Math.toRadians(random.nextBoolean() ? -30.0F : 30.0F);
        this.particleRandom = random.nextFloat();
        this.spinAcceleration = (float) Math.toRadians(random.nextBoolean() ? -5.0F : 5.0F);
        this.lifetime = 300;
        this.gravity = 7.5E-4F;
        float f = this.random.nextBoolean() ? 0.05F : 0.075F;
        this.quadSize = f;
        this.setSize(f, f);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class FloweringAppleProvider implements ParticleProvider<SimpleParticleType> {

        public final SpriteSet spriteSet;

        public FloweringAppleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double r, double g, double b) {
            return new FallingPetalsParticle(level, x, y, z, spriteSet);
        }

    }

    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (lifetime-- <= 0) {
            remove();
        }
        if (removed) {
            return;
        }
        float f = 300 - lifetime;
        float g = Math.min(f / 300.0F, 1.0F);
        double d = Math.cos(Math.toRadians(particleRandom * 60.0F)) * 2.0D * Math.pow(g, 1.25D);
        double e = Math.sin(Math.toRadians(particleRandom * 60.0F)) * 2.0D * Math.pow(g, 1.25D);
        xd += d * 0.0025D;
        zd += e * 0.0025D;
        yd -= gravity;
        rotSpeed += spinAcceleration / 20.0F;
        oRoll = roll;
        roll += rotSpeed / 20.0F;
        move(xd, yd, zd);
        if (onGround || lifetime < 299 && (xd == 0.0D || zd == 0.0D)) {
            remove();
        }
        if (removed) {
            return;
        }
    }

}