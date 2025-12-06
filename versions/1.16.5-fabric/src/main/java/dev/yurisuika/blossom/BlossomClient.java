package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.particle.FallingPetalsParticle;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;

import java.util.Objects;

public class BlossomClient implements ClientModInitializer {

    public static void registerParticleProviders() {
        ParticleFactoryRegistry.getInstance().register(BlossomParticleTypes.FLOWERING_APPLE_LEAVES, FallingPetalsParticle.FloweringAppleProvider::new);
    }

    public static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlossomBlocks.APPLE_SAPLING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlossomBlocks.APPLE_LEAVES, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(BlossomBlocks.FLOWERING_APPLE_LEAVES, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(BlossomBlocks.FRUITING_APPLE_LEAVES, RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(BlossomBlocks.APPLE_TRAPDOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlossomBlocks.POTTED_APPLE_SAPLING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlossomBlocks.APPLE_DOOR, RenderType.cutout());
    }

    public static void registerBlockColors() {
        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.APPLE_LEAVES);
        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FLOWERING_APPLE_LEAVES);
        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FRUITING_APPLE_LEAVES);
    }

    public static void registerItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.APPLE_LEAVES);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.FLOWERING_APPLE_LEAVES);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.FRUITING_APPLE_LEAVES);
    }

    public static void registerItemProperties() {
        FabricModelPredicateProviderRegistry.register(BlossomItems.FLOWERING_APPLE_LEAVES, ResourceLocation.tryParse("age"), (stack, world, entity) -> {
            CompoundTag tag = stack.getTagElement("BlockStateTag");
            return Objects.nonNull(tag) ? Integer.parseInt(tag.get(FloweringLeavesBlock.AGE.getName()).getAsString()) / 8.0F : 0.0F;
        });
        FabricModelPredicateProviderRegistry.register(BlossomItems.FRUITING_APPLE_LEAVES, ResourceLocation.tryParse("age"), (stack, world, entity) -> {
            CompoundTag tag = stack.getTagElement("BlockStateTag");
            return Objects.nonNull(tag) ? Integer.parseInt(tag.get(FruitingLeavesBlock.AGE.getName()).getAsString()) / 8.0F : 0.0F;
        });
    }

    @Override
    public void onInitializeClient() {
        registerParticleProviders();
        registerRenderLayers();
        registerBlockColors();
        registerItemColors();
        registerItemProperties();
    }

}