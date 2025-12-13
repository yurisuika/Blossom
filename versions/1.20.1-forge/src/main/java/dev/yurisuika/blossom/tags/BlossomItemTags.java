package dev.yurisuika.blossom.tags;

import dev.yurisuika.blossom.Blossom;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BlossomItemTags {

    public static final TagKey<Item> LEAVED_FLOWERS = TagKey.create(Registries.ITEM, new ResourceLocation(Blossom.MOD_ID, "leaved_flowers"));

}