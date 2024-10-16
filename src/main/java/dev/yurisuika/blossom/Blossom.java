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
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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

    public static final RegistryObject<Block> FRUITING_OAK_LEAVES = register("fruiting_oak_leaves", () -> new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, BlockBehaviour.Properties.of(Material.LEAVES)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(BlocksInvoker::invokeOcelotOrParrot)
            .isSuffocating(BlocksInvoker::invokeNever)
            .isViewBlocking(BlocksInvoker::invokeNever)), new Item.Properties());
    public static final RegistryObject<Block> FLOWERING_OAK_LEAVES = register("flowering_oak_leaves", () -> new FloweringLeavesBlock(Blocks.OAK_LEAVES, Blossom.FRUITING_OAK_LEAVES.get(), BlockBehaviour.Properties.of(Material.LEAVES)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(BlocksInvoker::invokeOcelotOrParrot)
            .isSuffocating(BlocksInvoker::invokeNever)
            .isViewBlocking(BlocksInvoker::invokeNever)), new Item.Properties());

    public static final RegistryObject<SimpleParticleType> BLOSSOM = PARTICLES.register("blossom", () -> new SimpleParticleType(false));

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), properties.tab(CreativeModeTab.TAB_DECORATIONS)));
        return block;
    }

    @Mod.EventBusSubscriber(modid = "blossom")
    public static class CommonForgeEvents {

        @SubscribeEvent
        public static void registerCommandsEvents(RegisterCommandsEvent event) {
            BlossomCommand.register(event.getDispatcher(), event.getEnvironment());
        }

        @SubscribeEvent
        public static void entityJoinWorldEvents(EntityJoinWorldEvent event) {
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
        public static void particleFactoryRegisterEvents(ParticleFactoryRegisterEvent event) {
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

            ItemProperties.register(FLOWERING_OAK_LEAVES.get().asItem(), new ResourceLocation("age"), (stack, world, entity, seed) -> {
                CompoundTag tag = stack.getTagElement("BlockStateTag");
                try {
                    if (Objects.nonNull(tag) && Objects.nonNull(tag.get(FloweringLeavesBlock.AGE.getName()))) {
                        return Integer.parseInt(tag.get(FloweringLeavesBlock.AGE.getName()).getAsString()) / 4.0F;
                    }
                } catch (NumberFormatException ignored) {}
                return 0.0F;
            });
            ItemProperties.register(FRUITING_OAK_LEAVES.get().asItem(), new ResourceLocation("age"), (stack, world, entity, seed) -> {
                CompoundTag tag = stack.getTagElement("BlockStateTag");
                try {
                    if (Objects.nonNull(tag) && Objects.nonNull(tag.get(FloweringLeavesBlock.AGE.getName()))) {
                        return Integer.parseInt(tag.get(FloweringLeavesBlock.AGE.getName()).getAsString()) / 8.0F;
                    }
                } catch (NumberFormatException ignored) {}
                return 0.0F;
            });
        }

        @SubscribeEvent
        public static void blockColorHandlerEvents(ColorHandlerEvent.Block event) {
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), Blossom.FLOWERING_OAK_LEAVES.get());
            event.getBlockColors().register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.get(0.5F, 1.0F), Blossom.FRUITING_OAK_LEAVES.get());
        }

        @SubscribeEvent
        public static void itemColorHandlerEvents(ColorHandlerEvent.Item event) {
            event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), Blossom.FLOWERING_OAK_LEAVES.get());
            event.getItemColors().register((stack, tintIndex) -> tintIndex > 0 ? -1 : Minecraft.getInstance().getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), FRUITING_OAK_LEAVES.get());
        }

    }

    public Blossom() {
        Config.loadConfig();
        Validate.checkBounds();

        MinecraftForge.EVENT_BUS.register(this);

        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}