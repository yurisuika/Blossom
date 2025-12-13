package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.Blossom;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.LootTable;

public class BlossomCeilingHangingSignBlock extends CeilingHangingSignBlock {

    public BlossomCeilingHangingSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    @Override
    public ResourceKey<LootTable> getLootTable() {
        return ResourceKey.create(Registries.LOOT_TABLE, new ResourceLocation(Blossom.MOD_ID, "blocks/" + type().name() + "_hanging_sign"));
    }

}