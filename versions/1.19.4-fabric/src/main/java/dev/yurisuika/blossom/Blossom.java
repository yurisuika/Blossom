package dev.yurisuika.blossom;

import dev.yurisuika.blossom.config.Config;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.mixin.minecraft.world.level.block.state.properties.BlockSetTypeInvoker;
import dev.yurisuika.blossom.mixin.minecraft.world.level.block.state.properties.WoodTypeInvoker;
import dev.yurisuika.blossom.registry.BlossomableLeavesRegistry;
import dev.yurisuika.blossom.registry.FruitableLeavesRegistry;
import dev.yurisuika.blossom.registry.HarvestableFruitRegistry;
import dev.yurisuika.blossom.registry.ShearableLeavesRegistry;
import dev.yurisuika.blossom.server.commands.BlossomCommand;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.item.BlossomCreativeModeTabs;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.state.properties.BlossomBlockSetType;
import dev.yurisuika.blossom.world.level.block.state.properties.BlossomWoodType;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Blossom implements ModInitializer {

    public static final String MOD_ID = "blossom";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void loadConfiguration() {
        Config.loadConfig();
        Validate.checkBounds();
    }

    public static void registerBlockSetTypes() {
        BlockSetTypeInvoker.invokeRegister(BlossomBlockSetType.APPLE);
    }

    public static void registerWoodTypes() {
        WoodTypeInvoker.invokeRegister(BlossomWoodType.APPLE);
    }

    public static void registerBlocks() {
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_planks"), BlossomBlocks.APPLE_PLANKS);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_sapling"), BlossomBlocks.APPLE_SAPLING);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_log"), BlossomBlocks.APPLE_LOG);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "stripped_apple_log"), BlossomBlocks.STRIPPED_APPLE_LOG);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_wood"), BlossomBlocks.APPLE_WOOD);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "stripped_apple_wood"), BlossomBlocks.STRIPPED_APPLE_WOOD);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_leaves"), BlossomBlocks.APPLE_LEAVES);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "flowering_apple_leaves"), BlossomBlocks.FLOWERING_APPLE_LEAVES);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "fruiting_apple_leaves"), BlossomBlocks.FRUITING_APPLE_LEAVES);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_sign"), BlossomBlocks.APPLE_SIGN);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_wall_sign"), BlossomBlocks.APPLE_WALL_SIGN);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_hanging_sign"), BlossomBlocks.APPLE_HANGING_SIGN);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_wall_hanging_sign"), BlossomBlocks.APPLE_WALL_HANGING_SIGN);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_pressure_plate"), BlossomBlocks.APPLE_PRESSURE_PLATE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_trapdoor"), BlossomBlocks.APPLE_TRAPDOOR);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "potted_apple_sapling"), BlossomBlocks.POTTED_APPLE_SAPLING);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_button"), BlossomBlocks.APPLE_BUTTON);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_stairs"), BlossomBlocks.APPLE_STAIRS);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_slab"), BlossomBlocks.APPLE_SLAB);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_fence_gate"), BlossomBlocks.APPLE_FENCE_GATE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_fence"), BlossomBlocks.APPLE_FENCE);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "apple_door"), BlossomBlocks.APPLE_DOOR);
    }

    public static void registerItems() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_planks"), BlossomItems.APPLE_PLANKS);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_sapling"), BlossomItems.APPLE_SAPLING);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_log"), BlossomItems.APPLE_LOG);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "stripped_apple_log"), BlossomItems.STRIPPED_APPLE_LOG);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_wood"), BlossomItems.APPLE_WOOD);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "stripped_apple_wood"), BlossomItems.STRIPPED_APPLE_WOOD);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_leaves"), BlossomItems.APPLE_LEAVES);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "flowering_apple_leaves"), BlossomItems.FLOWERING_APPLE_LEAVES);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "fruiting_apple_leaves"), BlossomItems.FRUITING_APPLE_LEAVES);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_sign"), BlossomItems.APPLE_SIGN);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_hanging_sign"), BlossomItems.APPLE_HANGING_SIGN);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_pressure_plate"), BlossomItems.APPLE_PRESSURE_PLATE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_trapdoor"), BlossomItems.APPLE_TRAPDOOR);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_button"), BlossomItems.APPLE_BUTTON);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_stairs"), BlossomItems.APPLE_STAIRS);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_slab"), BlossomItems.APPLE_SLAB);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_fence_gate"), BlossomItems.APPLE_FENCE_GATE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_fence"), BlossomItems.APPLE_FENCE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_door"), BlossomItems.APPLE_DOOR);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_boat"), BlossomItems.APPLE_BOAT);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "apple_chest_boat"), BlossomItems.APPLE_CHEST_BOAT);
    }

    public static void registerParticles() {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(MOD_ID, "flowering_apple_leaves"), BlossomParticleTypes.FLOWERING_APPLE_LEAVES);
    }

    public static void registerBlossmableLeaves() {
        BlossomableLeavesRegistry.add(BlossomBlocks.APPLE_LEAVES, BlossomBlocks.FLOWERING_APPLE_LEAVES);
    }

    public static void registerFruitableLeaves() {
        FruitableLeavesRegistry.add(BlossomBlocks.FLOWERING_APPLE_LEAVES, BlossomBlocks.FRUITING_APPLE_LEAVES);
    }
    
    public static void registerHarvestableFruits() {
        HarvestableFruitRegistry.add(BlossomBlocks.FRUITING_APPLE_LEAVES, Items.APPLE, 0.5714286F, 3);
    }

    public static void registerShearableLeaves() {
        ShearableLeavesRegistry.add(BlossomBlocks.FLOWERING_APPLE_LEAVES, Blocks.OAK_LEAVES);
        ShearableLeavesRegistry.add(BlossomBlocks.FRUITING_APPLE_LEAVES, Blocks.OAK_LEAVES);
    }

    public static void registerCompostables() {
        CompostingChanceRegistry.INSTANCE.add(BlossomBlocks.APPLE_LEAVES, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(BlossomBlocks.FLOWERING_APPLE_LEAVES, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(BlossomBlocks.FRUITING_APPLE_LEAVES, 0.3F);
    }

    public static void registerFlammables() {
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.APPLE_PLANKS, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.APPLE_SLAB, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.APPLE_FENCE_GATE, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.APPLE_FENCE, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.APPLE_STAIRS, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.APPLE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.STRIPPED_APPLE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.STRIPPED_APPLE_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.APPLE_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.APPLE_LEAVES, 30, 60);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.FLOWERING_APPLE_LEAVES, 30, 60);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.FRUITING_APPLE_LEAVES, 30, 60);
    }

    public static void registerCreativeModeTabs() {
        ItemGroupHelper.appendItemGroup(BlossomCreativeModeTabs.BLOSSOM);
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(BlossomCommand::register);
    }

    public static void registerGoals() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (entity instanceof Bee) {
                ((BeeInterface) entity).registerGoals((Bee) entity);
            }
        });
    }

    @Override
    public void onInitialize() {
        loadConfiguration();
        registerBlockSetTypes();
        registerWoodTypes();
        registerBlocks();
        registerItems();
        registerParticles();
        registerBlossmableLeaves();
        registerFruitableLeaves();
        registerHarvestableFruits();
        registerShearableLeaves();
        registerFlammables();
        registerCompostables();
        registerCreativeModeTabs();
        registerCommands();
        registerGoals();
    }

}