package dev.yurisuika.blossom.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;

import java.util.HashMap;
import java.util.Map;

public class HarvestableFruitRegistry {

    public static final Map<LeavesBlock, Item> HARVESTABLES = new HashMap<>();

    public static void add(Block leavesBlock, Item fruit) {
        HARVESTABLES.put((LeavesBlock) leavesBlock, fruit);
    }

}