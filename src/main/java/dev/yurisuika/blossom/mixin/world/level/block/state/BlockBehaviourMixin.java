package dev.yurisuika.blossom.mixin.world.level.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {

//    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
//    private void injectGetCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
//        if (context instanceof EntityCollisionContext) {
//            if (((EntityCollisionContext) context).getEntity() instanceof Bee) {
//                if (state.getBlock() instanceof LeavesBlock) {
//                    cir.setReturnValue(Shapes.empty());
//                }
//            }
//        }
//    }

//    @Inject(method = "isPathfindable", at = @At("HEAD"), cancellable = true)
//    private void injectCanPathfindThrough(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type, CallbackInfoReturnable<Boolean> cir) {
//        if (level.getBlockState(pos).getBlock() instanceof LeavesBlock) {
//            if (type == PathComputationType.AIR) {
//                cir.setReturnValue(true);
//            }
//        }
//    }

    @Mixin(BlockBehaviour.BlockStateBase.class)
    public static abstract class BlockStateBaseMixin {

        @Shadow
        public abstract Block getBlock();
        @Shadow
        protected BlockBehaviour.BlockStateBase.Cache cache;

        @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
        private void injectGetCollisionShape(BlockGetter level, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
            if (this.getBlock() instanceof LeavesBlock) {
                cir.setReturnValue(Shapes.empty());
            }
        }

        @Shadow public abstract boolean isCollisionShapeFullBlock(BlockGetter level, BlockPos pos);

        @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
        private void injectGetCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
            if (context instanceof EntityCollisionContext) {
                if (((EntityCollisionContext) context).getEntity() instanceof Bee) {
                    if (this.getBlock() instanceof LeavesBlock) {
                        cir.setReturnValue(Shapes.empty());
                    }
                }
            }
        }

        @Inject(method = "isPathfindable", at = @At("HEAD"), cancellable = true)
        private void injectIsPathfindable(BlockGetter level, BlockPos pos, PathComputationType type, CallbackInfoReturnable<Boolean> cir) {
            if (level.getBlockState(pos).getBlock() instanceof LeavesBlock) {
                if (type == PathComputationType.AIR) {
                    cir.setReturnValue(true);
                }
            }
        }

        @Inject(method = "getFaceOcclusionShape", at = @At("RETURN"), cancellable = true)
        private void injectGetFaceOcclusionShap(CallbackInfoReturnable<VoxelShape> cir) {
//            if (this.getBlock() instanceof LeavesBlock) {
//                cir.setReturnValue(Shapes.block());
//            }
        }

        @Inject(method = "getVisualShape", at = @At("RETURN"), cancellable = true)
        private void injectGetVisualShape(CallbackInfoReturnable<VoxelShape> cir) {
            if (this.getBlock() instanceof LeavesBlock) {
                cir.setReturnValue(Shapes.empty());
            }
        }

        @Inject(method = "isCollisionShapeFullBlock", at = @At("RETURN"), cancellable = true)
        private void injectIsCollisionShapeFullBlock(CallbackInfoReturnable<Boolean> cir) {
            if (cache != null && this.getBlock() instanceof LeavesBlock) {
                cir.setReturnValue(false);
            }
        }

        @Inject(method = "entityInside", at = @At("TAIL"))
        private void injectEntityInside(Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
            if (entity instanceof Bee || entity instanceof Parrot) {
                if (this.getBlock() instanceof LeavesBlock) {
                    entity.makeStuckInBlock(level.getBlockState(pos), new Vec3(0.800000011920929D, 0.75D, 0.800000011920929D));

                    entity.playSound(SoundEvents.GRASS_HIT, ThreadLocalRandom.current().nextFloat() * (float) entity.getDeltaMovement().length(), ThreadLocalRandom.current().nextFloat());

                    if (ThreadLocalRandom.current().nextInt(16) == 15) {
                        double d = pos.getX() + ThreadLocalRandom.current().nextDouble();
                        double e = pos.getY() - 0.05D;
                        double f = pos.getZ() + ThreadLocalRandom.current().nextDouble();
                        level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(pos).getBlock().defaultBlockState()), d, e, f, entity.getDeltaMovement().normalize().x * -4.0D, 1.5D, entity.getDeltaMovement().normalize().z * -4.0D);
                    }
                }
            }
        }

        @Inject(method = "isAir", at = @At("RETURN"), cancellable = true)
        private void injectIsAir(CallbackInfoReturnable<Boolean> cir) {
            if (this.getBlock() instanceof LeavesBlock) {
//                cir.setReturnValue(true);
            }
        }

        @Inject(method = "getShadeBrightness", at = @At("HEAD"),  cancellable = true)
        private void injectGetShadeBrightness(BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
            if (this.getBlock() instanceof LeavesBlock) {
                cir.setReturnValue(this.isCollisionShapeFullBlock(level, pos) ? 0.2F : 0.2F);
            }
        }

        @Inject(method = "isSuffocating", at = @At("RETURN"), cancellable = true)
        private void injectIsSuffocating(CallbackInfoReturnable<Boolean> cir) {
            if (this.getBlock() instanceof LeavesBlock) {
                cir.setReturnValue(false);
            }
        }

        @Inject(method = "isViewBlocking", at = @At("RETURN"), cancellable = true)
        private void injectIsViewBlocking(CallbackInfoReturnable<Boolean> cir) {
            if (this.getBlock() instanceof LeavesBlock) {
                cir.setReturnValue(false);
            }
        }

        @Inject(method = "canOcclude", at = @At("RETURN"), cancellable = true)
        private void injectCanOcclude(CallbackInfoReturnable<Boolean> cir) {
            if (this.getBlock() instanceof LeavesBlock) {
                cir.setReturnValue(false);
            }
        }

    }

}