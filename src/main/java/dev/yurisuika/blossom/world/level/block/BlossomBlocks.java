package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.mixin.world.level.block.BlocksInvoker;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlossomBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "blossom");

    public static final RegistryObject<Block> FRUITING_OAK_LEAVES = BLOCKS.register("fruiting_oak_leaves", () -> new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, BlockBehaviour.Properties.of()
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
    public static final RegistryObject<Block> FLOWERING_OAK_LEAVES = BLOCKS.register("flowering_oak_leaves", () -> new FloweringLeavesBlock(Blocks.OAK_LEAVES, FRUITING_OAK_LEAVES.get(), BlockBehaviour.Properties.of()
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