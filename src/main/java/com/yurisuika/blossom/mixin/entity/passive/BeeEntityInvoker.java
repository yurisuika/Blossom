package com.yurisuika.blossom.mixin.entity.passive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeeEntity.class)
public interface BeeEntityInvoker {

    @Invoker("<init>")
    static BeeEntity invokeInit(EntityType<? extends BeeEntity> entityType, World world) {
        throw new IllegalStateException();
    }

}
