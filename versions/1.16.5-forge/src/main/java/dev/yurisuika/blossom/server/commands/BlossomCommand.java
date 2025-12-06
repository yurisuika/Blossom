package dev.yurisuika.blossom.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import dev.yurisuika.blossom.config.Config;
import dev.yurisuika.blossom.config.Options;
import dev.yurisuika.blossom.util.Configure;
import dev.yurisuika.blossom.util.Validate;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class BlossomCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandSelection selection) {
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
                                    Configure.setFilter(new Options().getFilter());
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.config.reset"), true);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("filter")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("temperature")
                                .executes(commandContext -> {
                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.temperature.min", Configure.getTemperatureMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.temperature.min.tooltip"))));
                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.temperature.max", Configure.getTemperatureMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.temperature.max.tooltip"))));
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.temperature.query", min, max), false);
                                    return 1;
                                })
                                .then(Commands.argument("min", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                        .then(Commands.argument("max", FloatArgumentType.floatArg(-2.0F, 2.0F))
                                                .executes(commandContext -> {
                                                    Configure.setTemperatureMin(Math.min(FloatArgumentType.getFloat(commandContext, "min"), FloatArgumentType.getFloat(commandContext, "max")));
                                                    Configure.setTemperatureMax(Math.max(FloatArgumentType.getFloat(commandContext, "max"), FloatArgumentType.getFloat(commandContext, "min")));
                                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.temperature.min", Configure.getTemperatureMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.temperature.min.tooltip"))));
                                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.temperature.max", Configure.getTemperatureMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.temperature.max.tooltip"))));
                                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("downfall")
                                .executes(commandContext -> {
                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.downfall.min", Configure.getDownfallMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.downfall.min.tooltip"))));
                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.downfall.max", Configure.getDownfallMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.downfall.max.tooltip"))));
                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.temperature.query", min, max), false);
                                    return 1;
                                })
                                .then(Commands.argument("min", FloatArgumentType.floatArg(0.0F, 1.0F))
                                        .then(Commands.argument("max", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                .executes(commandContext -> {
                                                    Configure.setDownfallMin(Math.min(FloatArgumentType.getFloat(commandContext, "min"), FloatArgumentType.getFloat(commandContext, "max")));
                                                    Configure.setDownfallMax(Math.max(FloatArgumentType.getFloat(commandContext, "max"), FloatArgumentType.getFloat(commandContext, "min")));
                                                    MutableComponent min = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.downfall.min", Configure.getDownfallMin())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.downfall.min.tooltip"))));
                                                    MutableComponent max = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("commands.blossom.filter.downfall.max", Configure.getDownfallMax())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("commands.blossom.filter.downfall.max.tooltip"))));
                                                    commandContext.getSource().sendSuccess(new TranslatableComponent("commands.blossom.filter.temperature.set", min, max), false);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }

}