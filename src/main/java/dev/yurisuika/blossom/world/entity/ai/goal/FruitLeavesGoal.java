package dev.yurisuika.blossom.world.entity.ai.goal;

import dev.yurisuika.blossom.mixin.world.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.world.entity.MobAccessor;
import dev.yurisuika.blossom.mixin.world.entity.animal.BeeInvoker;
import dev.yurisuika.blossom.registry.FruitableLeavesRegistry;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class FruitLeavesGoal extends Goal {

    public final Bee entity;
    public Predicate<BlockState> validBlocks;
    public int successfulFruitingTicks;
    public int lastSoundPlayedTick;
    public boolean fruiting;
    public Vec3 hoverPos;
    public int fruitingTicks;
    public Map<Long, Long> unreachableLeavesCache = new HashMap();

    public FruitLeavesGoal(Bee bee) {
        super();
        this.entity = bee;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.validBlocks = state -> (!state.hasProperty(BlockStateProperties.WATERLOGGED) || !state.getValue(BlockStateProperties.WATERLOGGED)) && (FruitableLeavesRegistry.FRUITABLES.containsKey(state.getBlock()) && state.getValue(BlockStateProperties.AGE_7) >= 7);
    }

    public boolean canBeeUse() {
        if (((BeeInvoker) entity).invokeGetCropsGrownSincePollination() >= 10) {
            return false;
        }
        if (((EntityAccessor) entity).getRandom().nextFloat() > 0.2F) {
            return false;
        }
        if (entity.level().isRaining()) {
            return false;
        }
        if (findNearbyLeaves().isEmpty()) {
            return false;
        }
        if (!entity.hasNectar()) {
            return false;
        }
        if (!((BeeInvoker) entity).invokeIsHiveValid()) {
            return false;
        }
        ((BeeInterface) entity).setSavedLeavesPos(findNearbyLeaves().get());
        ((MobAccessor) entity).getNavigation().moveTo(((BeeInterface) entity).getSavedLeavesPos().getX() + 0.5D, ((BeeInterface) entity).getSavedLeavesPos().getY() + 0.5D, ((BeeInterface) entity).getSavedLeavesPos().getZ() + 0.5D, 1.2000000476837158D);
        return true;
    }

    public boolean canBeeContinueToUse() {
        if (!fruiting) {
            return false;
        }
        if (!((BeeInterface) entity).hasSavedLeavesPos()) {
            return false;
        }
        if (((BeeInterface) entity).getSavedLeavesPos() == null) {
            return false;
        }
        if (((BeeInvoker) entity).invokeGetCropsGrownSincePollination() >= 10) {
            return false;
        }
        if (entity.level().isRaining()) {
            return false;
        }
        if (hasFruitedLongEnough()) {
            return ((EntityAccessor) entity).getRandom().nextFloat() < 0.2F;
        }
        return true;
    }

    public boolean hasFruitedLongEnough() {
        return successfulFruitingTicks > 400;
    }

    public boolean isFruiting() {
        return fruiting;
    }

    public void stopFruiting() {
        fruiting = false;
    }

    public void start() {
        successfulFruitingTicks = 0;
        fruitingTicks = 0;
        lastSoundPlayedTick = 0;
        fruiting = true;
    }

    public void stop() {
        if (hasFruitedLongEnough()) {
            BlockPos blockPos = ((BeeInterface) entity).getSavedLeavesPos();
            if (blockPos != null) {
                BlockState blockState = entity.level().getBlockState(blockPos);
                if (validBlocks.test(blockState)) {
                    if (blockState.getValue(BlockStateProperties.AGE_7) >= 7) {
                        entity.level().levelEvent(2005, blockPos, 0);
                        entity.level().setBlockAndUpdate(blockPos, FruitableLeavesRegistry.FRUITABLES.get(blockState.getBlock()).defaultBlockState()
                                .setValue(FruitingLeavesBlock.DISTANCE, blockState.getValue(FloweringLeavesBlock.DISTANCE))
                                .setValue(FruitingLeavesBlock.PERSISTENT, blockState.getValue(FloweringLeavesBlock.PERSISTENT))
                                .setValue(FruitingLeavesBlock.WATERLOGGED, blockState.getValue(FloweringLeavesBlock.WATERLOGGED))
                        );
                        IntStream.range(0, 10).forEach(i -> ((BeeInvoker) entity).invokeIncrementNumCropsGrownSincePollination());
                    }
                }
            }
        }
        fruiting = false;
        ((MobAccessor) entity).getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public boolean canUse() {
        return canBeeUse() && !entity.isAngry() && Validate.passesFilters(entity.level(), entity.blockPosition());
    }

    public boolean canContinueToUse() {
        return canBeeContinueToUse() && !entity.isAngry() && Validate.passesFilters(entity.level(), entity.blockPosition());
    }

    public void tick() {
        if (((BeeInterface) entity).hasSavedLeavesPos()) {
            ++fruitingTicks;
            if (fruitingTicks > 600) {
                ((BeeInterface) entity).setSavedLeavesPos(null);
            } else {
                if (((BeeInterface) entity).getSavedLeavesPos() != null) {
                    Vec3 vec3 = Vec3.atBottomCenterOf(((BeeInterface) entity).getSavedLeavesPos()).add(0.0D, 0.6000000238418579D, 0.0D);
                    if (vec3.distanceTo(entity.position()) > 1.0D) {
                        hoverPos = vec3;
                        setWantedPos();
                    } else {
                        if (hoverPos == null) {
                            hoverPos = vec3;
                        }
                        boolean close = entity.position().distanceTo(hoverPos) <= 0.1D;
                        boolean chance = true;
                        if (!close && fruitingTicks > 600) {
                            ((BeeInterface) entity).setSavedLeavesPos(null);
                        } else {
                            if (close) {
                                if (((EntityAccessor) entity).getRandom().nextInt(25) == 0) {
                                    hoverPos = new Vec3(vec3.x() + getRandomOffset(), vec3.y(), vec3.z() + getRandomOffset());
                                    ((MobAccessor) entity).getNavigation().stop();
                                } else {
                                    chance = false;
                                }
                                entity.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
                            }
                            if (chance) {
                                setWantedPos();
                            }
                            ++successfulFruitingTicks;
                            if (((EntityAccessor) entity).getRandom().nextFloat() < 0.05F && successfulFruitingTicks > lastSoundPlayedTick + 60) {
                                lastSoundPlayedTick = successfulFruitingTicks;
                                entity.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }
    }

    public void setWantedPos() {
        entity.getMoveControl().setWantedPosition(hoverPos.x(), hoverPos.y(), hoverPos.z(), 0.3499999940395355D);
    }

    public float getRandomOffset() {
        return (((EntityAccessor) entity).getRandom().nextFloat() * 2.0F - 1.0F) * 0.33333334F;
    }

    public Optional<BlockPos> findNearbyLeaves() {
        return findNearestBlock(validBlocks, 10);
    }

    public Optional<BlockPos> findNearestBlock(Predicate<BlockState> predicate, int distance) {
        Map<Long, Long> map = new HashMap();

        for (BlockPos blockPos : BlockPos.withinManhattan(entity.blockPosition(), distance, distance, distance)) {
            long l = unreachableLeavesCache.getOrDefault(blockPos.asLong(), Long.MIN_VALUE);
            if (entity.level().getGameTime() < l) {
                map.put(blockPos.asLong(), l);
            } else if (predicate.test(entity.level().getBlockState(blockPos))) {
                Path path = ((MobAccessor) entity).getNavigation().createPath(blockPos, 1);
                if (path != null && path.canReach()) {
                    return Optional.of(blockPos);
                }

                map.put(blockPos.asLong(), entity.level().getGameTime() + 600L);
            }
        }

        unreachableLeavesCache = map;
        return Optional.empty();
    }

}