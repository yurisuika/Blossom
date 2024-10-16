package dev.yurisuika.blossom.world.entity.ai.goal;

import dev.yurisuika.blossom.Blossom;
import dev.yurisuika.blossom.mixin.world.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.world.entity.MobAccessor;
import dev.yurisuika.blossom.mixin.world.entity.animal.BeeAccessor;
import dev.yurisuika.blossom.mixin.world.entity.animal.BeeInvoker;
import dev.yurisuika.blossom.util.config.Option;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;
import java.util.function.Predicate;

public class FruitGoal extends BlossomGoal {

    public final Predicate<BlockState> targetPredicate = state -> {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
            return false;
        } else {
            if (state.is(Blossom.FLOWERING_OAK_LEAVES)) {
                return state.getValue(BlockStateProperties.AGE_3) >= 3;
            } else {
                return false;
            }
        }
    };

    public FruitGoal(Bee bee) {
        super(bee);
    }

    public boolean canBeeStart() {
        if (((BeeInvoker) entity).invokeGetCropsGrownSincePollination() >= 10) {
            return false;
        }
        if (((EntityAccessor) entity).getRandom().nextFloat() > Option.getFruitingChance()) {
            return false;
        }
        if (entity.level().isRaining()) {
            return false;
        }
        Optional<BlockPos> optional = findTarget();
        if (optional.isPresent()) {
            if (entity.hasNectar()) {
                if (((BeeInvoker) entity).invokeIsHiveValid()) {
                    ((BeeAccessor) entity).setSavedFlowerPos(optional.get());
                    ((MobAccessor) entity).getNavigation().moveTo(entity.getSavedFlowerPos().getX() + 0.5D, entity.getSavedFlowerPos().getY() + 0.5D, entity.getSavedFlowerPos().getZ() + 0.5D, 1.2000000476837158D);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public void stop() {
        if (completed()) {
            BlockPos blockPos = entity.getSavedFlowerPos();
            if (blockPos != null) {
                BlockState blockState = entity.level().getBlockState(blockPos);
                if (blockState.getBlock() == Blossom.FLOWERING_OAK_LEAVES) {
                    if (blockState.getValue(BlockStateProperties.AGE_3) >= 3) {
                        entity.level().levelEvent(2005, blockPos, 0);
                        entity.level().setBlockAndUpdate(blockPos, Blossom.FRUITING_OAK_LEAVES.defaultBlockState()
                                .setValue(FruitingLeavesBlock.DISTANCE, blockState.getValue(FruitingLeavesBlock.DISTANCE))
                                .setValue(FruitingLeavesBlock.PERSISTENT, blockState.getValue(FruitingLeavesBlock.PERSISTENT))
                                .setValue(FruitingLeavesBlock.WATERLOGGED, blockState.getValue(FruitingLeavesBlock.WATERLOGGED))
                        );
                        ((BeeInvoker) entity).invokeIncrementNumCropsGrownSincePollination();
                    }
                }
            }
        }
        running = false;
        ((MobAccessor) entity).getNavigation().stop();
    }

    public boolean isTarget(BlockPos pos) {
        return entity.level().isLoaded(pos) && entity.level().getBlockState(pos).getBlock() instanceof FloweringLeavesBlock;
    }

    public Optional<BlockPos> findTarget() {
        return findTarget(targetPredicate, Option.getFruitingDistance());
    }

}