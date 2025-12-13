package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.Blossom;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class BlossomCeilingHangingSignBlock extends CeilingHangingSignBlock {

    public BlossomCeilingHangingSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    @Override
    public final ResourceLocation getLootTable() {
        return new ResourceLocation(Blossom.MOD_ID, "blocks/" + type().name() + "_hanging_sign");
    }

}