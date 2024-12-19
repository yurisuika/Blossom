package dev.yurisuika.blossom.world.entity.ai.goal;

import dev.yurisuika.blossom.mixin.world.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.world.entity.MobAccessor;
import dev.yurisuika.blossom.mixin.world.entity.animal.BeeInvoker;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;

import java.util.EnumSet;

public class GoToKnownLeavesGoal extends Goal {

    public final Bee entity;
    public int travellingTicks;

    public GoToKnownLeavesGoal(Bee bee) {
        super();
        this.entity = bee;
        this.travellingTicks = ((EntityAccessor) entity).getRandom().nextInt(10);
        this.setFlags(EnumSet.of(Flag.MOVE));
    }
    
    public int getTravellingTicks() {
        return travellingTicks;
    }

    public boolean canUse() {
        return ((BeeInterface) entity).getSavedLeavesPos() != null && !entity.hasRestriction() && entity.hasNectar() && !((BeeInvoker) entity).invokeCloserThan(((BeeInterface) entity).getSavedLeavesPos(), 2) && !entity.isAngry();
    }

    public boolean canContinueToUse() {
        return canUse();
    }

    public void start() {
        travellingTicks = 0;
        super.start();
    }

    public void stop() {
        travellingTicks = 0;
        ((MobAccessor) entity).getNavigation().stop();
        ((MobAccessor) entity).getNavigation().resetMaxVisitedNodesMultiplier();
    }

    public void tick() {
        if (((BeeInterface) entity).getSavedLeavesPos() != null) {
            ++travellingTicks;
            if (travellingTicks > adjustedTickDelay(2400)) {
                ((BeeInterface) entity).dropLeaves();
            } else if (!((MobAccessor) entity).getNavigation().isInProgress()) {
                if (((BeeInvoker) entity).invokeIsTooFarAway(((BeeInterface) entity).getSavedLeavesPos())) {
                    ((BeeInterface) entity).dropLeaves();
                } else {
                    ((BeeInvoker) entity).invokePathfindRandomlyTowards(((BeeInterface) entity).getSavedLeavesPos());
                }
            }
        }
    }

}