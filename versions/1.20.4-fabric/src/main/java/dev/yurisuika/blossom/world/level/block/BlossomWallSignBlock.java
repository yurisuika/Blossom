package dev.yurisuika.blossom.world.level.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BlossomWallSignBlock extends WallSignBlock {

    public BlossomWallSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    @Override
    public final ResourceLocation getLootTable() {
        return ResourceLocation.tryParse("blossom:blocks/" + type().name() + "_wall_sign");
    }

}