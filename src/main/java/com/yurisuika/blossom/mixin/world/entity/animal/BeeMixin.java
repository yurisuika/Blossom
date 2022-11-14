package com.yurisuika.blossom.mixin.world.entity.animal;

import com.yurisuika.blossom.Blossom;
import com.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

import static net.minecraft.world.level.block.LeavesBlock.DISTANCE;
import static net.minecraft.world.level.block.LeavesBlock.PERSISTENT;

public class BeeMixin {

    @Mixin(Bee.class)
    public static class InitMixin {

        private EntityType<? extends Bee> entity;
        private Level here;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(EntityType<? extends Bee> entityType, Level level, CallbackInfo ci) {
            entity = entityType;
            here = level;
        }

        @Inject(method = "isFlowerValid", at = @At("RETURN"), cancellable = true)
        private void injectIsFlowerValid(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(((Bee)(Object)this).level.isLoaded(pos) && (((Bee)(Object)this).level.getBlockState(pos).is(BlockTags.FLOWERS)) || ((Bee)(Object)this).level.getBlockState(pos).is(Blocks.OAK_LEAVES) || ((Bee)(Object)this).level.getBlockState(pos).is(Blossom.FLOWERING_OAK_LEAVES.get()));
        }

    }

    @Mixin(Bee.BeeGrowCropGoal.class)
    public static class BeeGrowCropGoalMixin {

        private Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(Bee entityType, CallbackInfo ci) {
            entity = entityType;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void injectTick(CallbackInfo ci) {
            if (entity.random.nextInt(((Bee.BeeGrowCropGoal)(Object)this).adjustedTickDelay(30)) == 0) {
                for(int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.blockPosition().below(i);

                    BlockState blockState = entity.level.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    boolean flag = false;
                    IntegerProperty integerProperty = null;
                    if (blockState.is(BlockTags.BEE_GROWABLES)) {
                        if (block instanceof FloweringLeavesBlock floweringLeavesBlock) {
                            if (!floweringLeavesBlock.isMaxAge(blockState)) {
                                flag = true;
                                integerProperty = floweringLeavesBlock.getAgeProperty();
                            }
                        }
                        else if (block instanceof LeavesBlock leavesBlock) {
                            if (leavesBlock == Blocks.OAK_LEAVES) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            entity.level.levelEvent(2005, blockPos, 0);
                            if (block instanceof LeavesBlock) {
                                entity.level.setBlockAndUpdate(blockPos, Blossom.FLOWERING_OAK_LEAVES.get().defaultBlockState()
                                        .setValue(DISTANCE, blockState.getValue(DISTANCE))
                                        .setValue(PERSISTENT, blockState.getValue(PERSISTENT))
                                );
                            }
                            else if (block instanceof FloweringLeavesBlock) {
                                entity.level.setBlockAndUpdate(blockPos, blockState.setValue(integerProperty, blockState.getValue(integerProperty) + 1));
                            }
                            entity.incrementNumCropsGrownSincePollination();
                        }
                    }
                }
            }
        }

    }


    @Mixin(Bee.BeePollinateGoal.class)
    public abstract static class BeePollinateGoalMixin {

        private Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(Bee entityType, CallbackInfo ci) {
            entity = entityType;
        }

        @Inject(method = "findNearbyFlower", at = @At("RETURN"), cancellable = true)
        private void injectFindNearbyFlower(CallbackInfoReturnable<Optional<BlockPos>> cir) {

            Predicate<BlockState> flowerPredicate = (predicate) -> {
                if (predicate.is(BlockTags.FLOWERS)) {
                    if (predicate.is(Blocks.SUNFLOWER)) {
                        return predicate.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER;
                    } else {
                        return true;
                    }
                } else return predicate.is(Blocks.OAK_LEAVES) || predicate.is(Blossom.FLOWERING_OAK_LEAVES.get());
            };

            cir.setReturnValue(((Bee.BeePollinateGoal)(Object)this).findNearestBlock(flowerPredicate, 5.0D));

        }

        /**
         * Bees will spin when they collide with blocks when trying to pollinate their target.
         * They will collide with leaves when trying to pollinate them.
         * By adding 0.5D to the Y value, this can be resolved, but does not look as good.
         * At the same time, bees will still collide with other leaves when trying to pollinate deeper ones.
         * Lowering the searchDistance to 2.0D can help with this somewhat, but of course limits the bees searching.
         * A good remedy for this is to use the Passable Leaves mod.
         */

        @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"), index = 1)
        private double injectTick(double y) {
            return y;
        }

    }

}
