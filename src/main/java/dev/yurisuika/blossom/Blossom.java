package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.particle.BlossomParticle;
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
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Objects;

public class Blossom implements ModInitializer {

    public static final Block FRUITING_OAK_LEAVES = new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, FabricBlockSettings.create()
            .mapColor(MapColor.PLANT)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(SoundType.GRASS)
            .nonOpaque()
            .allowsSpawning(Blocks::ocelotOrParrot)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
            .burnable()
            .pistonBehavior(PushReaction.DESTROY)
            .solidBlock(Blocks::never));
    public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(Blocks.OAK_LEAVES, Blossom.FRUITING_OAK_LEAVES, FabricBlockSettings.create()
            .mapColor(MapColor.PLANT)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(SoundType.GRASS)
            .nonOpaque()
            .allowsSpawning(Blocks::ocelotOrParrot)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
            .burnable()
            .pistonBehavior(PushReaction.DESTROY)
            .solidBlock(Blocks::never));

    public static final SimpleParticleType BLOSSOM = FabricParticleTypes.simple(false);

    public static void registerBlocks() {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_oak_leaves"), FRUITING_OAK_LEAVES);
    }

    public static void registerItems() {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_oak_leaves"), new BlockItem(FLOWERING_OAK_LEAVES, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_oak_leaves"), new BlockItem(FRUITING_OAK_LEAVES, new Item.Properties()));
    }

    public static void registerParticles() {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath("blossom", "blossom"), BLOSSOM);
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
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(content -> {
            content.addAfter(Items.FLOWERING_AZALEA_LEAVES, FLOWERING_OAK_LEAVES.asItem(), FRUITING_OAK_LEAVES.asItem());
        });
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
        registerItemGroupEvents();
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
            ItemProperties.register(FLOWERING_OAK_LEAVES.asItem(), ResourceLocation.parse("age"), (stack, world, entity, seed) -> {
                BlockItemStateProperties blockItemStateProperties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
                Integer integer = blockItemStateProperties.get(FloweringLeavesBlock.AGE);
                if (Objects.nonNull(integer)) {
                    return integer.intValue() / 4.0F;
                }
                return 0.0F;
            });
            ItemProperties.register(FRUITING_OAK_LEAVES.asItem(), ResourceLocation.parse("age"), (stack, world, entity, seed) -> {
                BlockItemStateProperties blockItemStateProperties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
                Integer integer = blockItemStateProperties.get(FruitingLeavesBlock.AGE);
                if (Objects.nonNull(integer)) {
                    return integer.intValue() / 8.0F;
                }
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