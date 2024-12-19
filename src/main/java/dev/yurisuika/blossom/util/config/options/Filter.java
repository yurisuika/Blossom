package dev.yurisuika.blossom.util.config.options;

import dev.yurisuika.blossom.util.config.options.filter.Downfall;
import dev.yurisuika.blossom.util.config.options.filter.Temperature;

public class Filter {

    public Temperature temperature;
    public Downfall downfall;

    public Filter(Temperature temperature, Downfall downfall) {
        this.temperature = temperature;
        this.downfall = downfall;
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

}