package dev.yurisuika.blossom.entity.ai.goal;

import dev.yurisuika.blossom.mixin.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.entity.MobEntityAccessor;
import dev.yurisuika.blossom.mixin.entity.passive.BeeEntityAccessor;
import dev.yurisuika.blossom.mixin.entity.passive.BeeEntityInvoker;
import dev.yurisuika.blossom.mixin.world.biome.BiomeAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static dev.yurisuika.blossom.Blossom.*;
import static dev.yurisuika.blossom.client.option.BlossomConfig.*;
import static net.minecraft.block.LeavesBlock.*;

public class BlossomGoal extends Goal {

    public final BeeEntity entity;
    public final Predicate<BlockState> targetPredicate = (state) -> {
        if (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) {
            return false;
        } else {
            return state.isOf(Blocks.OAK_LEAVES);
        }
    };
    public int blossomingTicks;
    public int lastBlossomingTick;
    public boolean running;
    public Vec3d nextTarget;
    public int ticks;

    public BlossomGoal(BeeEntity beeEntity) {
        super();
        this.entity = beeEntity;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    public boolean checkFilters() {
        RegistryEntry<DimensionType> dimension = entity.getWorld().getDimensionEntry();
        RegistryEntry<Biome> biome = entity.getWorld().getBiome(entity.getBlockPos());
        float temperature = biome.value().getTemperature();
        float downfall = ((BiomeAccessor)(Object)biome.value()).getWeather().downfall();

        AtomicBoolean whitelist = new AtomicBoolean(false);
        if (config.toggle.whitelist) {
            Arrays.stream(config.filter.dimension.whitelist).forEach(entry -> {
                if (entry.startsWith("#")) {
                    TagKey<DimensionType> tag = TagKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier(entry.substring(1)));
                    if (tag != null) {
                        if (dimension.isIn(tag)) {
                            whitelist.set(true);
                        }
                    }
                } else if (Objects.equals(entry, dimension.getKey().get().getValue().toString())) {
                    whitelist.set(true);
                }
            });
            Arrays.stream(config.filter.biome.whitelist).forEach(entry -> {
                if (entry.startsWith("#")) {
                    TagKey<Biome> tag = TagKey.of(RegistryKeys.BIOME, new Identifier(entry.substring(1)));
                    if (tag != null) {
                        if (biome.isIn(tag)) {
                            whitelist.set(true);
                        }
                    }
                } else if (Objects.equals(entry, biome.getKey().get().getValue().toString())) {
                    whitelist.set(true);
                }
            });
        }

        AtomicBoolean blacklist = new AtomicBoolean(true);
        if (config.toggle.blacklist) {
            Arrays.stream(config.filter.dimension.blacklist).forEach(entry -> {
                if (entry.startsWith("#")) {
                    TagKey<DimensionType> tag = TagKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier(entry.substring(1)));
                    if (tag != null) {
                        if (dimension.isIn(tag)) {
                            blacklist.set(false);
                        }
                    }
                } else if (Objects.equals(entry, dimension.getKey().get().getValue().toString())) {
                    blacklist.set(false);
                }
            });
            Arrays.stream(config.filter.biome.blacklist).forEach(entry -> {
                if (entry.startsWith("#")) {
                    TagKey<Biome> tag = TagKey.of(RegistryKeys.BIOME, new Identifier(entry.substring(1)));
                    if (tag != null) {
                        if (biome.isIn(tag)) {
                            blacklist.set(false);
                        }
                    }
                } else if (Objects.equals(entry, biome.getKey().get().getValue().toString())) {
                    blacklist.set(false);
                }
            });
        }

