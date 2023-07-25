package dev.yurisuika.blossom.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import dev.yurisuika.blossom.command.argument.PrecipitationArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryPredicateArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.server.command.CommandManager.*;

public class BlossomCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        dispatcher.register(literal("blossom")
                .then(literal("config")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(literal("reload")
                                .executes(context -> {
                                    loadConfig();
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.config.reload"), true);
                                    return 1;
                                })
                        )
                        .then(literal("reset")
                                .executes(context -> {
                                    config.propagation = new Propagation(0.2F);
                                    config.fertilization = new Fertilization(0.06666667F);
                                    config.pollination = new Pollination(1);
                                    config.harvest = new Harvest(3, 0.5714286F);
                                    config.climate = new Climate(
                                            new String[]{"none", "rain", "snow"},
                                            new Climate.Temperature(-2.0F, 2.0F),
                                            new Climate.Downfall(0.0F, 1.0F),
                                            new Climate.Whitelist(false, new String[]{"minecraft:overworld"}, new String[]{"minecraft:forest"}),
                                            new Climate.Blacklist(false, new String[]{"minecraft:the_nether", "minecraft:the_end"}, new String[]{"minecraft:the_void"})
                                    );
                                    saveConfig();
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.config.reset"), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("propagation")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.propagation.query", config.propagation.chance), false);
                            return 1;
                        })
                        .then(CommandManager.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                .executes(context -> {
                                    config.propagation.chance = FloatArgumentType.getFloat(context, "chance");
                                    saveConfig();
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.propagation.set", config.propagation.chance), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("fertilization")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.fertilization.query", config.fertilization.chance), false);
                            return 1;
                        })
                        .then(CommandManager.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                .executes(context -> {
                                    config.fertilization.chance = FloatArgumentType.getFloat(context, "chance");
                                    saveConfig();
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.fertilization.set", config.fertilization.chance), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("pollination")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.pollination.query", config.pollination.age), false);
                            return 1;
                        })
                        .then(CommandManager.argument("age", IntegerArgumentType.integer(0, 7))
                                .executes(context -> {
                                    config.pollination.age = IntegerArgumentType.getInteger(context, "age");
                                    saveConfig();
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.pollination.set", config.pollination.age), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("harvest")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.harvest.query", config.harvest.extra, config.harvest.probability), false);
                            return 1;
                        })
                        .then(CommandManager.argument("extra", IntegerArgumentType.integer(0))
                                .then(CommandManager.argument("probability", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .executes(context -> {
                                            int extra = IntegerArgumentType.getInteger(context, "extra");
                                            float probability = FloatArgumentType.getFloat(context, "probability");
                                            config.harvest = new Harvest(extra, probability);
                                            saveConfig();
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.harvest.set", extra, probability), true);
                                            return 1;
                                        })
                                )
                        )
                )
                .then(literal("climate")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(literal("precipitation")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.precipitation.query", Arrays.asList(config.climate.precipitation)), false);
                                    return 1;
                                })
                                .then(literal("add")
                                        .then(argument("precipitation", PrecipitationArgumentType.precipitation())
                                                .executes(context -> {
                                                    String precipitation = PrecipitationArgumentType.getPrecipitation(context, "precipitation").asString();
                                                    if (Arrays.stream(config.climate.precipitation).anyMatch(precipitation::equalsIgnoreCase)) {
                                                        context.getSource().sendError(Text.translatable("commands.blossom.climate.precipitation.add.failed", precipitation));
                                                        return 0;
                                                    } else {
                                                        config.climate.precipitation = ArrayUtils.add(config.climate.precipitation, precipitation);
                                                        Arrays.sort(config.climate.precipitation);
                                                        saveConfig();
                                                        context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.precipitation.add", precipitation), false);
                                                        return 1;
                                                    }
                                                })
                                        )
                                )
                                .then(literal("remove")
                                        .then(argument("precipitation", PrecipitationArgumentType.precipitation())
                                                .executes(context -> {
                                                    String precipitation = PrecipitationArgumentType.getPrecipitation(context, "precipitation").asString();
                                                    if (Arrays.stream(config.climate.precipitation).noneMatch(precipitation::equalsIgnoreCase)) {
                                                        context.getSource().sendError(Text.translatable("commands.blossom.climate.precipitation.remove.failed", precipitation));
                                                        return 0;
                                                    } else {
                                                        for (String str : config.climate.precipitation) {
                                                            if (str.equalsIgnoreCase(precipitation)) {
                                                                int index = ArrayUtils.indexOf(config.climate.precipitation, str);
                                                                config.climate.precipitation = ArrayUtils.remove(config.climate.precipitation, index);
                                                            }
                                                        }
                                                        saveConfig();
                                                        context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.precipitation.remove", precipitation), false);
                                                        return 1;
                                                    }
                                                })
                                        )
                                )
                        )
                        .then(literal("temperature")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.temperature.query", config.climate.temperature.min, config.climate.temperature.max), false);
                                    return 1;
                                })
                                .then(argument("min", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                        .then(argument("max", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                                .executes(context -> {
                                                    float min = Math.min(FloatArgumentType.getFloat(context, "min"), FloatArgumentType.getFloat(context, "max"));
                                                    float max = Math.max(FloatArgumentType.getFloat(context, "max"), FloatArgumentType.getFloat(context, "min"));
                                                    config.climate.temperature.min = min;
                                                    config.climate.temperature.max = max;
                                                    saveConfig();
                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("downfall")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.downfall.query", config.climate.downfall.min, config.climate.downfall.max), false);
                                    return 1;
                                })
                                .then(argument("min", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(argument("max", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                .executes(context -> {
                                                    float min = Math.min(FloatArgumentType.getFloat(context, "min"), FloatArgumentType.getFloat(context, "max"));
                                                    float max = Math.max(FloatArgumentType.getFloat(context, "max"), FloatArgumentType.getFloat(context, "min"));
                                                    config.climate.downfall.min = min;
                                                    config.climate.downfall.max = max;
                                                    saveConfig();
                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.downfall.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("whitelist")
                                .then(literal("enabled")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.whitelist.enabled.query", config.climate.whitelist.enabled), false);
                                            return 1;
                                        })
                                        .then(argument("value", BoolArgumentType.bool())
                                                .executes(context -> {
                                                    config.climate.whitelist.enabled = BoolArgumentType.getBool(context, "value");
                                                    saveConfig();
                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.whitelist.enabled.set", config.climate.whitelist.enabled), false);
                                                    return 1;
                                                })
                                        )
                                )
                                .then(literal("dimensions")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.whitelist.dimensions.query", Arrays.asList(config.climate.whitelist.dimensions)), false);
                                            return 1;
                                        })
                                        .then(literal("add")
                                                .then(argument("dimension", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.DIMENSION_TYPE))
                                                        .executes(context -> {
                                                            String dimension = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "dimension", RegistryKeys.DIMENSION_TYPE).asString();
                                                            if (Arrays.stream(config.climate.whitelist.dimensions).anyMatch(dimension::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.climate.whitelist.dimensions.add.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                config.climate.whitelist.dimensions = ArrayUtils.add(config.climate.whitelist.dimensions, dimension);
                                                                Arrays.sort(config.climate.whitelist.dimensions);
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.whitelist.dimensions.add", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(literal("remove")
                                                .then(argument("dimension", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.DIMENSION_TYPE))
                                                        .executes(context -> {
                                                            String dimension = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "dimension", RegistryKeys.DIMENSION_TYPE).asString();
                                                            if (Arrays.stream(config.climate.whitelist.dimensions).noneMatch(dimension::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.climate.whitelist.dimensions.remove.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                for (String str : config.climate.whitelist.dimensions) {
                                                                    if (str.equalsIgnoreCase(dimension)) {
                                                                        int index = ArrayUtils.indexOf(config.climate.whitelist.dimensions, str);
                                                                        config.climate.whitelist.dimensions = ArrayUtils.remove(config.climate.whitelist.dimensions, index);
                                                                    }
                                                                }
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.whitelist.dimensions.remove", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                                .then(literal("biomes")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.whitelist.biomes.query", Arrays.asList(config.climate.whitelist.biomes)), false);
                                            return 1;
                                        })
                                        .then(literal("add")
                                                .then(argument("biome", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.BIOME))
                                                        .executes(context -> {
                                                            String biome = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "biome", RegistryKeys.BIOME).asString();
                                                            if (Arrays.stream(config.climate.whitelist.biomes).anyMatch(biome::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.climate.whitelist.biomes.add.failed", biome));
                                                                return 0;
                                                            } else {
                                                                config.climate.whitelist.biomes = ArrayUtils.add(config.climate.whitelist.biomes, biome);
                                                                Arrays.sort(config.climate.whitelist.biomes);
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.whitelist.biomes.add", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(literal("remove")
                                                .then(argument("biome", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.BIOME))
                                                        .executes(context -> {
                                                            String biome = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "biome", RegistryKeys.BIOME).asString();
                                                            if (Arrays.stream(config.climate.whitelist.biomes).noneMatch(biome::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.climate.whitelist.biomes.remove.failed", biome));
                                                                return 0;
                                                            } else {
                                                                for (String str : config.climate.whitelist.biomes) {
                                                                    if (str.equalsIgnoreCase(biome)) {
                                                                        int index = ArrayUtils.indexOf(config.climate.whitelist.biomes, str);
                                                                        config.climate.whitelist.biomes = ArrayUtils.remove(config.climate.whitelist.biomes, index);
                                                                    }
                                                                }
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.whitelist.biomes.remove", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(literal("blacklist")
                                .then(literal("enabled")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.blacklist.enabled.query", config.climate.blacklist.enabled), false);
                                            return 1;
                                        })
                                        .then(argument("value", BoolArgumentType.bool())
                                                .executes(context -> {
                                                    config.climate.blacklist.enabled = BoolArgumentType.getBool(context, "value");
                                                    saveConfig();
                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.blacklist.enabled.set", config.climate.blacklist.enabled), false);
                                                    return 1;
                                                })
                                        )
                                )
                                .then(literal("dimensions")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.blacklist.dimensions.query", Arrays.asList(config.climate.blacklist.dimensions)), false);
                                            return 1;
                                        })
                                        .then(literal("add")
                                                .then(argument("dimension", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.DIMENSION_TYPE))
                                                        .executes(context -> {
                                                            String dimension = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "dimension", RegistryKeys.DIMENSION_TYPE).asString();
                                                            if (Arrays.stream(config.climate.blacklist.dimensions).anyMatch(dimension::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.climate.blacklist.dimensions.add.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                config.climate.blacklist.dimensions = ArrayUtils.add(config.climate.blacklist.dimensions, dimension);
                                                                Arrays.sort(config.climate.blacklist.dimensions);
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.blacklist.dimensions.add", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(literal("remove")
                                                .then(argument("dimension", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.DIMENSION_TYPE))
                                                        .executes(context -> {
                                                            String dimension = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "dimension", RegistryKeys.DIMENSION_TYPE).asString();
                                                            if (Arrays.stream(config.climate.blacklist.dimensions).noneMatch(dimension::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.climate.blacklist.dimensions.remove.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                for (String str : config.climate.blacklist.dimensions) {
                                                                    if (str.equalsIgnoreCase(dimension)) {
                                                                        int index = ArrayUtils.indexOf(config.climate.blacklist.dimensions, str);
                                                                        config.climate.blacklist.dimensions = ArrayUtils.remove(config.climate.blacklist.dimensions, index);
                                                                    }
                                                                }
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.blacklist.dimensions.remove", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                                .then(literal("biomes")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.blacklist.biomes.query", Arrays.asList(config.climate.blacklist.biomes)), false);
                                            return 1;
                                        })
                                        .then(literal("add")
                                                .then(argument("biome", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.BIOME))
                                                        .executes(context -> {
                                                            String biome = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "biome", RegistryKeys.BIOME).asString();
                                                            if (Arrays.stream(config.climate.blacklist.biomes).anyMatch(biome::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.climate.blacklist.biomes.add.failed", biome));
                                                                return 0;
                                                            } else {
                                                                config.climate.blacklist.biomes = ArrayUtils.add(config.climate.blacklist.biomes, biome);
                                                                Arrays.sort(config.climate.blacklist.biomes);
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.blacklist.biomes.add", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(literal("remove")
                                                .then(argument("biome", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.BIOME))
                                                        .executes(context -> {
                                                            String biome = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "biome", RegistryKeys.BIOME).asString();
                                                            if (Arrays.stream(config.climate.blacklist.biomes).noneMatch(biome::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.climate.blacklist.biomes.remove.failed", biome));
                                                                return 0;
                                                            } else {
                                                                for (String str : config.climate.blacklist.biomes) {
                                                                    if (str.equalsIgnoreCase(biome)) {
                                                                        int index = ArrayUtils.indexOf(config.climate.blacklist.biomes, str);
                                                                        config.climate.blacklist.biomes = ArrayUtils.remove(config.climate.blacklist.biomes, index);
                                                                    }
                                                                }
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.climate.blacklist.biomes.remove", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                        )
                )
        );
    }

}