package com.yurisuika.blossom.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BlossomBlockTags {

    public static final Tag<Block> AIR = TagFactory.BLOCK.create((new Identifier("blossom", "air")));

}