package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.particle.FallingPetalsParticle;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.mixin.world.level.block.ComposterBlockInvoker;
import dev.yurisuika.blossom.mixin.world.level.block.FireBlockInvoker;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("blossom")
public class Blossom {

    @Mod.EventBusSubscriber(modid = "blossom")

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

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModBusEvents {

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

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
                event.accept(BlossomBlocks.FRUITING_OAK_LEAVES);
                event.getEntries().putAfter(Items.FLOWERING_AZALEA_LEAVES.getDefaultInstance(), BlossomItems.FRUITING_OAK_LEAVES.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(BlossomBlocks.FLOWERING_OAK_LEAVES);
                event.getEntries().putAfter(Items.FLOWERING_AZALEA_LEAVES.getDefaultInstance(), BlossomItems.FLOWERING_OAK_LEAVES.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }

    }

    public Blossom(FMLJavaModLoadingContext context) {
        Config.loadConfig();
        Validate.checkBounds();

        BlossomBlocks.register(context.getModEventBus());
        BlossomItems.register(context.getModEventBus());
        BlossomParticleTypes.register(context.getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);
    }

}