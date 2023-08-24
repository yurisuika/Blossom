package dev.yurisuika.blossom.mixin.entity.ai.goal;

import net.minecraft.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Goal.class)
public interface GoalInvoker {

    @Invoker("getTickCount")
    int invokeGetTickCount(int ticks);

}