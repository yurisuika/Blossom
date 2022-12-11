package com.yurisuika.blossom.mixin.entity.passive;

import com.yurisuika.blossom.Blossom;
import com.yurisuika.blossom.block.FloweringLeavesBlock;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

import static net.minecraft.block.LeavesBlock.DISTANCE;
import static net.minecraft.block.LeavesBlock.PERSISTENT;

public class BeeEntityMixin {

    @Mixin(BeeEntity.class)
    public static class InitMixin {

        private EntityType<? extends BeeEntity> entity;
        private World here;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(EntityType<? extends BeeEntity> entityType, World world, CallbackInfo ci) {
            entity = entityType;
            here = world;
        }

        @Inject(method = "isFlowers", at = @At("RETURN"), cancellable = true)
        private void injectIsFlowers(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(((BeeEntity)(Object)this).world.canSetBlock(pos) && (((BeeEntity)(Object)this).world.getBlockState(pos).isIn(BlockTags.FLOWERS)) || ((BeeEntity)(Object)this).world.getBlockState(pos).isOf(Blocks.OAK_LEAVES) || ((BeeEntity)(Object)this).world.getBlockState(pos).isOf(Blossom.FLOWERING_OAK_LEAVES));
        }

    }

    @Mixin(BeeEntity.GrowCropsGoal.class)
    public static class GrowCropsGoalMixin {

        private BeeEntity entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(BeeEntity entityType, CallbackInfo ci) {
            entity = entityType;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void injectTick(CallbackInfo ci) {
            if (entity.random.nextInt(((BeeEntity.GrowCropsGoal)(Object)this).getTickCount(30)) == 0) {
                for(int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.getBlockPos().down(i);

                    BlockState blockState = entity.world.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    boolean bl = false;
                    IntProperty intProperty = null;
                    if (blockState.isIn(BlockTags.BEE_GROWABLES)) {
                        if (block instanceof FloweringLeavesBlock floweringLeavesBlock) {
                            if (!floweringLeavesBlock.isMature(blockState)) {
                                bl = true;
                                intProperty = floweringLeavesBlock.getAgeProperty();
                            }
                        }
                        else if (block instanceof LeavesBlock leavesBlock) {
                            if (leavesBlock == Blocks.OAK_LEAVES) {
                                bl = true;
                            }
                        }
                        if (bl) {
                            entity.world.syncWorldEvent(2005, blockPos, 0);
                            if (block instanceof LeavesBlock) {
                                entity.world.setBlockState(blockPos, Blossom.FLOWERING_OAK_LEAVES.getDefaultState()
                                        .with(DISTANCE, blockState.get(DISTANCE))
                                        .with(PERSISTENT, blockState.get(PERSISTENT))
                                );
                            }
                            else if (block instanceof FloweringLeavesBlock) {
                                entity.world.setBlockState(blockPos, blockState.with(intProperty, blockState.get(intProperty) + 1));
                            }
                            entity.addCropCounter();
                        }
                    }
                }
            }
        }

    }

    @Mixin(BeeEntity.PollinateGoal.class)
    public abstract static class PollinateGoalMixin {

        private BeeEntity entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(BeeEntity entityType, CallbackInfo ci) {
            entity = entityType;
        }

        @Inject(method = "getFlower", at = @At("RETURN"), cancellable = true)
        private void injectGetFlower(CallbackInfoReturnable<Optional<BlockPos>> cir) {

            Predicate<BlockState> flowerPredicate = (predicate) -> {
                if (predicate.isIn(BlockTags.FLOWERS)) {
                    if (predicate.isOf(Blocks.SUNFLOWER)) {
                        return predicate.get(TallPlantBlock.HALF) == DoubleBlockHalf.UPPER;
                    } else {
                        return true;
                    }
                } else return predicate.isOf(Blocks.OAK_LEAVES) || predicate.isOf(Blossom.FLOWERING_OAK_LEAVES);
            };

            cir.setReturnValue(((BeeEntity.PollinateGoal)(Object)this).findFlower(flowerPredicate, 5.0D));

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

}
