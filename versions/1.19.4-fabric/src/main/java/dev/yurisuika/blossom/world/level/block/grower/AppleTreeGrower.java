package dev.yurisuika.blossom.world.level.block.grower;

import dev.yurisuika.blossom.data.worldgen.features.BlossomTreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class AppleTreeGrower extends AbstractTreeGrower {

    @Override
    public ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        return hasFlowers ? BlossomTreeFeatures.APPLE_BEES_005 : BlossomTreeFeatures.APPLE;
    }

}