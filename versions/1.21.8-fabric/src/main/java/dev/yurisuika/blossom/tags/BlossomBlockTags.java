package dev.yurisuika.blossom.tags;

import dev.yurisuika.blossom.Blossom;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlossomBlockTags {

    public static final TagKey<Block> LEAVED_FLOWERS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "leaved_flowers"));

}