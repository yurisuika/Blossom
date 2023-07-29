package dev.yurisuika.blossom.mixin.entity.passive;

import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.mixin.world.biome.BiomeAccessor;
import net.minecraft.block.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.block.LeavesBlock.*;

@Mixin(BeeEntity.class)
public class BeeEntityMixin {

    @Mixin(targets = "net.minecraft.entity.passive.BeeEntity$GrowCropsGoal")
    public static class GrowCropsGoalMixin {

        @Unique
        public BeeEntity entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(BeeEntity entityType, CallbackInfo ci) {
            entity = entityType;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void injectTick(CallbackInfo ci) {
            RegistryEntry<DimensionType> dimension = entity.getWorld().getDimensionEntry();
            RegistryEntry<Biome> biome = entity.getWorld().getBiome(entity.getBlockPos());
            float temperature = biome.value().getTemperature();
            float downfall = ((BiomeAccessor)(Object)biome.value()).getWeather().downfall();

            boolean bl = false;
            if (config.toggle.whitelist) {
                if (Arrays.asList(config.filter.dimension.whitelist).contains(dimension.getKey().get().getValue().toString()) && dimension.getKey().isPresent()) {
                    if (Arrays.asList(config.filter.biome.whitelist).contains(biome.getKey().get().getValue().toString()) && biome.getKey().isPresent()) {
                        bl = true;
                    }
                }
            } else if (config.toggle.blacklist) {
                if (!Arrays.asList(config.filter.dimension.blacklist).contains(dimension.getKey().get().getValue().toString()) && dimension.getKey().isPresent()) {
                    if (!Arrays.asList(config.filter.biome.blacklist).contains(biome.getKey().get().getValue().toString()) && biome.getKey().isPresent()) {
                        bl = true;
                    }
                }
            } else {
                bl = true;
            }

            if (bl) {
                if (temperature >= config.filter.temperature.min && temperature <= config.filter.temperature.max) {
                    if (downfall >= config.filter.downfall.min && downfall <= config.filter.downfall.max) {
                        if (ThreadLocalRandom.current().nextDouble() <= config.value.propagation.chance) {
                            for (int i = 1; i <= 2; ++i) {
                                BlockPos blockPos = entity.getBlockPos().down(i);
                                BlockState blockState = entity.getWorld().getBlockState(blockPos);
                                if (Arrays.stream(Direction.values()).anyMatch(direction -> !entity.getWorld().getBlockState(blockPos.offset(direction)).isSolid())) {
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
                        }
                    }
                }
            }

            if (ThreadLocalRandom.current().nextDouble() <= config.value.fertilization.chance) {
                for (int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.getBlockPos().down(i);
                    BlockState blockState = entity.getWorld().getBlockState(blockPos);
                    if (blockState.isIn(BlockTags.BEE_GROWABLES)) {
                        if (blockState.getBlock() instanceof FloweringLeavesBlock floweringLeavesBlock) {
                            if (!floweringLeavesBlock.isMature(blockState)) {
                                IntProperty age = floweringLeavesBlock.getAgeProperty();
                                entity.getWorld().syncWorldEvent(2005, blockPos, 0);
                                entity.getWorld().setBlockState(blockPos, blockState.with(age, blockState.get(age) + 1));
                                ((BeeEntityInvoker)entity).invokeAddCropCounter();
                            }
                        }
                    }
                }
            }
        }

    }

    @Mixin(targets = "net.minecraft.entity.passive.BeeEntity$PollinateGoal")
    public abstract static class PollinateGoalMixin {

        @ModifyArg(method = "getFlower", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/BeeEntity$PollinateGoal;findFlower(Ljava/util/function/Predicate;D)Ljava/util/Optional;"), index = 0)
        private Predicate<BlockState> modifyGetFlower(Predicate<BlockState> predicate) {
            return predicate.and((state) -> state.isOf(FLOWERING_OAK_LEAVES) ? (state.get(Properties.AGE_7) <= config.value.pollination.age) : true);
        }

    }

}