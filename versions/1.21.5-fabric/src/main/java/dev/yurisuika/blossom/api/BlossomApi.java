package dev.yurisuika.blossom.api;

import dev.yurisuika.blossom.registry.BlossomableLeavesRegistry;
import dev.yurisuika.blossom.registry.FruitableLeavesRegistry;
import dev.yurisuika.blossom.registry.HarvestableFruitRegistry;
import dev.yurisuika.blossom.registry.ShearableLeavesRegistry;
import dev.yurisuika.blossom.world.entity.ai.goal.BlossomLeavesGoal;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;

public class BlossomApi {

    /**
     * <p>Registers a cast {@link LeavesBlock} key for bees to blossom into a cast {@link FloweringLeavesBlock} value
     * during {@link BlossomLeavesGoal}.
     *
     * <p>In order to keep leaves from blossoming when bees are nowhere to be found, bees act upon leaves randomly
     * within a radius to change them into their flowering form. This is done through a passive goal that allows the
     * flowering leaves to always be accessible to bees.
     *
     * @param leavesBlock the targeted Block cast to LeavesBlock {@link Block}
     * @param floweringLeavesBlock the resultant Block cast to FloweringLeavesBlock {@link Block}
     *
     * @see BlossomableLeavesRegistry
     * @see BlossomLeavesGoal
     */
    public static void registerBlossomableLeaves(Block leavesBlock, Block floweringLeavesBlock) {
        BlossomableLeavesRegistry.add(leavesBlock, floweringLeavesBlock);
    }

    /**
     * <p>Registers a cast {@code FloweringLeavesBlock} key for bees to fruit into a cast {@code FruitingLeavesBlock}.
     *
     * <p>When a bee pollinates flowering leaves that are at max {@link FloweringLeavesBlock#AGE}, it will set the
     * {@link FloweringLeavesBlock#POLLINATED} blockstate to true.
     *
     * @param floweringLeavesBlock the targeted Block cast to FloweringLeavesBlock {@link Block}
     * @param fruitingLeavesBlock the resultant Block cast to FruitingLeavesBlock {@link Block}
     *
     * @see FruitableLeavesRegistry
     */
    public static void registerFruitableLeaves(Block floweringLeavesBlock, Block fruitingLeavesBlock) {
        FruitableLeavesRegistry.add(floweringLeavesBlock, fruitingLeavesBlock);
    }

    /**
     * <p>Registers a cast {@code FruitingLeavesBlock} key that may drop an {@code Item} value when sheared.
     *
     * <p>Fruiting leaves that are max {@link FruitingLeavesBlock#AGE} will drop their associated fruit item when successfully sheared. A single
     * fruit is dropped along with any additional bonus fruit, which have a certain chance to successfully drop. This is
     * matched with the loot table of the fruiting leaves block.
     *
     * @param fruitingLeavesBlock the targeted Block cast to FruitingLeavesBlock {@link Block}
     * @param fruit the resultant Item {@link Item}
     * @param chance the chance for a bonus fruit to drop {@code float}
     * @param bonus the amount of bonus attempts {@code int}
     *
     * @see HarvestableFruitRegistry
     * @see #registerShearableLeaves(Block, Block)
     */
    public static void registerHarvestableFruit(Block fruitingLeavesBlock, Item fruit, float chance, int bonus) {
        HarvestableFruitRegistry.add(fruitingLeavesBlock, fruit, chance, bonus);
    }

    /**
     * <p>Registers a cast {@code LeavesBlock} key that may be sheared into a cast {@code LeavesBlock} value.
     *
     * <p>When flowering and fruiting leaves are sheared, they are replaced with their basal leaves form.
     *
     * @param shearableLeavesBlock the targeted Block cast to LeavesBlock {@link Block}
     * @param leavesBlock the resultant Block cast to LeavesBlock {@link Block}
     *
     * @see ShearableLeavesRegistry
     */
    public static void registerShearableLeaves(Block shearableLeavesBlock, Block leavesBlock) {
        ShearableLeavesRegistry.add(shearableLeavesBlock, leavesBlock);
    }

}