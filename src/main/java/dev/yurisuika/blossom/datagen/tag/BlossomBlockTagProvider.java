package dev.yurisuika.blossom.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.registry.tag.BlockTags.*;

public class BlossomBlockTagProvider extends FabricTagProvider<Block> {

    public BlossomBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, RegistryKeys.BLOCK, completableFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BEE_GROWABLES)
                .add(FLOWERING_OAK_LEAVES);
        getOrCreateTagBuilder(FLOWERS)
                .add(FLOWERING_OAK_LEAVES);
        getOrCreateTagBuilder(LEAVES)
                .add(FLOWERING_OAK_LEAVES);
    }

}
