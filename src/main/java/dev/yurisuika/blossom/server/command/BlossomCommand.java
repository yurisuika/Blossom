package dev.yurisuika.blossom.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static dev.yurisuika.blossom.Blossom.*;
import static net.minecraft.server.command.CommandManager.*;

public class BlossomCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("blossom")
                .then(literal("config")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(literal("reload")
                                .executes(context -> {
                                    loadConfig();
                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.config.reload"), true);
                                    return 1;
                                })
                        )
                        .then(literal("reset")
                                .executes(context -> {
                                    config.rate = 5;
                                    config.count = new Count(2, 4);
                                    saveConfig();
                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.config.reset"), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("query")
                        .then(literal("rate")
                                .executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.query.rate", config.rate), false);
                                    return 1;
                                })
                        )
                        .then(literal("count")
                                .executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.query.count", config.count.min, config.count.max), false);
                                    return 1;
                                })
                        )
                )
                .then(literal("set")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(literal("rate")
                                .then(CommandManager.argument("value", IntegerArgumentType.integer(1))
                                        .executes(context -> {
                                            config.rate = IntegerArgumentType.getInteger(context, "value");
                                            saveConfig();
                                            context.getSource().sendFeedback(Text.translatable("commands.blossom.set.rate", config.rate), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("count")
                                .then(CommandManager.argument("min", IntegerArgumentType.integer(1, 64))
                                        .then(CommandManager.argument("max", IntegerArgumentType.integer(1, 64))
                                                .executes(context -> {
                                                    int min = Math.min(IntegerArgumentType.getInteger(context, "min"), IntegerArgumentType.getInteger(context, "max"));
                                                    int max = Math.max(IntegerArgumentType.getInteger(context, "min"), IntegerArgumentType.getInteger(context, "max"));
                                                    config.count = new Count(min, max);
                                                    saveConfig();
                                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.set.count", config.count.min, config.count.max), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }

}