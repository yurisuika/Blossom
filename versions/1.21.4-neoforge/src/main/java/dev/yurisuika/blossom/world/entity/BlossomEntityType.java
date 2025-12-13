package dev.yurisuika.blossom.world.entity;

import dev.yurisuika.blossom.Blossom;
import dev.yurisuika.blossom.mixin.minecraft.world.entity.EntityTypeInvoker;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;

public class BlossomEntityType {

    public static final EntityType<Boat> APPLE_BOAT = EntityType.Builder.of(EntityTypeInvoker.invokeBoatFactory(() -> BlossomItems.APPLE_BOAT), MobCategory.MISC)
            .noLootTable()
            .sized(1.375F, 0.5625F)
            .eyeHeight(0.5625F)
            .clientTrackingRange(10)
            .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "apple_boat")));
    public static final EntityType<ChestBoat> APPLE_CHEST_BOAT = EntityType.Builder.of(EntityTypeInvoker.invokeChestBoatFactory(() -> BlossomItems.APPLE_CHEST_BOAT), MobCategory.MISC)
            .noLootTable()
            .sized(1.375F, 0.5625F)
            .eyeHeight(0.5625F)
            .clientTrackingRange(10)
            .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "apple_chest_boat")));

}