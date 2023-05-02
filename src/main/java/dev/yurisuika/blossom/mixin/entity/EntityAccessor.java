package dev.yurisuika.blossom.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor
    Random getRandom();

    @Accessor
    World getWorld();

}