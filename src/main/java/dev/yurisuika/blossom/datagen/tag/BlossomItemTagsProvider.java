package dev.yurisuika.blossom.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.registry.tag.ItemTags.*;

public class BlossomItemTagsProvider extends FabricTagProvider<Item> {

    public BlossomItemTagsProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(dataOutput, RegistryKeys.ITEM, completableFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(FLOWERS)
                .setReplace(false)
                .add(FLOWERING_OAK_LEAVES.asItem());
        getOrCreateTagBuilder(LEAVES)
                .setReplace(false)
                .add(FLOWERING_OAK_LEAVES.asItem());
    }

}