        if (temperature >= config.filter.temperature.min && temperature <= config.filter.temperature.max) {
            if (downfall >= config.filter.downfall.min && downfall <= config.filter.downfall.max) {
                if (!config.toggle.whitelist && !config.toggle.blacklist) {
                    return true;
                }
                if (config.toggle.whitelist && config.toggle.blacklist) {
                    return whitelist.get() && blacklist.get();
                } else if (config.toggle.whitelist) {
                    return whitelist.get();
                } else if (config.toggle.blacklist) {
                    return blacklist.get();
                }
            }
        }
        return false;
    }

    public boolean canBeeStart() {
        if (((BeeEntityInvoker)entity).invokeGetCropsGrownSincePollination() >= 10) {
            return false;
        }
        if (((EntityAccessor)entity).getRandom().nextFloat() > config.value.blossoming.chance) {
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

    public boolean canBeeContinue() {
        if (!running) {
            return false;
        }
        if (!entity.hasFlower()) {
            return false;
        }
        if (entity.getFlowerPos() == null) {
            return false;
        }
        if (((BeeEntityInvoker)entity).invokeGetCropsGrownSincePollination() >= 10) {
            return false;
        }
        if (entity.getWorld().isRaining()) {
            return false;
        }
        if (completed()) {
            return ((EntityAccessor) entity).getRandom().nextFloat() < 0.2F;
        }
        if (entity.age % 20 == 0 && !isTarget(entity.getFlowerPos())) {
            ((BeeEntityAccessor)entity).setFlowerPos(null);
            return false;
        }
        return true;
    }

    public boolean completed() {
        return blossomingTicks > 400;
    }

    public boolean isRunning() {
        return running;
    }

    public void cancel() {
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
            BlockPos blockPos = entity.getFlowerPos();
            if (blockPos != null) {
                BlockState blockState = entity.getWorld().getBlockState(blockPos);
                if (blockState.getBlock() == Blocks.OAK_LEAVES) {
                    entity.getWorld().syncWorldEvent(2005, blockPos, 0);
                    entity.getWorld().setBlockState(blockPos, FLOWERING_OAK_LEAVES.getDefaultState()
                            .with(DISTANCE, blockState.get(DISTANCE))
                            .with(PERSISTENT, blockState.get(PERSISTENT))
                            .with(WATERLOGGED, blockState.get(WATERLOGGED))
                    );
                    ((BeeEntityInvoker)entity).invokeAddCropCounter();
                }
            }
        }
        running = false;
        ((MobEntityAccessor)entity).getNavigation().stop();
    }

    public boolean shouldRunEveryTick() {
        return true;
    }

    public boolean canStart() {
        return canBeeStart() && !entity.hasAngerTime() && checkFilters();
    }

    public boolean shouldContinue() {
        return canBeeContinue() && !entity.hasAngerTime() && checkFilters();
    }

    public void tick() {
        ++ticks;
        if (ticks > 600) {
            ((BeeEntityAccessor)entity).setFlowerPos(null);
        } else {
            if (entity.getFlowerPos() != null) {
                Vec3d vec3d = Vec3d.ofBottomCenter(entity.getFlowerPos()).add(0.0, 0.6000000238418579, 0.0);
                if (vec3d.distanceTo(entity.getPos()) > 1.0) {
                    nextTarget = vec3d;
                    moveToNextTarget();
                } else {
                    if (nextTarget == null) {
                        nextTarget = vec3d;
                    }
                    boolean close = entity.getPos().distanceTo(nextTarget) <= 0.1;
                    boolean chance = true;
                    if (!close && ticks > 600) {
                        ((BeeEntityAccessor)entity).setFlowerPos(null);
                    } else {
                        if (close) {
                            if (((EntityAccessor)entity).getRandom().nextInt(25) == 0) {
                                nextTarget = new Vec3d(vec3d.getX() + (double)getRandomOffset(), vec3d.getY(), vec3d.getZ() + (double)getRandomOffset());
                                ((MobEntityAccessor)entity).getNavigation().stop();
                            } else {
                                chance = false;
                            }
                            entity.getLookControl().lookAt(vec3d.getX(), vec3d.getY(), vec3d.getZ());
                        }
                        if (chance) {
                            moveToNextTarget();
                        }
                        ++blossomingTicks;
                        if (((EntityAccessor)entity).getRandom().nextFloat() < 0.05F && blossomingTicks > lastBlossomingTick + 60) {
                            lastBlossomingTick = blossomingTicks;
                            entity.playSound(SoundEvents.ENTITY_BEE_POLLINATE, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    public boolean isTarget(BlockPos pos) {
        return entity.getWorld().canSetBlock(pos) && entity.getWorld().getBlockState(pos).isOf(Blocks.OAK_LEAVES);
    }

    public void moveToNextTarget() {
        entity.getMoveControl().moveTo(nextTarget.getX(), nextTarget.getY(), nextTarget.getZ(), 0.3499999940395355);
    }

    public float getRandomOffset() {
        return (((EntityAccessor)entity).getRandom().nextFloat() * 2.0F - 1.0F) * 0.33333334F;
    }

    public Optional<BlockPos> findTarget() {
        return findTarget(targetPredicate, config.value.blossoming.distance);
    }

    public Optional<BlockPos> findTarget(Predicate<BlockState> predicate, double searchDistance) {
        BlockPos blockPos = entity.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for(int i = 0; (double)i <= searchDistance; i = i > 0 ? -i : 1 - i) {
            for(int j = 0; (double)j < searchDistance; ++j) {
                for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        mutable.set(blockPos, k, i - 1, l);
                        if (blockPos.isWithinDistance(mutable, searchDistance) && predicate.test(entity.getWorld().getBlockState(mutable))) {
                            return Optional.of(mutable);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

}