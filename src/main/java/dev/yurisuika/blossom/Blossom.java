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
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Objects;

@Mod("blossom")
public class Blossom {

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class GameEvents {

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
    public static class ModEvents {

        @SubscribeEvent
        public static void registerBlocks(RegisterEvent event) {
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.tryParse("blossom:flowering_oak_leaves"), BlossomBlocks.FLOWERING_OAK_LEAVES));
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> helper.register(ResourceLocation.tryParse("blossom:fruiting_oak_leaves"), BlossomBlocks.FRUITING_OAK_LEAVES));
        }

        @SubscribeEvent
        public static void registerItems(RegisterEvent event) {
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.tryParse("blossom:flowering_oak_leaves"), BlossomItems.FLOWERING_OAK_LEAVES));
            event.register(ForgeRegistries.Keys.ITEMS, helper -> helper.register(ResourceLocation.tryParse("blossom:fruiting_oak_leaves"), BlossomItems.FRUITING_OAK_LEAVES));
        }

        @SubscribeEvent
        public static void registerParticles(RegisterEvent event) {
            event.register(ForgeRegistries.Keys.PARTICLE_TYPES, helper -> helper.register(ResourceLocation.tryParse("blossom:flowering_oak_leaves"), BlossomParticleTypes.FLOWERING_OAK_LEAVES));
        }

        @SubscribeEvent
        public static void registerBlossmables(FMLCommonSetupEvent event) {
            BlossomableLeavesRegistry.add(Blocks.OAK_LEAVES, BlossomBlocks.FLOWERING_OAK_LEAVES);
        }

        @SubscribeEvent
        public static void registerFruitables(FMLCommonSetupEvent event) {
            FruitableLeavesRegistry.add(BlossomBlocks.FLOWERING_OAK_LEAVES, BlossomBlocks.FRUITING_OAK_LEAVES);
        }

        @SubscribeEvent
        public static void registerHarvestables(FMLCommonSetupEvent event) {
            HarvestableFruitRegistry.add(BlossomBlocks.FRUITING_OAK_LEAVES, Items.APPLE, 0.5714286F, 3);
        }

        @SubscribeEvent
        public static void registerShearables(FMLCommonSetupEvent event) {
            ShearableLeavesRegistry.add(BlossomBlocks.FLOWERING_OAK_LEAVES, Blocks.OAK_LEAVES);
            ShearableLeavesRegistry.add(BlossomBlocks.FRUITING_OAK_LEAVES, Blocks.OAK_LEAVES);
        }

        @SubscribeEvent
        public static void registerCompostables(FMLCommonSetupEvent event) {
            ComposterBlockInvoker.invokeAdd(0.3F, BlossomBlocks.FLOWERING_OAK_LEAVES);
            ComposterBlockInvoker.invokeAdd(0.3F, BlossomBlocks.FRUITING_OAK_LEAVES);
        }

        @SubscribeEvent
        public static void registerFlammables(FMLCommonSetupEvent event) {
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.FLOWERING_OAK_LEAVES, 30, 60);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(BlossomBlocks.FRUITING_OAK_LEAVES, 30, 60);
        }

    }

    public Blossom() {
        Config.loadConfig();
        Validate.checkBounds();
    }

    @Mod("blossom")
    public static class Client {

        @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public static class ModEvents {

            @SubscribeEvent
            public static void registerParticles(RegisterParticleProvidersEvent event) {
                Minecraft.getInstance().particleEngine.register(BlossomParticleTypes.FLOWERING_OAK_LEAVES, FallingPetalsParticle.FloweringOakProvider::new);
            }

            @SubscribeEvent
            public static void registerRenderLayers(FMLClientSetupEvent event) {
                ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.FLOWERING_OAK_LEAVES, RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(BlossomBlocks.FRUITING_OAK_LEAVES, RenderType.cutout());
            }

            @SubscribeEvent
            public static void registerItemProperties(FMLClientSetupEvent event) {
                ItemProperties.register(BlossomItems.FLOWERING_OAK_LEAVES, ResourceLocation.tryParse("age"), (stack, world, entity, seed) -> {
                    CompoundTag tag = stack.getTagElement("BlockStateTag");
                    return Objects.nonNull(tag) ? Integer.parseInt(tag.get(FloweringLeavesBlock.AGE.getName()).getAsString()) / 8.0F : 0.0F;
                });
                ItemProperties.register(BlossomItems.FRUITING_OAK_LEAVES, ResourceLocation.tryParse("age"), (stack, world, entity, seed) -> {
                    CompoundTag tag = stack.getTagElement("BlockStateTag");
                    return Objects.nonNull(tag) ? Integer.parseInt(tag.get(FruitingLeavesBlock.AGE.getName()).getAsString()) / 8.0F : 0.0F;
                });
            }

            @SubscribeEvent
            public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
                event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FLOWERING_OAK_LEAVES);
                event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), BlossomBlocks.FRUITING_OAK_LEAVES);
            }

            @SubscribeEvent
            public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
                event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.FLOWERING_OAK_LEAVES);
                event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), BlossomBlocks.FRUITING_OAK_LEAVES);
            }

        }

    }

}