package dev.yurisuika.blossom.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;

import java.util.HashMap;
import java.util.Map;

public class HarvestableFruitRegistry {

    public static final Map<LeavesBlock, Entry> HARVESTABLES = new HashMap<>();

    public static void add(Block leavesBlock, Item fruit, float chance, int bonus) {
        HARVESTABLES.put((LeavesBlock) leavesBlock, new Entry(fruit, chance, bonus));
    }

    public static final class Entry {

        public final Item fruit;
        public final float chance;
        public final int bonus;

        public Entry(Item fruit, float chance, int bonus) {
            this.fruit = fruit;
            this.chance = chance;
            this.bonus = bonus;
        }

        public Item getFruit() {
            return fruit;
        }

        public float getChance() {
            return chance;
        }

        public int getBonus() {
            return bonus;
        }

        public boolean equals(Object o) {
            Entry other = (Entry) o;
            return o instanceof Entry && other.fruit == fruit && other.chance == chance && other.bonus == bonus;
        }

    }

}