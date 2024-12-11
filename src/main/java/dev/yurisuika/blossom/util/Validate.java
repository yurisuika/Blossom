package dev.yurisuika.blossom.util;

import dev.yurisuika.blossom.util.config.Config;
import dev.yurisuika.blossom.util.config.Option;

import java.util.Arrays;

public class Validate {

    public static void checkBounds() {
        Option.setBlossomingChance(Math.max(Math.min(Option.getBlossomingChance(), 1.0F), 0.0F));
        Option.setFruitingChance(Math.max(Math.min(Option.getFruitingChance(), 1.0F), 0.0F));
        Option.setHarvestingChance(Math.max(Math.min(Option.getHarvestingChance(), 1.0F), 0.0F));
        Option.setHarvestingBonus(Math.max(Option.getHarvestingBonus(), 0));

        float temperatureMin = Math.max(Math.min(Math.min(Option.getTemperatureMin(), 2.0F), Option.getTemperatureMax()), -2.0F);
        float temperatureMax = Math.max(Math.max(Math.min(Option.getTemperatureMax(), 2.0F), Option.getTemperatureMin()), -2.0F);
        Option.setTemperatureMin(temperatureMin);
        Option.setTemperatureMax(temperatureMax);

        float downfallMin = Math.max(Math.min(Math.min(Option.getDownfallMin(), 1.0F), Option.getDownfallMax()), 0.0F);
        float downfallMax = Math.max(Math.max(Math.min(Option.getDownfallMax(), 1.0F), Option.getDownfallMin()), 0.0F);
        Option.setDownfallMin(downfallMin);
        Option.setDownfallMax(downfallMax);

        sort(Option.getDimensionWhitelist());
        sort(Option.getDimensionBlacklist());
        sort(Option.getBiomeWhitelist());
        sort(Option.getBiomeBlacklist());
    }

    public static void sort(String[] array) {
        Arrays.sort(array);
        Config.saveConfig();
    }

}