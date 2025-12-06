package dev.yurisuika.blossom.mixin.minecraft.world.entity.animal;

import dev.yurisuika.blossom.mixin.minecraft.world.entity.MobAccessor;
import dev.yurisuika.blossom.tags.BlossomBlockTags;
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
        ((MobAccessor) bee).getGoalSelector().addGoal(4, new BlossomLeavesGoal(bee));
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

        @Inject(method = "method_21819(Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
        private static void pollinateIfLeavedFlowers(BlockState state, CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(cir.getReturnValue() || state.is(BlossomBlockTags.LEAVED_FLOWERS));
        }

        @Inject(method = "canBeeUse", at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;", shift = At.Shift.AFTER), cancellable = true)
        private void cancelPollinationIfNotBlossoming(CallbackInfoReturnable<Boolean> cir) {
            if (entity.hasSavedFlowerPos()) {
                BlockState blockState = entity.getCommandSenderWorld().getBlockState(entity.getSavedFlowerPos());
                if (blockState.getBlock() instanceof FloweringLeavesBlock && !((FloweringLeavesBlock) blockState.getBlock()).isMaxAge(blockState)) {
                    cir.setReturnValue(false);
                }
            }
        }

        @Inject(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee$BeePollinateGoal;hasPollinatedLongEnough()Z", shift = At.Shift.AFTER))
        private void setFloweringLeavesPollinated(CallbackInfo ci) {
            if (entity.hasSavedFlowerPos()) {
                BlockPos blockPos = ((BeeInvoker) entity).invokeGetSavedFlowerPos();
                BlockState blockState = entity.getCommandSenderWorld().getBlockState(blockPos);
                if (blockState.getBlock() instanceof FloweringLeavesBlock && ((FloweringLeavesBlock) blockState.getBlock()).isMaxAge(blockState)) {
                    entity.getCommandSenderWorld().setBlockAndUpdate(blockPos, blockState
                            .setValue(FloweringLeavesBlock.POLLINATED, true));
                }   
            }
        }

    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal")
    public abstract static class BeeGrowCropGoalMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>(Lnet/minecraft/world/entity/animal/Bee;)V", at = @At(value = "TAIL"))
        private void assignEntity(Bee bee, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void growFruitingLeaves(CallbackInfo ci) {
            if (entity.getRandom().nextInt(30) != 0) {
                for (int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.blockPosition().below(i);
                    BlockState blockState = entity.getCommandSenderWorld().getBlockState(blockPos);
                    if (blockState.is(BlockTags.BEE_GROWABLES)) {
                        if (blockState.getBlock() instanceof FruitingLeavesBlock) {
                            FruitingLeavesBlock fruitingLeavesBlock = (FruitingLeavesBlock) blockState.getBlock();
                            if (!fruitingLeavesBlock.isMaxAge(blockState)) {
                                IntegerProperty age = fruitingLeavesBlock.getAgeProperty();
                                entity.getCommandSenderWorld().levelEvent(2005, blockPos, 0);
                                entity.getCommandSenderWorld().setBlockAndUpdate(blockPos, blockState.setValue(age, blockState.getValue(age) + 1));
                                ((BeeInvoker) entity).invokeIncrementNumCropsGrownSincePollination();
                            }
                        }
                    }
                }
            }
        }

    }

}