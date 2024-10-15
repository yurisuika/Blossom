package dev.yurisuika.blossom.mixin.world.entity.animal;

import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
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
public abstract class BeeMixin {

    @Inject(method = "getWalkTargetValue", at = @At("HEAD"), cancellable = true)
    private void injectGetPathfindingFavor(BlockPos pos, LevelReader level, CallbackInfoReturnable<Float> cir) {
        if (level.getBlockState(pos).getBlock() instanceof LeavesBlock) {
            cir.setReturnValue(2.0F);
        }
    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal")
    public abstract static class BeeGrowCropGoalMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>(Lnet/minecraft/world/entity/animal/Bee;)V", at = @At(value = "TAIL"))
        private void injectInit(Bee bee, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void injectTick(CallbackInfo ci) {
            if (entity.getRandom().nextInt(30) != 0) {
                for (int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.blockPosition().below(i);
                    BlockState blockState = entity.getCommandSenderWorld().getBlockState(blockPos);
                    if (blockState.is(BlockTags.BEE_GROWABLES)) {
                        if (blockState.getBlock() instanceof FruitingLeavesBlock) {
                            FruitingLeavesBlock fruitingLeavesBlock = (FruitingLeavesBlock)blockState.getBlock();
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

    @Mixin(Bee.BeeGoToKnownFlowerGoal.class)
    public abstract static class BeeGoToKnownFlowerGoalMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(Bee bee, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "wantsToGoToKnownFlower", at = @At("RETURN"), cancellable = true)
        private void injectWantsToGoToKnownFlower(CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(cir.getReturnValue() || (entity.getCommandSenderWorld().getBlockState(entity.getSavedFlowerPos()).is(Blocks.OAK_LEAVES) && entity.hasNectar()));
        }

    }

}