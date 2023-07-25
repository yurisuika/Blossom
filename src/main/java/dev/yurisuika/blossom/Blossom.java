package dev.yurisuika.blossom;

import com.google.common.base.Enums;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.command.argument.PrecipitationArgumentType;
import dev.yurisuika.blossom.mixin.block.ComposterBlockInvoker;
import dev.yurisuika.blossom.mixin.block.FireBlockInvoker;
import dev.yurisuika.blossom.server.command.BlossomCommand;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.item.*;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Supplier;

@Mod("blossom")
public class Blossom {

    public static File file = new File(FMLPaths.CONFIGDIR.get().toFile(), "blossom.json");
    public static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    public static Config config = new Config();

    public static class Config {

        public Propagation propagation = new Propagation(0.2F);
        public Fertilization fertilization = new Fertilization(0.06666667F);
        public Pollination pollination = new Pollination(1);
        public Harvest harvest = new Harvest(3, 0.5714286F);
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

    public static class Harvest {

        public int extra;
        public float probability;

        public Harvest(int extra, float probability) {
            this.extra = extra;
            this.probability = probability;
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
        config.harvest.extra = Math.max(config.harvest.extra, 0);
        config.harvest.probability = Math.max(Math.min(config.harvest.probability, 1.0F), 0.0F);

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
            ((FireBlockInvoker) Blocks.FIRE).invokeRegisterFlammableBlock(Blossom.FLOWERING_OAK_LEAVES.get(), 30, 60);

            ArgumentTypes.registerByClass(PrecipitationArgumentType.class, ConstantArgumentSerializer.of(PrecipitationArgumentType::new));
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

        @SubscribeEvent
        public static void registerCreativeModeTabEvent(BuildCreativeModeTabContentsEvent event) {
            if(event.getTabKey() == ItemGroups.NATURAL) {
                event.accept(FLOWERING_OAK_LEAVES);
                event.getEntries().putAfter(Items.FLOWERING_AZALEA_LEAVES.getDefaultStack(), Item.fromBlock(FLOWERING_OAK_LEAVES.get()).getDefaultStack(), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            }
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