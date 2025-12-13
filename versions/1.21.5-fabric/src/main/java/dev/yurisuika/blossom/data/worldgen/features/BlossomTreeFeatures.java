package dev.yurisuika.blossom.data.worldgen.features;

import com.google.common.collect.ImmutableList;
import dev.yurisuika.blossom.Blossom;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLogsDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.PlaceOnGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.List;

public class BlossomTreeFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "apple"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_BEES_0002 = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "apple_bees_0002"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_BEES_002 = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "apple_bees_002"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_BEES_005 = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "apple_bees_005"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_LEAF_LITTER = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "apple_leaf_litter"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_BEES_0002_LEAF_LITTER = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "apple_bees_0002_leaf_litter"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_APPLE_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "fallen_apple_tree"));

    public static TreeConfiguration.TreeConfigurationBuilder createApple() {
        return new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlossomBlocks.APPLE_LOG), new StraightTrunkPlacer(3, 1, 0), new RandomizedIntStateProvider(new WeightedStateProvider(WeightedList.of(new Weighted<>(BlossomBlocks.APPLE_LEAVES.defaultBlockState(), 1), new Weighted<>(BlossomBlocks.FLOWERING_APPLE_LEAVES.defaultBlockState(), 1))), FloweringLeavesBlock.AGE, UniformInt.of(0, 7)), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))
                .ignoreVines();
    }

    public static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenApple() {
        return new FallenTreeConfiguration.FallenTreeConfigurationBuilder(BlockStateProvider.simple(BlossomBlocks.APPLE_LOG), UniformInt.of(4, 7))
                .logDecorators(ImmutableList.of(new AttachedToLogsDecorator(0.1F, new WeightedStateProvider(WeightedList.of(new Weighted<>(Blocks.RED_MUSHROOM.defaultBlockState(), 2), new Weighted<>(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1))), List.of(Direction.UP))))
                .stumpDecorators(ImmutableList.of(TrunkVineDecorator.INSTANCE));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(APPLE, new ConfiguredFeature<>(Feature.TREE, createApple().build()));
        context.register(APPLE_BEES_0002, new ConfiguredFeature<>(Feature.TREE, createApple().decorators(List.of(new BeehiveDecorator(0.002F))).build()));
        context.register(APPLE_BEES_002, new ConfiguredFeature<>(Feature.TREE, createApple().decorators(List.of(new BeehiveDecorator(0.02F))).build()));
        context.register(APPLE_BEES_005, new ConfiguredFeature<>(Feature.TREE, createApple().decorators(List.of(new BeehiveDecorator(0.05F))).build()));
        context.register(APPLE_LEAF_LITTER, new ConfiguredFeature<>(Feature.TREE, createApple().decorators(ImmutableList.of(new PlaceOnGroundDecorator(96, 4, 2, new WeightedStateProvider(VegetationFeatures.leafLitterPatchBuilder(1, 3))), new PlaceOnGroundDecorator(150, 2, 2, new WeightedStateProvider(VegetationFeatures.leafLitterPatchBuilder(1, 4))))).build()));
        context.register(APPLE_BEES_0002_LEAF_LITTER, new ConfiguredFeature<>(Feature.TREE, createApple().decorators(List.of(new BeehiveDecorator(0.002F), new PlaceOnGroundDecorator(96, 4, 2, new WeightedStateProvider(VegetationFeatures.leafLitterPatchBuilder(1, 3))), new PlaceOnGroundDecorator(150, 2, 2, new WeightedStateProvider(VegetationFeatures.leafLitterPatchBuilder(1, 4))))).build()));
        context.register(FALLEN_APPLE_TREE, new ConfiguredFeature<>(Feature.FALLEN_TREE, createFallenApple().build()));
    }

}