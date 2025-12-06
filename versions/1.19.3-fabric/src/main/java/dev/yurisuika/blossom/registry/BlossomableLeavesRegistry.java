package dev.yurisuika.blossom.registry;

import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;

import java.util.HashMap;
import java.util.Map;

public class BlossomableLeavesRegistry {

    public static final Map<LeavesBlock, FloweringLeavesBlock> BLOSSOMABLES = new HashMap<>();

    public static void add(Block leavesBlock, Block floweringLeavesBlock) {
        BLOSSOMABLES.put((LeavesBlock) leavesBlock, (FloweringLeavesBlock) floweringLeavesBlock);
    }

}