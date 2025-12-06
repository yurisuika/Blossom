package dev.yurisuika.blossom.world.level.item;

import dev.yurisuika.blossom.world.entity.BlossomEntityType;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public class BlossomItems {

    public static final Item APPLE_PLANKS = new BlockItem(BlossomBlocks.APPLE_PLANKS, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_planks"))));
    public static final Item APPLE_SAPLING = new BlockItem(BlossomBlocks.APPLE_SAPLING, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_sapling"))));
    public static final Item APPLE_LOG = new BlockItem(BlossomBlocks.APPLE_LOG, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_log"))));
    public static final Item STRIPPED_APPLE_LOG = new BlockItem(BlossomBlocks.STRIPPED_APPLE_LOG, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "stripped_apple_log"))));
    public static final Item APPLE_WOOD = new BlockItem(BlossomBlocks.APPLE_WOOD, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_wood"))));
    public static final Item STRIPPED_APPLE_WOOD = new BlockItem(BlossomBlocks.STRIPPED_APPLE_WOOD, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "stripped_apple_wood"))));
    public static final Item APPLE_LEAVES = new BlockItem(BlossomBlocks.APPLE_LEAVES, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_leaves"))));
    public static final Item FLOWERING_APPLE_LEAVES = new BlockItem(BlossomBlocks.FLOWERING_APPLE_LEAVES, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_apple_leaves"))));
    public static final Item FRUITING_APPLE_LEAVES = new BlockItem(BlossomBlocks.FRUITING_APPLE_LEAVES, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_apple_leaves"))));
    public static final Item APPLE_SIGN = new SignItem(BlossomBlocks.APPLE_SIGN, BlossomBlocks.APPLE_WALL_SIGN, new Item.Properties().stacksTo(16)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_sign"))));
    public static final Item APPLE_HANGING_SIGN = new HangingSignItem(BlossomBlocks.APPLE_HANGING_SIGN, BlossomBlocks.APPLE_WALL_HANGING_SIGN, new Item.Properties().stacksTo(16)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_hanging_sign"))));
    public static final Item APPLE_PRESSURE_PLATE = new BlockItem(BlossomBlocks.APPLE_PRESSURE_PLATE, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_pressure_plate"))));
    public static final Item APPLE_TRAPDOOR = new BlockItem(BlossomBlocks.APPLE_TRAPDOOR, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_trapdoor"))));
    public static final Item APPLE_BUTTON = new BlockItem(BlossomBlocks.APPLE_BUTTON, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_button"))));
    public static final Item APPLE_STAIRS = new BlockItem(BlossomBlocks.APPLE_STAIRS, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_stairs"))));
    public static final Item APPLE_SLAB = new BlockItem(BlossomBlocks.APPLE_SLAB, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_slab"))));
    public static final Item APPLE_FENCE_GATE = new BlockItem(BlossomBlocks.APPLE_FENCE_GATE, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_fence_gate"))));
    public static final Item APPLE_FENCE = new BlockItem(BlossomBlocks.APPLE_FENCE, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_fence"))));
    public static final Item APPLE_DOOR = new DoubleHighBlockItem(BlossomBlocks.APPLE_DOOR, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_door"))));
    public static final Item APPLE_BOAT = new BoatItem(BlossomEntityType.APPLE_BOAT, new Item.Properties().stacksTo(1)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_boat"))));
    public static final Item APPLE_CHEST_BOAT = new BoatItem(BlossomEntityType.APPLE_CHEST_BOAT, new Item.Properties().stacksTo(1)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "apple_chest_boat"))));

}