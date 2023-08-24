package dev.yurisuika.blossom.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryPredicateArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
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
                                            new Value.Blossoming(0.2F, 10.0D),
                                            new Value.Fruiting(0.2F, 10.0D),
                                            new Value.Harvesting(3, 0.5714286F)
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
                        .then(literal("blossoming")
                                .executes(context -> {
                                    MutableText chance = Texts.bracketed(Text.translatable("commands.blossom.value.blossoming.chance", config.value.blossoming.chance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.blossoming.chance.tooltip"))));
                                    MutableText distance = Texts.bracketed(Text.translatable("commands.blossom.value.blossoming.distance", config.value.blossoming.distance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.blossoming.distance.tooltip"))));

                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.blossoming.query", chance, distance), true);
                                    return 1;
                                })
                                .then(CommandManager.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(CommandManager.argument("distance", DoubleArgumentType.doubleArg(0.0D))
                                                .executes(context -> {
                                                    config.value.blossoming.chance = FloatArgumentType.getFloat(context, "chance");
                                                    config.value.blossoming.distance = DoubleArgumentType.getDouble(context, "distance");
                                                    saveConfig();

                                                    MutableText chance = Texts.bracketed(Text.translatable("commands.blossom.value.blossoming.chance", config.value.blossoming.chance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.blossoming.chance.tooltip"))));
                                                    MutableText distance = Texts.bracketed(Text.translatable("commands.blossom.value.blossoming.distance", config.value.blossoming.distance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.blossoming.distance.tooltip"))));

                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.blossoming.set", chance, distance), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("fruiting")
                                .executes(context -> {
                                    MutableText chance = Texts.bracketed(Text.translatable("commands.blossom.value.fruiting.chance", config.value.fruiting.chance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.fruiting.chance.tooltip"))));
                                    MutableText distance = Texts.bracketed(Text.translatable("commands.blossom.value.fruiting.distance", config.value.fruiting.distance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.blossoming.fruiting.tooltip"))));

                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.fruiting.query", chance, distance), false);
                                    return 1;
                                })
                                .then(CommandManager.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(CommandManager.argument("distance", DoubleArgumentType.doubleArg(0.0D))
                                                .executes(context -> {
                                                    config.value.fruiting.chance = FloatArgumentType.getFloat(context, "chance");
                                                    config.value.fruiting.distance = DoubleArgumentType.getDouble(context, "distance");
                                                    saveConfig();

                                                    MutableText chance = Texts.bracketed(Text.translatable("commands.blossom.value.fruiting.chance", config.value.fruiting.chance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.fruiting.chance.tooltip"))));
                                                    MutableText distance = Texts.bracketed(Text.translatable("commands.blossom.value.fruiting.distance", config.value.fruiting.distance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.blossoming.fruiting.tooltip"))));

                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.fruiting.set", chance, distance), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("harvesting")
                                .executes(context -> {
                                    MutableText bonus = Texts.bracketed(Text.translatable("commands.blossom.value.harvesting.bonus", config.value.harvesting.bonus)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.harvesting.bonus.tooltip"))));

                                    MutableText chance = Texts.bracketed(Text.translatable("commands.blossom.value.harvesting.chance", config.value.harvesting.chance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.harvesting.chance.tooltip"))));

                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.harvesting.query", bonus, chance), true);
                                    return 1;
                                })
                                .then(CommandManager.argument("bonus", IntegerArgumentType.integer(0))
                                        .then(CommandManager.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                .executes(context -> {
                                                    config.value.harvesting.bonus = IntegerArgumentType.getInteger(context, "bonus");
                                                    config.value.harvesting.chance =  FloatArgumentType.getFloat(context, "chance");
                                                    saveConfig();

                                                    MutableText bonus = Texts.bracketed(Text.translatable("commands.blossom.value.harvesting.bonus", config.value.harvesting.bonus)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.harvesting.bonus.tooltip"))));
                                                    MutableText chance = Texts.bracketed(Text.translatable("commands.blossom.value.harvesting.chance", config.value.harvesting.chance)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.value.harvesting.chance.tooltip"))));

                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.value.harvesting.set", bonus, chance), true);
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
                                    MutableText min = Texts.bracketed(Text.translatable("commands.blossom.filter.temperature.min", config.filter.temperature.min)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.temperature.min.tooltip"))));
                                    MutableText max = Texts.bracketed(Text.translatable("commands.blossom.filter.temperature.max", config.filter.temperature.max)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.temperature.max.tooltip"))));

                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.temperature.query", min, max), false);
                                    return 1;
                                })
                                .then(argument("min", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                        .then(argument("max", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                                .executes(context -> {
                                                    config.filter.temperature.min = Math.min(FloatArgumentType.getFloat(context, "min"), FloatArgumentType.getFloat(context, "max"));
                                                    config.filter.temperature.max = Math.max(FloatArgumentType.getFloat(context, "max"), FloatArgumentType.getFloat(context, "min"));
                                                    saveConfig();

                                                    MutableText min = Texts.bracketed(Text.translatable("commands.blossom.filter.temperature.min", config.filter.temperature.min)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.temperature.min.tooltip"))));
                                                    MutableText max = Texts.bracketed(Text.translatable("commands.blossom.filter.temperature.max", config.filter.temperature.max)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.temperature.max.tooltip"))));

                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("downfall")
                                .executes(context -> {
                                    MutableText min = Texts.bracketed(Text.translatable("commands.blossom.filter.downfall.min", config.filter.downfall.min)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.downfall.min.tooltip"))));
                                    MutableText max = Texts.bracketed(Text.translatable("commands.blossom.filter.downfall.max", config.filter.downfall.max)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.downfall.max.tooltip"))));

                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.temperature.query", min, max), false);
                                    return 1;
                                })
                                .then(argument("min", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(argument("max", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                .executes(context -> {
                                                    config.filter.downfall.min = Math.min(FloatArgumentType.getFloat(context, "min"), FloatArgumentType.getFloat(context, "max"));
                                                    config.filter.downfall.max = Math.max(FloatArgumentType.getFloat(context, "max"), FloatArgumentType.getFloat(context, "min"));
                                                    saveConfig();

                                                    MutableText min = Texts.bracketed(Text.translatable("commands.blossom.filter.downfall.min", config.filter.downfall.min)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.downfall.min.tooltip"))));
                                                    MutableText max = Texts.bracketed(Text.translatable("commands.blossom.filter.downfall.max", config.filter.downfall.max)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.downfall.max.tooltip"))));

                                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("dimension")
                                .then(literal("whitelist")
                                        .executes(context -> {
                                            MutableText list = Text.translatable("commands.blossom.filter.dimension.whitelist.list", Arrays.toString(config.filter.dimension.whitelist)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.dimension.whitelist.list.tooltip"))));

                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.dimension.whitelist.query", list), false);
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
                                            MutableText list = Text.translatable("commands.blossom.filter.dimension.blacklist.list", Arrays.toString(config.filter.dimension.blacklist)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.dimension.blacklist.list.tooltip"))));

                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.dimension.blacklist.query", list), false);
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
                                            MutableText list = Text.translatable("commands.blossom.filter.biome.whitelist.list", Arrays.toString(config.filter.biome.whitelist)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.biome.whitelist.list.tooltip"))));

                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.biome.whitelist.query", list), false);
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
                                            MutableText list = Text.translatable("commands.blossom.filter.biome.blacklist.list", Arrays.toString(config.filter.biome.blacklist)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.filter.biome.blacklist.list.tooltip"))));

                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.filter.biome.blacklist.query", list), false);
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
                                    MutableText toggle = Texts.bracketed(Text.translatable("commands.blossom.toggle.whitelist.toggle", config.toggle.whitelist)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.toggle.whitelist.toggle.tooltip"))));

                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.toggle.whitelist.query", toggle), false);
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            config.toggle.whitelist = BoolArgumentType.getBool(context, "value");
                                            saveConfig();

                                            MutableText toggle = Texts.bracketed(Text.translatable("commands.blossom.toggle.whitelist.toggle", config.toggle.whitelist)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.toggle.whitelist.toggle.tooltip"))));

                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.toggle.whitelist.set", toggle), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("blacklist")
                                .executes(context -> {
                                    MutableText toggle = Texts.bracketed(Text.translatable("commands.blossom.toggle.blacklist.toggle", config.toggle.blacklist)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.toggle.blacklist.toggle.tooltip"))));

                                    context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.toggle.blacklist.query", toggle), false);
                                    return 1;
                                })
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            config.toggle.blacklist = BoolArgumentType.getBool(context, "value");
                                            saveConfig();

                                            MutableText toggle = Texts.bracketed(Text.translatable("commands.blossom.toggle.blacklist.toggle", config.toggle.blacklist)).styled(style -> style.withColor(Formatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands.blossom.toggle.blacklist.toggle.tooltip"))));

                                            context.getSource().sendFeedback(() -> Text.translatable("commands.blossom.toggle.blacklist.set", toggle), false);
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }

}