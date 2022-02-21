package com.yurisuika.blossom.mixin.entity.passive;

import com.yurisuika.blossom.Blossom;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(BeeEntity.PollinateGoal.class)
public abstract class BeeEntityPollinateGoalMixin {

    private BeeEntity entity;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void injectInit(BeeEntity entityType, CallbackInfo ci) {
        entity = entityType;
    }

    @Inject(method = "getFlower", at = @At("RETURN"), cancellable = true)
    private void injectGetFlower(CallbackInfoReturnable<Optional<BlockPos>> cir) {

        Predicate<BlockState>flowerPredicate = (predicate) -> {
            if (predicate.isIn(BlockTags.FLOWERS)) {
                if (predicate.isOf(Blocks.SUNFLOWER)) {
                    return predicate.get(TallPlantBlock.HALF) == DoubleBlockHalf.UPPER;
                } else {
                    return true;
                }
            } else return predicate.isOf(Blocks.OAK_LEAVES) || predicate.isOf(Blossom.FLOWERING_OAK_LEAVES);
        };

        cir.setReturnValue(BeeEntityPollinateGoalInvoker.invokePollinateGoal(entity).findFlower(flowerPredicate, 5.0D));

    }

    /**
     * Bees will spin when they collide with blocks when trying to pollinate their target.
     * They will collide with leaves when trying to pollinate them.
     * By adding 0.5D to the Y value, this can be resolved, but does not look as good.
     * At the same time, bees will still collide with other leaves when trying to pollinate deeper ones.
     * Lowering the searchDistance to 2.0D can help with this somewhat, but of course limits the bees searching.
     * A good remedy for this is to use the Passable Leaves mod.
     */

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;"), index = 1)
    private double injectTick(double y) {
        return y;
    }

}
