package dev.yurisuika.blossom;

import dev.yurisuika.blossom.config.Config;
import dev.yurisuika.blossom.mixin.minecraft.world.level.block.ComposterBlockInvoker;
import dev.yurisuika.blossom.mixin.minecraft.world.level.block.FireBlockInvoker;
import dev.yurisuika.blossom.registry.BlossomableLeavesRegistry;
import dev.yurisuika.blossom.registry.FruitableLeavesRegistry;
import dev.yurisuika.blossom.registry.HarvestableFruitRegistry;
import dev.yurisuika.blossom.registry.ShearableLeavesRegistry;
import dev.yurisuika.blossom.server.commands.BlossomCommand;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.world.entity.BlossomEntityType;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.item.BlossomCreativeModeTabs;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.state.properties.BlossomBlockSetType;
import dev.yurisuika.blossom.world.level.block.state.properties.BlossomWoodType;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("blossom")
public class Blossom {

    public static final Logger LOGGER = LoggerFactory.getLogger("blossom");

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class GameEvents {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            BlossomCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        }

        @SubscribeEvent
        public static void registerGoals(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof Bee entity) {
                ((BeeInterface) entity).registerGoals(entity);
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
        public static void registerBlockSetTypes(RegisterEvent event) {
            BlockSetType.register(BlossomBlockSetType.APPLE);
        }

        @SubscribeEvent
        public static void registerWoodTypes(RegisterEvent event) {
            WoodType.register(BlossomWoodType.APPLE);
        }

        @SubscribeEvent
        public static void registerBlocks(RegisterEvent event) {
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_planks"), BlossomBlocks.APPLE_PLANKS));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_sapling"), BlossomBlocks.APPLE_SAPLING));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_log"), BlossomBlocks.APPLE_LOG));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "stripped_apple_log"), BlossomBlocks.STRIPPED_APPLE_LOG));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_wood"), BlossomBlocks.APPLE_WOOD));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "stripped_apple_wood"), BlossomBlocks.STRIPPED_APPLE_WOOD));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_leaves"), BlossomBlocks.APPLE_LEAVES));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "flowering_apple_leaves"), BlossomBlocks.FLOWERING_APPLE_LEAVES));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_apple_leaves"), BlossomBlocks.FRUITING_APPLE_LEAVES));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_sign"), BlossomBlocks.APPLE_SIGN));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_wall_sign"), BlossomBlocks.APPLE_WALL_SIGN));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_hanging_sign"), BlossomBlocks.APPLE_HANGING_SIGN));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_wall_hanging_sign"), BlossomBlocks.APPLE_WALL_HANGING_SIGN));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_pressure_plate"), BlossomBlocks.APPLE_PRESSURE_PLATE));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_trapdoor"), BlossomBlocks.APPLE_TRAPDOOR));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "potted_apple_sapling"), BlossomBlocks.POTTED_APPLE_SAPLING));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_button"), BlossomBlocks.APPLE_BUTTON));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_stairs"), BlossomBlocks.APPLE_STAIRS));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_slab"), BlossomBlocks.APPLE_SLAB));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_fence_gate"), BlossomBlocks.APPLE_FENCE_GATE));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_fence"), BlossomBlocks.APPLE_FENCE));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_door"), BlossomBlocks.APPLE_DOOR));
        }

        @SubscribeEvent
        public static void registerItems(RegisterEvent event) {
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_planks"), BlossomItems.APPLE_PLANKS));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_sapling"), BlossomItems.APPLE_SAPLING));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_log"), BlossomItems.APPLE_LOG));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "stripped_apple_log"), BlossomItems.STRIPPED_APPLE_LOG));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_wood"), BlossomItems.APPLE_WOOD));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "stripped_apple_wood"), BlossomItems.STRIPPED_APPLE_WOOD));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_leaves"), BlossomItems.APPLE_LEAVES));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "flowering_apple_leaves"), BlossomItems.FLOWERING_APPLE_LEAVES));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_apple_leaves"), BlossomItems.FRUITING_APPLE_LEAVES));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_sign"), BlossomItems.APPLE_SIGN));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_hanging_sign"), BlossomItems.APPLE_HANGING_SIGN));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_pressure_plate"), BlossomItems.APPLE_PRESSURE_PLATE));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_trapdoor"), BlossomItems.APPLE_TRAPDOOR));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_button"), BlossomItems.APPLE_BUTTON));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_stairs"), BlossomItems.APPLE_STAIRS));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_slab"), BlossomItems.APPLE_SLAB));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_fence_gate"), BlossomItems.APPLE_FENCE_GATE));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_fence"), BlossomItems.APPLE_FENCE));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_door"), BlossomItems.APPLE_DOOR));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_boat"), BlossomItems.APPLE_BOAT));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_chest_boat"), BlossomItems.APPLE_CHEST_BOAT));
        }

        @SubscribeEvent
        public static void registerEntityTypes(RegisterEvent event) {
            event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_boat"), BlossomEntityType.APPLE_BOAT));
            event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "apple_chest_boat"), BlossomEntityType.APPLE_CHEST_BOAT));
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

        @SubscribeEvent
        public static void registerCreativeModeTabs(RegisterEvent event) {
            event.register(BuiltInRegistries.CREATIVE_MODE_TAB.key(), helper -> helper.register(ResourceLocation.fromNamespaceAndPath("blossom", "blossom"), BlossomCreativeModeTabs.BLOSSOM));
        }

    }

    public Blossom() {}

}