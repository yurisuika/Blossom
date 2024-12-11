package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.mixin.world.level.block.BlocksInvoker;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BlossomBlocks {

    public static final Block FRUITING_OAK_LEAVES = new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, BlockBehaviour.Properties.of()
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
            .isRedstoneConductor(BlocksInvoker::invokeNever)
            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_oak_leaves"))));
    public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(Blocks.OAK_LEAVES, FRUITING_OAK_LEAVES, BlockBehaviour.Properties.of()
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
            .isRedstoneConductor(BlocksInvoker::invokeNever)
            .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_oak_leaves"))));

    public static void register() {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_oak_leaves"), FRUITING_OAK_LEAVES);
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
    }

}