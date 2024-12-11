package dev.yurisuika.blossom.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlossomBlockTags {

    public static final TagKey<Block> LEAVED_FLOWERS = TagKey.create(Registry.BLOCK_REGISTRY, ResourceLocation.tryParse("blossom:leaved_flowers"));

}