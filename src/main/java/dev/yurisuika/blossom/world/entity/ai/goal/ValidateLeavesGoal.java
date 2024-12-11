package dev.yurisuika.blossom.world.entity.ai.goal;

import dev.yurisuika.blossom.mixin.world.entity.EntityAccessor;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;

public class ValidateLeavesGoal extends Goal {

    public final Bee entity;
    public final int validateLeavesCooldown;
    public long lastValidateTick;

    public ValidateLeavesGoal(Bee bee) {
        super();
        this.entity = bee;
        this.validateLeavesCooldown = Mth.nextInt(((EntityAccessor) entity).getRandom(), 20, 40);
        this.lastValidateTick = -1L;
    }

    public boolean canUse() {
        return entity.level().getGameTime() > lastValidateTick + validateLeavesCooldown && !entity.isAngry();
    }

    public boolean canContinueToUse() {
        return false;
    }

    public void start() {
        if (((BeeInterface) entity).getSavedLeavesPos() != null && entity.level().isLoaded(((BeeInterface) entity).getSavedLeavesPos()) && !this.isLeaves(((BeeInterface) entity).getSavedLeavesPos())) {
            ((BeeInterface) entity).dropLeaves();
        }
        lastValidateTick = entity.level().getGameTime();
    }

    public boolean isLeaves(BlockPos blockPos) {
        return entity.level().getBlockState(blockPos).is(BlockTags.LEAVES);
    }

}