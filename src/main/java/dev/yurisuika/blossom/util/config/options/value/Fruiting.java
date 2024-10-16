package dev.yurisuika.blossom.util.config.options.value;

public class Fruiting {

    public float chance;
    public double distance;

    public Fruiting(float chance, double distance) {
        this.chance = chance;
        this.distance = distance;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}