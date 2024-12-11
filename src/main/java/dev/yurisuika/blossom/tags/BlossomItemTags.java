package dev.yurisuika.blossom.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class BlossomItemTags {

    public static final Tag<Item> LEAVED_FLOWERS = ItemTags.getAllTags().getTag(ResourceLocation.tryParse("blossom:leaved_flowers"));

}