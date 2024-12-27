package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.mixin.world.level.block.BlocksInvoker;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BlossomBlocks {

    public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(BlossomParticleTypes.FLOWERING_OAK_LEAVES, BlockBehaviour.Properties.of()
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
            .isRedstoneConductor(BlocksInvoker::invokeNever));
    public static final Block FRUITING_OAK_LEAVES = new FruitingLeavesBlock(BlockBehaviour.Properties.of()
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
            .isRedstoneConductor(BlocksInvoker::invokeNever));

}