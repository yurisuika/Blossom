package dev.yurisuika.blossom.entity.ai.goal;

import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.mixin.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.entity.MobEntityAccessor;
import dev.yurisuika.blossom.mixin.entity.passive.BeeEntityAccessor;
import dev.yurisuika.blossom.mixin.entity.passive.BeeEntityInvoker;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.function.Predicate;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.block.LeavesBlock.*;

public class FruitGoal extends BlossomGoal {

    public final Predicate<BlockState> targetPredicate = (state) -> {
        if (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) {
            return false;
        } else {
            if (state.isOf(FLOWERING_OAK_LEAVES)) {
                return state.get(Properties.AGE_3) >= 3;
            } else {
                return false;
            }
        }
    };

    public FruitGoal(BeeEntity beeEntity) {
        super(beeEntity);
    }

    public boolean canBeeStart() {
        if (((BeeEntityInvoker)entity).invokeGetCropsGrownSincePollination() >= 10) {
            return false;
        }
        if (((EntityAccessor)entity).getRandom().nextFloat() > config.value.fruiting.chance) {
            return false;
        }
        if (entity.getWorld().isRaining()) {
            return false;
        }
        Optional<BlockPos> optional = findTarget();
        if (optional.isPresent()) {
            if (entity.hasNectar()) {
                if (((BeeEntityInvoker)entity).invokeIsHiveValid()) {
                    ((BeeEntityAccessor)entity).setFlowerPos(optional.get());
                    ((MobEntityAccessor)entity).getNavigation().startMovingTo((double)entity.getFlowerPos().getX() + 0.5, (double)entity.getFlowerPos().getY() + 0.5, (double)entity.getFlowerPos().getZ() + 0.5, 1.2000000476837158);
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
            BlockPos blockPos = entity.getFlowerPos();
            if (blockPos != null) {
                BlockState blockState = entity.getWorld().getBlockState(blockPos);
                if (blockState.getBlock() == FLOWERING_OAK_LEAVES) {
                    if (blockState.get(Properties.AGE_3) >= 3) {
                        entity.getWorld().syncWorldEvent(2005, blockPos, 0);
                        entity.getWorld().setBlockState(blockPos, FRUITING_OAK_LEAVES.getDefaultState()
                                .with(DISTANCE, blockState.get(DISTANCE))
                                .with(PERSISTENT, blockState.get(PERSISTENT))
                                .with(WATERLOGGED, blockState.get(WATERLOGGED))
                        );
                        ((BeeEntityInvoker)entity).invokeAddCropCounter();
                    }
                }
            }
        }
        running = false;
        ((MobEntityAccessor)entity).getNavigation().stop();
    }

    public boolean isTarget(BlockPos pos) {
        return entity.getWorld().canSetBlock(pos) && entity.getWorld().getBlockState(pos).getBlock() instanceof FloweringLeavesBlock;
    }

    public Optional<BlockPos> findTarget() {
        return findTarget(targetPredicate, config.value.fruiting.distance);
    }

}