package dev.yurisuika.blossom.registry;

import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class FruitableLeavesRegistry {

    public static final Map<FloweringLeavesBlock, FruitingLeavesBlock> FRUITABLES = new HashMap<>();

    public static void add(Block floweringLeavesBlock, Block fruitingLeavesBlock) {
        FRUITABLES.put((FloweringLeavesBlock) floweringLeavesBlock, (FruitingLeavesBlock) fruitingLeavesBlock);
    }

}