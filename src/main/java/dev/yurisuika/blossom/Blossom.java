package dev.yurisuika.blossom;

import com.google.common.base.Enums;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.command.argument.PrecipitationArgumentType;
import dev.yurisuika.blossom.server.command.BlossomCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Arrays;

public class Blossom implements ModInitializer, ClientModInitializer {

    public static File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "blossom.json");
    public static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    public static Config config = new Config();

    public static class Config {

        public Propagation propagation = new Propagation(0.2F);
        public Fertilization fertilization = new Fertilization(0.0666666666F);
        public Pollination pollination = new Pollination(1);
        public Count count = new Count(2, 4);
        public Climate climate = new Climate(
                new String[]{"none", "rain", "snow"},
                new Climate.Temperature(-2.0F, 2.0F),
                new Climate.Downfall(0.0F, 1.0F),
                new Climate.Whitelist(false, new String[]{"minecraft:overworld"}, new String[]{"minecraft:forest"}),
                new Climate.Blacklist(false, new String[]{"minecraft:the_nether", "minecraft:the_end"}, new String[]{"minecraft:the_void"})
        );

    }

    public static class Propagation {

        public float chance;

        public Propagation(float chance) {
            this.chance = chance;
        }

    }

    public static class Fertilization {

        public float chance;

        public Fertilization(float chance) {
            this.chance = chance;
        }

    }

    public static class Pollination {

        public int age;

        public Pollination(int age) {
            this.age = age;
        }

    }

    public static class Count {

        public int min;
        public int max;

        public Count(int min, int max) {
            this.min = min;
            this.max = max;
        }

    }

    public static class Climate {

       public String[] precipitation;
       public Temperature temperature;
       public Downfall downfall;
       public Whitelist whitelist;
       public Blacklist blacklist;

       public Climate(String[] precipitation, Temperature temperature, Downfall downfall, Whitelist whitelist, Blacklist blacklist) {
           this.precipitation = precipitation;
           this.temperature = temperature;
           this.downfall = downfall;
           this.whitelist = whitelist;
           this.blacklist = blacklist;
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

        public static class Whitelist {

            public boolean enabled;
            public String[] dimensions;
            public String[] biomes;

            public Whitelist(boolean enabled, String[] dimensions, String[] biomes) {
                this.enabled = enabled;
                this.dimensions = dimensions;
                this.biomes = biomes;
            }

        }

        public static class Blacklist {

            public boolean enabled;
            public String[] dimensions;
            public String[] biomes;

            public Blacklist(boolean enabled, String[] dimensions, String[] biomes) {
                this.enabled = enabled;
                this.dimensions = dimensions;
                this.biomes = biomes;
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
        Blossom.config = config;
    }

    public static Config getConfig() {
        return config;
    }

    public static void checkBounds() {
        config.propagation.chance = Math.max(Math.min(config.propagation.chance, 1.0F), 0.0F);
        config.fertilization.chance = Math.max(Math.min(config.fertilization.chance, 1.0F), 0.0F);
        config.pollination.age = Math.max(Math.min(config.pollination.age, 7), 0);

        int countMin = Math.max(Math.min(Math.min(config.count.min, 64), config.count.max), 1);
        int countMax = Math.max(Math.max(Math.min(config.count.max, 64), config.count.min), 1);
        config.count.min = countMin;
        config.count.max = countMax;

        Arrays.stream(config.climate.precipitation).forEach(precipitation -> {
            if(!Enums.getIfPresent(Biome.Precipitation.class, precipitation.toUpperCase()).isPresent()) {
                int index = ArrayUtils.indexOf(config.climate.precipitation, precipitation);
                config.climate.precipitation = ArrayUtils.remove(config.climate.precipitation, index);
            }
        });
        Arrays.sort(config.climate.precipitation);

        float temperatureMin = Math.max(Math.min(Math.min(config.climate.temperature.min, 2.0F), config.climate.temperature.max), -2.0F);
        float temperatureMax = Math.max(Math.max(Math.min(config.climate.temperature.max, 2.0F), config.climate.temperature.min), -2.0F);
        config.climate.temperature.min = temperatureMin;
        config.climate.temperature.max = temperatureMax;

        float downfallMin = Math.max(Math.min(Math.min(config.climate.downfall.min, 2.0F), config.climate.downfall.max), -2.0F);
        float downfallMax = Math.max(Math.max(Math.min(config.climate.downfall.max, 2.0F), config.climate.downfall.min), -2.0F);
        config.climate.downfall.min = downfallMin;
        config.climate.downfall.max = downfallMax;

        Arrays.sort(config.climate.whitelist.dimensions);
        Arrays.sort(config.climate.whitelist.biomes);
        Arrays.sort(config.climate.blacklist.dimensions);
        Arrays.sort(config.climate.blacklist.biomes);

        saveConfig();
    }

    public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(Blocks.OAK_LEAVES, FabricBlockSettings.copy(Blocks.OAK_LEAVES).requiresTool());

    public static void registerCompostables() {
        CompostingChanceRegistry.INSTANCE.add(Blossom.FLOWERING_OAK_LEAVES, 0.3F);
    }

    public static void registerFlammables() {
        FlammableBlockRegistry.getDefaultInstance().add(Blossom.FLOWERING_OAK_LEAVES, 30, 60);
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blossom.FLOWERING_OAK_LEAVES, RenderLayer.getCutout());
    }

    @Environment(EnvType.CLIENT)
    public static void registerColorProviders() {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getColor(0.5, 1.0), Blossom.FLOWERING_OAK_LEAVES);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : MinecraftClient.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex), Blossom.FLOWERING_OAK_LEAVES);
    }

    @Override
    public void onInitialize() {
        if (!file.exists()) {
            saveConfig();
        }
        loadConfig();

        CommandRegistrationCallback.EVENT.register(BlossomCommand::register);

        Registry.register(Registries.BLOCK, new Identifier("blossom", "flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
        Registry.register(Registries.ITEM, new Identifier("blossom", "flowering_oak_leaves"), new BlockItem(FLOWERING_OAK_LEAVES, new Item.Settings()));

        Blossom.registerFlammables();
        Blossom.registerCompostables();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Items.FLOWERING_AZALEA_LEAVES, Item.fromBlock(FLOWERING_OAK_LEAVES));
        });

        ArgumentTypeRegistry.registerArgumentType(new Identifier("blossom", "precipitation"), PrecipitationArgumentType.class, ConstantArgumentSerializer.of(PrecipitationArgumentType::precipitation));
    }

    @Override
    public void onInitializeClient() {
        Blossom.registerRenderLayers();
        Blossom.registerColorProviders();
    }

}