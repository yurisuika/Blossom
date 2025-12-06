package dev.yurisuika.blossom.mixin.minecraft.world.level.block;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin {

    @Inject(method = "getDistanceAt", at = @At("RETURN"), cancellable = true)
    private static void injectDistance(BlockState state, CallbackInfoReturnable<Integer> info) {
        if (state.hasProperty(LeavesBlock.DISTANCE)) {
            info.setReturnValue(state.getValue(LeavesBlock.DISTANCE));
        }
    }

}