package dev.yurisuika.blossom.util;

import dev.yurisuika.blossom.util.config.Option;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class Validate {

    public static void checkBounds() {
        float temperatureMin = Math.max(Math.min(Math.min(Option.getTemperatureMin(), 2.0F), Option.getTemperatureMax()), -2.0F);
        float temperatureMax = Math.max(Math.max(Math.min(Option.getTemperatureMax(), 2.0F), Option.getTemperatureMin()), -2.0F);
        Option.setTemperatureMin(temperatureMin);
        Option.setTemperatureMax(temperatureMax);

        float downfallMin = Math.max(Math.min(Math.min(Option.getDownfallMin(), 1.0F), Option.getDownfallMax()), 0.0F);
        float downfallMax = Math.max(Math.max(Math.min(Option.getDownfallMax(), 1.0F), Option.getDownfallMin()), 0.0F);
        Option.setDownfallMin(downfallMin);
        Option.setDownfallMax(downfallMax);
    }

    public static boolean passesFilters(Level level, BlockPos pos) {
        Biome biome = level.getBiome(pos);
        float temperature = biome.getBaseTemperature();
        float downfall = biome.getDownfall();
        return temperature >= Option.getTemperatureMin() && temperature <= Option.getTemperatureMax() && downfall >= Option.getDownfallMin() && downfall <= Option.getDownfallMax();
    }

}