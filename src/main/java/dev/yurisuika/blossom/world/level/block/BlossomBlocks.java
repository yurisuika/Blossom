package dev.yurisuika.blossom.world.level.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BlossomBlocks {

    public static final Block FRUITING_OAK_LEAVES = new FruitingLeavesBlock(Blocks.OAK_LEAVES, Items.APPLE, FabricBlockSettings.create()
            .mapColor(MapColor.PLANT)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(SoundType.GRASS)
            .nonOpaque()
            .allowsSpawning(Blocks::ocelotOrParrot)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
            .burnable()
            .pistonBehavior(PushReaction.DESTROY)
            .solidBlock(Blocks::never));
    public static final Block FLOWERING_OAK_LEAVES = new FloweringLeavesBlock(Blocks.OAK_LEAVES, FRUITING_OAK_LEAVES, FabricBlockSettings.create()
            .mapColor(MapColor.PLANT)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(SoundType.GRASS)
            .nonOpaque()
            .allowsSpawning(Blocks::ocelotOrParrot)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
            .burnable()
            .pistonBehavior(PushReaction.DESTROY)
            .solidBlock(Blocks::never));

    public static void register() {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath("blossom", "fruiting_oak_leaves"), FRUITING_OAK_LEAVES);
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath("blossom", "flowering_oak_leaves"), FLOWERING_OAK_LEAVES);
    }

}