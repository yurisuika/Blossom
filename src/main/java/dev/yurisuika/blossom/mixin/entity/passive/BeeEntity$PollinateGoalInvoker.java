package dev.yurisuika.blossom.mixin.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(BeeEntity.PollinateGoal.class)
public interface BeeEntity$PollinateGoalInvoker {

    @Invoker("findFlower")
    static Optional<BlockPos> invokeFindFlower(Predicate<BlockState> predicate, double searchDistance) {
        throw new AssertionError();
    }

}