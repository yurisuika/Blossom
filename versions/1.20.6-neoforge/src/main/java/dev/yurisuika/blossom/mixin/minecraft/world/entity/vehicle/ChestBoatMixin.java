package dev.yurisuika.blossom.mixin.minecraft.world.entity.vehicle;

import dev.yurisuika.blossom.world.entity.vehicle.BlossomBoat;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBoat.class)
public abstract class ChestBoatMixin {

    @Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
    public void setBoatType(CallbackInfoReturnable<Item> cir) {
        if (((Boat) (Object) this).getVariant().equals(BlossomBoat.Type.APPLE)) {
            cir.setReturnValue(BlossomItems.APPLE_CHEST_BOAT);
        }
    }

}