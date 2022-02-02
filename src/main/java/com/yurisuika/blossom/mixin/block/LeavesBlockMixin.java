package com.yurisuika.blossom.mixin.block;

import com.yurisuika.blossom.Blossom;
import com.yurisuika.blossom.block.FloweringLeavesBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static net.minecraft.block.LeavesBlock.DISTANCE;
import static net.minecraft.block.LeavesBlock.PERSISTENT;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void inject1(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockState blockStateD = world.getBlockState(pos.down());
        BlockState blockStateU = world.getBlockState(pos.up());
        BlockState blockStateN = world.getBlockState(pos.north());
        BlockState blockStateE = world.getBlockState(pos.east());
        BlockState blockStateS = world.getBlockState(pos.south());
        BlockState blockStateW = world.getBlockState(pos.west());
        BlockState block = world.getBlockState(pos.up(0));
        boolean oak = block.isOf(Blocks.OAK_LEAVES);
        if (!(Boolean)state.get(PERSISTENT) && (blockStateD.isAir() || blockStateU.isAir() || blockStateN.isAir() || blockStateE.isAir() || blockStateS.isAir() || blockStateW.isAir()) && random.nextInt(10) == 0 && oak) {
            world.setBlockState(pos, Blossom.FLOWERING_OAK_LEAVES.getDefaultState()
                    .with(DISTANCE, state.get(DISTANCE))
                    .with(PERSISTENT, state.get(PERSISTENT))
            );
        }
    }

    @Inject(method = "hasRandomTicks", at = @At("RETURN"), cancellable = true)
    private void injected3(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!(Boolean)state.get(PERSISTENT));
    }

    @Inject(method = "getDistanceFromLog(Lnet/minecraft/block/BlockState;)I", at = @At("RETURN"), cancellable = true)
    private static void injectDistance(BlockState state, CallbackInfoReturnable<Integer> info) {
        if (state.isIn(BlockTags.LOGS)){
            info.setReturnValue(0);
        } else {
            info.setReturnValue(state.getBlock() instanceof FloweringLeavesBlock || state.getBlock() instanceof net.minecraft.block.LeavesBlock ? (Integer)state.get(DISTANCE) : 7);
//            info.setReturnValue(state.isOf((Block) BlockTags.LEAVES) ? (Integer)state.get(DISTANCE) : 7);
        }
    }

}
