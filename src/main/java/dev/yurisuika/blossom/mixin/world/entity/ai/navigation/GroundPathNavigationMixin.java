package dev.yurisuika.blossom.mixin.world.entity.ai.navigation;

import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GroundPathNavigation.class)
public abstract class GroundPathNavigationMixin {

//    @Inject(method = "findPathTo(Lnet/minecraft/entity/Entity;I)Lnet/minecraft/entity/ai/pathing/Path;", at = @At("RETURN"))
//    private void injectFindPathTo(Entity entity, int distance, CallbackInfoReturnable<Path> cir) {
//        if (entity instanceof Bee) {
//            BlockPos target = entity.blockPosition();
//            BlockPos blockPos;
//            WorldChunk worldChunk = ((PathNavigationAccessor)this).getWorld().getChunkManager().getWorldChunk(ChunkSectionPos.getSectionCoord(target.getX()), ChunkSectionPos.getSectionCoord(entity.blockPosition().getZ()));
//            if (worldChunk == null) {
//                cir.setReturnValue(null);
//            }
//            if (worldChunk.getBlockState(target).isAir()) {
//                blockPos = target.down();
//                while (blockPos.getY() > ((PathNavigationAccessor)this).getWorld().getBottomY() && worldChunk.getBlockState(blockPos).isAir()) {
//                    blockPos = blockPos.down();
//                }
//                if (blockPos.getY() > ((PathNavigationAccessor)this).getWorld().getBottomY()) {
//                    return super.findPathTo(blockPos.up(), distance);
//                }
//                while (blockPos.getY() < ((PathNavigationAccessor)this).getWorld().getTopY() && worldChunk.getBlockState(blockPos).isAir()) {
//                    blockPos = blockPos.up();
//                }
//                target = blockPos;
//            }
//            if (worldChunk.getBlockState(target).isSolid()) {
//                blockPos = target.up();
//                while (blockPos.getY() < ((PathNavigationAccessor)this).getWorld().getTopY() && worldChunk.getBlockState(blockPos).isSolid()) {
//                    blockPos = blockPos.up();
//                }
//                return super.findPathTo(blockPos, distance);
//            }
//            return super.findPathTo(target, distance);
//        }
//    }

    @Redirect(method = "createPath(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/pathfinder/Path;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"))
    private boolean redirectFindPathTo(BlockState blockState) {
        return blockState.isAir() || blockState.getBlock() instanceof LeavesBlock;
    }

}