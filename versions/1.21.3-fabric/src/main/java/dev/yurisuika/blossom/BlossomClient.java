package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.model.geom.BlossomModelLayers;
import dev.yurisuika.blossom.client.particle.FallingPetalsParticle;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.world.entity.BlossomEntityType;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.BlockItemStateProperties;
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
        ItemProperties.register(BlossomItems.FLOWERING_APPLE_LEAVES, ResourceLocation.tryParse("age"), (stack, world, entity, seed) -> {
            Integer integer = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).get(FloweringLeavesBlock.AGE);
            return Objects.nonNull(integer) ? integer / 8.0F : 0.0F;
        });
        ItemProperties.register(BlossomItems.FRUITING_APPLE_LEAVES, ResourceLocation.tryParse("age"), (stack, world, entity, seed) -> {
            Integer integer = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).get(FruitingLeavesBlock.AGE);
            return Objects.nonNull(integer) ? integer / 8.0F : 0.0F;
        });
    }

    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(BlossomModelLayers.APPLE_BOAT, BoatModel::createBoatModel);
        EntityModelLayerRegistry.registerModelLayer(BlossomModelLayers.APPLE_CHEST_BOAT, BoatModel::createChestBoatModel);
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(BlossomEntityType.APPLE_BOAT, context -> new BoatRenderer(context, BlossomModelLayers.APPLE_BOAT));
        EntityRendererRegistry.register(BlossomEntityType.APPLE_CHEST_BOAT, context -> new BoatRenderer(context, BlossomModelLayers.APPLE_CHEST_BOAT));
    }

    @Override
    public void onInitializeClient() {
        registerParticleProviders();
        registerRenderLayers();
        registerBlockColors();
        registerItemColors();
        registerItemProperties();
        registerModelLayers();
        registerEntityRenderers();
    }

}