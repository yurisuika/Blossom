package dev.yurisuika.blossom.mixin.world.level.block;

import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.level.block.LeavesBlock.DISTANCE;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

    @Inject(method = "getDistanceAt", at = @At("RETURN"), cancellable = true)
    private static void injectDistance(BlockState state, CallbackInfoReturnable<Integer> info) {
        if (state.getBlock() instanceof FloweringLeavesBlock) {
            info.setReturnValue(state.getValue(DISTANCE));
        }
    }

}