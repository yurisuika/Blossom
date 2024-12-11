package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.mixin.world.level.block.BlocksInvoker;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlossomBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("blossom");

    public static final DeferredBlock<Block> FRUITING_OAK_LEAVES = BLOCKS.register("fruiting_oak_leaves", () -> new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, BlockBehaviour.Properties.of()
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
            .isRedstoneConductor(BlocksInvoker::invokeNever)));
    public static final DeferredBlock<Block> FLOWERING_OAK_LEAVES = BLOCKS.register("flowering_oak_leaves", () -> new FloweringLeavesBlock(Blocks.OAK_LEAVES, FRUITING_OAK_LEAVES.get(), BlockBehaviour.Properties.of()
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
            .isRedstoneConductor(BlocksInvoker::invokeNever)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}