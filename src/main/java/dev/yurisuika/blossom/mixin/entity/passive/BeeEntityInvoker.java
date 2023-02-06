package dev.yurisuika.blossom.mixin.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(BeeEntity.class)
public interface BeeEntityInvoker {

    @Invoker("addCropCounter")
    void invokeAddCropCounter();

    @Mixin(targets = "net.minecraft.entity.passive.BeeEntity$PollinateGoal")
    interface PollinateGoalInvoker {

        @Invoker("findFlower")
        Optional<BlockPos> invokeFindFlower(Predicate<BlockState> predicate, double searchDistance);

    }

}