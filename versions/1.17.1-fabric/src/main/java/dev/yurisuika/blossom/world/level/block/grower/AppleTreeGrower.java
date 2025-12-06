package dev.yurisuika.blossom.world.level.block.grower;

import dev.yurisuika.blossom.data.worldgen.features.BlossomTreeFeatures;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import java.util.Random;

public class AppleTreeGrower extends AbstractTreeGrower {

    @Override
    public ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random random, boolean largeHive) {
        return largeHive ? BlossomTreeFeatures.APPLE_BEES_005 : BlossomTreeFeatures.APPLE;
    }

}