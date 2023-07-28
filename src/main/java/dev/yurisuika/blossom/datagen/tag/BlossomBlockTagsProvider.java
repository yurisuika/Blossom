package dev.yurisuika.blossom.datagen.tag;

import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.registry.tag.BlockTags.*;

public class BlossomBlockTagsProvider extends BlockTagsProvider {

    public BlossomBlockTagsProvider(DataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, ExistingFileHelper existingFileHelper) {
        super(dataOutput, completableFuture, "blossom", existingFileHelper);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(BEE_GROWABLES)
                .replace(false)
                .add(FLOWERING_OAK_LEAVES.get());
        getOrCreateTagBuilder(FLOWERS)
                .replace(false)
                .add(FLOWERING_OAK_LEAVES.get());
        getOrCreateTagBuilder(LEAVES)
                .replace(false)
                .add(FLOWERING_OAK_LEAVES.get());
    }

}