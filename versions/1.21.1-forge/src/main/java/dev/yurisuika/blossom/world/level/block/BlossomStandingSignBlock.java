package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.Blossom;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.LootTable;

public class BlossomStandingSignBlock extends StandingSignBlock {

    public BlossomStandingSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    @Override
    public ResourceKey<LootTable> getLootTable() {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "blocks/" + type().name() + "_sign"));
    }

}