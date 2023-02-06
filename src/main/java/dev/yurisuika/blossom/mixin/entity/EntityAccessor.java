package dev.yurisuika.blossom.mixin.entity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor
    static Random getRandom() {
        throw new AssertionError();
    }

}