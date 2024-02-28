package dev.yurisuika.blossom;

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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static dev.yurisuika.blossom.client.option.BlossomConfig.*;

@Mod("blossom")
public class Blossom {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "blossom");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "blossom");
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "blossom");

    public static final RegistryObject<Block> FRUITING_OAK_LEAVES = register("fruiting_oak_leaves", () -> new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, AbstractBlock.Settings.of(Material.LEAVES)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(BlockSoundGroup.GRASS)
            .nonOpaque()
            .allowsSpawning(BlocksInvoker::invokeCanSpawnOnLeaves)
            .suffocates(BlocksInvoker::invokeNever)
            .blockVision(BlocksInvoker::invokeNever)), new Item.Settings());
    public static final RegistryObject<Block> FLOWERING_OAK_LEAVES = register("flowering_oak_leaves", () -> new FloweringLeavesBlock(Blocks.OAK_LEAVES, Blossom.FRUITING_OAK_LEAVES.get(), AbstractBlock.Settings.of(Material.LEAVES)
            .strength(0.2F)
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