package dev.yurisuika.blossom.world.level.item;

import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class BlossomItems {

    public static final Item FLOWERING_OAK_LEAVES = new BlockItem(BlossomBlocks.FLOWERING_OAK_LEAVES, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_oak_leaves"))));
    public static final Item FRUITING_OAK_LEAVES = new BlockItem(BlossomBlocks.FRUITING_OAK_LEAVES, new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_oak_leaves"))));

}