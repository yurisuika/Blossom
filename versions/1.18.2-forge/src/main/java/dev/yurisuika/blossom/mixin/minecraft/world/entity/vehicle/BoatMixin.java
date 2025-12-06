package dev.yurisuika.blossom.mixin.minecraft.world.entity.vehicle;

import dev.yurisuika.blossom.world.entity.vehicle.BlossomBoat;
import dev.yurisuika.blossom.world.level.block.BlossomBlocks;
import dev.yurisuika.blossom.world.level.item.BlossomItems;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(Boat.class)
public abstract class BoatMixin {

    @Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
    public void setBoatType(CallbackInfoReturnable<Item> cir) {
        if (((Boat) (Object) this).getBoatType().equals(BlossomBoat.Type.APPLE)) {
            cir.setReturnValue(BlossomItems.APPLE_BOAT);
        }
    }

    @Mixin(Boat.Type.class)
    public static abstract class TypeMixin {

        @SuppressWarnings("ShadowTarget")
        @Shadow
        @Final
        @Mutable
        private static Boat.Type[] field_7724;

        @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/vehicle/Boat$Type;$VALUES:[Lnet/minecraft/world/entity/vehicle/Boat$Type;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
        private static void addBoatType(CallbackInfo ci) {
            List<Boat.Type> types = new ArrayList<>(Arrays.asList(field_7724));

            Boat.Type apple = BoatInvoker.TypeInvoker.invokeType("APPLE", types.get(types.size() - 1).ordinal() + 1, BlossomBlocks.APPLE_PLANKS, "apple");
            BlossomBoat.Type.APPLE = apple;
            types.add(apple);

            field_7724 = types.toArray(new Boat.Type[0]);
        }

    }

}