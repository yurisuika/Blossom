package dev.yurisuika.blossom.mixin.minecraft.world.phys.shapes;

import dev.yurisuika.blossom.world.phys.shapes.EntityCollisionContextInterface;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityCollisionContext.class)
public abstract class EntityCollisionContextMixin implements EntityCollisionContextInterface {

    @Unique
    private Entity entity;

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/Entity;)V", at = @At("RETURN"))
    private void assignEntity(Entity entity, CallbackInfo ci) {
        this.entity = entity;
    }

}