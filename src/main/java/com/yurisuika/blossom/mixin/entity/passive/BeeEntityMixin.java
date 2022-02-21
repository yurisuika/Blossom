package com.yurisuika.blossom.mixin.entity.passive;

import com.yurisuika.blossom.Blossom;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeEntity.class)
public class BeeEntityMixin {

    private EntityType<? extends BeeEntity> entity;
    private World here;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void injectInit(EntityType<? extends BeeEntity> entityType, World world, CallbackInfo ci) {
        entity = entityType;
        here = world;
    }

    @Inject(method = "isFlowers", at = @At("RETURN"), cancellable = true)
    private void injectIsFlowers(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(BeeEntityInvoker.invokeInit(entity, here).world.canSetBlock(pos) && (BeeEntityInvoker.invokeInit(entity, here).world.getBlockState(pos).isIn(BlockTags.FLOWERS)) || BeeEntityInvoker.invokeInit(entity, here).world.getBlockState(pos).isOf(Blocks.OAK_LEAVES) || BeeEntityInvoker.invokeInit(entity, here).world.getBlockState(pos).isOf(Blossom.FLOWERING_OAK_LEAVES));
    }

}
