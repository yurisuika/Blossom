package dev.yurisuika.blossom.world.entity.ai.goal;

import dev.yurisuika.blossom.mixin.world.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.world.entity.MobAccessor;
import dev.yurisuika.blossom.mixin.world.entity.animal.BeeInvoker;
import dev.yurisuika.blossom.mixin.world.level.biome.BiomeAccessor;
import dev.yurisuika.blossom.util.config.Option;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class BlossomLeavesGoal extends Goal {

    public final Bee entity;
    public Predicate<BlockState> validBlocks;
    public int successfulBlossomingTicks;
    public int lastSoundPlayedTick;
    public boolean blossoming;
    public Vec3 hoverPos;
    public int blossomingTicks;
    public float goalChance;

    public BlossomLeavesGoal(Bee bee) {
        super();
        this.entity = bee;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.validBlocks = state -> (!state.hasProperty(BlockStateProperties.WATERLOGGED) || !state.getValue(BlockStateProperties.WATERLOGGED)) && state.is(Blocks.OAK_LEAVES);
        this.goalChance = Option.getBlossomingChance();
    }

    public boolean checkFilters() {
        Holder<DimensionType> dimension = entity.level().dimensionTypeRegistration();
        Holder<Biome> biome = entity.level().getBiome(entity.blockPosition());
        float temperature = biome.value().getBaseTemperature();
        float downfall = ((BiomeAccessor) (Object) biome.value()).getClimateSettings().downfall();

        AtomicBoolean whitelist = new AtomicBoolean(false);
        if (Option.getWhitelist()) {
            Arrays.stream(Option.getDimensionWhitelist()).filter(entry -> entry.startsWith("#") && dimension.is(TagKey.create(Registries.DIMENSION_TYPE, ResourceLocation.tryParse(entry.substring(1)))) || Objects.equals(entry, dimension.unwrapKey().get().location().toString())).map(entry -> true).forEach(whitelist::set);
            Arrays.stream(Option.getBiomeWhitelist()).filter(entry -> entry.startsWith("#") && biome.is(TagKey.create(Registries.BIOME, ResourceLocation.tryParse(entry.substring(1)))) || Objects.equals(entry, biome.unwrapKey().get().location().toString())).map(entry -> true).forEach(whitelist::set);
        }

        AtomicBoolean blacklist = new AtomicBoolean(true);
        if (Option.getBlacklist()) {
            Arrays.stream(Option.getDimensionBlacklist()).filter(entry -> entry.startsWith("#") && dimension.is(TagKey.create(Registries.DIMENSION_TYPE, ResourceLocation.tryParse(entry.substring(1)))) || Objects.equals(entry, dimension.unwrapKey().get().location().toString())).map(entry -> false).forEach(blacklist::set);
            Arrays.stream(Option.getBiomeBlacklist()).filter(entry -> entry.startsWith("#") && biome.is(TagKey.create(Registries.BIOME, ResourceLocation.tryParse(entry.substring(1)))) || Objects.equals(entry, biome.unwrapKey().get().location().toString())).map(entry -> false).forEach(blacklist::set);
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

    public boolean canBeeUse() {
        if (((BeeInvoker) entity).invokeGetCropsGrownSincePollination() >= 10) {
            return false;
        }
        if (((EntityAccessor) entity).getRandom().nextFloat() > goalChance) {
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
        if (!blossoming) {
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
        if (hasBlossomedLongEnough()) {
            return ((EntityAccessor) entity).getRandom().nextFloat() < 0.2F;
        }
        if (entity.tickCount % 20 == 0 && !((BeeInterface) entity).areLeavesValid(((BeeInterface) entity).getSavedLeavesPos())) {
            ((BeeInterface) entity).setSavedLeavesPos(null);
            return false;
        }
        return true;
    }

    public boolean hasBlossomedLongEnough() {
        return successfulBlossomingTicks > 400;
    }

    public boolean isBlossoming() {
        return blossoming;
    }

    public void stopBlossoming() {
        blossoming = false;
    }

    public void start() {
        successfulBlossomingTicks = 0;
        blossomingTicks = 0;
        lastSoundPlayedTick = 0;
        blossoming = true;
    }

    public void stop() {
        if (hasBlossomedLongEnough()) {
            BlockPos blockPos = ((BeeInterface) entity).getSavedLeavesPos();
            if (blockPos != null) {
                BlockState blockState = entity.level().getBlockState(blockPos);
                if (blockState.getBlock() == Blocks.OAK_LEAVES) {
                    entity.level().levelEvent(2005, blockPos, 0);
                    entity.level().setBlockAndUpdate(blockPos, BlossomBlocks.FLOWERING_OAK_LEAVES.defaultBlockState()
                            .setValue(FloweringLeavesBlock.DISTANCE, blockState.getValue(LeavesBlock.DISTANCE))
                            .setValue(FloweringLeavesBlock.PERSISTENT, blockState.getValue(LeavesBlock.PERSISTENT))
                            .setValue(FloweringLeavesBlock.WATERLOGGED, blockState.getValue(LeavesBlock.WATERLOGGED))
                    );
                    ((BeeInvoker) entity).invokeIncrementNumCropsGrownSincePollination();
                }
            }
        }
        blossoming = false;
        ((MobAccessor) entity).getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public boolean canUse() {
        return canBeeUse() && !entity.isAngry() && checkFilters();
    }

    public boolean canContinueToUse() {
        return canBeeContinueToUse() && !entity.isAngry() && checkFilters();
    }

    public void tick() {
        ++blossomingTicks;
        if (blossomingTicks > 600) {
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
                    if (!close && blossomingTicks > 600) {
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
                        ++successfulBlossomingTicks;
                        if (((EntityAccessor) entity).getRandom().nextFloat() < 0.05F && successfulBlossomingTicks > lastSoundPlayedTick + 60) {
                            lastSoundPlayedTick = successfulBlossomingTicks;
                            entity.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
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
        return findNearestBlock(validBlocks, 10.0D);
    }

    public Optional<BlockPos> findNearestBlock(Predicate<BlockState> predicate, double distance) {
        BlockPos blockPos = entity.blockPosition();
        MutableBlockPos mutableBlockPos = new MutableBlockPos();
        for (int i = 0; i <= distance; i = i > 0 ? -i : 1 - i) {
            for (int j = 0; j < distance; ++j) {
                for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        mutableBlockPos.setWithOffset(blockPos, k, i - 1, l);
                        if (blockPos.closerThan(mutableBlockPos, distance) && predicate.test(entity.level().getBlockState(mutableBlockPos))) {
                            return Optional.of(mutableBlockPos);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

}