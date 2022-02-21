package com.yurisuika.blossom.mixin.block;

import com.yurisuika.blossom.block.FloweringLeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.LeavesBlock.DISTANCE;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

    @Inject(method = "getDistanceFromLog(Lnet/minecraft/block/BlockState;)I", at = @At("RETURN"), cancellable = true)
    private static void injectDistance(BlockState state, CallbackInfoReturnable<Integer> info) {
        if (state.isIn(BlockTags.LOGS)) {
            info.setReturnValue(0);
        } else if (state.getBlock() instanceof LeavesBlock || state.getBlock() instanceof FloweringLeavesBlock) {
            info.setReturnValue((Integer)state.get(DISTANCE));
        } else {
            info.setReturnValue(7);
        }
    }

}
