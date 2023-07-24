package dev.yurisuika.blossom.mixin.entity.passive;

import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.mixin.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.entity.ai.goal.GoalInvoker;
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

            boolean enabled = false;
            if (config.climate.whitelist.enabled) {
                if (Arrays.asList(config.climate.whitelist.dimensions).contains(dimension.getKey().get().getValue().toString()) && dimension.getKey().isPresent()) {
                    if (Arrays.asList(config.climate.whitelist.biomes).contains(biome.getKey().get().getValue().toString()) && biome.getKey().isPresent()) {
                        enabled = true;
                    }
                }
            } else if (config.climate.blacklist.enabled) {
                if (!Arrays.asList(config.climate.blacklist.dimensions).contains(dimension.getKey().get().getValue().toString()) && dimension.getKey().isPresent()) {
                    if (!Arrays.asList(config.climate.blacklist.biomes).contains(biome.getKey().get().getValue().toString()) && biome.getKey().isPresent()) {
                        enabled = true;
                    }
                }
            } else {
                enabled = true;
            }

            if (enabled) {
                float temperature = biome.value().getTemperature();
                float downfall = ((BiomeAccessor)(Object)biome.value()).getWeather().downfall();
                Biome.Precipitation precipitation = biome.value().getPrecipitation(entity.getBlockPos());

                if (Arrays.stream(config.climate.precipitation).anyMatch(precipitation.name()::equalsIgnoreCase)) {
                    if (temperature >= config.climate.temperature.min && temperature <= config.climate.temperature.max) {
                        if (downfall >= config.climate.downfall.min && downfall <= config.climate.downfall.max) {
                            if (((EntityAccessor)entity).getRandom().nextInt(((GoalInvoker)this).invokeGetTickCount((int)(1.0F / config.propagation.chance))) == 0) {
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
            }

            if (((EntityAccessor)entity).getRandom().nextInt(((GoalInvoker)this).invokeGetTickCount((int)(1.0F / config.fertilization.chance))) == 0) {
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
            return predicate.and((state) -> state.isOf(FLOWERING_OAK_LEAVES) ? (state.get(Properties.AGE_7) <= 1) : true);
        }

    }

}