package dev.yurisuika.blossom;

import com.mojang.logging.LogUtils;
import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import dev.yurisuika.blossom.mixin.block.ComposterBlockInvoker;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod("blossom")
public class Blossom {

	public static final String MOD_ID = "blossom";

	private static final Logger LOGGER = LogUtils.getLogger();

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Blossom.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Blossom.MOD_ID);

	public static final RegistryObject<Block> FLOWERING_OAK_LEAVES = register("flowering_oak_leaves", () -> new FloweringLeavesBlock(Blocks.OAK_LEAVES, AbstractBlock.Settings.copy(Blocks.OAK_LEAVES).requiresTool()) {
		@Override
		public boolean isFlammable(BlockState state, BlockView world, BlockPos pos, Direction direction) {
			return true;
		}
		@Override
		public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction direction) {
			return 30;
		}
		@Override
		public int getFireSpreadSpeed(BlockState state, BlockView world, BlockPos pos, Direction direction) {
			return 60;
		}
	}, new Item.Settings());

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Settings settings) {
		RegistryObject<T> block = BLOCKS.register(name, supplier);
		ITEMS.register(name, () -> new BlockItem(block.get(), settings));
		return block;
	}

	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModBusEvents {
		@SubscribeEvent
		public static void colorBlocks(final RegisterColorHandlersEvent.Block color) {
			color.getBlockColors().registerColorProvider((state, world, pos, tintIndex) -> {
						if (world != null && pos != null) {
							return BiomeColors.getFoliageColor(world, pos);
						}
						return FoliageColors.getColor(0.5, 1.0);
					}, Blossom.FLOWERING_OAK_LEAVES.get()
			);
		}
		@SubscribeEvent
		public static void colorItems(final RegisterColorHandlersEvent.Item color) {
			color.getItemColors().register((stack, tintIndex) -> {
						if (tintIndex > 0) return -1;
						BlockColors colors = MinecraftClient.getInstance().getBlockColors();
						return colors.getColor(((BlockItem) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex);
					}, Blossom.FLOWERING_OAK_LEAVES.get()
			);
		}
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			RenderLayers.setRenderLayer(Blossom.FLOWERING_OAK_LEAVES.get(), RenderLayer.getCutout());
		}
	}

	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class CommonModBusEvents {
		@SubscribeEvent
		public static void commonSetup(FMLCommonSetupEvent event) {
			ComposterBlockInvoker.invokeRegisterCompstableItem(0.3F, Blossom.FLOWERING_OAK_LEAVES.get());
		}
	}

	public Blossom() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);

		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public void setup(final FMLClientSetupEvent event) {
		LOGGER.info("Loading Blossom!");
	}

}