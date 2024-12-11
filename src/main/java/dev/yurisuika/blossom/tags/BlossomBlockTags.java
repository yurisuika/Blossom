package dev.yurisuika.blossom.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class BlossomBlockTags {

    public static final Tag<Block> LEAVED_FLOWERS = BlockTags.getAllTags().getTag(ResourceLocation.tryParse("blossom:leaved_flowers"));

}