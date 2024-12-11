package dev.yurisuika.blossom.world.level.item;

import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class BlossomItems {

    public static final Item FRUITING_OAK_LEAVES = new BlockItem(BlossomBlocks.FRUITING_OAK_LEAVES, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    public static final Item FLOWERING_OAK_LEAVES = new BlockItem(BlossomBlocks.FLOWERING_OAK_LEAVES, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));

    public static void register() {
        Registry.register(Registry.ITEM, ResourceLocation.tryParse("blossom:fruiting_oak_leaves"), FRUITING_OAK_LEAVES);
        Registry.register(Registry.ITEM, ResourceLocation.tryParse("blossom:flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
    }

}