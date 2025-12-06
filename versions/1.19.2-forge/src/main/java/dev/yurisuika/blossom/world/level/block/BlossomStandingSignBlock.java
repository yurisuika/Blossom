package dev.yurisuika.blossom.world.level.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BlossomStandingSignBlock extends StandingSignBlock {

    public BlossomStandingSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Override
    public final ResourceLocation getLootTable() {
        return ResourceLocation.tryParse("blossom:blocks/" + type().name() + "_sign");
    }

}