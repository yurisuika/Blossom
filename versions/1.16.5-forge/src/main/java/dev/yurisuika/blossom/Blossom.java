package dev.yurisuika.blossom;

import dev.yurisuika.blossom.config.Config;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.data.worldgen.features.BlossomTreeFeatures;
import dev.yurisuika.blossom.mixin.minecraft.world.level.block.ComposterBlockInvoker;
import dev.yurisuika.blossom.mixin.minecraft.world.level.block.FireBlockInvoker;
import dev.yurisuika.blossom.registry.BlossomableLeavesRegistry;
import dev.yurisuika.blossom.registry.FruitableLeavesRegistry;
import dev.yurisuika.blossom.registry.HarvestableFruitRegistry;
import dev.yurisuika.blossom.registry.ShearableLeavesRegistry;
import dev.yurisuika.blossom.server.commands.BlossomCommand;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.state.properties.BlossomWoodType;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod("blossom")
public class Blossom {

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class GameEvents {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            BlossomCommand.register(event.getDispatcher(), event.getEnvironment());
        }

        @SubscribeEvent
        public static void entityJoinWorldEvents(EntityJoinWorldEvent event) {
            if (event.getEntity() instanceof Bee) {
                ((BeeInterface) event.getEntity()).registerGoals((Bee) event.getEntity());
            }
        }

    }

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void loadConfiguration(FMLCommonSetupEvent event) {
            Config.loadConfig();
            Validate.checkBounds();
        }

        @SubscribeEvent
        public static void registerWoodTypes(RegistryEvent.Register<?> event) {
            WoodType.register(BlossomWoodType.APPLE);
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            event.getRegistry().register(BlossomBlocks.APPLE_PLANKS.setRegistryName(ResourceLocation.tryParse("blossom:apple_planks")));
            event.getRegistry().register(BlossomBlocks.APPLE_SAPLING.setRegistryName(ResourceLocation.tryParse("blossom:apple_sapling")));
            event.getRegistry().register(BlossomBlocks.APPLE_LOG.setRegistryName(ResourceLocation.tryParse("blossom:apple_log")));
            event.getRegistry().register(BlossomBlocks.STRIPPED_APPLE_LOG.setRegistryName(ResourceLocation.tryParse("blossom:stripped_apple_log")));
            event.getRegistry().register(BlossomBlocks.APPLE_WOOD.setRegistryName(ResourceLocation.tryParse("blossom:apple_wood")));
            event.getRegistry().register(BlossomBlocks.STRIPPED_APPLE_WOOD.setRegistryName(ResourceLocation.tryParse("blossom:stripped_apple_wood")));
            event.getRegistry().register(BlossomBlocks.APPLE_LEAVES.setRegistryName(ResourceLocation.tryParse("blossom:apple_leaves")));
            event.getRegistry().register(BlossomBlocks.FLOWERING_APPLE_LEAVES.setRegistryName(ResourceLocation.tryParse("blossom:flowering_apple_leaves")));
            event.getRegistry().register(BlossomBlocks.FRUITING_APPLE_LEAVES.setRegistryName(ResourceLocation.tryParse("blossom:fruiting_apple_leaves")));
            event.getRegistry().register(BlossomBlocks.APPLE_SIGN.setRegistryName(ResourceLocation.tryParse("blossom:apple_sign")));
            event.getRegistry().register(BlossomBlocks.APPLE_WALL_SIGN.setRegistryName(ResourceLocation.tryParse("blossom:apple_wall_sign")));
            event.getRegistry().register(BlossomBlocks.APPLE_PRESSURE_PLATE.setRegistryName(ResourceLocation.tryParse("blossom:apple_pressure_plate")));
            event.getRegistry().register(BlossomBlocks.APPLE_TRAPDOOR.setRegistryName(ResourceLocation.tryParse("blossom:apple_trapdoor")));
            event.getRegistry().register(BlossomBlocks.POTTED_APPLE_SAPLING.setRegistryName(ResourceLocation.tryParse("blossom:potted_apple_sapling")));
            event.getRegistry().register(BlossomBlocks.APPLE_BUTTON.setRegistryName(ResourceLocation.tryParse("blossom:apple_button")));
            event.getRegistry().register(BlossomBlocks.APPLE_STAIRS.setRegistryName(ResourceLocation.tryParse("blossom:apple_stairs")));
            event.getRegistry().register(BlossomBlocks.APPLE_SLAB.setRegistryName(ResourceLocation.tryParse("blossom:apple_slab")));
            event.getRegistry().register(BlossomBlocks.APPLE_FENCE_GATE.setRegistryName(ResourceLocation.tryParse("blossom:apple_fence_gate")));
            event.getRegistry().register(BlossomBlocks.APPLE_FENCE.setRegistryName(ResourceLocation.tryParse("blossom:apple_fence")));
            event.getRegistry().register(BlossomBlocks.APPLE_DOOR.setRegistryName(ResourceLocation.tryParse("blossom:apple_door")));
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(BlossomItems.APPLE_PLANKS.setRegistryName(ResourceLocation.tryParse("blossom:apple_planks")));
            event.getRegistry().register(BlossomItems.APPLE_SAPLING.setRegistryName(ResourceLocation.tryParse("blossom:apple_sapling")));
            event.getRegistry().register(BlossomItems.APPLE_LOG.setRegistryName(ResourceLocation.tryParse("blossom:apple_log")));
            event.getRegistry().register(BlossomItems.STRIPPED_APPLE_LOG.setRegistryName(ResourceLocation.tryParse("blossom:stripped_apple_log")));
            event.getRegistry().register(BlossomItems.APPLE_WOOD.setRegistryName(ResourceLocation.tryParse("blossom:apple_wood")));
            event.getRegistry().register(BlossomItems.STRIPPED_APPLE_WOOD.setRegistryName(ResourceLocation.tryParse("blossom:stripped_apple_wood")));
            event.getRegistry().register(BlossomItems.APPLE_LEAVES.setRegistryName(ResourceLocation.tryParse("blossom:apple_leaves")));
            event.getRegistry().register(BlossomItems.FLOWERING_APPLE_LEAVES.setRegistryName(ResourceLocation.tryParse("blossom:flowering_apple_leaves")));
            event.getRegistry().register(BlossomItems.FRUITING_APPLE_LEAVES.setRegistryName(ResourceLocation.tryParse("blossom:fruiting_apple_leaves")));
            event.getRegistry().register(BlossomItems.APPLE_SIGN.setRegistryName(ResourceLocation.tryParse("blossom:apple_sign")));
            event.getRegistry().register(BlossomItems.APPLE_PRESSURE_PLATE.setRegistryName(ResourceLocation.tryParse("blossom:apple_pressure_plate")));
            event.getRegistry().register(BlossomItems.APPLE_TRAPDOOR.setRegistryName(ResourceLocation.tryParse("blossom:apple_trapdoor")));
            event.getRegistry().register(BlossomItems.APPLE_BUTTON.setRegistryName(ResourceLocation.tryParse("blossom:apple_button")));
            event.getRegistry().register(BlossomItems.APPLE_STAIRS.setRegistryName(ResourceLocation.tryParse("blossom:apple_stairs")));
            event.getRegistry().register(BlossomItems.APPLE_SLAB.setRegistryName(ResourceLocation.tryParse("blossom:apple_slab")));
            event.getRegistry().register(BlossomItems.APPLE_FENCE_GATE.setRegistryName(ResourceLocation.tryParse("blossom:apple_fence_gate")));
            event.getRegistry().register(BlossomItems.APPLE_FENCE.setRegistryName(ResourceLocation.tryParse("blossom:apple_fence")));
            event.getRegistry().register(BlossomItems.APPLE_DOOR.setRegistryName(ResourceLocation.tryParse("blossom:apple_door")));
            event.getRegistry().register(BlossomItems.APPLE_BOAT.setRegistryName(ResourceLocation.tryParse("blossom:apple_boat")));
        }

        @SubscribeEvent
        public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
            event.getRegistry().register(BlossomParticleTypes.FLOWERING_APPLE_LEAVES.setRegistryName(ResourceLocation.tryParse("blossom:flowering_apple_leaves")));
        }

        @SubscribeEvent
        public static void registerConfiguredFeatures(FMLCommonSetupEvent event) {
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ResourceLocation.tryParse("blossom:apple"), BlossomTreeFeatures.APPLE);
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ResourceLocation.tryParse("blossom:apple_bees_0002"), BlossomTreeFeatures.APPLE_BEES_0002);
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ResourceLocation.tryParse("blossom:apple_bees_002"), BlossomTreeFeatures.APPLE_BEES_002);
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ResourceLocation.tryParse("blossom:apple_bees_005"), BlossomTreeFeatures.APPLE_BEES_005);
        }

        @SubscribeEvent
        public static void registerBlossmableLeaves(FMLCommonSetupEvent event) {
            BlossomableLeavesRegistry.add(BlossomBlocks.APPLE_LEAVES, BlossomBlocks.FLOWERING_APPLE_LEAVES);
        }

        @SubscribeEvent
        public static void registerFruitableLeaves(FMLCommonSetupEvent event) {
            FruitableLeavesRegistry.add(BlossomBlocks.FLOWERING_APPLE_LEAVES, BlossomBlocks.FRUITING_APPLE_LEAVES);
        }

        @SubscribeEvent
        public static void registerHarvestableFruits(FMLCommonSetupEvent event) {
            HarvestableFruitRegistry.add(BlossomBlocks.FRUITING_APPLE_LEAVES, Items.APPLE, 0.5714286F, 3);
        }

        @SubscribeEvent
        public static void registerShearableLeaves(FMLCommonSetupEvent event) {
            ShearableLeavesRegistry.add(BlossomBlocks.FLOWERING_APPLE_LEAVES, Blocks.OAK_LEAVES);
            ShearableLeavesRegistry.add(BlossomBlocks.FRUITING_APPLE_LEAVES, Blocks.OAK_LEAVES);
        }

        @SubscribeEvent
        public static void registerCompostables(FMLCommonSetupEvent event) {
            ComposterBlockInvoker.invokeAdd(0.3F, BlossomBlocks.APPLE_LEAVES);
            ComposterBlockInvoker.invokeAdd(0.3F, BlossomBlocks.FLOWERING_APPLE_LEAVES);
            ComposterBlockInvoker.invokeAdd(0.3F, BlossomBlocks.FRUITING_APPLE_LEAVES);
        }

        @SubscribeEvent
        public static void registerFlammables(FMLCommonSetupEvent event) {
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.APPLE_PLANKS, 5, 20);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.APPLE_SLAB, 5, 20);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.APPLE_FENCE_GATE, 5, 20);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.APPLE_FENCE, 5, 20);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.APPLE_STAIRS, 5, 20);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.APPLE_LOG, 5, 5);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.STRIPPED_APPLE_LOG, 5, 5);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.STRIPPED_APPLE_WOOD, 5, 5);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.APPLE_WOOD, 5, 5);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.APPLE_LEAVES, 30, 60);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.FLOWERING_APPLE_LEAVES, 30, 60);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.FRUITING_APPLE_LEAVES, 30, 60);
        }

    }

    public Blossom() {}

}