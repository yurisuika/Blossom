package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.world.level.block.grower.AppleTreeGrower;
import dev.yurisuika.blossom.world.level.block.state.properties.BlossomWoodType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class BlossomBlocks {

    public static final Block APPLE_PLANKS = new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    public static final Block APPLE_SAPLING = new SaplingBlock(new AppleTreeGrower(), BlockBehaviour.Properties.of(Material.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.GRASS));
    public static final Block APPLE_LOG = new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MaterialColor.WOOD : MaterialColor.PODZOL)
            .strength(2.0F)
            .sound(SoundType.WOOD));
    public static final Block STRIPPED_APPLE_LOG = new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, state -> MaterialColor.WOOD)
            .strength(2.0F)
            .sound(SoundType.WOOD));
    public static final Block APPLE_WOOD = new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
            .strength(2.0F)
            .sound(SoundType.WOOD));
    public static final Block STRIPPED_APPLE_WOOD = new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
            .strength(2.0F)
            .sound(SoundType.WOOD));
    public static final Block APPLE_LEAVES = new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(Blocks::ocelotOrParrot)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never));
    public static final Block FLOWERING_APPLE_LEAVES = new FloweringLeavesBlock(0.1F, BlossomParticleTypes.FLOWERING_APPLE_LEAVES, BlockBehaviour.Properties.of(Material.LEAVES)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(Blocks::ocelotOrParrot)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never));
    public static final Block FRUITING_APPLE_LEAVES = new FruitingLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(Blocks::ocelotOrParrot)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never));
    public static final Block APPLE_SIGN = new BlossomStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD)
            .noCollission()
            .strength(1.0F)
            .sound(SoundType.WOOD),
            BlossomWoodType.APPLE);
    public static final Block APPLE_WALL_SIGN = new BlossomWallSignBlock(BlockBehaviour.Properties.of(Material.WOOD)
            .noCollission()
            .strength(1.0F)
            .sound(SoundType.WOOD)
            .dropsLike(APPLE_SIGN),
            BlossomWoodType.APPLE);
    public static final Block APPLE_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, APPLE_PLANKS.defaultMaterialColor())
            .noCollission()
            .strength(0.5F)
            .sound(SoundType.WOOD));
    public static final Block APPLE_TRAPDOOR = new TrapDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
            .strength(3.0F)
            .sound(SoundType.WOOD)
            .noOcclusion()
            .isValidSpawn(Blocks::never));
    public static final Block POTTED_APPLE_SAPLING = new FlowerPotBlock(APPLE_SAPLING, BlockBehaviour.Properties.of(Material.DECORATION)
            .instabreak()
            .noOcclusion());
    public static final Block APPLE_BUTTON = new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION)
            .noCollission()
            .strength(0.5F)
            .sound(SoundType.WOOD));
    public static final Block APPLE_STAIRS = new StairBlock(APPLE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    public static final Block APPLE_SLAB = new SlabBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    public static final Block APPLE_FENCE_GATE = new FenceGateBlock(BlockBehaviour.Properties.of(Material.WOOD, APPLE_PLANKS.defaultMaterialColor())
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    public static final Block APPLE_FENCE = new FenceBlock(BlockBehaviour.Properties.of(Material.WOOD, APPLE_PLANKS.defaultMaterialColor())
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    public static final Block APPLE_DOOR = new DoorBlock(BlockBehaviour.Properties.of(Material.WOOD, APPLE_PLANKS.defaultMaterialColor())
            .strength(3.0F)
            .sound(SoundType.WOOD)
            .noOcclusion());

}