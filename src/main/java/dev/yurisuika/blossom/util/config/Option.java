package dev.yurisuika.blossom.util.config;

import dev.yurisuika.blossom.util.config.options.Filter;
import dev.yurisuika.blossom.util.config.options.Toggle;
import dev.yurisuika.blossom.util.config.options.Value;
import dev.yurisuika.blossom.util.config.options.filter.Biome;
import dev.yurisuika.blossom.util.config.options.filter.Dimension;
import dev.yurisuika.blossom.util.config.options.filter.Downfall;
import dev.yurisuika.blossom.util.config.options.filter.Temperature;
import dev.yurisuika.blossom.util.config.options.value.Blossoming;
import dev.yurisuika.blossom.util.config.options.value.Fruiting;
import dev.yurisuika.blossom.util.config.options.value.Harvesting;

import java.util.Arrays;

public class Option {

    public static Value getValue() {
        return Config.getOptions().getValue();
    }

    public static void setValue(Value value) {
        Config.getOptions().setValue(value);
        Config.saveConfig();
    }

    public static Blossoming getBlossoming() {
        return getValue().getBlossoming();
    }

    public static void setBlossoming(Blossoming blossoming) {
        getValue().setBlossoming(blossoming);
        Config.saveConfig();
    }

    public static float getBlossomingChance() {
        return getBlossoming().getChance();
    }

    public static void setBlossomingChance(float chance) {
        getBlossoming().setChance(chance);
        Config.saveConfig();
    }

    public static Fruiting getFruiting() {
        return getValue().getFruiting();
    }

    public static void setFruiting(Fruiting fruiting) {
        getValue().setFruiting(fruiting);
        Config.saveConfig();
    }

    public static float getFruitingChance() {
        return getFruiting().getChance();
    }

    public static void setFruitingChance(float chance) {
        getFruiting().setChance(chance);
        Config.saveConfig();
    }

    public static Harvesting getHarvesting() {
        return getValue().getHarvesting();
    }

    public static void setHarvesting(Harvesting harvesting) {
        getValue().setHarvesting(harvesting);
        Config.saveConfig();
    }

    public static float getHarvestingChance() {
        return getHarvesting().getChance();
    }

    public static void setHarvestingChance(float chance) {
        getHarvesting().setChance(chance);
        Config.saveConfig();
    }

    public static int getHarvestingBonus() {
        return getHarvesting().getBonus();
    }

    public static void setHarvestingBonus(int bonus) {
        getHarvesting().setBonus(bonus);
        Config.saveConfig();
    }

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

    public static Dimension getDimension() {
        return getFilter().getDimension();
    }

    public static void setDimension(Dimension dimension) {
        getFilter().setDimension(dimension);
        Config.saveConfig();
    }

    public static String[] getDimensionWhitelist() {
        return getDimension().getWhitelist();
    }

    public static void setDimensionWhitelist(String[] whitelist) {
        getDimension().setWhitelist(whitelist);
        Arrays.sort(getDimensionWhitelist());
        Config.saveConfig();
    }

    public static String[] getDimensionBlacklist() {
        return getDimension().getBlacklist();
    }

    public static void setDimensionBlacklist(String[] blacklist) {
        getDimension().setBlacklist(blacklist);
        Arrays.sort(getDimensionBlacklist());
        Config.saveConfig();
    }

    public static Biome getBiome() {
        return getFilter().getBiome();
    }

    public static void setBiome (Biome biome) {
        getFilter().setBiome(biome);
        Config.saveConfig();
    }

    public static String[] getBiomeWhitelist() {
        return getBiome().getWhitelist();
    }

    public static void setBiomeWhitelist(String[] whitelist) {
        getBiome().setWhitelist(whitelist);
        Arrays.sort(getBiomeWhitelist());
        Config.saveConfig();
    }

    public static String[] getBiomeBlacklist() {
        return getBiome().getBlacklist();
    }

    public static void setBiomeBlacklist(String[] blacklist) {
        getBiome().setBlacklist(blacklist);
        Arrays.sort(getBiomeBlacklist());
        Config.saveConfig();
    }

    public static Toggle getToggle() {
        return Config.getOptions().getToggle();
    }

    public static void setToggle(Toggle toggle) {
        Config.getOptions().setToggle(toggle);
        Config.saveConfig();
    }

    public static boolean getWhitelist() {
        return getToggle().getWhitelist();
    }

    public static void setWhitelist(boolean whitelist) {
        getToggle().setWhitelist(whitelist);
        Config.saveConfig();
    }

    public static boolean getBlacklist() {
        return getToggle().getBlacklist();
    }

    public static void setBlacklist(boolean blacklist) {
        getToggle().setBlacklist(blacklist);
        Config.saveConfig();
    }

}