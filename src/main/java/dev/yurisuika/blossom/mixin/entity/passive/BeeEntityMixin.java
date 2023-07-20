package dev.yurisuika.blossom.mixin.entity.passive;

import dev.yurisuika.blossom.Blossom;
import dev.yurisuika.blossom.mixin.entity.EntityAccessor;
import dev.yurisuika.blossom.mixin.entity.ai.goal.GoalInvoker;
import dev.yurisuika.blossom.mixin.world.biome.BiomeAccessor;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.function.Predicate;

import static net.minecraft.block.LeavesBlock.*;

@Mixin(BeeEntity.class)
public class BeeEntityMixin {

    @Unique
    public World world;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void injectInit(EntityType<? extends BeeEntity> entityType, World world, CallbackInfo ci) {
        this.world = world;
    }

    @Inject(method = "isFlowers", at = @At("RETURN"), cancellable = true)
    private void injectIsFlowers(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || (world.canSetBlock(pos) && world.getBlockState(pos).isIn(Blossom.BLOSSOMS)));
    }

    @Mixin(targets = "net.minecraft.entity.passive.BeeEntity$GrowCropsGoal")
    public static class GrowCropsGoalMixin {

        @Unique
        public BeeEntity entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(BeeEntity entityType, CallbackInfo ci) {
            entity = entityType;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void injectTick(CallbackInfo ci) {
            if (((EntityAccessor)entity).getRandom().nextInt(((GoalInvoker)this).invokeGetTickCount(Blossom.config.rate)) == 0) {
                for(int i = 1; i <= 2; ++i) {
                    RegistryEntry<DimensionType> dimension = entity.getWorld().getDimensionEntry();
                    RegistryEntry<Biome> biome = entity.getWorld().getBiome(entity.getBlockPos());

//                    LoggerFactory.getLogger("Blossom").info("dim: " + dimension.getKey().get().getValue().toString());
//                    LoggerFactory.getLogger("Blossom").info("biome: " + biome.getKey().get().getValue().toString());

                    boolean bl = false;

                    if(Blossom.config.climate.whitelist.enabled) {
                        if(Arrays.asList(Blossom.config.climate.whitelist.dimensions).contains(dimension.getKey().get().getValue().toString())) {
                            if(Arrays.asList(Blossom.config.climate.whitelist.biomes).contains(biome.getKey().get().getValue().toString())) {
                                bl = true;
                            }
                        }
                    } else if(Blossom.config.climate.blacklist.enabled) {
                        if(!Arrays.asList(Blossom.config.climate.blacklist.dimensions).contains(dimension.getKey().get().getValue().toString())) {
                            if(!Arrays.asList(Blossom.config.climate.blacklist.biomes).contains(biome.getKey().get().getValue().toString())) {
                                bl = true;
                            }
                        }
                    } else {
                        bl = true;
                    }

                    if(bl) {
                        float temperature = biome.value().getTemperature();
                        float downfall = ((BiomeAccessor)(Object)biome.value()).getWeather().downfall();
                        Biome.Precipitation precipitation = biome.value().getPrecipitation(entity.getBlockPos());

                        if(Arrays.asList(Blossom.config.climate.precipitation).contains(precipitation.name()) && (temperature >= Blossom.config.climate.temperature.min && temperature <= Blossom.config.climate.temperature.max) && (downfall >= Blossom.config.climate.downfall.min && downfall <= Blossom.config.climate.downfall.max)) {


                            BlockPos blockPos = entity.getBlockPos().down(i);
                            BlockState blockState = entity.getWorld().getBlockState(blockPos);
                            if (blockState.isIn(BlockTags.BEE_GROWABLES) && (Arrays.stream(Direction.values()).anyMatch(direction -> !entity.getWorld().getBlockState(blockPos.offset(direction)).isSolid()))) {
                                if (blockState.getBlock() == Blocks.OAK_LEAVES) {
                                    entity.getWorld().syncWorldEvent(2005, blockPos, 0);
                                    entity.getWorld().setBlockState(blockPos, Blossom.FLOWERING_OAK_LEAVES.getDefaultState()
                                            .with(DISTANCE, blockState.get(DISTANCE))
                                            .with(PERSISTENT, blockState.get(PERSISTENT))
                                            .with(WATERLOGGED, blockState.get(WATERLOGGED))
                                    );
                                    ((BeeEntityInvoker)entity).invokeAddCropCounter();
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @Mixin(targets = "net.minecraft.entity.passive.BeeEntity$PollinateGoal")
    public abstract static class PollinateGoalMixin {

        @ModifyArg(method = "getFlower", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/BeeEntity$PollinateGoal;findFlower(Ljava/util/function/Predicate;D)Ljava/util/Optional;"), index = 0)
        private Predicate<BlockState> modifyGetFlower(Predicate<BlockState> predicate) {
            return predicate.or((state) -> (!state.contains(Properties.WATERLOGGED) || !state.get(Properties.WATERLOGGED)) && state.isIn(Blossom.BLOSSOMS));
        }

    }

}