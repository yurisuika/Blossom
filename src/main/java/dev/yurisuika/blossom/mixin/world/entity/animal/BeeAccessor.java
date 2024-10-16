package dev.yurisuika.blossom.mixin.world.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Bee.class)
public interface BeeAccessor {

    @Accessor("savedFlowerPos")
    void setSavedFlowerPos(BlockPos savedFlowerPos);

    @Accessor
    Bee.BeePollinateGoal getBeePollinateGoal();

}