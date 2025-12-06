package dev.yurisuika.blossom.mixin.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Array;

@Mixin(BoatRenderer.class)
public class BoatRendererMixin {

    @Shadow
    @Final
    @Mutable
    private static ResourceLocation[] BOAT_TEXTURE_LOCATIONS;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addBlossomBoat(EntityRenderDispatcher entityRenderDispatcher, CallbackInfo ci) {
        ResourceLocation[] original = BOAT_TEXTURE_LOCATIONS;
        BOAT_TEXTURE_LOCATIONS = new ResourceLocation[BOAT_TEXTURE_LOCATIONS.length + 1];
        System.arraycopy(original, 0, BOAT_TEXTURE_LOCATIONS, 0, original.length);
        Array.set(BOAT_TEXTURE_LOCATIONS, BOAT_TEXTURE_LOCATIONS.length - 1, ResourceLocation.tryParse("textures/entity/boat/apple.png"));
    }

}