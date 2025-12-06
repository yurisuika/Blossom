package dev.yurisuika.blossom.mixin.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Goal.class)
public interface GoalInvoker {

    @Invoker("adjustedTickDelay")
    int invokeAdjustedTickDelay(int ticks);

}