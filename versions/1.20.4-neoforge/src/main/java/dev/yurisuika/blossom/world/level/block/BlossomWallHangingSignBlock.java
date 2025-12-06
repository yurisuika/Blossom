package dev.yurisuika.blossom.world.level.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BlossomWallHangingSignBlock extends WallHangingSignBlock {

    public BlossomWallHangingSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    @Override
    public final ResourceLocation getLootTable() {
        return ResourceLocation.tryParse("blossom:blocks/" + type().name() + "_wall_hanging_sign");
    }

}