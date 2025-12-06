package dev.yurisuika.blossom.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.yurisuika.blossom.registry.HarvestableFruitRegistry;
import dev.yurisuika.blossom.registry.ShearableLeavesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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
import java.util.concurrent.ThreadLocalRandom;

public class FruitingLeavesBlock extends LeavesBlock implements BonemealableBlock {

    public static final MapCodec<FruitingLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec()).apply(instance, FruitingLeavesBlock::new));
    public static final IntegerProperty DISTANCE =  BlockStateProperties.DISTANCE;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;
    public static final BooleanProperty RIPE = BooleanProperty.create("ripe");

    public MapCodec<? extends FruitingLeavesBlock> codec() {
        return CODEC;
    }

    public FruitingLeavesBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(DISTANCE, 1)
                .setValue(PERSISTENT, false)
                .setValue(WATERLOGGED, false)
                .setValue(AGE, 0)
                .setValue(MOISTURE, 0)
                .setValue(RIPE, false));
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
        return 7;
    }

    public int getAge(BlockState state) {
        return state.getValue(getAgeProperty());
    }

    public boolean isMaxAge(BlockState state) {
        return state.getValue(getAgeProperty()) >= getMaxAge();
    }

    public IntegerProperty getMoistureProperty() {
        return MOISTURE;
    }

    public int getMaxMoisture() {
        return 7;
    }

    public int getMoisture(BlockState state) {
        return state.getValue(getMoistureProperty());
    }

    public BooleanProperty getRipeProperty() {
        return RIPE;
    }

    public boolean getRipe(BlockState state) {
        return state.getValue(getRipeProperty());
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        int moisture = state.getValue(MOISTURE);
        if (isNearWater(level, pos) || level.isRainingAt(pos.above())) {
            if (moisture < getMaxMoisture()) {
                level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
            }
        } else if (moisture > 0) {
            level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
        }
        if (state.getValue(WATERLOGGED)) {
            if (ShearableLeavesRegistry.SHEARABLES.containsKey(state.getBlock())) {
                level.setBlockAndUpdate(pos, ShearableLeavesRegistry.SHEARABLES.get(state.getBlock()).defaultBlockState()
                        .setValue(DISTANCE, state.getValue(DISTANCE))
                        .setValue(PERSISTENT, state.getValue(PERSISTENT))
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
            }
        } else if (!isMaxAge(state) && level.getRawBrightness(pos, 0) >= 9) {
            if (random.nextInt((int) (25.0F / getMoisture(state) > 0 ? 4.0F : 1.0F) + 1) == 0) {
                level.setBlock(pos, state.setValue(AGE, getAge(state) + 1), 2);
            }
        } else if (isMaxAge(state) && getRipe(state) && random.nextInt(10) == 0) {
            if (ShearableLeavesRegistry.SHEARABLES.containsKey(state.getBlock())) {
                if (HarvestableFruitRegistry.HARVESTABLES.containsKey(state.getBlock())) {
                    HarvestableFruitRegistry.Entry entry = HarvestableFruitRegistry.HARVESTABLES.get(state.getBlock());
                    dropFruit(level, pos, entry.getFruit(), entry.getChance(), entry.getBonus(), 0);
                }
                level.setBlockAndUpdate(pos, ShearableLeavesRegistry.SHEARABLES.get(state.getBlock()).defaultBlockState()
                        .setValue(DISTANCE, state.getValue(DISTANCE))
                        .setValue(PERSISTENT, state.getValue(PERSISTENT))
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
            }
        } else if (isMaxAge(state) && !getRipe(state) && random.nextInt(10) == 0) {
            level.setBlock(pos, state.setValue(RIPE, true), 2);
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

    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 1;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        int i = getDistanceFromLog(neighborState) + 1;
        if (i != 1 || state.getValue(DISTANCE) != i) {
            level.scheduleTick(pos, this, 1);
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
    }

    public void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED, AGE, MOISTURE, RIPE);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return updateDistance(defaultBlockState()
                .setValue(PERSISTENT, true)
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
                .setValue(AGE, 0)
                .setValue(MOISTURE, 0)
                .setValue(RIPE, false), context.getLevel(), context.getClickedPos());
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

    public static boolean isNearWater(LevelReader level, BlockPos pos) {
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-20, -20, -20), pos.offset(20, 0, 20))) {
            if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    public static void dropFruit(Level level, BlockPos pos, Item item, float chance, int bonus, int fortune) {
        int count = 1;
        for (int i = 0; i < bonus + fortune; i++) {
            if (ThreadLocalRandom.current().nextFloat() <= chance) {
                count++;
            }
        }
        popResource(level, pos, new ItemStack(item, count));
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Item item = stack.getItem();
        if (item instanceof ShearsItem) {
            if (ShearableLeavesRegistry.SHEARABLES.containsKey(state.getBlock())) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROP_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                if (isMaxAge(state)) {
                    if (HarvestableFruitRegistry.HARVESTABLES.containsKey(state.getBlock())) {
                        HarvestableFruitRegistry.Entry entry = HarvestableFruitRegistry.HARVESTABLES.get(state.getBlock());
                        dropFruit(level, pos, entry.getFruit(), entry.getChance(), entry.getBonus(), (stack.isEnchanted() && EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().contains(Enchantments.FORTUNE)) ? EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FORTUNE, stack) : 0);
                    }
                }
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                if (!level.isClientSide()) {
                    player.awardStat(Stats.ITEM_USED.get(item));
                }
                level.gameEvent(player, GameEvent.SHEAR, pos);
                level.setBlockAndUpdate(pos, ShearableLeavesRegistry.SHEARABLES.get(state.getBlock()).defaultBlockState()
                        .setValue(DISTANCE, state.getValue(DISTANCE))
                        .setValue(PERSISTENT, state.getValue(PERSISTENT))
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED)));
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

}