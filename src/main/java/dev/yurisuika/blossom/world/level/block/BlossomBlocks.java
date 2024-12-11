package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.mixin.world.level.block.BlocksInvoker;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class BlossomBlocks {

    public static final Block FRUITING_OAK_LEAVES = new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, FabricBlockSettings.of(Material.LEAVES)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(SoundType.GRASS)
            .nonOpaque()
            .allowsSpawning(BlocksInvoker::invokeOcelotOrParrot)
            .suffocates(BlocksInvoker::invokeNever)
            .blockVision(BlocksInvoker::invokeNever));
    public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(Blocks.OAK_LEAVES, FRUITING_OAK_LEAVES, FabricBlockSettings.of(Material.LEAVES)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(SoundType.GRASS)
            .nonOpaque()
            .allowsSpawning(BlocksInvoker::invokeOcelotOrParrot)
            .suffocates(BlocksInvoker::invokeNever)
            .blockVision(BlocksInvoker::invokeNever));

    public static void register() {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.tryParse("blossom:fruiting_oak_leaves"), FRUITING_OAK_LEAVES);
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.tryParse("blossom:flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
    }

}