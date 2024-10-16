package dev.yurisuika.blossom.mixin.world.level.block.state;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {

//    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
//    private void injectGetCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
//        if (context instanceof EntityCollisionContext) {
//            if (((EntityCollisionContext) context).getEntity() instanceof Bee) {
//                if (state.getBlock() instanceof LeavesBlock) {
//                    cir.setReturnValue(Shapes.empty());
//                }
//            }
//        }
//    }

}