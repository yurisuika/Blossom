package dev.yurisuika.blossom.tags;

import dev.yurisuika.blossom.Blossom;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BlossomItemTags {

    public static final TagKey<Item> LEAVED_FLOWERS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Blossom.MOD_ID, "leaved_flowers"));

}