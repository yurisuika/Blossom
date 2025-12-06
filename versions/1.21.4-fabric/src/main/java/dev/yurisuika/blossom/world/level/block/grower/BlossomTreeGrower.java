package dev.yurisuika.blossom.world.level.block.grower;

import dev.yurisuika.blossom.data.worldgen.features.BlossomTreeFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class BlossomTreeGrower {

    public static final TreeGrower APPLE = new TreeGrower("apple", Optional.empty(), Optional.of(BlossomTreeFeatures.APPLE), Optional.of(BlossomTreeFeatures.APPLE_BEES_005));

}