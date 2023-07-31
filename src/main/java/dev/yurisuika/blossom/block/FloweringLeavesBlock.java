package dev.yurisuika.blossom.block;

import dev.yurisuika.blossom.mixin.world.biome.BiomeAccessor;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;

import java.util.OptionalInt;
import java.util.concurrent.ThreadLocalRandom;

import static dev.yurisuika.blossom.Blossom.*;

public class FloweringLeavesBlock extends LeavesBlock implements Fertilizable {

    public final Block shearedBlock;
    public final Item shearedItem;

    public static final IntProperty DISTANCE =  Properties.DISTANCE_1_7;
    public static final BooleanProperty PERSISTENT = Properties.PERSISTENT;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final IntProperty AGE = Properties.AGE_7;

    public FloweringLeavesBlock(Block shearedBlock, Item shearedItem, Settings settings) {
        super(settings);
        this.shearedBlock = shearedBlock;
        this.shearedItem = shearedItem;
        this.setDefaultState(this.stateManager.getDefaultState().with(DISTANCE, 1).with(PERSISTENT, false).with(WATERLOGGED, false).with(AGE, 0));
    }

    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    public boolean hasRandomTicks(BlockState state) {
        return state.get(DISTANCE) == 7 || !this.isMature(state) || state.get(WATERLOGGED);
    }

    public IntProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 7;
    }

    public int getAge(BlockState state) {
        return state.get(this.getAgeProperty());
    }

    public BlockState withAge(BlockState state, int age) {
        return state.with(this.getAgeProperty(), age);
    }

    public boolean isMature(BlockState state) {
        return state.get(this.getAgeProperty()) >= this.getMaxAge();
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!(Boolean)state.get(PERSISTENT) && state.get(DISTANCE) == 7) {
            dropStacks(state, world, pos);
            world.removeBlock(pos, false);
        } else if (state.get(WATERLOGGED)) {
            world.setBlockState(pos, shearedBlock.getDefaultState()
                    .with(DISTANCE, state.get(DISTANCE))
                    .with(PERSISTENT, state.get(PERSISTENT))
                    .with(WATERLOGGED, state.get(WATERLOGGED))
            );
        } else if (!this.isMature(state) && world.getBaseLightLevel(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float temperature = world.getBiome(pos).value().getTemperature();
                float downfall = ((BiomeAccessor)(Object)world.getBiome(pos).value()).getWeather().downfall();
                temperature += 2;
                float f = (downfall * temperature) / 4;
                f = ((4 - 1) * f) + 1;
                Biome.Precipitation precipitation = world.getBiome(pos).value().getPrecipitation(pos);
                if (world.isRaining() && precipitation == Biome.Precipitation.RAIN) {
                    f = 5.0F;
                }
                if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                    world.setBlockState(pos, state.with(AGE, i + 1), 2);
                }
            }
        }
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, updateDistanceFromLogs(state, world, pos), 3);
    }

    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + this.getGrowthAmount(world);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        world.setBlockState(pos, state.with(AGE, i), 2);
    }

    public int getGrowthAmount(World world) {
        return MathHelper.nextInt(world.random, 2, 5);
    }

    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return 1;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        int i = getDistanceFromLog(neighborState) + 1;
        if (i != 1 || state.get(DISTANCE) != i) {
            world.scheduleBlockTick(pos, this, 1);
        }
        return state;
    }

    public static BlockState updateDistanceFromLogs(BlockState state, WorldAccess world, BlockPos pos) {
        int i = 7;
        Mutable mutable = new Mutable();
        for (Direction direction : Direction.values()) {
            mutable.set(pos, direction);
            i = Math.min(i, getDistanceFromLog(world.getBlockState(mutable)) + 1);
            if (i == 1) {
                break;
            }
        }
        return state.with(DISTANCE, i);
    }

    public static int getDistanceFromLog(BlockState state) {
        return getOptionalDistanceFromLog(state).orElse(7);
    }

    public static OptionalInt getOptionalDistanceFromLog(BlockState state) {
        if (state.isIn(BlockTags.LOGS)) {
            return OptionalInt.of(0);
        }
        if (state.contains(DISTANCE)) {
            return OptionalInt.of(state.get(DISTANCE));
        }
        return OptionalInt.empty();
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (world.hasRain(pos.up())) {
            if (random.nextInt(15) == 1) {
                BlockPos blockPos = pos.down();
                BlockState blockState = world.getBlockState(blockPos);
                if (!blockState.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, Direction.UP)) {
                    double d = (double)pos.getX() + random.nextDouble();
                    double e = (double)pos.getY() - 0.05D;
                    double f = (double)pos.getZ() + random.nextDouble();
                    world.addParticle(ParticleTypes.DRIPPING_WATER, d, e, f, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED, AGE);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateDistanceFromLogs(this.getDefaultState().with(PERSISTENT, true).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER).with(AGE, 0), ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !this.isMature(state);
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        this.applyGrowth(world, pos, state);
    }

    public static void dropFruit(World world, BlockPos pos, Item item, int bonus) {
        int count = 1;
        for(int i = 0; i < config.value.fruit.bonus + bonus; i++) {
            if (ThreadLocalRandom.current().nextFloat() <= config.value.fruit.chance) {
                count++;
            }
        }
        dropStack(world, pos, new ItemStack(item, count));
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (state.get(AGE) == 7) {
            Item item = itemStack.getItem();
            if (item instanceof ShearsItem) {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_CROP_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                dropFruit(world, pos, this.shearedItem, (itemStack.hasEnchantments() && EnchantmentHelper.get(itemStack).containsKey(Enchantments.FORTUNE)) ? EnchantmentHelper.getLevel(Enchantments.FORTUNE, itemStack) : 0);
                itemStack.damage(1, player, (entity) -> {
                    entity.sendToolBreakStatus(hand);
                });
                if (!world.isClient()) {
                    player.incrementStat(Stats.USED.getOrCreateStat(item));
                }
                world.emitGameEvent(player, GameEvent.SHEAR, pos);
                world.setBlockState(pos, this.shearedBlock.getDefaultState()
                        .with(DISTANCE, state.get(DISTANCE))
                        .with(PERSISTENT, state.get(PERSISTENT))
                        .with(WATERLOGGED, state.get(WATERLOGGED))
                );
            }
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }

}