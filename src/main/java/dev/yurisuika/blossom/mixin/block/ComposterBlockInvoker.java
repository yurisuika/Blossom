package dev.yurisuika.blossom.mixin.block;

import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ComposterBlock.class)
public interface ComposterBlockInvoker {

    @Invoker("registerCompostableItem")
    static void invokeRegisterCompstableItem(float levelIncreaseChance, ItemConvertible item) {
        throw new AssertionError();
    }

}