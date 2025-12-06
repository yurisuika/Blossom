package dev.yurisuika.blossom.world.item;

import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class BlossomCreativeModeTabs {

    public static final CreativeModeTab BLOSSOM = CreativeModeTab.builder(null, -1)
            .icon(() -> new ItemStack(BlossomItems.APPLE_SAPLING))
            .title(Component.translatable("itemGroup.blossom.blossom"))
            .displayItems((set, output, bl) -> {
                output.accept(BlossomItems.APPLE_PLANKS);
                output.accept(BlossomItems.APPLE_SAPLING);
                output.accept(BlossomItems.APPLE_LOG);
                output.accept(BlossomItems.STRIPPED_APPLE_LOG);
                output.accept(BlossomItems.APPLE_WOOD);
                output.accept(BlossomItems.STRIPPED_APPLE_WOOD);
                output.accept(BlossomItems.APPLE_LEAVES);
                output.accept(BlossomItems.FLOWERING_APPLE_LEAVES);
                output.accept(BlossomItems.FRUITING_APPLE_LEAVES);
                output.accept(BlossomItems.APPLE_SIGN);
                output.accept(BlossomItems.APPLE_HANGING_SIGN);
                output.accept(BlossomItems.APPLE_PRESSURE_PLATE);
                output.accept(BlossomItems.APPLE_TRAPDOOR);
                output.accept(BlossomItems.APPLE_BUTTON);
                output.accept(BlossomItems.APPLE_STAIRS);
                output.accept(BlossomItems.APPLE_SLAB);
                output.accept(BlossomItems.APPLE_FENCE_GATE);
                output.accept(BlossomItems.APPLE_FENCE);
                output.accept(BlossomItems.APPLE_DOOR);
                output.accept(BlossomItems.APPLE_BOAT);
                output.accept(BlossomItems.APPLE_CHEST_BOAT);
            })
            .build();

}