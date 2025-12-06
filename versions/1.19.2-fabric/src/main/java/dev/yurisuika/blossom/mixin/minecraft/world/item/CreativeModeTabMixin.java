package dev.yurisuika.blossom.mixin.minecraft.world.item;

import dev.yurisuika.blossom.world.item.CreativeModeTabInterface;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreativeModeTab.class)
public abstract class CreativeModeTabMixin implements CreativeModeTabInterface {

    @Shadow
    @Final
    @Mutable
    public static CreativeModeTab[] TABS;

    @Override
    public void expandTabs() {
        CreativeModeTab[] original = TABS;
        TABS = new CreativeModeTab[TABS.length + 1];
        System.arraycopy(original, 0, TABS, 0, original.length);
    }

}