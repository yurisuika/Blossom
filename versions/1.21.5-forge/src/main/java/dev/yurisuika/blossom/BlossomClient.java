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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("blossom")
public class BlossomClient {

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEvents {

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            Minecraft.getInstance().particleEngine.register(BlossomParticleTypes.FLOWERING_APPLE_LEAVES, FallingPetalsParticle.FloweringAppleProvider::new);
        }

        @SubscribeEvent
        public static void registerRenderLayers(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.APPLE_SAPLING, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.APPLE_LEAVES, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.FLOWERING_APPLE_LEAVES, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.FRUITING_APPLE_LEAVES, RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.APPLE_TRAPDOOR, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.POTTED_APPLE_SAPLING, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.APPLE_DOOR, RenderType.cutout());
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