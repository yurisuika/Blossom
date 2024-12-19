package dev.yurisuika.blossom.mixin.world.entity.animal;

import dev.yurisuika.blossom.mixin.world.entity.EntityMixin;
import dev.yurisuika.blossom.mixin.world.entity.ai.goal.GoalInvoker;
import dev.yurisuika.blossom.world.entity.ai.goal.BlossomLeavesGoal;
import dev.yurisuika.blossom.world.entity.ai.goal.FruitLeavesGoal;
import dev.yurisuika.blossom.world.entity.ai.goal.GoToKnownLeavesGoal;
import dev.yurisuika.blossom.world.entity.animal.BeeInterface;
import dev.yurisuika.blossom.world.level.block.FruitingLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Bee.class)
public abstract class BeeMixin extends EntityMixin implements BeeInterface {

    @Unique
    public BlossomLeavesGoal blossomLeavesGoal;
    @Unique
    public FruitLeavesGoal fruitLeavesGoal;
    @Unique
    public GoToKnownLeavesGoal goToKnownLeavesGoal;
    @Unique
    public BlockPos savedLeavesPos;
    @Unique
    public int remainingCooldownBeforeLocatingNewLeaves;

    @Unique
    public void registerGoals(Bee bee) {
        blossomLeavesGoal = new BlossomLeavesGoal(bee);
        fruitLeavesGoal = new FruitLeavesGoal(bee);
        goToKnownLeavesGoal = new GoToKnownLeavesGoal(bee);
        bee.getGoalSelector().addGoal(4, getBlossomLeavesGoal());
        bee.getGoalSelector().addGoal(4, getFruitLeavesGoal());
        bee.getGoalSelector().addGoal(6, getGoToKnownLeavesGoal());
    }

    @Unique
    public BlossomLeavesGoal getBlossomLeavesGoal() {
        return blossomLeavesGoal;
    }

    public  FruitLeavesGoal getFruitLeavesGoal() {
        return fruitLeavesGoal;
    }

    @Unique
    public GoToKnownLeavesGoal getGoToKnownLeavesGoal() {
        return goToKnownLeavesGoal;
    }

    @Unique
    public BlockPos getSavedLeavesPos() {
        return savedLeavesPos;
    }

    @Unique
    public boolean hasSavedLeavesPos() {
        return savedLeavesPos != null;
    }

    @Unique
    public void setSavedLeavesPos(BlockPos savedLeavesPos) {
        this.savedLeavesPos = savedLeavesPos;
    }

    @Unique
    public void dropLeaves() {
        setSavedLeavesPos(null);
        remainingCooldownBeforeLocatingNewLeaves = Mth.nextInt(random, 20, 60);
    }

    @Unique
    public boolean areLeavesValid(BlockPos pos) {
        return level.isLoaded(pos) && level.getBlockState(getSavedLeavesPos()).is(BlockTags.LEAVES);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void assignEntity(EntityType entityType, Level level, CallbackInfo ci) {
        remainingCooldownBeforeLocatingNewLeaves = Mth.nextInt(random, 20, 60);
    }


    @Inject(method = "getWalkTargetValue", at = @At("HEAD"), cancellable = true)
    private void allowMovementThroughLeaves(BlockPos pos, LevelReader level, CallbackInfoReturnable<Float> cir) {
        if (level.getBlockState(pos).getBlock() instanceof LeavesBlock) {
            cir.setReturnValue(2.0F);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void saveLeavesPos(CompoundTag compound, CallbackInfo ci) {
        if (hasSavedLeavesPos()) {
            compound.put("leaves_pos", NbtUtils.writeBlockPos(getSavedLeavesPos()));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readLeavesPos(CompoundTag compound, CallbackInfo ci) {
        setSavedLeavesPos(null);
        if (compound.contains("leaves_pos")) {
            setSavedLeavesPos(NbtUtils.readBlockPos(compound.getCompound("leaves_pos")));
        }
    }

    @Inject(method = "getTravellingTicks", at = @At("RETURN"), cancellable = true)
    private void adjustTravellingTicks(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.max(cir.getReturnValue(), getGoToKnownLeavesGoal().getTravellingTicks()));
    }

    @Inject(method = "wantsToEnterHive", at = @At("HEAD"), cancellable = true)
    private void disallowEnteringHiveIfFruiting(CallbackInfoReturnable<Boolean> cir) {
        if (getFruitLeavesGoal().isFruiting()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "aiStep", at = @At("RETURN"))
    private void reduceCooldown(CallbackInfo ci) {
        if (!level.isClientSide()) {
            if (remainingCooldownBeforeLocatingNewLeaves > 0) {
                --remainingCooldownBeforeLocatingNewLeaves;
            }
        }
    }


    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private void stopFruitingUponHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!level.isClientSide()) {
            getFruitLeavesGoal().stopFruiting();
        }
    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$1")
    public abstract static class FlyingPathNavigationMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void assignEntity(Bee bee, Mob mob, Level level, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/navigation/FlyingPathNavigation;tick()V"), cancellable = true)
        private void cancelTickIfFruiting(CallbackInfo ci) {
            if (((BeeInterface) entity).getFruitLeavesGoal().isFruiting()) {
                ci.cancel();
            }
        }

    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeeLookControl")
    public abstract static class BeeLookControlMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void assignEntity(Bee bee, Mob mob, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "resetXRotOnTick", at = @At("RETURN"), cancellable = true)
        private void resetLookIfFruiting(CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(cir.getReturnValue() || !((BeeInterface) entity).getFruitLeavesGoal().isFruiting());
        }

    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal")
    public abstract static class BeeGrowCropGoalMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void assignEntity(Bee bee, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void growFruitingLeaves(CallbackInfo ci) {
            if (entity.getRandom().nextInt(((GoalInvoker) this).invokeAdjustedTickDelay(30)) != 0) {
                for (int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.blockPosition().below(i);
                    BlockState blockState = entity.getLevel().getBlockState(blockPos);
                    if (blockState.is(BlockTags.BEE_GROWABLES)) {
                        if (blockState.getBlock() instanceof FruitingLeavesBlock fruitingLeavesBlock) {
                            if (!fruitingLeavesBlock.isMaxAge(blockState)) {
                                IntegerProperty age = fruitingLeavesBlock.getAgeProperty();
                                entity.getLevel().levelEvent(2005, blockPos, 0);
                                entity.getLevel().setBlockAndUpdate(blockPos, blockState.setValue(age, blockState.getValue(age) + 1));
                                ((BeeInvoker) entity).invokeIncrementNumCropsGrownSincePollination();
                            }
                        }
                    }
                }
            }
        }

    }

}