package dev.yurisuika.blossom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.server.command.BlossomCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

public class Blossom implements ModInitializer, ClientModInitializer {

    public static File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "blossom.json");
    public static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    public static Config config = new Config();

    public static class Config {

        public int rate = 5;

        public Count count = new Count(2, 4);

    }

    public static class Count {

        public int min;
        public int max;

        public Count(int min, int max) {
            this.min = min;
            this.max = max;
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
        int min = Math.max(Math.min(Math.min(config.count.min, 64), config.count.max), 1);
        int max = Math.max(Math.max(Math.min(config.count.max, 64), config.count.min), 1);
        config.rate = Math.max(config.rate, 1);
        config.count.min = min;
        config.count.max = max;
        saveConfig();
    }

    public static final TagKey<Block> BLOSSOMS = TagKey.of(RegistryKeys.BLOCK, new Identifier("blossom", "blossoms"));

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
    }

    @Override
    public void onInitializeClient() {
        Blossom.registerRenderLayers();
        Blossom.registerColorProviders();
    }

}