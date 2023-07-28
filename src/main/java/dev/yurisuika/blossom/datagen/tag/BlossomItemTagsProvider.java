package dev.yurisuika.blossom.datagen.tag;

import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.ItemTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.registry.tag.ItemTags.*;

public class BlossomItemTagsProvider extends ItemTagProvider {

    public BlossomItemTagsProvider(DataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataOutput, completableFuture, blockTagProvider, "blossom", existingFileHelper);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(FLOWERS)
                .replace(false)
                .add(FLOWERING_OAK_LEAVES.get().asItem());
        getOrCreateTagBuilder(LEAVES)
                .replace(false)
                .add(FLOWERING_OAK_LEAVES.get().asItem());
    }

}