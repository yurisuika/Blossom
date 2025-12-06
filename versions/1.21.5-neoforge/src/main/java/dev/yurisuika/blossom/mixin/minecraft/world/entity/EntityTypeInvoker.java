package dev.yurisuika.blossom.mixin.minecraft.world.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(EntityType.class)
public interface EntityTypeInvoker {

    @Invoker("boatFactory")
    static EntityType.EntityFactory<Boat> invokeBoatFactory(Supplier<Item> boatItemGetter) {
        throw new AssertionError();
    }

    @Invoker("chestBoatFactory")
    static EntityType.EntityFactory<ChestBoat> invokeChestBoatFactory(Supplier<Item> boatItemGetter) {
        throw new AssertionError();
    }

}