package dev.yurisuika.blossom.world.entity.ai.goal;

import dev.yurisuika.blossom.mixin.minecraft.world.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.minecraft.world.entity.animal.BeeInvoker;
import dev.yurisuika.blossom.registry.BlossomableLeavesRegistry;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.EnumSet;
import java.util.function.Predicate;

public class BlossomLeavesGoal extends Goal {

    public final Bee entity;
    public Predicate<BlockState> validBlocks;

    public BlossomLeavesGoal(Bee bee) {
        super();
        this.entity = bee;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.validBlocks = state -> (!state.hasProperty(BlockStateProperties.WATERLOGGED) || !state.getValue(BlockStateProperties.WATERLOGGED)) && BlossomableLeavesRegistry.BLOSSOMABLES.containsKey(state.getBlock());
    }

    public boolean canBeeUse() {
        return !(((EntityAccessor) entity).getRandom().nextFloat() > 0.2F) && ((BeeInvoker) entity).invokeIsHiveValid();
    }

    public boolean canBeeContinueToUse() {
        return canBeeUse();
    }

    public boolean canUse() {
        return canBeeUse() && !entity.isAngry() && Validate.passesFilters(entity.getCommandSenderWorld(), entity.blockPosition());
    }

    public boolean canContinueToUse() {
        return canBeeContinueToUse() && !entity.isAngry() && Validate.passesFilters(entity.getCommandSenderWorld(), entity.blockPosition());
    }

    public void tick() {
        if (((EntityAccessor) entity).getRandom().nextInt(30) != 0) {
            return;
        }
        int l;
        int k;
        int i = 16;
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                for (l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d = (float) j / 15.0F * 2.0F - 1.0F;
                    double e = (float) k / 15.0F * 2.0F - 1.0F;
                    double f = (float) l / 15.0F * 2.0F - 1.0F;
                    double g = Math.sqrt(d * d + e * e + f * f);
                    d /= g;
                    e /= g;
                    f /= g;
                    double x = entity.getX();
                    double y = entity.getY();
                    double z = entity.getZ();
                    for (float h = 6 * (0.7F + ((EntityAccessor) entity).getRandom().nextFloat() * 0.6F); h > 0.0F; h -= 0.22500001F) {
                        BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
                        BlockState blockState = entity.getCommandSenderWorld().getBlockState(blockPos);
                        if (validBlocks.test(blockState)) {
                            if (((EntityAccessor) entity).getRandom().nextFloat() <= 0.01F) {
                                entity.getCommandSenderWorld().levelEvent(2005, blockPos, 0);
                                entity.getCommandSenderWorld().setBlockAndUpdate(blockPos, BlossomableLeavesRegistry.BLOSSOMABLES.get(blockState.getBlock()).defaultBlockState()
                                        .setValue(FloweringLeavesBlock.DISTANCE, blockState.getValue(LeavesBlock.DISTANCE))
                                        .setValue(FloweringLeavesBlock.PERSISTENT, blockState.getValue(LeavesBlock.PERSISTENT))
                                        .setValue(FloweringLeavesBlock.WATERLOGGED, blockState.getValue(LeavesBlock.WATERLOGGED)));
                            }
                        }
                        x += d * 0.3D;
                        y += e * 0.3D;
                        z += f * 0.3D;
                    }
                }
            }
        }
    }

}