package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.particle.FallingPetalsParticle;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.server.commands.BlossomCommand;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.util.config.Config;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;

import java.util.Objects;

public class Blossom implements ModInitializer {

    public static void registerCompostables() {
        CompostingChanceRegistry.INSTANCE.add(BlossomBlocks.FRUITING_OAK_LEAVES, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(BlossomBlocks.FLOWERING_OAK_LEAVES, 0.3F);
    }

    public static void registerFlammables() {
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.FRUITING_OAK_LEAVES, 30, 60);
        FlammableBlockRegistry.getDefaultInstance().add(BlossomBlocks.FLOWERING_OAK_LEAVES, 30, 60);
    }

    public static void registerGoals() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (entity instanceof Bee) {
                ((BeeInterface) entity).registerGoals((Bee) entity);
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

        BlossomBlocks.register();
        BlossomItems.register();
        BlossomParticleTypes.register();
        registerFlammables();
        registerCompostables();
        registerCommands();
        registerGoals();
    }

    public static class Client implements ClientModInitializer {

        public static void registerParticles() {
            ParticleFactoryRegistry.getInstance().register(BlossomParticleTypes.FLOWERING_OAK_LEAVES, FallingPetalsParticle.FloweringOakProvider::new);
        }

        public static void registerRenderLayers() {
            BlockRenderLayerMap.INSTANCE.putBlock(BlossomBlocks.FRUITING_OAK_LEAVES, RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(BlossomBlocks.FLOWERING_OAK_LEAVES, RenderType.cutout());
        }

        public static void registerBlockColors() {
            ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FRUITING_OAK_LEAVES);
            ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FLOWERING_OAK_LEAVES);
        }

        public static void registerItemColors() {
            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.FRUITING_OAK_LEAVES);
            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.FLOWERING_OAK_LEAVES);
        }

        public static void registerItemProperties() {
            FabricModelPredicateProviderRegistry.register(BlossomItems.FRUITING_OAK_LEAVES, ResourceLocation.tryParse("age"), (stack, world, entity) -> {
                CompoundTag tag = stack.getTagElement("BlockStateTag");
                return Objects.nonNull(tag) ? Integer.parseInt(tag.get(FruitingLeavesBlock.AGE.getName()).getAsString()) / 8.0F : 0.0F;
            });
            FabricModelPredicateProviderRegistry.register(BlossomItems.FLOWERING_OAK_LEAVES, ResourceLocation.tryParse("age"), (stack, world, entity) -> {
                CompoundTag tag = stack.getTagElement("BlockStateTag");
                return Objects.nonNull(tag) ? Integer.parseInt(tag.get(FloweringLeavesBlock.AGE.getName()).getAsString()) / 4.0F : 0.0F;
            });
        }

        @Override
        public void onInitializeClient() {
            registerParticles();
            registerRenderLayers();
            registerBlockColors();
            registerItemColors();
            registerItemProperties();
        }

    }

}