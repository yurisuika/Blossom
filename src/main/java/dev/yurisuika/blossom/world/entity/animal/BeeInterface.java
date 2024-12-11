package dev.yurisuika.blossom.world.entity.animal;

import dev.yurisuika.blossom.world.entity.ai.goal.BlossomLeavesGoal;
import dev.yurisuika.blossom.world.entity.ai.goal.FruitLeavesGoal;
import dev.yurisuika.blossom.world.entity.ai.goal.GoToKnownLeavesGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;

public interface BeeInterface {

    void registerGoals(Bee bee);

    BlossomLeavesGoal getBlossomLeavesGoal();

    FruitLeavesGoal getFruitLeavesGoal();

    GoToKnownLeavesGoal getGoToKnownLeavesGoal();

    BlockPos getSavedLeavesPos();

    boolean hasSavedLeavesPos();

    void setSavedLeavesPos(BlockPos savedLeavesPos);

    void dropLeaves();

    boolean areLeavesValid(BlockPos pos);

}