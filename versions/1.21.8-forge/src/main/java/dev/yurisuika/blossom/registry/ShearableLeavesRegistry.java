package dev.yurisuika.blossom.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;

import java.util.HashMap;
import java.util.Map;

public class ShearableLeavesRegistry {

    public static final Map<LeavesBlock, LeavesBlock> SHEARABLES = new HashMap<>();

    public static void add(Block shearableLeavesBlock, Block leavesBlock) {
        SHEARABLES.put((LeavesBlock) shearableLeavesBlock, (LeavesBlock) leavesBlock);
    }

}