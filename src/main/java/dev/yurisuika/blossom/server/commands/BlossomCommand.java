package dev.yurisuika.blossom.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.yurisuika.blossom.config.Options;
import dev.yurisuika.blossom.util.Validate;
import dev.yurisuika.blossom.util.config.Config;
import dev.yurisuika.blossom.util.config.Option;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class BlossomCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
        dispatcher.register(Commands.literal("blossom")
                .then(Commands.literal("config")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("reload")
                                .executes(commandContext -> {
                                    Config.loadConfig();
                                    Validate.checkBounds();
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.config.reload"), true);
                                    return 1;
                                })
                        )
                        .then(Commands.literal("reset")
                                .executes(commandContext -> {
                                    Option.setValue(new Options().getValue());
                                    Option.setFilter(new Options().getFilter());
                                    Option.setToggle(new Options().getToggle());
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.config.reset"), true);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("value")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("blossoming")
                                .executes(commandContext -> {
                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.value.blossoming.chance", Option.getBlossomingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.value.blossoming.chance.tooltip"))));
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.value.blossoming.query", chance), true);
                                    return 1;
                                })
                                .then(Commands.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .executes(commandContext -> {
                                            Option.setBlossomingChance(FloatArgumentType.getFloat(commandContext, "chance"));
                                            MutableComponent chance = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.value.blossoming.chance", Option.getBlossomingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.value.blossoming.chance.tooltip"))));
                                            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.value.blossoming.set", chance), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("fruiting")
                                .executes(commandContext -> {
                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.value.fruiting.chance", Option.getFruitingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.value.fruiting.chance.tooltip"))));
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.value.fruiting.query", chance), false);
                                    return 1;
                                })
                                .then(Commands.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .executes(commandContext -> {
                                            Option.setFruitingChance(FloatArgumentType.getFloat(commandContext, "chance"));
                                            MutableComponent chance = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.value.fruiting.chance", Option.getFruitingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.value.fruiting.chance.tooltip"))));
                                            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.value.fruiting.set", chance), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("harvesting")
                                .executes(commandContext -> {
                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.value.harvesting.chance", Option.getHarvestingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.value.harvesting.chance.tooltip"))));
                                    MutableComponent bonus = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.value.harvesting.bonus", Option.getHarvestingBonus())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.value.harvesting.bonus.tooltip"))));
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.value.harvesting.query", chance, bonus), true);
                                    return 1;
                                })
                                .then(Commands.argument("chance", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(Commands.argument("bonus", IntegerArgumentType.integer(0))
                                                .executes(commandContext -> {
                                                    Option.setHarvestingChance( FloatArgumentType.getFloat(commandContext, "chance"));
                                                    Option.setHarvestingBonus(IntegerArgumentType.getInteger(commandContext, "bonus"));
                                                    MutableComponent chance = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.value.harvesting.chance", Option.getHarvestingChance())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.value.harvesting.chance.tooltip"))));
                                                    MutableComponent bonus = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.value.harvesting.bonus", Option.getHarvestingBonus())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.value.harvesting.bonus.tooltip"))));
                                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.value.harvesting.set", chance, bonus), true);
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
                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.temperature.min", Option.getTemperatureMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.temperature.min.tooltip"))));
                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.temperature.max", Option.getTemperatureMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.temperature.max.tooltip"))));
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.temperature.query", min, max), false);
                                    return 1;
                                })
                                .then(Commands.argument("min", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                        .then(Commands.argument("max", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                                .executes(commandContext -> {
                                                    Option.setTemperatureMin(Math.min(FloatArgumentType.getFloat(commandContext, "min"), FloatArgumentType.getFloat(commandContext, "max")));
                                                    Option.setTemperatureMax(Math.max(FloatArgumentType.getFloat(commandContext, "max"), FloatArgumentType.getFloat(commandContext, "min")));
                                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.temperature.min", Option.getTemperatureMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.temperature.min.tooltip"))));
                                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.temperature.max", Option.getTemperatureMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.temperature.max.tooltip"))));
                                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("downfall")
                                .executes(commandContext -> {
                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.downfall.min", Option.getDownfallMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.downfall.min.tooltip"))));
                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.downfall.max", Option.getDownfallMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.downfall.max.tooltip"))));
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.temperature.query", min, max), false);
                                    return 1;
                                })
                                .then(Commands.argument("min", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(Commands.argument("max", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                .executes(commandContext -> {
                                                    Option.setDownfallMin(Math.min(FloatArgumentType.getFloat(commandContext, "min"), FloatArgumentType.getFloat(commandContext, "max")));
                                                    Option.setDownfallMax(Math.max(FloatArgumentType.getFloat(commandContext, "max"), FloatArgumentType.getFloat(commandContext, "min")));
                                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.downfall.min", Option.getDownfallMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.downfall.min.tooltip"))));
                                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.downfall.max", Option.getDownfallMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.downfall.max.tooltip"))));
                                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("dimension")
                                .then(Commands.literal("whitelist")
                                        .executes(commandContext -> {
                                            MutableComponent list = new TextComponent(Arrays.toString(Option.getDimensionWhitelist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.dimension.whitelist.tooltip"))));
                                            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.dimension.whitelist.query", list), false);
                                            return 1;
                                        })
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                        .executes(commandContext -> {
                                                            String dimension = commandContext.getSource().registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getKey(DimensionArgument.getDimension(commandContext, "dimension").dimensionType()).toString();
                                                            if (Arrays.stream(Option.getDimensionWhitelist()).anyMatch(dimension::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(new TranslatableComponent("commands.blossom.filter.dimension.whitelist.add.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                Option.setDimensionWhitelist(ArrayUtils.add(Option.getDimensionWhitelist(), dimension));
                                                                Arrays.sort(Option.getDimensionWhitelist());
                                                                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.dimension.whitelist.add", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                        .executes(commandContext -> {
                                                            String dimension = commandContext.getSource().registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getKey(DimensionArgument.getDimension(commandContext, "dimension").dimensionType()).toString();
                                                            if (Arrays.stream(Option.getDimensionWhitelist()).noneMatch(dimension::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(new TranslatableComponent("commands.blossom.filter.dimension.whitelist.remove.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                for (String entry : Option.getDimensionWhitelist()) {
                                                                    if (entry.equalsIgnoreCase(dimension)) {
                                                                        int index = ArrayUtils.indexOf(Option.getDimensionWhitelist(), entry);
                                                                        Option.setDimensionWhitelist(ArrayUtils.remove(Option.getDimensionWhitelist(), index));
                                                                    }
                                                                }
                                                                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.dimension.whitelist.remove", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                                .then(Commands.literal("blacklist")
                                        .executes(commandContext -> {
                                            MutableComponent list = new TextComponent(Arrays.toString(Option.getDimensionBlacklist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.dimension.blacklist.tooltip"))));
                                            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.dimension.blacklist.query", list), false);
                                            return 1;
                                        })
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                        .executes(commandContext -> {
                                                            String dimension = commandContext.getSource().registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getKey(DimensionArgument.getDimension(commandContext, "dimension").dimensionType()).toString();
                                                            if (Arrays.stream(Option.getDimensionBlacklist()).anyMatch(dimension::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(new TranslatableComponent("commands.blossom.filter.biome.blacklist.add.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                Option.setDimensionBlacklist(ArrayUtils.add(Option.getDimensionBlacklist(), dimension));
                                                                Arrays.sort(Option.getDimensionBlacklist());
                                                                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.dimension.blacklist.add", dimension), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                        .executes(commandContext -> {
                                                            String dimension = commandContext.getSource().registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getKey(DimensionArgument.getDimension(commandContext, "dimension").dimensionType()).toString();
                                                            if (Arrays.stream(Option.getDimensionBlacklist()).noneMatch(dimension::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(new TranslatableComponent("commands.blossom.filter.dimension.blacklist.remove.failed", dimension));
                                                                return 0;
                                                            } else {
                                                                for (String entry : Option.getDimensionBlacklist()) {
                                                                    if (entry.equalsIgnoreCase(dimension)) {
                                                                        int index = ArrayUtils.indexOf(Option.getDimensionBlacklist(), entry);
                                                                        Option.setDimensionBlacklist(ArrayUtils.remove(Option.getDimensionBlacklist(), index));
                                                                    }
                                                                }
                                                                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.dimension.blacklist.remove", dimension), false);
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
                                            MutableComponent list = new TextComponent(Arrays.toString(Option.getBiomeWhitelist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.biome.whitelist.tooltip"))));
                                            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.biome.whitelist.query", list), false);
                                            return 1;
                                        })
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("biome", ResourceLocationArgument.id())
                                                        .suggests(SuggestionProviders.AVAILABLE_BIOMES)
                                                        .executes(commandContext -> {
                                                            String biome = commandContext.getArgument("biome", ResourceLocation.class).toString();
                                                            if (Arrays.stream(Option.getBiomeWhitelist()).anyMatch(biome::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(new TranslatableComponent("commands.blossom.filter.biome.whitelist.add.failed", biome));
                                                                return 0;
                                                            } else {
                                                                Option.setBiomeWhitelist(ArrayUtils.add(Option.getBiomeWhitelist(), biome));
                                                                Arrays.sort(Option.getBiomeWhitelist());
                                                                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.biome.whitelist.add", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("biome", ResourceLocationArgument.id())
                                                        .suggests(SuggestionProviders.AVAILABLE_BIOMES)
                                                        .executes(commandContext -> {
                                                            String biome = commandContext.getArgument("biome", ResourceLocation.class).toString();
                                                            if (Arrays.stream(Option.getBiomeWhitelist()).noneMatch(biome::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(new TranslatableComponent("commands.blossom.filter.biome.whitelist.remove.failed", biome));
                                                                return 0;
                                                            } else {
                                                                for (String entry : Option.getBiomeWhitelist()) {
                                                                    if (entry.equalsIgnoreCase(biome)) {
                                                                        int index = ArrayUtils.indexOf(Option.getBiomeWhitelist(), entry);
                                                                        Option.setBiomeWhitelist(ArrayUtils.remove(Option.getBiomeWhitelist(), index));
                                                                    }
                                                                }
                                                                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.biome.whitelist.remove", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                )
                                .then(Commands.literal("blacklist")
                                        .executes(commandContext -> {
                                            MutableComponent list = new TextComponent(Arrays.toString(Option.getBiomeBlacklist())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.biome.blacklist.tooltip"))));
                                            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.biome.blacklist.query", list), false);
                                            return 1;
                                        })
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("biome", ResourceLocationArgument.id())
                                                        .suggests(SuggestionProviders.AVAILABLE_BIOMES)
                                                        .executes(commandContext -> {
                                                            String biome = commandContext.getArgument("biome", ResourceLocation.class).toString();
                                                            if (Arrays.stream(Option.getBiomeBlacklist()).anyMatch(biome::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(new TranslatableComponent("commands.blossom.filter.biome.blacklist.add.failed", biome));
                                                                return 0;
                                                            } else {
                                                                Option.setBiomeBlacklist(ArrayUtils.add(Option.getBiomeBlacklist(), biome));
                                                                Arrays.sort(Option.getBiomeBlacklist());
                                                                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.biome.blacklist.add", biome), false);
                                                                return 1;
                                                            }
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("remove")
                                                .then(Commands.argument("biome", ResourceLocationArgument.id())
                                                        .suggests(SuggestionProviders.AVAILABLE_BIOMES)
                                                        .executes(commandContext -> {
                                                            String biome = commandContext.getArgument("biome", ResourceLocation.class).toString();
                                                            if (Arrays.stream(Option.getBiomeBlacklist()).noneMatch(biome::equalsIgnoreCase)) {
                                                                commandContext.getSource().sendFailure(new TranslatableComponent("commands.blossom.filter.biome.blacklist.remove.failed", biome));
                                                                return 0;
                                                            } else {
                                                                for (String entry : Option.getBiomeBlacklist()) {
                                                                    if (entry.equalsIgnoreCase(biome)) {
                                                                        int index = ArrayUtils.indexOf(Option.getBiomeBlacklist(), entry);
                                                                        Option.setBiomeBlacklist(ArrayUtils.remove(Option.getBiomeBlacklist(), index));
                                                                    }
                                                                }
                                                                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.biome.blacklist.remove", biome), false);
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
                                    MutableComponent toggle = ComponentUtils.wrapInSquareBrackets(new TextComponent(String.valueOf(Option.getWhitelist()))).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.toggle.whitelist.tooltip"))));
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.toggle.whitelist.query", toggle), false);
                                    return 1;
                                })
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(commandContext -> {
                                            Option.setWhitelist(BoolArgumentType.getBool(commandContext, "value"));
                                            MutableComponent toggle = ComponentUtils.wrapInSquareBrackets(new TextComponent(String.valueOf(Option.getWhitelist()))).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.toggle.whitelist.tooltip"))));
                                            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.toggle.whitelist.set", toggle), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("blacklist")
                                .executes(commandContext -> {
                                    MutableComponent toggle = ComponentUtils.wrapInSquareBrackets(new TextComponent(String.valueOf(Option.getBlacklist()))).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.toggle.blacklist.tooltip"))));
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.toggle.blacklist.query", toggle), false);
                                    return 1;
                                })
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(commandContext -> {
                                            Option.setBlacklist(BoolArgumentType.getBool(commandContext, "value"));
                                            MutableComponent toggle = ComponentUtils.wrapInSquareBrackets(new TextComponent(String.valueOf(Option.getBlacklist()))).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.toggle.blacklist.tooltip"))));
                                            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.toggle.blacklist.set", toggle), false);
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }

}