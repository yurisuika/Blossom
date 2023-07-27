package dev.yurisuika.blossom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.datagen.lang.BlossomEnglishLanguageProvider;
import dev.yurisuika.blossom.datagen.loottable.BlossomBlockLootTableGenerator;
import dev.yurisuika.blossom.datagen.tag.BlossomBlockTagProvider;
import dev.yurisuika.blossom.datagen.tag.BlossomItemTagProvider;
import dev.yurisuika.blossom.server.command.BlossomCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
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
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Arrays;

public class Blossom implements ModInitializer, ClientModInitializer, DataGeneratorEntrypoint {

    public static File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "blossom.json");
    public static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    public static Config config = new Config();

    public static class Config {

        public Value value = new Value(
                new Value.Propagation(0.2F),
                new Value.Fertilization(0.06666667F),
                new Value.Pollination(1),
                new Value.Fruit(3, 0.5714286F)
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

    }

    public static class Value {

        public Propagation propagation;
        public Fertilization fertilization;
        public Pollination pollination;
        public Fruit fruit;

        public Value(Propagation propagation, Fertilization fertilization, Pollination pollination, Fruit fruit) {
            this.propagation = propagation;
            this.fertilization = fertilization;
            this.pollination = pollination;
            this.fruit = fruit;
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

        public static class Fruit {

            public int bonus;
            public float chance;

            public Fruit(int bonus, float chance) {
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
        config.value.propagation.chance = Math.max(Math.min(config.value.propagation.chance, 1.0F), 0.0F);
        config.value.fertilization.chance = Math.max(Math.min(config.value.fertilization.chance, 1.0F), 0.0F);
        config.value.pollination.age = Math.max(Math.min(config.value.pollination.age, 7), 0);
        config.value.fruit.bonus = Math.max(config.value.fruit.bonus, 0);
        config.value.fruit.chance = Math.max(Math.min(config.value.fruit.chance, 1.0F), 0.0F);

        float temperatureMin = Math.max(Math.min(Math.min(config.filter.temperature.min, 2.0F), config.filter.temperature.max), -2.0F);
        float temperatureMax = Math.max(Math.max(Math.min(config.filter.temperature.max, 2.0F), config.filter.temperature.min), -2.0F);
        config.filter.temperature.min = temperatureMin;
        config.filter.temperature.max = temperatureMax;

        float downfallMin = Math.max(Math.min(Math.min(config.filter.downfall.min, 2.0F), config.filter.downfall.max), -2.0F);
        float downfallMax = Math.max(Math.max(Math.min(config.filter.downfall.max, 2.0F), config.filter.downfall.min), -2.0F);
        config.filter.downfall.min = downfallMin;
        config.filter.downfall.max = downfallMax;

        Arrays.sort(config.filter.dimension.whitelist);
        Arrays.sort(config.filter.dimension.blacklist);
        Arrays.sort(config.filter.biome.whitelist);
        Arrays.sort(config.filter.biome.blacklist);

        saveConfig();
    }

    public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(Blocks.OAK_LEAVES, FabricBlockSettings.copy(Blocks.OAK_LEAVES));

    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, new Identifier("blossom", "flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
    }

    public static void registerItems() {
        Registry.register(Registries.ITEM, new Identifier("blossom", "flowering_oak_leaves"), new BlockItem(FLOWERING_OAK_LEAVES, new Item.Settings()));
    }

    public static void registerCompostables() {
        CompostingChanceRegistry.INSTANCE.add(Blossom.FLOWERING_OAK_LEAVES, 0.3F);
    }

    public static void registerFlammables() {
        FlammableBlockRegistry.getDefaultInstance().add(Blossom.FLOWERING_OAK_LEAVES, 30, 60);
    }

    public static void registerItemGroupEvents() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Items.FLOWERING_AZALEA_LEAVES, FLOWERING_OAK_LEAVES.asItem());
        });
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(BlossomCommand::register);
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

    @Environment(EnvType.CLIENT)
    public static void registerModelPredicateProviders() {
        ModelPredicateProviderRegistry.register(FLOWERING_OAK_LEAVES.asItem(), new Identifier("age"), (stack, world, entity, seed) -> {
            NbtCompound nbtCompound = stack.getSubNbt("BlockStateTag");
            try {
                NbtElement nbtElement;
                if (nbtCompound != null && nbtCompound.get(FloweringLeavesBlock.AGE.getName()) != null) {
                    return (float)Integer.parseInt(nbtCompound.get(FloweringLeavesBlock.AGE.getName()).asString()) / 8.0F;
                }
            } catch (NumberFormatException ignored) {}
            return 0.0F;
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerDataProviders(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.createPack().addProvider(BlossomEnglishLanguageProvider::new);
        fabricDataGenerator.createPack().addProvider(BlossomBlockLootTableGenerator::new);
        fabricDataGenerator.createPack().addProvider(BlossomBlockTagProvider::new);
        fabricDataGenerator.createPack().addProvider(BlossomItemTagProvider::new);
    }

    @Override
    public void onInitialize() {
        if (!file.exists()) {
            saveConfig();
        }
        loadConfig();

        registerBlocks();
        registerItems();
        registerFlammables();
        registerCompostables();
        registerItemGroupEvents();
        registerCommands();
    }

    @Override
    public void onInitializeClient() {
        registerRenderLayers();
        registerColorProviders();
        registerModelPredicateProviders();
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        registerDataProviders(fabricDataGenerator);
    }

}