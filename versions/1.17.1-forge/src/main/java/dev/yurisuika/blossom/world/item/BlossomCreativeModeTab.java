package dev.yurisuika.blossom.world.item;

import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class BlossomCreativeModeTab {

    public static final CreativeModeTab BLOSSOM = createTab("blossom.blossom", () -> BlossomItems.APPLE_SAPLING);

    public static CreativeModeTab createTab(String langId, Supplier<Item> itemSupplier) {
        ((CreativeModeTabInterface) CreativeModeTab.TAB_BUILDING_BLOCKS).expandTabs();
        return new CreativeModeTab(CreativeModeTab.TABS.length - 1, langId) {

            @Override
            public ItemStack makeIcon() {
                return new ItemStack(itemSupplier.get());
            }

        };
    }

}