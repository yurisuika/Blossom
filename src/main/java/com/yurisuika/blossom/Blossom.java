package com.yurisuika.blossom;

import com.mojang.logging.LogUtils;
import com.yurisuika.blossom.world.level.block.FloweringLeavesBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
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

	public static final RegistryObject<Block> FLOWERING_OAK_LEAVES = register("flowering_oak_leaves", () -> new FloweringLeavesBlock(Blocks.OAK_LEAVES, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).requiresCorrectToolForDrops()) {
		@Override
		public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
			return true;
		}

		@Override
		public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
			return 30;
		}

		@Override
		public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
			return 60;
		}
	}, new Item.Properties());

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties) {
		RegistryObject<T> block = BLOCKS.register(name, supplier);
		ITEMS.register(name, () -> new BlockItem(block.get(), properties));
		return block;
	}

	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModBusEvents {
		@SubscribeEvent
		public static void colorBlocks(final ColorHandlerEvent.Block color) {
			color.getBlockColors().register((state, level, pos, tintIndex) -> {
						if (level != null && pos != null) {
							return BiomeColors.getAverageFoliageColor(level, pos);
						}
						return FoliageColor.get(0.5, 1.0);
					}, Blossom.FLOWERING_OAK_LEAVES.get()
			);
		}
		@SubscribeEvent
		public static void colorItems(final ColorHandlerEvent.Item color) {
			color.getItemColors().register((stack, tintIndex) -> {
						if (tintIndex > 0) return -1;
						BlockColors colors = Minecraft.getInstance().getBlockColors();
						return colors.getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex);
					}, Blossom.FLOWERING_OAK_LEAVES.get()
			);
		}
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			ItemBlockRenderTypes.setRenderLayer(Blossom.FLOWERING_OAK_LEAVES.get(), RenderType.cutout());
		}
	}

	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class CommonModBusEvents {
		@SubscribeEvent
		public static void commonSetup(FMLCommonSetupEvent event) {
			ComposterBlock.add(0.3F, Blossom.FLOWERING_OAK_LEAVES.get());
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