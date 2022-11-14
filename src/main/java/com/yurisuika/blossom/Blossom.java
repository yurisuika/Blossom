package com.yurisuika.blossom;

import com.yurisuika.blossom.block.FloweringLeavesBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Blossom implements ModInitializer, ClientModInitializer {

	public static final String MOD_ID = "blossom";

	public static final Logger LOGGER = LoggerFactory.getLogger("blossom");

	public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(Blocks.OAK_LEAVES, FabricBlockSettings.copy(Blocks.OAK_LEAVES));

	public static void registerFlammables() {
		FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();

		registry.add(Blossom.FLOWERING_OAK_LEAVES, 30, 60);
	}

	public static void registerCompostables() {
		CompostingChanceRegistry registry = CompostingChanceRegistry.INSTANCE;

		registry.add(Blossom.FLOWERING_OAK_LEAVES, 0.30F);
	}

	@Environment(EnvType.CLIENT)
	public static void registerRenderLayers() {
		BlockRenderLayerMap.INSTANCE.putBlock(Blossom.FLOWERING_OAK_LEAVES, RenderLayer.getCutout());
	}

	@Environment(EnvType.CLIENT)
	public static void registerColorProviders() {
		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex) -> {
					if (world != null && pos != null) {
						return BiomeColors.getFoliageColor(world, pos);
					}
					return FoliageColors.getColor(0.5, 1.0);
				},
				Blossom.FLOWERING_OAK_LEAVES
		);
		ColorProviderRegistry.ITEM.register(
				(stack, tintIndex) -> {
					if (tintIndex > 0) return -1;
					BlockColors colors = MinecraftClient.getInstance().getBlockColors();
					return colors.getColor(((BlockItem) stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex);
				},
				Blossom.FLOWERING_OAK_LEAVES
		);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Loading Blossom!");

		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "flowering_oak_leaves"), new BlockItem(FLOWERING_OAK_LEAVES, new Item.Settings()));

		Blossom.registerFlammables();
		Blossom.registerCompostables();
	}

	@Override
	public void onInitializeClient() {
		Blossom.registerRenderLayers();
		Blossom.registerColorProviders();
	}

}