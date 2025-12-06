package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.model.geom.BlossomModelLayers;
import dev.yurisuika.blossom.client.particle.FallingPetalsParticle;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.world.entity.BlossomEntityType;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.world.level.FoliageColor;

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
        registerModelLayers();
        registerEntityRenderers();
    }

}