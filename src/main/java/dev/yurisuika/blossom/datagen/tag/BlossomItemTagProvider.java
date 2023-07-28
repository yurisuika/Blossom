package dev.yurisuika.blossom.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.registry.tag.ItemTags.*;

public class BlossomItemTagProvider extends FabricTagProvider<Item> {

    public BlossomItemTagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(dataOutput, RegistryKeys.ITEM, completableFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(FLOWERS)
                .add(FLOWERING_OAK_LEAVES.asItem());
        getOrCreateTagBuilder(LEAVES)
                .add(FLOWERING_OAK_LEAVES.asItem());
    }

}