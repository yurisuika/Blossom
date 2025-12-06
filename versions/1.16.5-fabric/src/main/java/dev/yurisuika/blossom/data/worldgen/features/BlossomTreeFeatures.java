package dev.yurisuika.blossom.data.worldgen.features;

import com.google.common.collect.ImmutableList;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class BlossomTreeFeatures {

    public static final ConfiguredFeature<TreeConfiguration, ?> APPLE = new ConfiguredFeature<>(Feature.TREE, createApple().build());
    public static final ConfiguredFeature<TreeConfiguration, ?> APPLE_BEES_0002 = new ConfiguredFeature<>(Feature.TREE, createApple().decorators(ImmutableList.of(new BeehiveDecorator(0.002F))).build());
    public static final ConfiguredFeature<TreeConfiguration, ?> APPLE_BEES_002 = new ConfiguredFeature<>(Feature.TREE, createApple().decorators(ImmutableList.of(new BeehiveDecorator(0.02F))).build());
    public static final ConfiguredFeature<TreeConfiguration, ?> APPLE_BEES_005 = new ConfiguredFeature<>(Feature.TREE, createApple().decorators(ImmutableList.of(new BeehiveDecorator(0.05F))).build());

    public static TreeConfiguration.TreeConfigurationBuilder createApple() {
        return new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(BlossomBlocks.APPLE_LOG.defaultBlockState()), new WeightedStateProvider().add(BlossomBlocks.APPLE_LEAVES.defaultBlockState(), 1).add(BlossomBlocks.FLOWERING_APPLE_LEAVES.defaultBlockState().setValue(FloweringLeavesBlock.AGE, 7), 1), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(3, 1, 0), new TwoLayersFeatureSize(1, 0, 1))
                .ignoreVines();
    }

}