package dev.yurisuika.blossom.world.level.item;

import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlossomItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "blossom");

    public static final RegistryObject<Item> FRUITING_OAK_LEAVES = ITEMS.register("fruiting_oak_leaves", () -> new BlockItem(BlossomBlocks.FRUITING_OAK_LEAVES.get(), new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_oak_leaves")))));
    public static final RegistryObject<Item> FLOWERING_OAK_LEAVES = ITEMS.register("flowering_oak_leaves", () -> new BlockItem(BlossomBlocks.FLOWERING_OAK_LEAVES.get(), new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_oak_leaves")))));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}