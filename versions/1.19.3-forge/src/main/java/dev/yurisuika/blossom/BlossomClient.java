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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Objects;

@Mod(Blossom.MOD_ID)
public class BlossomClient {

    @Mod.EventBusSubscriber(modid = Blossom.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
            ItemProperties.register(BlossomItems.FLOWERING_APPLE_LEAVES, new ResourceLocation(Blossom.MOD_ID, "age"), (stack, world, entity, seed) -> {
                CompoundTag tag = stack.getTagElement("BlockStateTag");
                return Objects.nonNull(tag) ? Integer.parseInt(tag.get(FloweringLeavesBlock.AGE.getName()).getAsString()) / 8.0F : 0.0F;
            });
            ItemProperties.register(BlossomItems.FRUITING_APPLE_LEAVES, new ResourceLocation(Blossom.MOD_ID, "age"), (stack, world, entity, seed) -> {
                CompoundTag tag = stack.getTagElement("BlockStateTag");
                return Objects.nonNull(tag) ? Integer.parseInt(tag.get(FruitingLeavesBlock.AGE.getName()).getAsString()) / 8.0F : 0.0F;
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

        @SubscribeEvent
        public static void registerCreativeModeTabContents(CreativeModeTabEvent.BuildContents event) {
            if (CreativeModeTabs.NATURAL_BLOCKS.equals(event.getTab())) {
                event.accept(BlossomItems.APPLE_PLANKS);
                event.accept(BlossomItems.APPLE_SAPLING);
                event.accept(BlossomItems.APPLE_LOG);
                event.accept(BlossomItems.STRIPPED_APPLE_LOG);
                event.accept(BlossomItems.APPLE_WOOD);
                event.accept(BlossomItems.STRIPPED_APPLE_WOOD);
                event.accept(BlossomItems.APPLE_LEAVES);
                event.accept(BlossomItems.FLOWERING_APPLE_LEAVES);
                event.accept(BlossomItems.FRUITING_APPLE_LEAVES);
                event.accept(BlossomItems.APPLE_SIGN);
                event.accept(BlossomItems.APPLE_HANGING_SIGN);
                event.accept(BlossomItems.APPLE_PRESSURE_PLATE);
                event.accept(BlossomItems.APPLE_TRAPDOOR);
                event.accept(BlossomItems.APPLE_BUTTON);
                event.accept(BlossomItems.APPLE_STAIRS);
                event.accept(BlossomItems.APPLE_SLAB);
                event.accept(BlossomItems.APPLE_FENCE_GATE);
                event.accept(BlossomItems.APPLE_FENCE);
                event.accept(BlossomItems.APPLE_DOOR);
            }
        }

    }

    public BlossomClient() {}

}