package dev.yurisuika.blossom.world.level.block;

import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.world.level.block.grower.AppleTreeGrower;
import dev.yurisuika.blossom.world.level.block.state.properties.BlossomBlockSetType;
import dev.yurisuika.blossom.world.level.block.state.properties.BlossomWoodType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BlossomBlocks {

    public static final Block APPLE_PLANKS = new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Block APPLE_SAPLING = new SaplingBlock(new AppleTreeGrower(), BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY));
    public static final Block APPLE_LOG = new RotatedPillarBlock(BlockBehaviour.Properties.of()
            .mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.WOOD : MapColor.PODZOL)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Block STRIPPED_APPLE_LOG = new RotatedPillarBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Block APPLE_WOOD = new RotatedPillarBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Block STRIPPED_APPLE_WOOD = new RotatedPillarBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Block APPLE_LEAVES = new LeavesBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .strength(0.2f)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(Blocks::ocelotOrParrot)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor(Blocks::never));
    public static final Block FLOWERING_APPLE_LEAVES = new FloweringLeavesBlock(0.1F, BlossomParticleTypes.FLOWERING_APPLE_LEAVES, BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(Blocks::ocelotOrParrot)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor(Blocks::never));
    public static final Block FRUITING_APPLE_LEAVES = new FruitingLeavesBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn(Blocks::ocelotOrParrot)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor(Blocks::never));
    public static final Block APPLE_SIGN = new BlossomStandingSignBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .ignitedByLava(),
            BlossomWoodType.APPLE);
    public static final Block APPLE_WALL_SIGN = new BlossomWallSignBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .ignitedByLava(),
            BlossomWoodType.APPLE);
    public static final Block APPLE_HANGING_SIGN = new BlossomCeilingHangingSignBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .ignitedByLava(),
            BlossomWoodType.APPLE);
    public static final Block APPLE_WALL_HANGING_SIGN = new BlossomWallHangingSignBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0F)
            .ignitedByLava(),
            BlossomWoodType.APPLE);
    public static final Block APPLE_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of()
            .mapColor(APPLE_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(0.5F)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY),
            BlossomBlockSetType.APPLE);
    public static final Block APPLE_TRAPDOOR = new TrapDoorBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(3.0F)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .ignitedByLava(),
            BlossomBlockSetType.APPLE);
    public static final Block POTTED_APPLE_SAPLING = new FlowerPotBlock(APPLE_SAPLING, BlockBehaviour.Properties.of()
            .instabreak()
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY));
    public static final Block APPLE_BUTTON = new ButtonBlock(BlockBehaviour.Properties.of()
            .noCollission()
            .strength(0.5F)
            .pushReaction(PushReaction.DESTROY),
            BlossomBlockSetType.APPLE, 30, true);
    public static final Block APPLE_STAIRS = new StairBlock(APPLE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Block APPLE_SLAB = new SlabBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Block APPLE_FENCE_GATE = new FenceGateBlock(BlockBehaviour.Properties.of()
            .mapColor(APPLE_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .ignitedByLava(),
            BlossomWoodType.APPLE);
    public static final Block APPLE_FENCE = new FenceBlock(BlockBehaviour.Properties.of()
            .mapColor(APPLE_PLANKS.defaultMapColor())
            .forceSolidOn()
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
            .ignitedByLava());
    public static final Block APPLE_DOOR = new DoorBlock(BlockBehaviour.Properties.of()
            .mapColor(APPLE_PLANKS.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .strength(3.0F)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY),
            BlossomBlockSetType.APPLE);

}