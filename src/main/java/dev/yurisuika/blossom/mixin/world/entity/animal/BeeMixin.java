package dev.yurisuika.blossom.mixin.world.entity.animal;

import dev.yurisuika.blossom.mixin.world.entity.EntityMixin;
import dev.yurisuika.blossom.mixin.world.entity.MobAccessor;
import dev.yurisuika.blossom.tags.BlossomBlockTags;
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
        ((MobAccessor) bee).getGoalSelector().addGoal(4, getBlossomLeavesGoal());
        ((MobAccessor) bee).getGoalSelector().addGoal(4, getFruitLeavesGoal());
        ((MobAccessor) bee).getGoalSelector().addGoal(6, getGoToKnownLeavesGoal());
    }

    @Unique
    public BlossomLeavesGoal getBlossomLeavesGoal() {
        return blossomLeavesGoal;
    }

    @Unique
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
    private void injectInit(EntityType entityType, Level level, CallbackInfo ci) {
        remainingCooldownBeforeLocatingNewLeaves = Mth.nextInt(random, 20, 60);
    }


    @Inject(method = "getWalkTargetValue", at = @At("HEAD"), cancellable = true)
    private void injectGetWalkTargetValue(BlockPos pos, LevelReader level, CallbackInfoReturnable<Float> cir) {
        if (level.getBlockState(pos).getBlock() instanceof LeavesBlock) {
            cir.setReturnValue(2.0F);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void injectAddAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (hasSavedLeavesPos()) {
            compound.put("leaves_pos", NbtUtils.writeBlockPos(getSavedLeavesPos()));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void injectReadAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        setSavedLeavesPos(null);
        if (compound.contains("leaves_pos")) {
            setSavedLeavesPos(NbtUtils.readBlockPos(compound.getCompound("leaves_pos")));
        }
    }

    @Inject(method = "wantsToEnterHive", at = @At("HEAD"), cancellable = true)
    private void injectWantsToEnterHive(CallbackInfoReturnable<Boolean> cir) {
        if (getBlossomLeavesGoal().isBlossoming() || getFruitLeavesGoal().isBlossoming()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "aiStep", at = @At("RETURN"))
    private void injectAiStep(CallbackInfo ci) {
        if (!level.isClientSide()) {
            if (remainingCooldownBeforeLocatingNewLeaves > 0) {
                --remainingCooldownBeforeLocatingNewLeaves;
            }
        }
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private void injectHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!level.isClientSide()) {
            getBlossomLeavesGoal().stopBlossoming();
            getFruitLeavesGoal().stopBlossoming();
        }
    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$1")
    public abstract static class FlyingPathNavigationMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(Bee bee, Mob mob, Level level, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/navigation/FlyingPathNavigation;tick()V"), cancellable = true)
        private void injectTick(CallbackInfo ci) {
            if (((BeeInterface) entity).getBlossomLeavesGoal().isBlossoming() || ((BeeInterface) entity).getFruitLeavesGoal().isBlossoming()) {
                ci.cancel();
            }
        }

    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeeLookControl")
    public abstract static class BeeLookControlMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>", at = @At(value = "TAIL"))
        private void injectInit(Bee bee, Mob mob, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "resetXRotOnTick", at = @At("RETURN"), cancellable = true)
        private void injectResetXRotOnTick(CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(cir.getReturnValue() || !((BeeInterface) entity).getBlossomLeavesGoal().isBlossoming() || !((BeeInterface) entity).getFruitLeavesGoal().isBlossoming());
        }

    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal")
    public abstract static class BeeGrowCropGoalMixin {

        @Unique
        public Bee entity;

        @Inject(method = "<init>(Lnet/minecraft/world/entity/animal/Bee;)V", at = @At(value = "TAIL"))
        private void injectInit(Bee bee, CallbackInfo ci) {
            this.entity = bee;
        }

        @Inject(method = "tick", at = @At(value = "HEAD"))
        private void injectTick(CallbackInfo ci) {
            if (entity.getRandom().nextInt(30) != 0) {
                for (int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = entity.blockPosition().below(i);
                    BlockState blockState = entity.getCommandSenderWorld().getBlockState(blockPos);
                    if (blockState.is(BlockTags.BEE_GROWABLES)) {
                        if (blockState.getBlock() instanceof FruitingLeavesBlock) {
                            FruitingLeavesBlock fruitingLeavesBlock = (FruitingLeavesBlock) blockState.getBlock();
                            if (!fruitingLeavesBlock.isMaxAge(blockState)) {
                                IntegerProperty age = fruitingLeavesBlock.getAgeProperty();
                                entity.getCommandSenderWorld().levelEvent(2005, blockPos, 0);
                                entity.getCommandSenderWorld().setBlockAndUpdate(blockPos, blockState.setValue(age, blockState.getValue(age) + 1));
                                ((BeeInvoker) entity).invokeIncrementNumCropsGrownSincePollination();
                            }
                        }
                    }
                }
            }
        }

    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeePollinateGoal")
    public abstract static class BeePollinateGoalMixin {

        @Inject(method = "method_21819(Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
        private static void injectLambda(BlockState state, CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(cir.getReturnValue() || state.is(BlossomBlockTags.LEAVED_FLOWERS));
        }

    }

}