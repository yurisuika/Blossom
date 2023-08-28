package dev.yurisuika.blossom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.block.FruitingLeavesBlock;
import dev.yurisuika.blossom.client.particle.BlossomParticle;
import dev.yurisuika.blossom.entity.ai.goal.BlossomGoal;
import dev.yurisuika.blossom.entity.ai.goal.FruitGoal;
import dev.yurisuika.blossom.mixin.block.BlocksInvoker;
import dev.yurisuika.blossom.mixin.block.ComposterBlockInvoker;
import dev.yurisuika.blossom.mixin.block.FireBlockInvoker;
import dev.yurisuika.blossom.server.command.BlossomCommand;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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

    }

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

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "blossom");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "blossom");
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "blossom");

    public static final RegistryObject<Block> FRUITING_OAK_LEAVES = register("fruiting_oak_leaves", () -> new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, AbstractBlock.Settings.of(Material.LEAVES)
            .strength(0.2f)
            .ticksRandomly()
            .sounds(BlockSoundGroup.GRASS)
            .nonOpaque()
            .allowsSpawning(BlocksInvoker::invokeCanSpawnOnLeaves)
            .suffocates(BlocksInvoker::invokeNever)
            .blockVision(BlocksInvoker::invokeNever)), new Item.Settings());
    public static final RegistryObject<Block> FLOWERING_OAK_LEAVES = register("flowering_oak_leaves", () -> new FloweringLeavesBlock(Blocks.OAK_LEAVES, Blossom.FRUITING_OAK_LEAVES.get(), AbstractBlock.Settings.of(Material.LEAVES)
            .strength(0.2f)
            .ticksRandomly()
            .sounds(BlockSoundGroup.GRASS)
            .nonOpaque()
            .allowsSpawning(BlocksInvoker::invokeCanSpawnOnLeaves)
            .suffocates(BlocksInvoker::invokeNever)
            .blockVision(BlocksInvoker::invokeNever)), new Item.Settings());

    public static RegistryObject<DefaultParticleType> BLOSSOM = PARTICLES.register("blossom", () -> new DefaultParticleType(false));

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Settings settings) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), settings.group(ItemGroup.DECORATIONS)));
        return block;
    }

    @Mod.EventBusSubscriber(modid = "blossom")
    public static class CommonForgeEvents {

        @SubscribeEvent
        public static void registerCommandsEvents(RegisterCommandsEvent event) {
            BlossomCommand.register(event.getDispatcher(), event.getEnvironment());
        }

        @SubscribeEvent
        public static void entityJoinWorldEvents(EntityJoinWorldEvent event) {
            Entity entity = event.getEntity();
            if (entity instanceof BeeEntity) {
                ((BeeEntity)entity).getGoalSelector().add(4, new BlossomGoal((BeeEntity)entity));
                ((BeeEntity)entity).getGoalSelector().add(4, new FruitGoal((BeeEntity)entity));
            }
        }

    }

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModBusEvents {

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            ComposterBlockInvoker.invokeRegisterComposableItem(0.3F, Blossom.FLOWERING_OAK_LEAVES.get());
            ComposterBlockInvoker.invokeRegisterComposableItem(0.3F, Blossom.FRUITING_OAK_LEAVES.get());

            ((FireBlockInvoker)Blocks.FIRE).invokeRegisterFlammableBlock(Blossom.FLOWERING_OAK_LEAVES.get(), 30, 60);
            ((FireBlockInvoker) Blocks.FIRE).invokeRegisterFlammableBlock(Blossom.FRUITING_OAK_LEAVES.get(), 30, 60);
        }

        @SubscribeEvent
        public static void particleFactoryRegisterEvents(ParticleFactoryRegisterEvent event) {
            MinecraftClient.getInstance().particleManager.registerFactory(BLOSSOM.get(), BlossomParticle.Factory::new);
        }

    }

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SuppressWarnings("removal")
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            RenderLayers.setRenderLayer(Blossom.FLOWERING_OAK_LEAVES.get(), RenderLayer.getCutout());
            RenderLayers.setRenderLayer(Blossom.FRUITING_OAK_LEAVES.get(), RenderLayer.getCutout());

            ModelPredicateProviderRegistry.register(FLOWERING_OAK_LEAVES.get().asItem(), new Identifier("age"), (stack, world, entity, seed) -> {
                NbtCompound nbtCompound = stack.getSubNbt("BlockStateTag");
                try {
                    if (nbtCompound != null && nbtCompound.get(FloweringLeavesBlock.AGE.getName()) != null) {
                        return (float)Integer.parseInt(nbtCompound.get(FloweringLeavesBlock.AGE.getName()).asString()) / 4.0F;
                    }
                } catch (NumberFormatException ignored) {}
                return 0.0F;
            });
            ModelPredicateProviderRegistry.register(FRUITING_OAK_LEAVES.get().asItem(), new Identifier("age"), (stack, world, entity, seed) -> {
                NbtCompound nbtCompound = stack.getSubNbt("BlockStateTag");
                try {
                    if (nbtCompound != null && nbtCompound.get(FloweringLeavesBlock.AGE.getName()) != null) {
                        return (float)Integer.parseInt(nbtCompound.get(FloweringLeavesBlock.AGE.getName()).asString()) / 8.0F;
                    }
                } catch (NumberFormatException ignored) {}
                return 0.0F;
            });
        }

        @SubscribeEvent
        public static void blockColorHandlerEvents(ColorHandlerEvent.Block event) {
            event.getBlockColors().registerColorProvider((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getColor(0.5, 1.0), Blossom.FLOWERING_OAK_LEAVES.get());
            event.getBlockColors().registerColorProvider((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getColor(0.5, 1.0), Blossom.FRUITING_OAK_LEAVES.get());
        }

        @SubscribeEvent
        public static void itemColorHandlerEvents(ColorHandlerEvent.Item event) {
            event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : MinecraftClient.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex), Blossom.FLOWERING_OAK_LEAVES.get());
            event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : MinecraftClient.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex), FRUITING_OAK_LEAVES.get());
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
        PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}