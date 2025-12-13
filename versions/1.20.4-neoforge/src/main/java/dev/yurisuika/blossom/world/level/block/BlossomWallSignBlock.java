package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.Blossom;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BlossomWallSignBlock extends WallSignBlock {

    public BlossomWallSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    @Override
    public final ResourceLocation getLootTable() {
        return new ResourceLocation(Blossom.MOD_ID, "blocks/" + type().name() + "_wall_sign");
    }

}