package dev.yurisuika.blossom.client.option;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Arrays;

public class BlossomConfig {

    public static File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "blossom.json");
    public static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    public static Config config = new Config();

    public static class Config {

        public Value value = new Value(
                new Value.Blossoming(0.2F, 10.0D),
                new Value.Fruiting(0.2F, 10.0D),
                new Value.Harvesting(3, 0.5714286F)
        );
        public Filter filter = new Filter(
                new Filter.Temperature(-2.0F, 2.0F),
                new Filter.Downfall(0.0F, 1.0F),
                new Filter.Dimension(new String[]{"minecraft:overworld"}, new String[]{"minecraft:the_nether", "minecraft:the_end"}),
                new Filter.Biome(new String[]{"minecraft:forest"}, new String[]{"minecraft:the_void"})
        );
        public Toggle toggle = new Toggle(
                false,
                false
        );

        public static class Value {

            public Blossoming blossoming;
            public Fruiting fruiting;
            public Harvesting harvesting;

            public Value(Blossoming blossoming, Fruiting fruiting, Harvesting harvesting) {
                this.blossoming = blossoming;
                this.fruiting = fruiting;
                this.harvesting = harvesting;
            }

            public static class Blossoming {

                public float chance;
                public double distance;

                public Blossoming(float chance, double distance) {
                    this.chance = chance;
                    this.distance = distance;
                }

            }

            public static class Fruiting {

                public float chance;
                public double distance;

                public Fruiting(float chance, double distance) {
                    this.chance = chance;
                    this.distance = distance;
                }

            }

            public static class Harvesting {

                public int bonus;
                public float chance;

                public Harvesting(int bonus, float chance) {
                    this.bonus = bonus;
                    this.chance = chance;
                }

            }

        }

        public static class Filter {

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

            public static class Temperature {

                public float min;
                public float max;

                public Temperature(float min, float max) {
                    this.min = min;
                    this.max = max;
                }

            }

            public static class Downfall {

                public float min;
                public float max;

                public Downfall(float min, float max) {
                    this.min = min;
                    this.max = max;
                }

            }

            public static class Dimension {

                public String[] whitelist;
                public String[] blacklist;

                public Dimension(String[] whitelist, String[] blacklist) {
                    this.whitelist = whitelist;
                    this.blacklist = blacklist;
                }

            }

            public static class Biome {

                public String[] whitelist;
                public String[] blacklist;

                public Biome(String[] whitelist, String[] blacklist) {
                    this.whitelist = whitelist;
                    this.blacklist = blacklist;
                }

            }

        }

        public static class Toggle {

            public boolean whitelist;
            public boolean blacklist;

            public Toggle(boolean whitelist, boolean blacklist) {
                this.whitelist = whitelist;
                this.blacklist = blacklist;
            }

        }

    }

    public static void saveConfig() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(getConfig()));
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        try {
            if (file.exists()) {
                config = gson.fromJson(Files.readString(file.toPath()), Config.class);
            } else {
                config = new Config();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkBounds();
        setConfig(config);
    }

    public static void setConfig(Config config) {
        BlossomConfig.config = config;
    }

    public static Config getConfig() {
        return config;
    }

    public static void checkBounds() {
        config.value.blossoming.chance = Math.max(Math.min(config.value.blossoming.chance, 1.0F), 0.0F);
        config.value.blossoming.distance = Math.max(config.value.blossoming.distance, 0.0D);
        config.value.fruiting.chance = Math.max(Math.min(config.value.fruiting.chance, 1.0F), 0.0F);
        config.value.fruiting.distance = Math.max(config.value.fruiting.distance, 0.0D);
        config.value.harvesting.bonus = Math.max(config.value.harvesting.bonus, 0);
        config.value.harvesting.chance = Math.max(Math.min(config.value.harvesting.chance, 1.0F), 0.0F);

        float temperatureMin = Math.max(Math.min(Math.min(config.filter.temperature.min, 2.0F), config.filter.temperature.max), -2.0F);
        float temperatureMax = Math.max(Math.max(Math.min(config.filter.temperature.max, 2.0F), config.filter.temperature.min), -2.0F);
        config.filter.temperature.min = temperatureMin;
        config.filter.temperature.max = temperatureMax;

        float downfallMin = Math.max(Math.min(Math.min(config.filter.downfall.min, 1.0F), config.filter.downfall.max), 0.0F);
        float downfallMax = Math.max(Math.max(Math.min(config.filter.downfall.max, 1.0F), config.filter.downfall.min), 0.0F);
        config.filter.downfall.min = downfallMin;
        config.filter.downfall.max = downfallMax;

        Arrays.sort(config.filter.dimension.whitelist);
        Arrays.sort(config.filter.dimension.blacklist);
        Arrays.sort(config.filter.biome.whitelist);
        Arrays.sort(config.filter.biome.blacklist);

        saveConfig();
    }

}