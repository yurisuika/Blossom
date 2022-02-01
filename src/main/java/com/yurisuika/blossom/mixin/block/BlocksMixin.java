package com.yurisuika.blossom.mixin.block;

import com.yurisuika.blossom.Blossom;
import com.yurisuika.blossom.block.BlossomLeavesBlock;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {

    private static BlossomLeavesBlock createBlossomLeavesBlock(Block floweringBlock, BlockSoundGroup soundGroup) {
        return new BlossomLeavesBlock(floweringBlock, AbstractBlock.Settings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(soundGroup).nonOpaque().allowsSpawning(Blocks::canSpawnOnLeaves).suffocates(Blocks::never).blockVision(Blocks::never));
    }

    @Redirect(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=oak_leaves")),
            at = @At(value = "INVOKE",
                    target = "net/minecraft/block/Blocks.createLeavesBlock (Lnet/minecraft/sound/BlockSoundGroup;)Lnet/minecraft/block/LeavesBlock;",
                    ordinal = 0
            )
    )
    private static LeavesBlock redirectedOak(BlockSoundGroup soundGroup) {
        return createBlossomLeavesBlock(Blossom.FLOWERING_OAK_LEAVES, BlockSoundGroup.GRASS);
    }

}
