package dev.yurisuika.blossom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.mixin.block.ComposterBlockInvoker;
import dev.yurisuika.blossom.mixin.block.FireBlockInvoker;
import dev.yurisuika.blossom.server.command.BlossomCommand;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.function.Supplier;

@Mod("blossom")
public class Blossom {

    public static File file = new File(FMLPaths.CONFIGDIR.get().toFile(), "blossom.json");
    public static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    public static Config config = new Config();

    public static class Config {

        public boolean exposed = true;
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

    public static final TagKey<Biome> ALLOWS_BLOSSOMS = TagKey.of(RegistryKeys.BIOME, new Identifier("blossom", "allows_blossoms"));
    public static final TagKey<Block> BLOSSOMS = TagKey.of(RegistryKeys.BLOCK, new Identifier("blossom", "blossoms"));

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "blossom");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "blossom");

    public static final RegistryObject<Block> FLOWERING_OAK_LEAVES = register("flowering_oak_leaves", () -> new FloweringLeavesBlock(Blocks.OAK_LEAVES, AbstractBlock.Settings.copy(Blocks.OAK_LEAVES)), new Item.Settings());

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Settings settings) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), settings));
        return block;
    }

    @Mod.EventBusSubscriber(modid = "blossom")
    public static class CommonForgeEvents {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            BlossomCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        }

    }

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModBusEvents {

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            ComposterBlockInvoker.invokeRegisterComposableItem(0.3F, Blossom.FLOWERING_OAK_LEAVES.get());
            ((FireBlockInvoker)Blocks.FIRE).invokeRegisterFlammableBlock(Blossom.FLOWERING_OAK_LEAVES.get(), 30, 60);
        }

    }

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SuppressWarnings("removal")
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            RenderLayers.setRenderLayer(Blossom.FLOWERING_OAK_LEAVES.get(), RenderLayer.getCutout());
        }

        @SubscribeEvent
        public static void registerBlockColorProviders(final RegisterColorHandlersEvent.Block color) {
            color.getBlockColors().registerColorProvider((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getColor(0.5, 1.0), Blossom.FLOWERING_OAK_LEAVES.get());
        }

        @SubscribeEvent
        public static void registerItemColorProviders(final RegisterColorHandlersEvent.Item color) {
            color.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : MinecraftClient.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex), FLOWERING_OAK_LEAVES.get());
        }

    }

    public Blossom() {
        if (!file.exists()) {
            saveConfig();
        }
        loadConfig();

        MinecraftForge.EVENT_BUS.register(this);

        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}