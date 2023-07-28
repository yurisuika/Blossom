package dev.yurisuika.blossom.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.registry.tag.BlockTags.*;

public class BlossomBlockTagsProvider extends FabricTagProvider<Block> {

    public BlossomBlockTagsProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(dataOutput, RegistryKeys.BLOCK, completableFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(BEE_GROWABLES)
                .setReplace(false)
                .add(FLOWERING_OAK_LEAVES);
        getOrCreateTagBuilder(FLOWERS)
                .setReplace(false)
                .add(FLOWERING_OAK_LEAVES);
        getOrCreateTagBuilder(LEAVES)
                .setReplace(false)
                .add(FLOWERING_OAK_LEAVES);
    }

}