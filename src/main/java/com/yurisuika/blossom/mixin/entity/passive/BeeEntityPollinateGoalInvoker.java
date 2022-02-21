package com.yurisuika.blossom.mixin.entity.passive;

import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeeEntity.PollinateGoal.class)
public interface BeeEntityPollinateGoalInvoker {

    @Invoker("<init>")
    static BeeEntity.PollinateGoal invokePollinateGoal(BeeEntity entityType) {
        throw new IllegalStateException();
    }

}
