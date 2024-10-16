package dev.yurisuika.blossom.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.yurisuika.blossom.config.Options;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.util.config.Config;
import dev.yurisuika.blossom.util.config.Option;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class BlossomCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, CommandSelection selection) {
        dispatcher.register(Commands.literal("blossom")
                .then(Commands.literal("config")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("reload")
                                .executes(commandContext -> {
                                    Config.loadConfig();
                                    Validate.checkBounds();
                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.config.reload"), true);
                                    return 1;
                                })
                        )
                        .then(Commands.literal("reset")
                                .executes(commandContext -> {
                                    Option.setValue(new Options().getValue());
                                    Option.setFilter(new Options().getFilter());
                                    Option.setToggle(new Options().getToggle());
                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.config.reset"), true);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("value")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("blossoming")
                                .executes(commandContext -> {
                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.blossoming.chance", Option.getBlossomingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.blossoming.chance.tooltip"))));
                                    MutableComponent distance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.blossoming.distance", Option.getBlossomingDistance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.blossoming.distance.tooltip"))));
                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.value.blossoming.query", chance, distance), true);
                                    return 1;
                                })
                                .then(Commands.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(Commands.argument("distance", DoubleArgumentType.doubleArg(0.0D))
                                                .executes(commandContext -> {
                                                    Option.setBlossomingChance(FloatArgumentType.getFloat(commandContext, "chance"));
                                                    Option.setBlossomingDistance(DoubleArgumentType.getDouble(commandContext, "distance"));
                                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.blossoming.chance", Option.getBlossomingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.blossoming.chance.tooltip"))));
                                                    MutableComponent distance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.blossoming.distance", Option.getBlossomingDistance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.blossoming.distance.tooltip"))));
                                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.value.blossoming.set", chance, distance), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("fruiting")
                                .executes(commandContext -> {
                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.fruiting.chance", Option.getFruitingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.fruiting.chance.tooltip"))));
                                    MutableComponent distance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.fruiting.distance", Option.getFruitingDistance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.blossoming.distance.tooltip"))));
                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.value.fruiting.query", chance, distance), false);
                                    return 1;
                                })
                                .then(Commands.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(Commands.argument("distance", DoubleArgumentType.doubleArg(0.0D))
                                                .executes(commandContext -> {
                                                    Option.setFruitingChance(FloatArgumentType.getFloat(commandContext, "chance"));
                                                    Option.setFruitingDistance(DoubleArgumentType.getDouble(commandContext, "distance"));
                                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.fruiting.chance", Option.getFruitingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.fruiting.chance.tooltip"))));
                                                    MutableComponent distance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.fruiting.distance", Option.getFruitingDistance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.blossoming.distance.tooltip"))));
                                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.value.fruiting.set", chance, distance), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("harvesting")
                                .executes(commandContext -> {
                                    MutableComponent bonus = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.harvesting.bonus", Option.getHarvestingBonus())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.harvesting.bonus.tooltip"))));
                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.harvesting.chance", Option.getHarvestingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.harvesting.chance.tooltip"))));
                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.value.harvesting.query", bonus, chance), true);
                                    return 1;
                                })
                                .then(Commands.argument("bonus", IntegerArgumentType.integer(0))
                                        .then(Commands.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                .executes(commandContext -> {
                                                    Option.setHarvestingBonus(IntegerArgumentType.getInteger(commandContext, "bonus"));
                                                    Option.setHarvestingChance( FloatArgumentType.getFloat(commandContext, "chance"));
                                                    MutableComponent bonus = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.harvesting.bonus", Option.getHarvestingBonus())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.harvesting.bonus.tooltip"))));
                                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.value.harvesting.chance", Option.getHarvestingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.value.harvesting.chance.tooltip"))));
                                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.value.harvesting.set", bonus, chance), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .then(Commands.literal("filter")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("temperature")
                                .executes(commandContext -> {
                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.filter.temperature.min", Option.getTemperatureMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.temperature.min.tooltip"))));
                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.filter.temperature.max", Option.getTemperatureMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.temperature.max.tooltip"))));
                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.temperature.query", min, max), false);
                                    return 1;
                                })
                                .then(Commands.argument("min", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                        .then(Commands.argument("max", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                                .executes(commandContext -> {
                                                    Option.setTemperatureMin(Math.min(FloatArgumentType.getFloat(commandContext, "min"), FloatArgumentType.getFloat(commandContext, "max")));
                                                    Option.setTemperatureMax(Math.max(FloatArgumentType.getFloat(commandContext, "max"), FloatArgumentType.getFloat(commandContext, "min")));
                                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.filter.temperature.min", Option.getTemperatureMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.temperature.min.tooltip"))));
                                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.filter.temperature.max", Option.getTemperatureMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.temperature.max.tooltip"))));
                                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("downfall")
                                .executes(commandContext -> {
                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.filter.downfall.min", Option.getDownfallMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.downfall.min.tooltip"))));
                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.filter.downfall.max", Option.getDownfallMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.downfall.max.tooltip"))));
                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.temperature.query", min, max), false);
                                    return 1;
                                })
                                .then(Commands.argument("min", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(Commands.argument("max", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                .executes(commandContext -> {
                                                    Option.setDownfallMin(Math.min(FloatArgumentType.getFloat(commandContext, "min"), FloatArgumentType.getFloat(commandContext, "max")));
                                                    Option.setDownfallMax(Math.max(FloatArgumentType.getFloat(commandContext, "max"), FloatArgumentType.getFloat(commandContext, "min")));
                                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.filter.downfall.min", Option.getDownfallMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.downfall.min.tooltip"))));
                                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.filter.downfall.max", Option.getDownfallMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.downfall.max.tooltip"))));
                                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("dimension")
                                .then(Commands.literal("whitelist")
                                        .executes(commandContext -> {
                                            MutableComponent list = Component.translatable("commands.blossom.filter.dimension.whitelist.list", Arrays.toString(Option.getDimensionWhitelist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.dimension.whitelist.list.tooltip"))));
                                            commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.dimension.whitelist.query", list), false);
                                            return 1;
                                        })
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                        .executes(commandContext -> {
                                                            String dimension = DimensionArgument.getDimension(commandContext, "dimension").dimensionTypeRegistration().unwrapKey().get().location().toString();
                                                            if (Arrays.stream(Option.getDimensionWhitelist()).anyMatch(dimension::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(Component.translatable("commands.blossom.filter.dimension.whitelist.add.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                Option.setDimensionWhitelist(ArrayUtils.add(Option.getDimensionWhitelist(), dimension));
                                                                Arrays.sort(Option.getDimensionWhitelist());
                                                                commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.dimension.whitelist.add", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                        .executes(commandContext -> {
                                                            String dimension = DimensionArgument.getDimension(commandContext, "dimension").dimensionTypeRegistration().unwrapKey().get().location().toString();
                                                            if (Arrays.stream(Option.getDimensionWhitelist()).noneMatch(dimension::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(Component.translatable("commands.blossom.filter.dimension.whitelist.remove.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                for (String entry : Option.getDimensionWhitelist()) {
                                                                    if (entry.equalsIgnoreCase(dimension)) {
                                                                        int index = ArrayUtils.indexOf(Option.getDimensionWhitelist(), entry);
                                                                        Option.setDimensionWhitelist(ArrayUtils.remove(Option.getDimensionWhitelist(), index));
                                                                    }
                                                                }
                                                                commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.dimension.whitelist.remove", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                                .then(Commands.literal("blacklist")
                                        .executes(commandContext -> {
                                            MutableComponent list = Component.translatable("commands.blossom.filter.dimension.blacklist.list", Arrays.toString(Option.getDimensionBlacklist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.dimension.blacklist.list.tooltip"))));

                                            commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.dimension.blacklist.query", list), false);
                                            return 1;
                                        })
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                        .executes(commandContext -> {
                                                            String dimension = DimensionArgument.getDimension(commandContext, "dimension").dimensionTypeRegistration().unwrapKey().get().location().toString();
                                                            if (Arrays.stream(Option.getDimensionBlacklist()).anyMatch(dimension::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(Component.translatable("commands.blossom.filter.biome.blacklist.add.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                Option.setDimensionBlacklist(ArrayUtils.add(Option.getDimensionBlacklist(), dimension));
                                                                Arrays.sort(Option.getDimensionBlacklist());
                                                                commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.dimension.blacklist.add", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                        .executes(commandContext -> {
                                                            String dimension = DimensionArgument.getDimension(commandContext, "dimension").dimensionTypeRegistration().unwrapKey().get().location().toString();
                                                            if (Arrays.stream(Option.getDimensionBlacklist()).noneMatch(dimension::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(Component.translatable("commands.blossom.filter.dimension.blacklist.remove.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                for (String entry : Option.getDimensionBlacklist()) {
                                                                    if (entry.equalsIgnoreCase(dimension)) {
                                                                        int index = ArrayUtils.indexOf(Option.getDimensionBlacklist(), entry);
                                                                        Option.setDimensionBlacklist(ArrayUtils.remove(Option.getDimensionBlacklist(), index));
                                                                    }
                                                                }
                                                                commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.dimension.blacklist.remove", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("biome")
                                .then(Commands.literal("whitelist")
                                        .executes(commandContext -> {
                                            MutableComponent list = Component.translatable("commands.blossom.filter.biome.whitelist.list", Arrays.toString(Option.getBiomeWhitelist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.biome.whitelist.list.tooltip"))));
                                            commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.biome.whitelist.query", list), false);
                                            return 1;
                                        })
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY))
                                                        .executes(commandContext -> {
                                                            String biome = ResourceOrTagLocationArgument.getRegistryType(commandContext, "biome", Registry.BIOME_REGISTRY, new DynamicCommandExceptionType(id -> Component.translatable("commands.locate.biome.invalid", id))).toString();
                                                            if (Arrays.stream(Option.getBiomeWhitelist()).anyMatch(biome::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(Component.translatable("commands.blossom.filter.biome.whitelist.add.failed", biome));
                                                                return 0;
                                                            } else {
                                                                Option.setBiomeWhitelist(ArrayUtils.add(Option.getBiomeWhitelist(), biome));
                                                                Arrays.sort(Option.getBiomeWhitelist());
                                                                commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.biome.whitelist.add", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY))
                                                        .executes(commandContext -> {
                                                            String biome = ResourceOrTagLocationArgument.getRegistryType(commandContext, "biome", Registry.BIOME_REGISTRY, new DynamicCommandExceptionType(id -> Component.translatable("commands.locate.biome.invalid", id))).toString();
                                                            if (Arrays.stream(Option.getBiomeWhitelist()).noneMatch(biome::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(Component.translatable("commands.blossom.filter.biome.whitelist.remove.failed", biome));
                                                                return 0;
                                                            } else {
                                                                for (String entry : Option.getBiomeWhitelist()) {
                                                                    if (entry.equalsIgnoreCase(biome)) {
                                                                        int index = ArrayUtils.indexOf(Option.getBiomeWhitelist(), entry);
                                                                        Option.setBiomeWhitelist(ArrayUtils.remove(Option.getBiomeWhitelist(), index));
                                                                    }
                                                                }
                                                                commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.biome.whitelist.remove", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                                .then(Commands.literal("blacklist")
                                        .executes(commandContext -> {
                                            MutableComponent list = Component.translatable("commands.blossom.filter.biome.blacklist.list", Arrays.toString(Option.getBiomeBlacklist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.filter.biome.blacklist.list.tooltip"))));
                                            commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.biome.blacklist.query", list), false);
                                            return 1;
                                        })
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY))
                                                        .executes(commandContext -> {
                                                            String biome = ResourceOrTagLocationArgument.getRegistryType(commandContext, "biome", Registry.BIOME_REGISTRY, new DynamicCommandExceptionType(id -> Component.translatable("commands.locate.biome.invalid", id))).toString();
                                                            if (Arrays.stream(Option.getBiomeBlacklist()).anyMatch(biome::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(Component.translatable("commands.blossom.filter.biome.blacklist.add.failed", biome));
                                                                return 0;
                                                            } else {
                                                                Option.setBiomeBlacklist(ArrayUtils.add(Option.getBiomeBlacklist(), biome));
                                                                Arrays.sort(Option.getBiomeBlacklist());
                                                                commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.biome.blacklist.add", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY))
                                                        .executes(commandContext -> {
                                                            String biome = ResourceOrTagLocationArgument.getRegistryType(commandContext, "biome", Registry.BIOME_REGISTRY, new DynamicCommandExceptionType(id -> Component.translatable("commands.locate.biome.invalid", id))).toString();
                                                            if (Arrays.stream(Option.getBiomeBlacklist()).noneMatch(biome::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(Component.translatable("commands.blossom.filter.biome.blacklist.remove.failed", biome));
                                                                return 0;
                                                            } else {
                                                                for (String entry : Option.getBiomeBlacklist()) {
                                                                    if (entry.equalsIgnoreCase(biome)) {
                                                                        int index = ArrayUtils.indexOf(Option.getBiomeBlacklist(), entry);
                                                                        Option.setBiomeBlacklist(ArrayUtils.remove(Option.getBiomeBlacklist(), index));
                                                                    }
                                                                }
                                                                commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.filter.biome.blacklist.remove", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("toggle")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("whitelist")
                                .executes(commandContext -> {
                                    MutableComponent toggle = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.toggle.whitelist.toggle", Option.getWhitelist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.toggle.whitelist.toggle.tooltip"))));
                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.toggle.whitelist.query", toggle), false);
                                    return 1;
                                })
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(commandContext -> {
                                            Option.setWhitelist(BoolArgumentType.getBool(commandContext, "value"));
                                            MutableComponent toggle = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.toggle.whitelist.toggle", Option.getWhitelist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.toggle.whitelist.toggle.tooltip"))));
                                            commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.toggle.whitelist.set", toggle), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("blacklist")
                                .executes(commandContext -> {
                                    MutableComponent toggle = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.toggle.blacklist.toggle", Option.getBlacklist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.toggle.blacklist.toggle.tooltip"))));
                                    commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.toggle.blacklist.query", toggle), false);
                                    return 1;
                                })
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(commandContext -> {
                                            Option.setBlacklist(BoolArgumentType.getBool(commandContext, "value"));
                                            MutableComponent toggle = ComponentUtils.wrapInSquareBrackets(Component.translatable("commands.blossom.toggle.blacklist.toggle", Option.getBlacklist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.blossom.toggle.blacklist.toggle.tooltip"))));
                                            commandContext.getSource().sendSuccess(Component.translatable("commands.blossom.toggle.blacklist.set", toggle), false);
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }

}