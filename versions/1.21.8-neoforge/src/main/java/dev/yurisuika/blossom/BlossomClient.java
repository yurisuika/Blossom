package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.model.geom.BlossomModelLayers;
import dev.yurisuika.blossom.client.particle.FallingPetalsParticle;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.world.entity.BlossomEntityType;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@Mod(value = "blossom", dist = Dist.CLIENT)
public class BlossomClient {

    @EventBusSubscriber(modid = "blossom", value = Dist.CLIENT)
    public static class ModEvents {

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            Minecraft.getInstance().particleEngine.register(BlossomParticleTypes.FLOWERING_APPLE_LEAVES, FallingPetalsParticle.FloweringAppleProvider::new);
        }

        @SubscribeEvent
        public static void registerRenderLayers(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.APPLE_SAPLING, ChunkSectionLayer.CUTOUT);
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.APPLE_LEAVES, ChunkSectionLayer.CUTOUT_MIPPED);
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.FLOWERING_APPLE_LEAVES, ChunkSectionLayer.CUTOUT_MIPPED);
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.FRUITING_APPLE_LEAVES, ChunkSectionLayer.CUTOUT_MIPPED);
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.APPLE_TRAPDOOR, ChunkSectionLayer.CUTOUT);
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.POTTED_APPLE_SAPLING, ChunkSectionLayer.CUTOUT);
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.APPLE_DOOR, ChunkSectionLayer.CUTOUT);
        }

        @SubscribeEvent
        public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.APPLE_LEAVES);
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FLOWERING_APPLE_LEAVES);
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FRUITING_APPLE_LEAVES);
        }

        @SubscribeEvent
        public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(BlossomModelLayers.APPLE_BOAT, BoatModel::createBoatModel);
            event.registerLayerDefinition(BlossomModelLayers.APPLE_CHEST_BOAT, BoatModel::createChestBoatModel);
        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BlossomEntityType.APPLE_BOAT, context -> new BoatRenderer(context, BlossomModelLayers.APPLE_BOAT));
            event.registerEntityRenderer(BlossomEntityType.APPLE_CHEST_BOAT, context -> new BoatRenderer(context, BlossomModelLayers.APPLE_CHEST_BOAT));
        }

    }

    public BlossomClient() {}

}