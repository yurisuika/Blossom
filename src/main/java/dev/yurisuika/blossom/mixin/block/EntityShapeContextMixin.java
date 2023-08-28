package dev.yurisuika.blossom.mixin.block;

import dev.yurisuika.blossom.block.EntityShapeContextInterface;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityShapeContext.class)
public abstract class EntityShapeContextMixin implements EntityShapeContextInterface {

    @Unique
    private Entity entity;

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/Entity;)V", at = @At("RETURN"))
    private void injectInit(Entity entity, CallbackInfo ci) {
        this.entity = entity;
    }

}