package dev.yurisuika.blossom.mixin.entity.passive;

import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeeEntity.class)
public interface BeeEntityInvoker {

    @Intrinsic
    @Invoker("addCropCounter")
    void invokeAddCropCounter();

    @Invoker("isHiveValid")
    boolean invokeIsHiveValid();

    @Invoker("getCropsGrownSincePollination")
    int invokeGetCropsGrownSincePollination();

}