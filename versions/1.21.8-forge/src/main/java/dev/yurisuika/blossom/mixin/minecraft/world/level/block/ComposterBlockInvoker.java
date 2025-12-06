package dev.yurisuika.blossom.mixin.minecraft.world.level.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ComposterBlock.class)
public interface ComposterBlockInvoker {

    @Invoker("add")
    static void invokeAdd(float levelIncreaseChance, ItemLike item) {
        throw new AssertionError();
    }

}