package dev.yurisuika.blossom;

import dev.yurisuika.blossom.client.particle.BlossomParticle;
import dev.yurisuika.blossom.mixin.world.level.block.BlocksInvoker;
import dev.yurisuika.blossom.mixin.world.level.block.ComposterBlockInvoker;
import dev.yurisuika.blossom.mixin.world.level.block.FireBlockInvoker;
import dev.yurisuika.blossom.server.commands.BlossomCommand;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.util.config.Config;
import dev.yurisuika.blossom.world.entity.ai.goal.BlossomGoal;
import dev.yurisuika.blossom.world.entity.ai.goal.FruitGoal;
import dev.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Supplier;

@Mod("blossom")
public class Blossom {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "blossom");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "blossom");
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "blossom");

    public static final RegistryObject<Block> FRUITING_OAK_LEAVES = register("fruiting_oak_leaves", () -> new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(BlocksInvoker::invokeOcelotOrParrot)
            .isSuffocating(BlocksInvoker::invokeNever)
            .isViewBlocking(BlocksInvoker::invokeNever)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor(BlocksInvoker::invokeNever)), new Item.Properties());
    public static final RegistryObject<Block> FLOWERING_OAK_LEAVES = register("flowering_oak_leaves", () -> new FloweringLeavesBlock(Blocks.OAK_LEAVES, Blossom.FRUITING_OAK_LEAVES.get(), BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(BlocksInvoker::invokeOcelotOrParrot)
            .isSuffocating(BlocksInvoker::invokeNever)
            .isViewBlocking(BlocksInvoker::invokeNever)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor(BlocksInvoker::invokeNever)), new Item.Properties());

    public static final RegistryObject<SimpleParticleType> BLOSSOM = PARTICLES.register("blossom", () -> new SimpleParticleType(false));

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }

    @Mod.EventBusSubscriber(modid = "blossom")
    public static class CommonForgeEvents {

        @SubscribeEvent
        public static void registerCommandsEvents(RegisterCommandsEvent event) {
            BlossomCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        }

        @SubscribeEvent
        public static void entityJoinLevelEvents(EntityJoinLevelEvent event) {
            Entity entity = event.getEntity();
            if (entity instanceof Bee) {
                ((Bee) entity).getGoalSelector().addGoal(4, new BlossomGoal((Bee) entity));
                ((Bee) entity).getGoalSelector().addGoal(4, new FruitGoal((Bee) entity));
            }
        }

    }

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModBusEvents {

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            ComposterBlockInvoker.invokeAdd(0.3F, Blossom.FLOWERING_OAK_LEAVES.get());
            ComposterBlockInvoker.invokeAdd(0.3F, Blossom.FRUITING_OAK_LEAVES.get());

            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(Blossom.FLOWERING_OAK_LEAVES.get(), 30, 60);
            ((FireBlockInvoker) Blocks.FIRE).invokeSetFlammable(Blossom.FRUITING_OAK_LEAVES.get(), 30, 60);
        }

        @SubscribeEvent
        public static void registerParticleProvidersEvents(RegisterParticleProvidersEvent event) {
            Minecraft.getInstance().particleEngine.register(BLOSSOM.get(), BlossomParticle.Factory::new);
        }

    }

    @Mod.EventBusSubscriber(modid = "blossom", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SuppressWarnings("removal")
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(Blossom.FLOWERING_OAK_LEAVES.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(Blossom.FRUITING_OAK_LEAVES.get(), RenderType.cutout());

            ItemProperties.register(FLOWERING_OAK_LEAVES.get().asItem(), ResourceLocation.tryParse("age"), (stack, world, entity, seed) -> {
                BlockItemStateProperties blockItemStateProperties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
                Integer integer = blockItemStateProperties.get(FloweringLeavesBlock.AGE);
                if (Objects.nonNull(integer)) {
                    return integer.intValue() / 4.0F;
                }
                return 0.0F;
            });
            ItemProperties.register(FRUITING_OAK_LEAVES.get().asItem(), ResourceLocation.tryParse("age"), (stack, world, entity, seed) -> {
                BlockItemStateProperties blockItemStateProperties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
                Integer integer = blockItemStateProperties.get(FruitingLeavesBlock.AGE);
                if (Objects.nonNull(integer)) {
                    return integer.intValue() / 8.0F;
                }
                return 0.0F;
            });
        }

        @SubscribeEvent
        public static void registerBlockColorHandlerEvents(RegisterColorHandlersEvent.Block events) {
            events.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), Blossom.FLOWERING_OAK_LEAVES.get());
            events.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), Blossom.FRUITING_OAK_LEAVES.get());
        }

        @SubscribeEvent
        public static void registerItemColorHandlerEvents(RegisterColorHandlersEvent.Item events) {
            events.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), FLOWERING_OAK_LEAVES.get());
            events.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), FRUITING_OAK_LEAVES.get());
        }

        @SubscribeEvent
        public static void buildCreativeModeTabContentsEvents(BuildCreativeModeTabContentsEvent event) {
            if(event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
                event.accept(FLOWERING_OAK_LEAVES);
                event.getEntries().putAfter(Items.FLOWERING_AZALEA_LEAVES.getDefaultInstance(), FLOWERING_OAK_LEAVES.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(FRUITING_OAK_LEAVES);
                event.getEntries().putAfter(FLOWERING_OAK_LEAVES.get().asItem().getDefaultInstance(), FRUITING_OAK_LEAVES.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }

    }

    public Blossom(FMLJavaModLoadingContext context) {
        Config.loadConfig();
        Validate.checkBounds();

        MinecraftForge.EVENT_BUS.register(this);

        BLOCKS.register(context.getModEventBus());
        ITEMS.register(context.getModEventBus());
        PARTICLES.register(context.getModEventBus());
    }

}