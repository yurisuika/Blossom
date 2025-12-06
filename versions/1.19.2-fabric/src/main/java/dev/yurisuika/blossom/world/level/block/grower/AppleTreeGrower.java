package dev.yurisuika.blossom.world.level.block.grower;

import dev.yurisuika.blossom.data.worldgen.features.BlossomTreeFeatures;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class AppleTreeGrower extends AbstractTreeGrower {

    @Override
    public Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean largeHive) {
        return largeHive ? Holder.direct(BlossomTreeFeatures.APPLE_BEES_005) : Holder.direct(BlossomTreeFeatures.APPLE);
    }

}