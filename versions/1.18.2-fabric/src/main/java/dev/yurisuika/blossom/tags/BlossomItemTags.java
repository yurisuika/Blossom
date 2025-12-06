package dev.yurisuika.blossom.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BlossomItemTags {

    public static final TagKey<Item> LEAVED_FLOWERS = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation.tryParse("blossom:leaved_flowers"));

}