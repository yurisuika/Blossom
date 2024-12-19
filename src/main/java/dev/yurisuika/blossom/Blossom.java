package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.particle.FallingPetalsParticle;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.mixin.world.level.block.ComposterBlockInvoker;
import dev.yurisuika.blossom.mixin.world.level.block.FireBlockInvoker;
import dev.yurisuika.blossom.registry.BlossomableLeavesRegistry;
import dev.yurisuika.blossom.registry.FruitableLeavesRegistry;
import dev.yurisuika.blossom.registry.HarvestableFruitRegistry;
import dev.yurisuika.blossom.registry.ShearableLeavesRegistry;
import dev.yurisuika.blossom.server.commands.BlossomCommand;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.util.config.Config;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod("blossom")
public class Blossom {

    @EventBusSubscriber(modid = "blossom")
    public static class CommonForgeEvents {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            BlossomCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        }

        @SubscribeEvent
        public static void registerGoals(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof Bee entity) {
                ((BeeInterface) entity).registerGoals(entity);
            }
        }

    }

    @EventBusSubscriber(modid = "blossom", bus = EventBusSubscriber.Bus.MOD)
    public static class CommonModBusEvents {

        @SubscribeEvent
        public static void registerBlossmables(FMLCommonSetupEvent event) {
            BlossomableLeavesRegistry.add(Blocks.OAK_LEAVES, BlossomBlocks.FLOWERING_OAK_LEAVES.get());
        }

        @SubscribeEvent
        public static void registerFruitables(FMLCommonSetupEvent event) {
            FruitableLeavesRegistry.add(BlossomBlocks.FLOWERING_OAK_LEAVES.get(), BlossomBlocks.FRUITING_OAK_LEAVES.get());
        }

        @SubscribeEvent
        public static void registerHarvestables(FMLCommonSetupEvent event) {
            HarvestableFruitRegistry.add(BlossomBlocks.FRUITING_OAK_LEAVES.get(), Items.APPLE);
        }

        @SubscribeEvent
        public static void registerShearables(FMLCommonSetupEvent event) {
            ShearableLeavesRegistry.add(BlossomBlocks.FRUITING_OAK_LEAVES.get(), Blocks.OAK_LEAVES);
            ShearableLeavesRegistry.add(BlossomBlocks.FLOWERING_OAK_LEAVES.get(), Blocks.OAK_LEAVES);
        }

        @SubscribeEvent
        public static void registerCompostables(FMLCommonSetupEvent event) {
            ComposterBlockInvoker.invokeAdd(0.3F, BlossomBlocks.FRUITING_OAK_LEAVES.get());
            ComposterBlockInvoker.invokeAdd(0.3F, BlossomBlocks.FLOWERING_OAK_LEAVES.get());
        }

        @SubscribeEvent
        public static void registerFlammables(FMLCommonSetupEvent event) {
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.FRUITING_OAK_LEAVES.get(), 30, 60);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.FLOWERING_OAK_LEAVES.get(), 30, 60);
        }

    }

    @EventBusSubscriber(modid = "blossom", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            Minecraft.getInstance().particleEngine.register(BlossomParticleTypes.FLOWERING_OAK_LEAVES.get(), FallingPetalsParticle.FloweringOakProvider::new);
        }

        @SubscribeEvent
        public static void registerRenderLayers(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.FRUITING_OAK_LEAVES.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.FLOWERING_OAK_LEAVES.get(), RenderType.cutout());
        }

        @SubscribeEvent
        public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FRUITING_OAK_LEAVES.get());
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FLOWERING_OAK_LEAVES.get());
        }

        @SubscribeEvent
        public static void registerCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
                event.insertAfter(Items.FLOWERING_AZALEA_LEAVES.getDefaultInstance(), BlossomItems.FRUITING_OAK_LEAVES.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.FLOWERING_AZALEA_LEAVES.getDefaultInstance(), BlossomItems.FLOWERING_OAK_LEAVES.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }

    }

    public Blossom(ModLoadingContext context) {
        Config.loadConfig();
        Validate.checkBounds();

        BlossomBlocks.register(context.getActiveContainer().getEventBus());
        BlossomItems.register(context.getActiveContainer().getEventBus());
        BlossomParticleTypes.register(context.getActiveContainer().getEventBus());
    }

}