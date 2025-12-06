package dev.yurisuika.blossom.mixin.minecraft.world.entity.animal;

import dev.yurisuika.blossom.mixin.minecraft.world.entity.ai.goal.GoalInvoker;
import dev.yurisuika.blossom.world.entity.ai.goal.BlossomLeavesGoal;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Bee.class)
public abstract class BeeMixin implements BeeInterface {

    @Unique
    public void registerGoals(Bee bee) {
        bee.getGoalSelector().addGoal(4, new BlossomLeavesGoal(bee));
    }

    @Inject(method = "getWalkTargetValue", at = @At("HEAD"), cancellable = true)
    private void allowMovementThroughLeaves(BlockPos pos, LevelReader level, CallbackInfoReturnable<Float> cir) {
        if (level.getBlockState(pos).getBlock() instanceof LeavesBlock) {
            cir.setReturnValue(2.0F);
        }
    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeePollinateGoal")
    public abstract static class BeePollinateGoalMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void assignEntity(Bee bee, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "canBeeUse", at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;", shift = At.Shift.AFTER), cancellable = true)
        private void cancelPollinationIfNotBlossoming(CallbackInfoReturnable<Boolean> cir) {
            if (entity.hasSavedFlowerPos()) {
                BlockState blockState = entity.level().getBlockState(entity.getSavedFlowerPos());
                if (blockState.getBlock() instanceof FloweringLeavesBlock floweringLeavesBlock && !floweringLeavesBlock.isMaxAge(blockState)) {
                    cir.setReturnValue(false);
                }
            }
        }

        @Inject(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee$BeePollinateGoal;hasPollinatedLongEnough()Z", shift = At.Shift.AFTER))
        private void setFloweringLeavesPollinated(CallbackInfo ci) {
            if (entity.hasSavedFlowerPos()) {
                BlockPos blockPos = ((BeeInvoker) entity).invokeGetSavedFlowerPos();
                BlockState blockState = entity.level().getBlockState(blockPos);
                if (blockState.getBlock() instanceof FloweringLeavesBlock floweringLeavesBlock && floweringLeavesBlock.isMaxAge(blockState)) {
                    entity.level().setBlockAndUpdate(blockPos, blockState
                            .setValue(FloweringLeavesBlock.POLLINATED, true));
                }
            }
        }

    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal")
    public abstract static class BeeGrowCropGoalMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void assignEntity(Bee bee, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void growFruitingLeaves(CallbackInfo ci) {
            if (entity.getRandom().nextInt(((GoalInvoker) this).invokeAdjustedTickDelay(30)) != 0) {
                for (int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.blockPosition().below(i);
                    BlockState blockState = entity.level().getBlockState(blockPos);
                    if (blockState.is(BlockTags.BEE_GROWABLES)) {
                        if (blockState.getBlock() instanceof FruitingLeavesBlock fruitingLeavesBlock) {
                            if (!fruitingLeavesBlock.isMaxAge(blockState)) {
                                IntegerProperty age = fruitingLeavesBlock.getAgeProperty();
                                entity.level().levelEvent(2005, blockPos, 0);
                                entity.level().setBlockAndUpdate(blockPos, blockState.setValue(age, blockState.getValue(age) + 1));
                                ((BeeInvoker) entity).invokeIncrementNumCropsGrownSincePollination();
                            }
                        }
                    }
                }
            }
        }

    }

}