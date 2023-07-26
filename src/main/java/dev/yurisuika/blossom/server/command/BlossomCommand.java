package dev.yurisuika.blossom.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
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
                                    config.value = new Value(
                                            new Value.Propagation(0.2F),
                                            new Value.Fertilization(0.06666667F),
                                            new Value.Pollination(1),
                                            new Value.Fruit(3, 0.5714286F)
                                    );
                                    config.filter = new Filter(
                                            new Filter.Temperature(-2.0F, 2.0F),
                                            new Filter.Downfall(0.0F, 1.0F),
                                            new Filter.Dimension(new String[]{"minecraft:overworld"}, new String[]{"minecraft:the_nether", "minecraft:the_end"}),
                                            new Filter.Biome(new String[]{"minecraft:forest"}, new String[]{"minecraft:the_void"})
                                    );
                                    config.toggle = new Toggle(
                                            false,
                                            false
                                    );
                                    saveConfig();
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.config.reset"), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("value")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(literal("propagation")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.propagation.query", config.value.propagation.chance), false);
                                    return 1;
                                })
                                .then(CommandManager.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .executes(context -> {
                                            config.value.propagation.chance = FloatArgumentType.getFloat(context, "chance");
                                            saveConfig();
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.propagation.set", config.value.propagation.chance), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("fertilization")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.fertilization.query", config.value.fertilization.chance), false);
                                    return 1;
                                })
                                .then(CommandManager.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .executes(context -> {
                                            config.value.fertilization.chance = FloatArgumentType.getFloat(context, "chance");
                                            saveConfig();
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.fertilization.set", config.value.fertilization.chance), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("pollination")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.pollination.query", config.value.pollination.age), false);
                                    return 1;
                                })
                                .then(CommandManager.argument("age", IntegerArgumentType.integer(0, 7))
                                        .executes(context -> {
                                            config.value.pollination.age = IntegerArgumentType.getInteger(context, "age");
                                            saveConfig();
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.pollination.set", config.value.pollination.age), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("fruit")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.fruit.query", config.value.fruit.bonus, config.value.fruit.chance), false);
                                    return 1;
                                })
                                .then(CommandManager.argument("bonus", IntegerArgumentType.integer(0))
                                        .then(CommandManager.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                .executes(context -> {
                                                    int bonus = IntegerArgumentType.getInteger(context, "bonus");
                                                    float chance = FloatArgumentType.getFloat(context, "chance");
                                                    config.value.fruit.bonus = bonus;
                                                    config.value.fruit.chance = chance;
                                                    saveConfig();
                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.fruit.set", bonus, chance), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .then(literal("filter")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(literal("temperature")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.temperature.query", config.filter.temperature.min, config.filter.temperature.max), false);
                                    return 1;
                                })
                                .then(argument("min", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                        .then(argument("max", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                                .executes(context -> {
                                                    float min = Math.min(FloatArgumentType.getFloat(context, "min"), FloatArgumentType.getFloat(context, "max"));
                                                    float max = Math.max(FloatArgumentType.getFloat(context, "max"), FloatArgumentType.getFloat(context, "min"));
                                                    config.filter.temperature.min = min;
                                                    config.filter.temperature.max = max;
                                                    saveConfig();
                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("downfall")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.downfall.query", config.filter.downfall.min, config.filter.downfall.max), false);
                                    return 1;
                                })
                                .then(argument("min", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(argument("max", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                .executes(context -> {
                                                    float min = Math.min(FloatArgumentType.getFloat(context, "min"), FloatArgumentType.getFloat(context, "max"));
                                                    float max = Math.max(FloatArgumentType.getFloat(context, "max"), FloatArgumentType.getFloat(context, "min"));
                                                    config.filter.downfall.min = min;
                                                    config.filter.downfall.max = max;
                                                    saveConfig();
                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.downfall.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("dimension")
                                .then(literal("whitelist")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.dimension.whitelist.query", String.join(", ", config.filter.dimension.whitelist)), false);
                                            return 1;
                                        })
                                        .then(literal("add")
                                                .then(argument("dimension", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.DIMENSION_TYPE))
                                                        .executes(context -> {
                                                            String dimension = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "dimension", RegistryKeys.DIMENSION_TYPE).asString();
                                                            if (Arrays.stream(config.filter.dimension.whitelist).anyMatch(dimension::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.filter.dimension.whitelist.add.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                config.filter.dimension.whitelist = ArrayUtils.add(config.filter.dimension.whitelist, dimension);
                                                                Arrays.sort(config.filter.dimension.whitelist);
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.dimension.whitelist.add", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(literal("remove")
                                                .then(argument("dimension", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.DIMENSION_TYPE))
                                                        .executes(context -> {
                                                            String dimension = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "dimension", RegistryKeys.DIMENSION_TYPE).asString();
                                                            if (Arrays.stream(config.filter.dimension.whitelist).noneMatch(dimension::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.filter.dimension.whitelist.remove.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                for (String entry : config.filter.dimension.whitelist) {
                                                                    if (entry.equalsIgnoreCase(dimension)) {
                                                                        int index = ArrayUtils.indexOf(config.filter.dimension.whitelist, entry);
                                                                        config.filter.dimension.whitelist = ArrayUtils.remove(config.filter.dimension.whitelist, index);
                                                                    }
                                                                }
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.dimension.whitelist.remove", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                                .then(literal("blacklist")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.dimension.blacklist.query", String.join(", ", config.filter.dimension.blacklist)), false);
                                            return 1;
                                        })
                                        .then(literal("add")
                                                .then(argument("dimension", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.DIMENSION_TYPE))
                                                        .executes(context -> {
                                                            String dimension = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "dimension", RegistryKeys.DIMENSION_TYPE).asString();
                                                            if (Arrays.stream(config.filter.dimension.blacklist).anyMatch(dimension::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.filter.biome.blacklist.add.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                config.filter.dimension.blacklist = ArrayUtils.add(config.filter.dimension.blacklist, dimension);
                                                                Arrays.sort(config.filter.dimension.blacklist);
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.dimension.blacklist.add", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(literal("remove")
                                                .then(argument("dimension", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.DIMENSION_TYPE))
                                                        .executes(context -> {
                                                            String dimension = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "dimension", RegistryKeys.DIMENSION_TYPE).asString();
                                                            if (Arrays.stream(config.filter.dimension.blacklist).noneMatch(dimension::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.filter.dimension.blacklist.remove.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                for (String entry : config.filter.dimension.blacklist) {
                                                                    if (entry.equalsIgnoreCase(dimension)) {
                                                                        int index = ArrayUtils.indexOf(config.filter.dimension.blacklist, entry);
                                                                        config.filter.dimension.blacklist = ArrayUtils.remove(config.filter.dimension.blacklist, index);
                                                                    }
                                                                }
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.dimension.blacklist.remove", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(literal("biome")
                                .then(literal("whitelist")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.biome.whitelist.query", String.join(", ", config.filter.biome.whitelist)), false);
                                            return 1;
                                        })
                                        .then(literal("add")
                                                .then(argument("biome", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.BIOME))
                                                        .executes(context -> {
                                                            String biome = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "biome", RegistryKeys.BIOME).asString();
                                                            if (Arrays.stream(config.filter.biome.whitelist).anyMatch(biome::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.filter.biome.whitelist.add.failed", biome));
                                                                return 0;
                                                            } else {
                                                                config.filter.biome.whitelist = ArrayUtils.add(config.filter.biome.whitelist, biome);
                                                                Arrays.sort(config.filter.biome.whitelist);
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.biome.whitelist.add", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(literal("remove")
                                                .then(argument("biome", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.BIOME))
                                                        .executes(context -> {
                                                            String biome = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "biome", RegistryKeys.BIOME).asString();
                                                            if (Arrays.stream(config.filter.biome.whitelist).noneMatch(biome::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.filter.biome.whitelist.remove.failed", biome));
                                                                return 0;
                                                            } else {
                                                                for (String entry : config.filter.biome.whitelist) {
                                                                    if (entry.equalsIgnoreCase(biome)) {
                                                                        int index = ArrayUtils.indexOf(config.filter.biome.whitelist, entry);
                                                                        config.filter.biome.whitelist = ArrayUtils.remove(config.filter.biome.whitelist, index);
                                                                    }
                                                                }
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.biome.whitelist.remove", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                                .then(literal("blacklist")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.biome.blacklist.query", String.join(", ", config.filter.biome.blacklist)), false);
                                            return 1;
                                        })
                                        .then(literal("add")
                                                .then(argument("biome", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.BIOME))
                                                        .executes(context -> {
                                                            String biome = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "biome", RegistryKeys.BIOME).asString();
                                                            if (Arrays.stream(config.filter.biome.blacklist).anyMatch(biome::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.filter.biome.blacklist.add.failed", biome));
                                                                return 0;
                                                            } else {
                                                                config.filter.biome.blacklist = ArrayUtils.add(config.filter.biome.blacklist, biome);
                                                                Arrays.sort(config.filter.biome.blacklist);
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.biome.blacklist.add", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(literal("remove")
                                                .then(argument("biome", RegistryEntryPredicateArgumentType.registryEntryPredicate(registryAccess, RegistryKeys.BIOME))
                                                        .executes(context -> {
                                                            String biome = RegistryEntryPredicateArgumentType.getRegistryEntryPredicate(context, "biome", RegistryKeys.BIOME).asString();
                                                            if (Arrays.stream(config.filter.biome.blacklist).noneMatch(biome::equalsIgnoreCase)) {
                                                                context.getSource().sendError(Text.translatable("commands.blossom.filter.biome.blacklist.remove.failed", biome));
                                                                return 0;
                                                            } else {
                                                                for (String entry : config.filter.biome.blacklist) {
                                                                    if (entry.equalsIgnoreCase(biome)) {
                                                                        int index = ArrayUtils.indexOf(config.filter.biome.blacklist, entry);
                                                                        config.filter.biome.blacklist = ArrayUtils.remove(config.filter.biome.blacklist, index);
                                                                    }
                                                                }
                                                                saveConfig();
                                                                context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.biome.blacklist.remove", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                        )
                )
                .then(literal("toggle")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(literal("whitelist")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.toggle.whitelist.query", config.toggle.whitelist), false);
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            config.toggle.whitelist = BoolArgumentType.getBool(context, "value");
                                            saveConfig();
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.toggle.whitelist.set", config.toggle.whitelist), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("blacklist")
                                .executes(context -> {
                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.toggle.blacklist.query", config.toggle.blacklist), false);
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            config.toggle.blacklist = BoolArgumentType.getBool(context, "value");
                                            saveConfig();
                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.toggle.blacklist.set", config.toggle.blacklist), false);
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }

}