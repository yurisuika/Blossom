package dev.yurisuika.blossom.util;

import dev.yurisuika.blossom.mixin.minecraft.world.level.biome.BiomeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class Validate {

    public static void checkBounds() {
        float temperatureMin = Math.max(Math.min(Math.min(Configure.getTemperatureMin(), 2.0F), Configure.getTemperatureMax()), -2.0F);
        float temperatureMax = Math.max(Math.max(Math.min(Configure.getTemperatureMax(), 2.0F), Configure.getTemperatureMin()), -2.0F);
        Configure.setTemperatureMin(temperatureMin);
        Configure.setTemperatureMax(temperatureMax);

        float downfallMin = Math.max(Math.min(Math.min(Configure.getDownfallMin(), 1.0F), Configure.getDownfallMax()), 0.0F);
        float downfallMax = Math.max(Math.max(Math.min(Configure.getDownfallMax(), 1.0F), Configure.getDownfallMin()), 0.0F);
        Configure.setDownfallMin(downfallMin);
        Configure.setDownfallMax(downfallMax);
    }

    public static boolean passesFilters(Level level, BlockPos pos) {
        Holder<Biome> biome = level.getBiome(pos);
        float temperature = biome.value().getBaseTemperature();
        float downfall = ((BiomeAccessor) (Object) biome.value()).getClimateSettings().downfall();
        return temperature >= Configure.getTemperatureMin() && temperature <= Configure.getTemperatureMax() && downfall >= Configure.getDownfallMin() && downfall <= Configure.getDownfallMax();
    }

}