package dev.yurisuika.blossom.mixin.world.entity.ai.navigation;

import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PathNavigation.class)
public abstract class PathNavigationMixin {

//    @Inject(method = "getGroundY", at = @At("RETURN"), cancellable = true)
//    private void injectAdjustTargetY(Vec3 pos, CallbackInfoReturnable<Double> cir) {
//        BlockPos blockPos = BlockPos.ofFloored(pos);
//        BlockState blockState = ((PathNavigationAccessor)this).getWorld().getBlockState(blockPos.down());
//        cir.setReturnValue((blockState.isAir() || blockState.getBlock() instanceof LeavesBlock) ? pos.y : LandPathNodeMaker.getFeetY(((PathNavigationAccessor)this).getWorld(), blockPos));
//    }

    @Redirect(method = "getGroundY", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"))
    private boolean redirectGetGroundY(BlockState blockState) {
        return blockState.isAir() || blockState.getBlock() instanceof LeavesBlock;
    }

}