package com.yurisuika.blossom.mixin.entity.passive;

import net.minecraft.entity.passive.BeeEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeeEntity.GrowCropsGoal.class)
public interface BeeEntityGrowCropsGoalInvoker {

    @Invoker("<init>")
    static BeeEntity.GrowCropsGoal callGrowCropsGoal(BeeEntity entityType) {
        throw new IllegalStateException();
    }

}