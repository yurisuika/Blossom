package dev.yurisuika.blossom.world.level.item;

import dev.yurisuika.blossom.world.entity.vehicle.BlossomBoat;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import net.minecraft.world.item.*;

public class BlossomItems {

    public static final Item APPLE_PLANKS = new BlockItem(BlossomBlocks.APPLE_PLANKS, new Item.Properties());
    public static final Item APPLE_SAPLING = new BlockItem(BlossomBlocks.APPLE_SAPLING, new Item.Properties());
    public static final Item APPLE_LOG = new BlockItem(BlossomBlocks.APPLE_LOG, new Item.Properties());
    public static final Item STRIPPED_APPLE_LOG = new BlockItem(BlossomBlocks.STRIPPED_APPLE_LOG, new Item.Properties());
    public static final Item APPLE_WOOD = new BlockItem(BlossomBlocks.APPLE_WOOD, new Item.Properties());
    public static final Item STRIPPED_APPLE_WOOD = new BlockItem(BlossomBlocks.STRIPPED_APPLE_WOOD, new Item.Properties());
    public static final Item APPLE_LEAVES = new BlockItem(BlossomBlocks.APPLE_LEAVES, new Item.Properties());
    public static final Item FLOWERING_APPLE_LEAVES = new BlockItem(BlossomBlocks.FLOWERING_APPLE_LEAVES, new Item.Properties());
    public static final Item FRUITING_APPLE_LEAVES = new BlockItem(BlossomBlocks.FRUITING_APPLE_LEAVES, new Item.Properties());
    public static final Item APPLE_SIGN = new SignItem(new Item.Properties().stacksTo(16), BlossomBlocks.APPLE_SIGN, BlossomBlocks.APPLE_WALL_SIGN);
    public static final Item APPLE_HANGING_SIGN = new HangingSignItem(BlossomBlocks.APPLE_HANGING_SIGN, BlossomBlocks.APPLE_WALL_HANGING_SIGN, new Item.Properties().stacksTo(16));
    public static final Item APPLE_PRESSURE_PLATE = new BlockItem(BlossomBlocks.APPLE_PRESSURE_PLATE, new Item.Properties());
    public static final Item APPLE_TRAPDOOR = new BlockItem(BlossomBlocks.APPLE_TRAPDOOR, new Item.Properties());
    public static final Item APPLE_BUTTON = new BlockItem(BlossomBlocks.APPLE_BUTTON, new Item.Properties());
    public static final Item APPLE_STAIRS = new BlockItem(BlossomBlocks.APPLE_STAIRS, new Item.Properties());
    public static final Item APPLE_SLAB = new BlockItem(BlossomBlocks.APPLE_SLAB, new Item.Properties());
    public static final Item APPLE_FENCE_GATE = new BlockItem(BlossomBlocks.APPLE_FENCE_GATE, new Item.Properties());
    public static final Item APPLE_FENCE = new BlockItem(BlossomBlocks.APPLE_FENCE, new Item.Properties());
    public static final Item APPLE_DOOR = new DoubleHighBlockItem(BlossomBlocks.APPLE_DOOR, new Item.Properties());
    public static final Item APPLE_BOAT = new BoatItem(false, BlossomBoat.Type.APPLE, new Item.Properties().stacksTo(1));
    public static final Item APPLE_CHEST_BOAT = new BoatItem(true, BlossomBoat.Type.APPLE, new Item.Properties().stacksTo(1));

}