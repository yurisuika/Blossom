package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.particle.FallingPetalsParticle;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.Objects;

@Mod(value = Blossom.MOD_ID, dist = Dist.CLIENT)
public class BlossomClient {

    @EventBusSubscriber(modid = Blossom.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        public static void registerItemProperties(FMLClientSetupEvent event) {
            ItemProperties.register(BlossomItems.FLOWERING_APPLE_LEAVES, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "age"), (stack, world, entity, seed) -> {
                Integer integer = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).get(FloweringLeavesBlock.AGE);
                return Objects.nonNull(integer) ? integer / 8.0F : 0.0F;
            });
            ItemProperties.register(BlossomItems.FRUITING_APPLE_LEAVES, ResourceLocation.fromNamespaceAndPath(Blossom.MOD_ID, "age"), (stack, world, entity, seed) -> {
                Integer integer = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).get(FruitingLeavesBlock.AGE);
                return Objects.nonNull(integer) ? integer / 8.0F : 0.0F;
            });
        }

        @SubscribeEvent
        public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.APPLE_LEAVES);
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FLOWERING_APPLE_LEAVES);
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FRUITING_APPLE_LEAVES);
        }

        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
            event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.APPLE_LEAVES);
            event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.FLOWERING_APPLE_LEAVES);
            event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.FRUITING_APPLE_LEAVES);
        }

    }

    public BlossomClient() {}

}