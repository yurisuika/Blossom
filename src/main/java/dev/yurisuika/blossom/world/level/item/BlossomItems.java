package dev.yurisuika.blossom.world.level.item;

import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlossomItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("blossom");

    public static final DeferredItem<Item> FRUITING_OAK_LEAVES = ITEMS.register("fruiting_oak_leaves", () -> new BlockItem(BlossomBlocks.FRUITING_OAK_LEAVES.get(), new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_oak_leaves")))));
    public static final DeferredItem<Item> FLOWERING_OAK_LEAVES = ITEMS.register("flowering_oak_leaves", () -> new BlockItem(BlossomBlocks.FLOWERING_OAK_LEAVES.get(), new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_oak_leaves")))));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}