package dev.yurisuika.blossom.world.level.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BlossomCeilingHangingSignBlock extends CeilingHangingSignBlock {

    public BlossomCeilingHangingSignBlock(Properties properties, WoodType woodType) {
        super(properties, woodType);
    }

    @Override
    public final ResourceLocation getLootTable() {
        return ResourceLocation.tryParse("blossom:blocks/" + type().name() + "_hanging_sign");
    }

}