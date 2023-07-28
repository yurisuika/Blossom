package dev.yurisuika.blossom.datagen.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class BlossomEnglishLanguageProvider extends FabricLanguageProvider {

    public BlossomEnglishLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("block.blossom.flowering_oak_leaves", "Flowering Oak Leaves");

        translationBuilder.add("commands.blossom.config.reload", "Blossom config reloaded");
        translationBuilder.add("commands.blossom.config.reset", "Blossom config reset to defaults");

        translationBuilder.add("commands.blossom.value.propagation.query", "Propagation is currently set to, (chance, %s)");
        translationBuilder.add("commands.blossom.value.propagation.set", "Propagation is now set to, (chance, %s)");

        translationBuilder.add("commands.blossom.value.fertilization.query", "Fertilization is currently set to, (chance, %s)");
        translationBuilder.add("commands.blossom.value.fertilization.set", "Fertilization is now set to, (chance, %s)");

        translationBuilder.add("commands.blossom.value.pollination.query", "Pollination is currently set to, (age, %s)");
        translationBuilder.add("commands.blossom.value.pollination.set", "Pollination is now set to, (age, %s)");

        translationBuilder.add("commands.blossom.value.fruit.query", "Fruit is currently set to, (bonus, %s); chance, %s)");
        translationBuilder.add("commands.blossom.value.fruit.set", "Fruit is now set to, (bonus, %s); chance, %s)");

        translationBuilder.add("commands.blossom.filter.temperature.query", "Temperature is currently set to, (min, %s); max, %s)");
        translationBuilder.add("commands.blossom.filter.temperature.set", "Temperature is now set to, (min, %s); max, %s)");

        translationBuilder.add("commands.blossom.filter.downfall.query", "Downfall is currently set to, (min, %s); max, %s)");
        translationBuilder.add("commands.blossom.filter.downfall.set", "Downfall is now set to, (min, %s); max, %s)");

        translationBuilder.add("commands.blossom.filter.dimension.whitelist.query", "Whitelisted dimensions, %s");
        translationBuilder.add("commands.blossom.filter.dimension.whitelist.add", "Added dimension '%s' to whitelist");
        translationBuilder.add("commands.blossom.filter.dimension.whitelist.add.failed", "Dimension '%s' is already in whitelist");
        translationBuilder.add("commands.blossom.filter.dimension.whitelist.remove", "Removed dimension '%s' from whitelist");
        translationBuilder.add("commands.blossom.filter.dimension.whitelist.remove.failed", "Dimension '%s' is not in whitelist");

        translationBuilder.add("commands.blossom.filter.dimension.blacklist.query", "Blacklisted dimensions, %s");
        translationBuilder.add("commands.blossom.filter.dimension.blacklist.add", "Added dimension '%s' to blacklist");
        translationBuilder.add("commands.blossom.filter.dimension.blacklist.add.failed", "Dimension '%s' is already in blacklist");
        translationBuilder.add("commands.blossom.filter.dimension.blacklist.remove", "Removed dimension '%s' from blacklist");
        translationBuilder.add("commands.blossom.filter.dimension.blacklist.remove.failed", "Dimension '%s' is not in blacklist");

        translationBuilder.add("commands.blossom.filter.biome.whitelist.query", "Whitelisted biomes, %s");
        translationBuilder.add("commands.blossom.filter.biome.whitelist.add", "Added biome '%s' to whitelist");
        translationBuilder.add("commands.blossom.filter.biome.whitelist.add.failed", "Biome '%s' is already in whitelist");
        translationBuilder.add("commands.blossom.filter.biome.whitelist.remove", "Removed biome '%s' from whitelist");
        translationBuilder.add("commands.blossom.filter.biome.whitelist.remove.failed", "Biome '%s' is not in whitelist");

        translationBuilder.add("commands.blossom.filter.biome.blacklist.query", "Blacklisted biomes, %s");
        translationBuilder.add("commands.blossom.filter.biome.blacklist.add", "Added biome '%s' to blacklist");
        translationBuilder.add("commands.blossom.filter.biome.blacklist.add.failed", "Biome '%s' is already in blacklist");
        translationBuilder.add("commands.blossom.filter.biome.blacklist.remove", "Removed biome '%s' from blacklist");
        translationBuilder.add("commands.blossom.filter.biome.blacklist.remove.failed", "Biome '%s' is not in blacklist");

        translationBuilder.add("commands.blossom.toggle.whitelist.query", "Whitelist is currently set to, %s");
        translationBuilder.add("commands.blossom.toggle.whitelist.set", "Whitelist is now set to, %s");

        translationBuilder.add("commands.blossom.toggle.blacklist.query", "Blacklist is currently set to, %s");
        translationBuilder.add("commands.blossom.toggle.blacklist.set", "Blacklist is now set to, %s");
    }

}