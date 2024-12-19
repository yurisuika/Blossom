package dev.yurisuika.blossom.util.config;

import dev.yurisuika.blossom.util.config.options.Filter;
import dev.yurisuika.blossom.util.config.options.filter.Downfall;
import dev.yurisuika.blossom.util.config.options.filter.Temperature;

public class Option {

    public static Filter getFilter() {
        return Config.getOptions().getFilter();
    }

    public static void setFilter(Filter filter) {
        Config.getOptions().setFilter(filter);
        Config.saveConfig();
    }

    public static Temperature getTemperature() {
        return getFilter().getTemperature();
    }

    public static void setTemperature(Temperature temperature) {
        getFilter().setTemperature(temperature);
        Config.saveConfig();
    }

    public static float getTemperatureMin() {
        return getTemperature().getMin();
    }

    public static void setTemperatureMin(float min) {
        getTemperature().setMin(min);
        Config.saveConfig();
    }

    public static float getTemperatureMax() {
        return getTemperature().getMax();
    }

    public static void setTemperatureMax(float max) {
        getTemperature().setMax(max);
        Config.saveConfig();
    }

    public static Downfall getDownfall() {
        return getFilter().getDownfall();
    }

    public static void setDownfall(Downfall downfall) {
        getFilter().setDownfall(downfall);
        Config.saveConfig();
    }

    public static float getDownfallMin() {
        return getDownfall().getMin();
    }

    public static void setDownfallMin(float min) {
        getDownfall().setMin(min);
        Config.saveConfig();
    }

    public static float getDownfallMax() {
        return getDownfall().getMax();
    }

    public static void setDownfallMax(float max) {
        getDownfall().setMax(max);
        Config.saveConfig();
    }

}