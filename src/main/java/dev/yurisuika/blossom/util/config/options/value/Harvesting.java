package dev.yurisuika.blossom.util.config.options.value;

public class Harvesting {

    public int bonus;
    public float chance;

    public Harvesting(int bonus, float chance) {
        this.bonus = bonus;
        this.chance = chance;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }
    
}