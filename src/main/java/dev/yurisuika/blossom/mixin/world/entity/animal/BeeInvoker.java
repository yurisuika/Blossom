package dev.yurisuika.blossom.mixin.world.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Bee.class)
public interface BeeInvoker {

    @Intrinsic
    @Invoker("incrementNumCropsGrownSincePollination")
    void invokeIncrementNumCropsGrownSincePollination();

    @Invoker("isHiveValid")
    boolean invokeIsHiveValid();

    @Invoker("getCropsGrownSincePollination")
    int invokeGetCropsGrownSincePollination();

    @Invoker("isTooFarAway")
    boolean invokeIsTooFarAway(BlockPos pos);

    @Invoker("pathfindRandomlyTowards")
    void invokePathfindRandomlyTowards(BlockPos pos);

    @Invoker("closerThan")
    boolean invokeCloserThan(BlockPos pos, int distance);

}