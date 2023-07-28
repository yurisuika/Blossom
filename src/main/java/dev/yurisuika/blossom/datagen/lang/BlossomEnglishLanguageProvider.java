package dev.yurisuika.blossom.datagen.lang;

import net.minecraft.data.DataOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class BlossomEnglishLanguageProvider extends LanguageProvider {

    public BlossomEnglishLanguageProvider(DataOutput dataOutput) {
        super(dataOutput, "blossom", "en_us");
    }

    @Override
    public void addTranslations() {
        add("block.blossom.flowering_oak_leaves", "Flowering Oak Leaves");

        add("commands.blossom.config.reload", "Blossom config reloaded");
        add("commands.blossom.config.reset", "Blossom config reset to defaults");

        add("commands.blossom.value.propagation.query", "Propagation is currently set to, (chance, %s)");
        add("commands.blossom.value.propagation.set", "Propagation is now set to, (chance, %s)");

        add("commands.blossom.value.fertilization.query", "Fertilization is currently set to, (chance, %s)");
        add("commands.blossom.value.fertilization.set", "Fertilization is now set to, (chance, %s)");

        add("commands.blossom.value.pollination.query", "Pollination is currently set to, (age, %s)");
        add("commands.blossom.value.pollination.set", "Pollination is now set to, (age, %s)");

        add("commands.blossom.value.fruit.query", "Fruit is currently set to, (bonus, %s); chance, %s)");
        add("commands.blossom.value.fruit.set", "Fruit is now set to, (bonus, %s); chance, %s)");

        add("commands.blossom.filter.temperature.query", "Temperature is currently set to, (min, %s); max, %s)");
        add("commands.blossom.filter.temperature.set", "Temperature is now set to, (min, %s); max, %s)");

        add("commands.blossom.filter.downfall.query", "Downfall is currently set to, (min, %s); max, %s)");
        add("commands.blossom.filter.downfall.set", "Downfall is now set to, (min, %s); max, %s)");

        add("commands.blossom.filter.dimension.whitelist.query", "Whitelisted dimensions, %s");
        add("commands.blossom.filter.dimension.whitelist.add", "Added dimension '%s' to whitelist");
        add("commands.blossom.filter.dimension.whitelist.add.failed", "Dimension '%s' is already in whitelist");
        add("commands.blossom.filter.dimension.whitelist.remove", "Removed dimension '%s' from whitelist");
        add("commands.blossom.filter.dimension.whitelist.remove.failed", "Dimension '%s' is not in whitelist");

        add("commands.blossom.filter.dimension.blacklist.query", "Blacklisted dimensions, %s");
        add("commands.blossom.filter.dimension.blacklist.add", "Added dimension '%s' to blacklist");
        add("commands.blossom.filter.dimension.blacklist.add.failed", "Dimension '%s' is already in blacklist");
        add("commands.blossom.filter.dimension.blacklist.remove", "Removed dimension '%s' from blacklist");
        add("commands.blossom.filter.dimension.blacklist.remove.failed", "Dimension '%s' is not in blacklist");

        add("commands.blossom.filter.biome.whitelist.query", "Whitelisted biomes, %s");
        add("commands.blossom.filter.biome.whitelist.add", "Added biome '%s' to whitelist");
        add("commands.blossom.filter.biome.whitelist.add.failed", "Biome '%s' is already in whitelist");
        add("commands.blossom.filter.biome.whitelist.remove", "Removed biome '%s' from whitelist");
        add("commands.blossom.filter.biome.whitelist.remove.failed", "Biome '%s' is not in whitelist");

        add("commands.blossom.filter.biome.blacklist.query", "Blacklisted biomes, %s");
        add("commands.blossom.filter.biome.blacklist.add", "Added biome '%s' to blacklist");
        add("commands.blossom.filter.biome.blacklist.add.failed", "Biome '%s' is already in blacklist");
        add("commands.blossom.filter.biome.blacklist.remove", "Removed biome '%s' from blacklist");
        add("commands.blossom.filter.biome.blacklist.remove.failed", "Biome '%s' is not in blacklist");

        add("commands.blossom.toggle.whitelist.query", "Whitelist is currently set to, %s");
        add("commands.blossom.toggle.whitelist.set", "Whitelist is now set to, %s");

        add("commands.blossom.toggle.blacklist.query", "Blacklist is currently set to, %s");
        add("commands.blossom.toggle.blacklist.set", "Blacklist is now set to, %s");
    }

}