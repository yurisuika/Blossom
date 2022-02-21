package com.yurisuika.blossom.mixin.entity.passive;

import com.yurisuika.blossom.Blossom;
import com.yurisuika.blossom.block.FloweringLeavesBlock;
import net.minecraft.block.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.block.LeavesBlock.DISTANCE;
import static net.minecraft.block.LeavesBlock.PERSISTENT;

@Mixin(BeeEntity.GrowCropsGoal.class)
public class BeeEntityGrowCropsGoalMixin {

    private BeeEntity entity;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void injectInit(BeeEntity entityType, CallbackInfo ci) {
        entity = entityType;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void injectTick(CallbackInfo ci) {
        if (entity.random.nextInt(BeeEntityGrowCropsGoalInvoker.invokeGrowCropsGoal(entity).getTickCount(30)) == 0) {
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
