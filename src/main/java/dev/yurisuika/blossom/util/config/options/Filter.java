package dev.yurisuika.blossom.util.config.options;

import dev.yurisuika.blossom.util.config.options.filter.Biome;
import dev.yurisuika.blossom.util.config.options.filter.Dimension;
import dev.yurisuika.blossom.util.config.options.filter.Downfall;
import dev.yurisuika.blossom.util.config.options.filter.Temperature;

public class Filter {

    public Temperature temperature;
    public Downfall downfall;
    public Dimension dimension;
    public Biome biome;

    public Filter(Temperature temperature, Downfall downfall, Dimension dimension, Biome biome) {
        this.temperature = temperature;
        this.downfall = downfall;
        this.dimension = dimension;
        this.biome = biome;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Downfall getDownfall() {
        return downfall;
    }

    public void setDownfall(Downfall downfall) {
        this.downfall = downfall;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

}