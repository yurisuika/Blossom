package dev.yurisuika.blossom.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class BlossomParticle extends SpriteBillboardParticle {

    public float wind;
    public final float drift;
    public final float gust;

    public BlossomParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        float f;
        this.setSprite(spriteProvider.getSprite(random.nextInt(12), 12));
        this.wind = (float)Math.toRadians(random.nextBoolean() ? -30.0F : 30.0F);
        this.drift = random.nextFloat();
        this.gust = (float)Math.toRadians(random.nextBoolean() ? -5.0F : 5.0F);
        this.maxAge = 300;
        this.gravityStrength = 7.5E-4F;
        this.scale = f = random.nextBoolean() ? 0.05F : 0.075F;
        this.setBoundingBoxSpacing(f, f);
        this.velocityMultiplier = 1.0F;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider) implements ParticleFactory<DefaultParticleType> {

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double r, double g, double b) {
            return new BlossomParticle(world, x, y, z, spriteProvider);
        }

    }

    public void tick() {
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
        if (maxAge-- <= 0) {
            markDead();
        }
        if (dead) {
            return;
        }
        float f = 300 - maxAge;
        float g = Math.min(f / 300.0F, 1.0F);
        double d = Math.cos(Math.toRadians(drift * 60.0F)) * 2.0D * Math.pow(g, 1.25D);
        double e = Math.sin(Math.toRadians(drift * 60.0F)) * 2.0D * Math.pow(g, 1.25D);
        velocityX += d * (double)0.0025F;
        velocityZ += e * (double)0.0025F;
        velocityY -= gravityStrength;
        wind += gust / 20.0F;
        prevAngle = angle;
        angle += wind / 20.0F;
        move(velocityX, velocityY, velocityZ);
        if (onGround || maxAge < 299 && (velocityX == 0.0D || velocityZ == 0.0D)) {
            markDead();
        }
        if (dead) {
            return;
        }
        velocityX *= velocityMultiplier;
        velocityY *= velocityMultiplier;
        velocityZ *= velocityMultiplier;
    }

}