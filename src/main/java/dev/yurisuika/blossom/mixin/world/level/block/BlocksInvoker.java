package dev.yurisuika.blossom.mixin.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Blocks.class)
public interface BlocksInvoker {

    @Invoker("never")
    static boolean invokeNever(BlockState state, BlockGetter level, BlockPos pos) {
        throw new AssertionError();
    }

    @Invoker("ocelotOrParrot")
    static Boolean invokeOcelotOrParrot(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> type) {
        throw new AssertionError();
    }

}