package dev.yurisuika.blossom.data.worldgen.features;

import dev.yurisuika.blossom.Blossom;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.List;

public class BlossomTreeFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Blossom.MOD_ID, "apple"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_BEES_0002 = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Blossom.MOD_ID, "apple_bees_0002"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_BEES_002 = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Blossom.MOD_ID, "apple_bees_002"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_BEES_005 = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Blossom.MOD_ID, "apple_bees_005"));

    public static TreeConfiguration.TreeConfigurationBuilder createApple() {
        return new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlossomBlocks.APPLE_LOG), new StraightTrunkPlacer(3, 1, 0), new RandomizedIntStateProvider(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState> builder().add(BlossomBlocks.APPLE_LEAVES.defaultBlockState(), 1).add(BlossomBlocks.FLOWERING_APPLE_LEAVES.defaultBlockState(), 1)), FloweringLeavesBlock.AGE, UniformInt.of(0, 7)), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))
                .ignoreVines();
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        context.register(APPLE, new ConfiguredFeature<>(Feature.TREE, createApple().build()));
        context.register(APPLE_BEES_0002, new ConfiguredFeature<>(Feature.TREE, createApple().decorators(List.of(new BeehiveDecorator(0.002F))).build()));
        context.register(APPLE_BEES_002, new ConfiguredFeature<>(Feature.TREE, createApple().decorators(List.of(new BeehiveDecorator(0.02F))).build()));
        context.register(APPLE_BEES_005, new ConfiguredFeature<>(Feature.TREE, createApple().decorators(List.of(new BeehiveDecorator(0.05F))).build()));
    }

}