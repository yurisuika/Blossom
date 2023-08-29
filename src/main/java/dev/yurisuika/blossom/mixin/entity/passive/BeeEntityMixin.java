package dev.yurisuika.blossom.mixin.entity.passive;

import dev.yurisuika.blossom.block.FruitingLeavesBlock;
import dev.yurisuika.blossom.mixin.entity.ai.goal.GoalInvoker;
import net.minecraft.block.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin {

    @Inject(method = "getPathfindingFavor", at = @At("HEAD"), cancellable = true)
    private void injectGetPathfindingFavor(BlockPos pos, WorldView world, CallbackInfoReturnable<Float> cir) {
        if (world.getBlockState(pos).getBlock() instanceof LeavesBlock) {
            cir.setReturnValue(2.0F);
        }
    }

    @Mixin(targets = "net.minecraft.entity.passive.BeeEntity$GrowCropsGoal")
    public abstract static class GrowCropsGoalMixin {

        @Unique
        public BeeEntity entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(BeeEntity beeEntity, CallbackInfo ci) {
            entity = beeEntity;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void injectTick(CallbackInfo ci) {
            if (entity.getRandom().nextInt(((GoalInvoker)this).invokeGetTickCount(30)) != 0) {
                for (int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.getBlockPos().down(i);
                    BlockState blockState = entity.getWorld().getBlockState(blockPos);
                    if (blockState.isIn(BlockTags.BEE_GROWABLES)) {
                        if (blockState.getBlock() instanceof FruitingLeavesBlock fruitingLeavesBlock) {
                            if (!fruitingLeavesBlock.isMature(blockState)) {
                                IntProperty age = fruitingLeavesBlock.getAgeProperty();
                                entity.getWorld().syncWorldEvent(2005, blockPos, 0);
                                entity.getWorld().setBlockState(blockPos, blockState.with(age, blockState.get(age) + 1));
                                ((BeeEntityInvoker)entity).invokeAddCropCounter();
                            }
                        }
                    }
                }
            }
        }

    }

    @Mixin(targets = "net.minecraft.entity.passive.BeeEntity$MoveToFlowerGoal")
    public abstract static class MoveToFlowerGoalMixin {

        @Unique
        public BeeEntity entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(BeeEntity beeEntity, CallbackInfo ci) {
            entity = beeEntity;
        }

        @Inject(method = "shouldMoveToFlower", at = @At("RETURN"), cancellable = true)
        private void injectTick(CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(cir.getReturnValue() || (entity.getWorld().getBlockState(entity.getFlowerPos()).isOf(Blocks.OAK_LEAVES) && entity.hasNectar()));
        }

    }

}