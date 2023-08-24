package dev.yurisuika.blossom.mixin.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void injectGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        Entity entity = null;
        if (context instanceof EntityShapeContext) {
            entity = ((EntityShapeContext)context).getEntity();
            if (entity instanceof BeeEntity) {
                if (state.getBlock() instanceof LeavesBlock) {
                    cir.setReturnValue(VoxelShapes.empty());
                }
            }
        }
    }

}