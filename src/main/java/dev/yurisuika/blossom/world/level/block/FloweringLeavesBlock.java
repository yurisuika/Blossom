package dev.yurisuika.blossom.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class FloweringLeavesBlock extends Block implements BonemealableBlock {
    private final Block shearedBlock;

    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
    public static final IntegerProperty AGE  = BlockStateProperties.AGE_7;

    public static final int DECAY_DISTANCE = 7;
    private static final int TICK_DELAY = 1;
    public static final int MAX_AGE = 7;

    public FloweringLeavesBlock(Block shearedBlock, BlockBehaviour.Properties properties) {
        super(properties);
        this.shearedBlock = shearedBlock;
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, 1).setValue(PERSISTENT, Boolean.FALSE).setValue(AGE, 0));
    }

    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    public boolean isRandomlyTicking(BlockState state) {
        return (state.getValue(DISTANCE) == 7 && !(Boolean)state.getValue(PERSISTENT)) || (!this.isMaxAge(state) && state.getValue(DISTANCE) <= 7  && !(Boolean)state.getValue(PERSISTENT));
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 7;
    }

    public int getAge(BlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(BlockState state, int age) {
        return state.setValue(this.getAgeProperty(), age);
    }

    public boolean isMaxAge(BlockState state) {
        return state.getValue(this.getAgeProperty()) >= this.getMaxAge();
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (!(Boolean)state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7) {
            dropResources(state, level, pos);
            level.removeBlock(pos, false);
        }

        else if (!this.isMaxAge(state) && level.getRawBrightness(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                level.setBlock(pos, state.setValue(AGE, i + 1), 2);
            }
        }
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        level.setBlock(pos, updateDistance(state, level, pos), 3);
    }

    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + this.getBonemealAgeIncrease(level);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        level.setBlock(pos, state.setValue(AGE, i), 2);
    }

    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 2, 5);
    }

    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 1;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        int i = getDistanceAt(neighborState) + 1;
        if (i != 1 || state.getValue(DISTANCE) != i) {
            level.scheduleTick(pos, this, 1);
        }

        return state;
    }

    private static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int i = 7;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(pos, direction);
            i = Math.min(i, getDistanceAt(level.getBlockState(blockpos$mutableblockpos)) + 1);
            if (i == 1) {
                break;
            }
        }

        return state.setValue(DISTANCE, i);
    }

    private static int getDistanceAt(BlockState state) {
        if (state.is(BlockTags.LOGS)) {
            return 0;
        } else {
            return (state.getBlock() instanceof LeavesBlock || state.getBlock() instanceof FloweringLeavesBlock) ? state.getValue(DISTANCE) : 7;
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (level.isRainingAt(pos.above())) {
            if (random.nextInt(15) == 1) {
                BlockPos blockPos = pos.below();
                BlockState blockState = level.getBlockState(blockPos);
                if (!blockState.canOcclude() || !blockState.isFaceSturdy(level, blockPos, Direction.UP)) {
                    double d = (double)pos.getX() + random.nextDouble();
                    double e = (double)pos.getY() - 0.05D;
                    double f = (double)pos.getZ() + random.nextDouble();
                    level.addParticle(ParticleTypes.DRIPPING_WATER, d, e, f, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, AGE);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return updateDistance(this.defaultBlockState().setValue(PERSISTENT, Boolean.TRUE).setValue(AGE, 0), ctx.getLevel(), ctx.getClickedPos());
    }

    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        return !this.isMaxAge(state);
    }

    public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
        this.growCrops(level, pos, state);
    }

    public static void dropApple(Level level, BlockPos pos) {
        popResource(level, pos, new ItemStack(Items.APPLE, 3));
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        int i = state.getValue(AGE);
        boolean flag = false;
        if (i == 7) {
            Item item = itemStack.getItem();
            if (item instanceof ShearsItem) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.CROP_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                dropApple(level, pos);
                itemStack.hurtAndBreak(1, player, (playerx) -> {
                    playerx.broadcastBreakEvent(hand);
                });
                flag = true;
                level.gameEvent(player, GameEvent.SHEAR, pos);
                level.setBlockAndUpdate(pos, this.shearedBlock.defaultBlockState()
                        .setValue(DISTANCE, state.getValue(DISTANCE))
                        .setValue(PERSISTENT, state.getValue(PERSISTENT))
                );
            }

            if (!level.isClientSide() && flag) {
                player.awardStat(Stats.ITEM_USED.get(item));
            }
            return InteractionResult.SUCCESS;

        } else {
            return InteractionResult.PASS;
        }
    }

}