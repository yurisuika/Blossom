package dev.yurisuika.blossom.mixin.entity.passive;

import dev.yurisuika.blossom.Blossom;
import dev.yurisuika.blossom.mixin.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.entity.ai.goal.GoalInvoker;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.function.Predicate;

import static net.minecraft.block.LeavesBlock.*;

@Mixin(BeeEntity.class)
public class BeeEntityMixin {

    public World world;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void injectInit(EntityType<? extends BeeEntity> entityType, World world, CallbackInfo ci) {
        this.world = world;
    }

    @Inject(method = "isFlowers", at = @At("RETURN"), cancellable = true)
    private void injectIsFlowers(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (world.getBiome(pos).isIn(Blossom.ALLOWS_BLOSSOMS)) {
            cir.setReturnValue(cir.getReturnValue() || (world.canSetBlock(pos) && world.getBlockState(pos).isIn(Blossom.BLOSSOMS) && world.getBiome(pos).isIn(Blossom.ALLOWS_BLOSSOMS)));
        }
    }

    @Mixin(targets = "net.minecraft.entity.passive.BeeEntity$GrowCropsGoal")
    public static class GrowCropsGoalMixin {

        public BeeEntity entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(BeeEntity entityType, CallbackInfo ci) {
            entity = entityType;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void injectTick(CallbackInfo ci) {
            if (((EntityAccessor)entity).getRandom().nextInt(((GoalInvoker)this).invokeGetTickCount(Blossom.config.rate)) == 0) {
                for(int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.getBlockPos().down(i);
                    BlockState blockState = entity.world.getBlockState(blockPos);
                    boolean bl = false;
                    if (blockState.isIn(BlockTags.BEE_GROWABLES) && (!Blossom.config.exposed || (Arrays.stream(Direction.values()).anyMatch(direction -> entity.world.getBlockState(blockPos.offset(direction)).getMaterial().equals(Material.ALLOWS_MOVEMENT_LIGHT_PASSES_THROUGH_NOT_SOLID) || entity.world.getBlockState(blockPos.offset(direction)).getMaterial().equals(Material.PLANT))))) {
                        if (blockState.getBlock() == Blocks.OAK_LEAVES) {
                            bl = true;
                        }
                        if (bl) {
                            entity.world.syncWorldEvent(2005, blockPos, 0);
                            entity.world.setBlockState(blockPos, Blossom.FLOWERING_OAK_LEAVES.getDefaultState()
                                    .with(DISTANCE, blockState.get(DISTANCE))
                                    .with(PERSISTENT, blockState.get(PERSISTENT))
                                    .with(WATERLOGGED, blockState.get(WATERLOGGED))
                            );
                            ((BeeEntityInvoker)entity).invokeAddCropCounter();
                        }
                    }
                }
            }
        }

    }

    @Mixin(targets = "net.minecraft.entity.passive.BeeEntity$PollinateGoal")
    public abstract static class PollinateGoalMixin {

        @ModifyArg(method = "getFlower", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/BeeEntity$PollinateGoal;findFlower(Ljava/util/function/Predicate;D)Ljava/util/Optional;"), index = 0)
        private Predicate<BlockState> modifyGetFlower(Predicate<BlockState> predicate) {
            return predicate.or((state) -> (!state.contains(Properties.WATERLOGGED) || !state.get(Properties.WATERLOGGED)) && state.isIn(Blossom.BLOSSOMS));
        }

    }

}