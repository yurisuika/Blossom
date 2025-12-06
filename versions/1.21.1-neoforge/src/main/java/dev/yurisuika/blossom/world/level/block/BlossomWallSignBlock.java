package dev.yurisuika.blossom.world.level.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.LootTable;

public class BlossomWallSignBlock extends WallSignBlock {

    public BlossomWallSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    @Override
    public ResourceKey<LootTable> getLootTable() {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.tryParse("blossom:blocks/" + type().name() + "_wall_sign"));
    }

}