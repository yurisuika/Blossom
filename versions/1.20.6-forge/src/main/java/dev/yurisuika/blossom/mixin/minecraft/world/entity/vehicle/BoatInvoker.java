package dev.yurisuika.blossom.mixin.minecraft.world.entity.vehicle;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public interface BoatInvoker {

    @Mixin(Boat.Type.class)
    interface TypeInvoker {

        @Invoker("<init>")
        static Boat.Type invokeType(String internalName, int internalId, Block planks, String name) {
            throw new AssertionError();
        }

    }

}