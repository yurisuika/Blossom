package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.particle.BlossomParticle;
import dev.yurisuika.blossom.mixin.world.level.block.BlocksInvoker;
import dev.yurisuika.blossom.server.commands.BlossomCommand;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.util.config.Config;
import dev.yurisuika.blossom.world.entity.ai.goal.BlossomGoal;
import dev.yurisuika.blossom.world.entity.ai.goal.FruitGoal;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import java.util.Objects;

public class Blossom implements ModInitializer {

    public static final Block FRUITING_OAK_LEAVES = new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, FabricBlockSettings.of(Material.LEAVES)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(SoundType.GRASS)
            .nonOpaque()
            .allowsSpawning(BlocksInvoker::invokeOcelotOrParrot)
            .suffocates(BlocksInvoker::invokeNever)
            .blockVision(BlocksInvoker::invokeNever));
    public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(Blocks.OAK_LEAVES, Blossom.FRUITING_OAK_LEAVES, FabricBlockSettings.of(Material.LEAVES)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(SoundType.GRASS)
            .nonOpaque()
            .allowsSpawning(BlocksInvoker::invokeOcelotOrParrot)
            .suffocates(BlocksInvoker::invokeNever)
            .blockVision(BlocksInvoker::invokeNever));

    public static final SimpleParticleType BLOSSOM = FabricParticleTypes.simple(false);

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new ResourceLocation("blossom", "flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
        Registry.register(Registry.BLOCK, new ResourceLocation("blossom", "fruiting_oak_leaves"), FRUITING_OAK_LEAVES);
    }

    public static void registerItems() {
        Registry.register(Registry.ITEM, new ResourceLocation("blossom", "flowering_oak_leaves"), new BlockItem(FLOWERING_OAK_LEAVES, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        Registry.register(Registry.ITEM, new ResourceLocation("blossom", "fruiting_oak_leaves"), new BlockItem(FRUITING_OAK_LEAVES, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    }

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("blossom", "blossom"), BLOSSOM);
    }

    public static void registerCompostables() {
        CompostingChanceRegistry.INSTANCE.add(FLOWERING_OAK_LEAVES, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(FRUITING_OAK_LEAVES, 0.3F);
    }

    public static void registerFlammables() {
        FlammableBlockRegistry.getDefaultInstance().add(FLOWERING_OAK_LEAVES, 30, 60);
        FlammableBlockRegistry.getDefaultInstance().add(FRUITING_OAK_LEAVES, 30, 60);
    }

    public static void registerServerEntityEvents() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (entity instanceof Bee) {
                ((Bee) entity).getGoalSelector().addGoal(4, new BlossomGoal((Bee) entity));
                ((Bee) entity).getGoalSelector().addGoal(4, new FruitGoal((Bee) entity));
            }
        });
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(BlossomCommand::register);
    }

    @Override
    public void onInitialize() {
        Config.loadConfig();
        Validate.checkBounds();

        registerBlocks();
        registerItems();
        registerParticles();
        registerFlammables();
        registerCompostables();
        registerServerEntityEvents();
        registerCommands();
    }

    public static class Client implements ClientModInitializer {

        @Environment(EnvType.CLIENT)
        public static void registerParticleFactories() {
            ParticleFactoryRegistry.getInstance().register(BLOSSOM, BlossomParticle.Factory::new);
        }

        @Environment(EnvType.CLIENT)
        public static void registerRenderLayers() {
            BlockRenderLayerMap.INSTANCE.putBlock(FLOWERING_OAK_LEAVES, RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(FRUITING_OAK_LEAVES, RenderType.cutout());
        }

        @Environment(EnvType.CLIENT)
        public static void registerColorProviders() {
            ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), FLOWERING_OAK_LEAVES);
            ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), FRUITING_OAK_LEAVES);

            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), FLOWERING_OAK_LEAVES);
            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), FRUITING_OAK_LEAVES);
        }

        @Environment(EnvType.CLIENT)
        public static void registerModelPredicateProviders() {
            ItemProperties.register(FLOWERING_OAK_LEAVES.asItem(), new ResourceLocation("age"), (stack, world, entity, seed) -> {
                CompoundTag tag = stack.getTagElement("BlockStateTag");
                try {
                    if (Objects.nonNull(tag) && Objects.nonNull(tag.get(FloweringLeavesBlock.AGE.getName()))) {
                        return Integer.parseInt(tag.get(FloweringLeavesBlock.AGE.getName()).getAsString()) / 4.0F;
                    }
                } catch (NumberFormatException ignored) {}
                return 0.0F;
            });
            ItemProperties.register(FRUITING_OAK_LEAVES.asItem(), new ResourceLocation("age"), (stack, world, entity, seed) -> {
                CompoundTag tag = stack.getTagElement("BlockStateTag");
                try {
                    if (Objects.nonNull(tag) && Objects.nonNull(tag.get(FruitingLeavesBlock.AGE.getName()))) {
                        return Integer.parseInt(tag.get(FruitingLeavesBlock.AGE.getName()).getAsString()) / 8.0F;
                    }
                } catch (NumberFormatException ignored) {}
                return 0.0F;
            });
        }

        @Override
        public void onInitializeClient() {
            registerParticleFactories();
            registerRenderLayers();
            registerColorProviders();
            registerModelPredicateProviders();
        }

    }

}