package dev.yurisuika.blossom.tags;

import dev.yurisuika.blossom.Blossom;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class BlossomItemTags {

    public static final Tag<Item> LEAVED_FLOWERS = ItemTags.getAllTags().getTag(new ResourceLocation(Blossom.MOD_ID, "leaved_flowers"));

}