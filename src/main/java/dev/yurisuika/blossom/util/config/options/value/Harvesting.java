package dev.yurisuika.blossom.util.config.options.value;

public class Harvesting {

    public float chance;
    public int bonus;

    public Harvesting(float chance, int bonus) {
        this.chance = chance;
        this.bonus = bonus;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
    
}