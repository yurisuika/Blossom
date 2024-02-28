package dev.yurisuika.blossom;

import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.block.FruitingLeavesBlock;
import dev.yurisuika.blossom.client.particle.BlossomParticle;
import dev.yurisuika.blossom.entity.ai.goal.BlossomGoal;
import dev.yurisuika.blossom.entity.ai.goal.FruitGoal;
import dev.yurisuika.blossom.server.command.BlossomCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static dev.yurisuika.blossom.client.option.BlossomConfig.*;

public class Blossom implements ModInitializer, ClientModInitializer {

    public static final Block FRUITING_OAK_LEAVES = new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, FabricBlockSettings.create()
            .mapColor(MapColor.DARK_GREEN)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(BlockSoundGroup.GRASS)
            .nonOpaque()
            .allowsSpawning(Blocks::canSpawnOnLeaves)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
            .burnable()
            .pistonBehavior(PistonBehavior.DESTROY)
            .solidBlock(Blocks::never));
    public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(Blocks.OAK_LEAVES, Blossom.FRUITING_OAK_LEAVES, FabricBlockSettings.create()
            .mapColor(MapColor.DARK_GREEN)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(BlockSoundGroup.GRASS)
            .nonOpaque()
            .allowsSpawning(Blocks::canSpawnOnLeaves)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
            .burnable()
            .pistonBehavior(PistonBehavior.DESTROY)
            .solidBlock(Blocks::never));

    public static final DefaultParticleType BLOSSOM = FabricParticleTypes.simple(false);

    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, new Identifier("blossom", "flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
        Registry.register(Registries.BLOCK, new Identifier("blossom", "fruiting_oak_leaves"), FRUITING_OAK_LEAVES);
    }

    public static void registerItems() {
        Registry.register(Registries.ITEM, new Identifier("blossom", "flowering_oak_leaves"), new BlockItem(FLOWERING_OAK_LEAVES, new Item.Settings()));
        Registry.register(Registries.ITEM, new Identifier("blossom", "fruiting_oak_leaves"), new BlockItem(FRUITING_OAK_LEAVES, new Item.Settings()));
    }

    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier("blossom", "blossom"), BLOSSOM);
    }

    public static void registerCompostables() {
        CompostingChanceRegistry.INSTANCE.add(FLOWERING_OAK_LEAVES, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(FRUITING_OAK_LEAVES, 0.3F);
    }

    public static void registerFlammables() {
        FlammableBlockRegistry.getDefaultInstance().add(FLOWERING_OAK_LEAVES, 30, 60);
        FlammableBlockRegistry.getDefaultInstance().add(FRUITING_OAK_LEAVES, 30, 60);
    }

    public static void registerItemGroupEvents() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
            content.addAfter(Items.FLOWERING_AZALEA_LEAVES, FLOWERING_OAK_LEAVES.asItem(), FRUITING_OAK_LEAVES.asItem());
        });
    }

    public static void registerServerEntityEvents() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof BeeEntity) {
                ((BeeEntity)entity).getGoalSelector().add(4, new BlossomGoal((BeeEntity)entity));
                ((BeeEntity)entity).getGoalSelector().add(4, new FruitGoal((BeeEntity)entity));
            }
        });
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(BlossomCommand::register);
    }

    @Environment(EnvType.CLIENT)
    public static void registerParticleFactories() {
        ParticleFactoryRegistry.getInstance().register(BLOSSOM, BlossomParticle.Factory::new);
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(FLOWERING_OAK_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FRUITING_OAK_LEAVES, RenderLayer.getCutout());
    }

    @Environment(EnvType.CLIENT)
    public static void registerColorProviders() {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getColor(0.5, 1.0), FLOWERING_OAK_LEAVES);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getColor(0.5, 1.0), FRUITING_OAK_LEAVES);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : MinecraftClient.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex), FLOWERING_OAK_LEAVES);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : MinecraftClient.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex), FRUITING_OAK_LEAVES);
    }

    @Environment(EnvType.CLIENT)
    public static void registerModelPredicateProviders() {
        ModelPredicateProviderRegistry.register(FLOWERING_OAK_LEAVES.asItem(), new Identifier("age"), (stack, world, entity, seed) -> {
            NbtCompound nbtCompound = stack.getSubNbt("BlockStateTag");
            try {
                if (nbtCompound != null && nbtCompound.get(FloweringLeavesBlock.AGE.getName()) != null) {
                    return (float)Integer.parseInt(nbtCompound.get(FloweringLeavesBlock.AGE.getName()).asString()) / 4.0F;
                }
            } catch (NumberFormatException ignored) {}
            return 0.0F;
        });
        ModelPredicateProviderRegistry.register(FRUITING_OAK_LEAVES.asItem(), new Identifier("age"), (stack, world, entity, seed) -> {
            NbtCompound nbtCompound = stack.getSubNbt("BlockStateTag");
            try {
                if (nbtCompound != null && nbtCompound.get(FruitingLeavesBlock.AGE.getName()) != null) {
                    return (float)Integer.parseInt(nbtCompound.get(FruitingLeavesBlock.AGE.getName()).asString()) / 8.0F;
                }
            } catch (NumberFormatException ignored) {}
            return 0.0F;
        });
    }

    @Override
    public void onInitialize() {
        if (!file.exists()) {
            saveConfig();
        }
        loadConfig();

        registerBlocks();
        registerItems();
        registerParticles();
        registerFlammables();
        registerCompostables();
        registerItemGroupEvents();
        registerServerEntityEvents();
        registerCommands();
    }

    @Override
    public void onInitializeClient() {
        registerParticleFactories();
        registerRenderLayers();
        registerColorProviders();
        registerModelPredicateProviders();
    }

}