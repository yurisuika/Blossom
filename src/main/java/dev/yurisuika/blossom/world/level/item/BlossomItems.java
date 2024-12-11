package dev.yurisuika.blossom.world.level.item;

import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlossomItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "blossom");

    public static final RegistryObject<Item> FRUITING_OAK_LEAVES = ITEMS.register("fruiting_oak_leaves", () -> new BlockItem(BlossomBlocks.FRUITING_OAK_LEAVES.get(), new Item.Properties()
            .tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final  RegistryObject<Item> FLOWERING_OAK_LEAVES = ITEMS.register("flowering_oak_leaves", () -> new BlockItem(BlossomBlocks.FLOWERING_OAK_LEAVES.get(), new Item.Properties()
            .tab(CreativeModeTab.TAB_DECORATIONS)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}