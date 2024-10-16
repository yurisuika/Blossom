package dev.yurisuika.blossom.world.entity.ai.goal;

import dev.yurisuika.blossom.Blossom;
import dev.yurisuika.blossom.mixin.world.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.world.entity.MobAccessor;
import dev.yurisuika.blossom.mixin.world.entity.animal.BeeAccessor;
import dev.yurisuika.blossom.mixin.world.entity.animal.BeeInvoker;
import dev.yurisuika.blossom.util.config.Option;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class BlossomGoal extends Goal {

    public final Bee entity;
    public final Predicate<BlockState> targetPredicate = state -> state.is(Blocks.OAK_LEAVES);
    public int blossomingTicks;
    public int lastBlossomingTick;
    public boolean running;
    public Vec3 nextTarget;
    public int ticks;

    public BlossomGoal(Bee bee) {
        super();
        this.entity = bee;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean checkFilters() {
        DimensionType dimension = entity.getCommandSenderWorld().dimensionType();
        Biome biome = entity.getCommandSenderWorld().getBiome(entity.blockPosition());
        float temperature = biome.getBaseTemperature();
        float downfall = biome.getDownfall();

        AtomicBoolean whitelist = new AtomicBoolean(false);
        if (Option.getWhitelist()) {
            Arrays.stream(Option.getDimensionWhitelist()).forEach(entry -> {
                if (Objects.equals(entry, entity.getCommandSenderWorld().registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getKey(dimension).toString())) {
                    whitelist.set(true);
                }
            });
            Arrays.stream(Option.getBiomeWhitelist()).forEach(entry -> {
                if (Objects.equals(entry, entity.getCommandSenderWorld().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome).toString())) {
                    whitelist.set(true);
                }
            });
        }

        AtomicBoolean blacklist = new AtomicBoolean(true);
        if (Option.getBlacklist()) {
            Arrays.stream(Option.getDimensionBlacklist()).forEach(entry -> {
                if (Objects.equals(entry, entity.getCommandSenderWorld().registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getKey(dimension).toString())) {
                    blacklist.set(false);
                }
            });
            Arrays.stream(Option.getBiomeBlacklist()).forEach(entry -> {
                if (Objects.equals(entry, entity.getCommandSenderWorld().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome).toString())) {
                    blacklist.set(false);
                }
            });
        }

        if (temperature >= Option.getTemperatureMin() && temperature <= Option.getTemperatureMax()) {
            if (downfall >= Option.getDownfallMin() && downfall <= Option.getDownfallMax()) {
                if (!Option.getWhitelist() && !Option.getBlacklist()) {
                    return true;
                }
                if (Option.getWhitelist() && Option.getBlacklist()) {
                    return whitelist.get() && blacklist.get();
                } else if (Option.getWhitelist()) {
                    return whitelist.get();
                } else if (Option.getBlacklist()) {
                    return blacklist.get();
                }
            }
        }
        return false;
    }

    public boolean canBeeStart() {
        if (((BeeInvoker) entity).invokeGetCropsGrownSincePollination() >= 10) {
            return false;
        }
        if (((EntityAccessor) entity).getRandom().nextFloat() > Option.getBlossomingChance()) {
            return false;
        }
        if (entity.getCommandSenderWorld().isRaining()) {
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

    public boolean canBeeContinue() {
        if (!running) {
            return false;
        }
        if (!entity.hasSavedFlowerPos()) {
            return false;
        }
        if (entity.getSavedFlowerPos() == null) {
            return false;
        }
        if (((BeeInvoker) entity).invokeGetCropsGrownSincePollination() >= 10) {
            return false;
        }
        if (entity.getCommandSenderWorld().isRaining()) {
            return false;
        }
        if (completed()) {
            return ((EntityAccessor) entity).getRandom().nextFloat() < 0.2F;
        }
        if (entity.tickCount % 20 == 0 && !isTarget(entity.getSavedFlowerPos())) {
            ((BeeAccessor) entity).setSavedFlowerPos(null);
            return false;
        }
        return true;
    }

    public boolean completed() {
        return blossomingTicks > 400;
    }

    public boolean isPollinating() {
        return running;
    }

    public void stopPollinating() {
        running = false;
    }

    public void start() {
        blossomingTicks = 0;
        ticks = 0;
        lastBlossomingTick = 0;
        running = true;
    }

    public void stop() {
        if (completed()) {
            BlockPos blockPos = entity.getSavedFlowerPos();
            if (blockPos != null) {
                BlockState blockState = entity.getCommandSenderWorld().getBlockState(blockPos);
                if (blockState.getBlock() == Blocks.OAK_LEAVES) {
                    entity.getCommandSenderWorld().levelEvent(2005, blockPos, 0);
                    entity.getCommandSenderWorld().setBlockAndUpdate(blockPos, Blossom.FLOWERING_OAK_LEAVES.get().defaultBlockState()
                            .setValue(FloweringLeavesBlock.DISTANCE, blockState.getValue(FloweringLeavesBlock.DISTANCE))
                            .setValue(FloweringLeavesBlock.PERSISTENT, blockState.getValue(FloweringLeavesBlock.PERSISTENT))
                    );
                    ((BeeInvoker) entity).invokeIncrementNumCropsGrownSincePollination();
                }
            }
        }
        running = false;
        ((MobAccessor) entity).getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public boolean canUse() {
        return canBeeStart() && !entity.isAngry() && checkFilters();
    }

    public boolean canContinueToUse() {
        return canBeeContinue() && !entity.isAngry() && checkFilters();
    }

    public void tick() {
        ++ticks;
        if (ticks > 600) {
            ((BeeAccessor) entity).setSavedFlowerPos(null);
        } else {
            if (entity.getSavedFlowerPos() != null) {
                Vec3 vec3 = Vec3.atBottomCenterOf(entity.getSavedFlowerPos()).add(0.0D, 0.6000000238418579D, 0.0D);
                if (vec3.distanceTo(entity.position()) > 1.0) {
                    nextTarget = vec3;
                    moveToNextTarget();
                } else {
                    if (nextTarget == null) {
                        nextTarget = vec3;
                    }
                    boolean close = entity.position().distanceTo(nextTarget) <= 0.1D;
                    boolean chance = true;
                    if (!close && ticks > 600) {
                        ((BeeAccessor) entity).setSavedFlowerPos(null);
                    } else {
                        if (close) {
                            if (((EntityAccessor) entity).getRandom().nextInt(25) == 0) {
                                nextTarget = new Vec3(vec3.x() + (double)getRandomOffset(), vec3.y(), vec3.z() + (double)getRandomOffset());
                                ((MobAccessor) entity).getNavigation().stop();
                            } else {
                                chance = false;
                            }
                            entity.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
                        }
                        if (chance) {
                            moveToNextTarget();
                        }
                        ++blossomingTicks;
                        if (((EntityAccessor) entity).getRandom().nextFloat() < 0.05F && blossomingTicks > lastBlossomingTick + 60) {
                            lastBlossomingTick = blossomingTicks;
                            entity.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    public boolean isTarget(BlockPos pos) {
        return entity.getCommandSenderWorld().isLoaded(pos) && entity.getCommandSenderWorld().getBlockState(pos).is(Blocks.OAK_LEAVES);
    }

    public void moveToNextTarget() {
        entity.getMoveControl().setWantedPosition(nextTarget.x(), nextTarget.y(), nextTarget.z(), 0.3499999940395355D);
    }

    public float getRandomOffset() {
        return (((EntityAccessor) entity).getRandom().nextFloat() * 2.0F - 1.0F) * 0.33333334F;
    }

    public Optional<BlockPos> findTarget() {
        return findTarget(targetPredicate, Option.getBlossomingDistance());
    }

    public Optional<BlockPos> findTarget(Predicate<BlockState> predicate, double searchDistance) {
        BlockPos blockPos = entity.blockPosition();
        MutableBlockPos mutableBlockPos = new MutableBlockPos();
        for(int i = 0; (double)i <= searchDistance; i = i > 0 ? -i : 1 - i) {
            for(int j = 0; (double)j < searchDistance; ++j) {
                for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        mutableBlockPos.setWithOffset(blockPos, k, i - 1, l);
                        if (blockPos.closerThan(mutableBlockPos, searchDistance) && predicate.test(entity.getCommandSenderWorld().getBlockState(mutableBlockPos))) {
                            return Optional.of(mutableBlockPos);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

}