package dev.yurisuika.blossom.world.entity.ai.goal;

import dev.yurisuika.blossom.mixin.world.entity.MobAccessor;
import dev.yurisuika.blossom.mixin.world.entity.animal.BeeInvoker;
import dev.yurisuika.blossom.util.config.Option;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.stream.IntStream;

public class FruitLeavesGoal extends BlossomLeavesGoal {
 
    public FruitLeavesGoal(Bee bee) {
        super(bee);
        this.validBlocks = state -> (!state.hasProperty(BlockStateProperties.WATERLOGGED) || !state.getValue(BlockStateProperties.WATERLOGGED)) && (state.getBlock() instanceof FloweringLeavesBlock && state.getValue(BlockStateProperties.AGE_3) >= 3);
        this.goalChance = Option.getFruitingChance();
    }

    public void stop() {
        if (hasBlossomedLongEnough()) {
            BlockPos blockPos = ((BeeInterface) entity).getSavedLeavesPos();
            if (blockPos != null) {
                BlockState blockState = entity.level().getBlockState(blockPos);
                if (blockState.getBlock() instanceof FloweringLeavesBlock) {
                    if (blockState.getValue(BlockStateProperties.AGE_3) >= 3) {
                        entity.level().levelEvent(2005, blockPos, 0);
                        entity.level().setBlockAndUpdate(blockPos, ((FloweringLeavesBlock) blockState.getBlock()).getPollinatedBlock().defaultBlockState()
                                .setValue(FruitingLeavesBlock.DISTANCE, blockState.getValue(FloweringLeavesBlock.DISTANCE))
                                .setValue(FruitingLeavesBlock.PERSISTENT, blockState.getValue(FloweringLeavesBlock.PERSISTENT))
                                .setValue(FruitingLeavesBlock.WATERLOGGED, blockState.getValue(FloweringLeavesBlock.WATERLOGGED))
                        );
                        IntStream.range(0, 10).forEach(i -> ((BeeInvoker) entity).invokeIncrementNumCropsGrownSincePollination());
                    }
                }
            }
        }
        blossoming = false;
        ((MobAccessor) entity).getNavigation().stop();
    }

}