package dev.yurisuika.blossom.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.yurisuika.blossom.core.particles.BlossomParticleTypes;
import dev.yurisuika.blossom.mixin.world.level.biome.BiomeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.OptionalInt;

public class FloweringLeavesBlock extends LeavesBlock implements BonemealableBlock {

    public final Block shearedBlock;
    public final Block pollinatedBlock;
    public static final MapCodec<FloweringLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("shearedBlock").forGetter(arg -> arg.shearedBlock), BuiltInRegistries.BLOCK.byNameCodec().fieldOf("pollinatedBlock").forGetter(arg -> arg.pollinatedBlock), propertiesCodec()).apply(instance, FloweringLeavesBlock::new));
    public static final IntegerProperty DISTANCE =  BlockStateProperties.DISTANCE;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final IntegerProperty RIPENESS = IntegerProperty.create("ripeness", 0, 7);

    public MapCodec<? extends FloweringLeavesBlock> codec() {
        return CODEC;
    }

    public FloweringLeavesBlock(Block shearedBlock, Block pollinatedBlock, Properties properties) {
        super(properties);
        this.shearedBlock = shearedBlock;
        this.pollinatedBlock = pollinatedBlock;
        registerDefaultState(stateDefinition.any().setValue(DISTANCE, 1).setValue(PERSISTENT, false).setValue(WATERLOGGED, false).setValue(AGE, 0).setValue(RIPENESS, 0));
    }

    public Block getShearedBlock() {
        return shearedBlock;
    }

    public Block getPollinatedBlock() {
        return pollinatedBlock;
    }

    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 3;
    }

    public int getAge(BlockState state) {
        return state.getValue(getAgeProperty());
    }

    public boolean isMaxAge(BlockState state) {
        return state.getValue(getAgeProperty()) >= getMaxAge();
    }

    public IntegerProperty getRipenessProperty() {
        return RIPENESS;
    }

    public int getMaxRipeness() {
        return 7;
    }

    public int getRipeness(BlockState state) {
        return state.getValue(getRipenessProperty());
    }

    public boolean isMaxRipeness(BlockState state) {
        return state.getValue(getRipenessProperty()) >= getMaxRipeness();
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7) {
            dropResources(state, level, pos);
            level.removeBlock(pos, false);
        } else if (state.getValue(WATERLOGGED)) {
            level.setBlockAndUpdate(pos, getShearedBlock().defaultBlockState()
                    .setValue(DISTANCE, state.getValue(DISTANCE))
                    .setValue(PERSISTENT, state.getValue(PERSISTENT))
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
            );
        } else if (!isMaxAge(state) && level.getRawBrightness(pos, 0) >= 9) {
            int i = getAge(state);
            if (i < getMaxAge()) {
                float temperature = level.getBiome(pos).value().getBaseTemperature();
                float downfall = ((BiomeAccessor) (Object) level.getBiome(pos).value()).getClimateSettings().downfall();
                temperature += 2;
                float f = (downfall * temperature) / 4;
                f = ((4 - 1) * f) + 1;
                Precipitation precipitation = level.getBiome(pos).value().getPrecipitationAt(pos, level.getSeaLevel());
                if (level.isRaining() && precipitation == Precipitation.RAIN) {
                    f = 5.0F;
                }
                if (random.nextInt((int) (25.0F / f) + 1) == 0) {
                    level.setBlock(pos, defaultBlockState().setValue(AGE, i + 1)
                            .setValue(DISTANCE, state.getValue(DISTANCE))
                            .setValue(PERSISTENT, state.getValue(PERSISTENT))
                            .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                            .setValue(RIPENESS, state.getValue(RIPENESS)), 2);
                }
            }
        } else if (isMaxAge(state)) {
            int i = getRipeness(state);
            if (i < getMaxRipeness()) {
                if (random.nextInt(25 + 1) == 0) {
                    level.setBlock(pos, state.setValue(RIPENESS, i + 1), 2);
                }
            }
        }
        if (isMaxRipeness(state)) {
            level.setBlockAndUpdate(pos, getShearedBlock().defaultBlockState()
                    .setValue(DISTANCE, state.getValue(DISTANCE))
                    .setValue(PERSISTENT, state.getValue(PERSISTENT))
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
            );
        }
        if (!isMaxAge(state) && state.getValue(RIPENESS) > 0) {
            level.setBlock(pos, state.setValue(RIPENESS, 0), 2);
        }
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, updateDistance(state, level, pos), 3);
    }

    public void applyGrowth(Level level, BlockPos pos, BlockState state) {
        int i = getAge(state) + getGrowthAmount(level);
        int j = getMaxAge();
        if (i > j) {
            i = j;
        }
        level.setBlock(pos, state.setValue(AGE, i), 2);
    }

    public int getGrowthAmount(Level level) {
        return Mth.nextInt(level.getRandom(), 2, 5);
    }

    public int getLightBlock(BlockState state) {
        return 1;
    }

    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tick, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tick.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        int i = getDistanceFromLog(neighborState) + 1;
        if (i != 1 || state.getValue(DISTANCE) != i) {
            tick.scheduleTick(pos, this, 1);
        }
        return state;
    }

    public static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int i = 7;
        MutableBlockPos mutableBlockPos = new MutableBlockPos();
        for (Direction direction : Direction.values()) {
            mutableBlockPos.setWithOffset(pos, direction);
            i = Math.min(i, getDistanceFromLog(level.getBlockState(mutableBlockPos)) + 1);
            if (i == 1) {
                break;
            }
        }
        return state.setValue(DISTANCE, i);
    }

    public static int getDistanceFromLog(BlockState state) {
        return getOptionalDistanceFromLog(state).orElse(7);
    }

    public static OptionalInt getOptionalDistanceFromLog(BlockState state) {
        if (state.is(BlockTags.LOGS)) {
            return OptionalInt.of(0);
        }
        if (state.hasProperty(LeavesBlock.DISTANCE)) {
            return OptionalInt.of(state.getValue(DISTANCE));
        }
        return OptionalInt.empty();
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(10 * (4 - getAge(state))) != 0) {
            return;
        }
        if (FloweringLeavesBlock.isFaceFull(level.getBlockState(pos.below()).getCollisionShape(level, pos.below()), Direction.UP)) {
            return;
        }
        ParticleUtils.spawnParticleBelow(level, pos, random, BlossomParticleTypes.FLOWERING_OAK_LEAVES.get());
    }

    public void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED, AGE, RIPENESS);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return updateDistance(defaultBlockState().setValue(PERSISTENT, true).setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER).setValue(AGE, 0).setValue(RIPENESS, 0), context.getLevel(), context.getClickedPos());
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !isMaxAge(state);
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        applyGrowth(level, pos, state);
    }

    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Item item = stack.getItem();
        if (item instanceof ShearsItem) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROP_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            if (!level.isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(item));
            }
            level.gameEvent(player, GameEvent.SHEAR, pos);
            level.setBlockAndUpdate(pos, getShearedBlock().defaultBlockState()
                    .setValue(DISTANCE, state.getValue(DISTANCE))
                    .setValue(PERSISTENT, state.getValue(PERSISTENT))
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
            );
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}