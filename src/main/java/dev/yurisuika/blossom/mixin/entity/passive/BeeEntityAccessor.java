package dev.yurisuika.blossom.mixin.entity.passive;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeeEntity.class)
public interface BeeEntityAccessor {

    @Accessor("flowerPos")
    void setFlowerPos(BlockPos flowerPos);

}