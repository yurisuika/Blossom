package dev.yurisuika.blossom.core.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BlossomParticleTypes {

    public static final SimpleParticleType FLOWERING_OAK_LEAVES = FabricParticleTypes.simple(false);

    public static void register() {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
    }

}