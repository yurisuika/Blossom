package dev.yurisuika.blossom.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.yurisuika.blossom.Blossom;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;

public class BlossomCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("blossom")
                .then(literal("config")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(literal("reload")
                                .executes(context -> {
                                    Blossom.loadConfig();
                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.config.reload"), true);
                                    return 1;
                                })
                        )
                        .then(literal("reset")
                                .executes(context -> {
                                    Blossom.config.exposed = true;
                                    Blossom.config.rate = 5;
                                    Blossom.config.count = new Blossom.Count(2, 4);
                                    Blossom.saveConfig();
                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.config.reset"), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("query")
                        .then(literal("exposed")
                                .executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.query.exposed", Blossom.config.exposed), false);
                                    return 1;
                                })
                        )
                        .then(literal("rate")
                                .executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.query.rate", Blossom.config.rate), false);
                                    return 1;
                                })
                        )
                        .then(literal("count")
                                .executes(context -> {
                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.query.count", Blossom.config.count.min, Blossom.config.count.max), false);
                                    return 1;
                                })
                        )
                )
                .then(literal("set")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(literal("exposed")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            Blossom.config.exposed = BoolArgumentType.getBool(context, "value");
                                            Blossom.saveConfig();
                                            context.getSource().sendFeedback(Text.translatable("commands.blossom.set.exposed", Blossom.config.exposed), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal("rate")
                                .then(CommandManager.argument("value", IntegerArgumentType.integer(1))
                                        .executes(context -> {
                                            Blossom.config.rate = IntegerArgumentType.getInteger(context, "value");
                                            Blossom.saveConfig();
                                            context.getSource().sendFeedback(Text.translatable("commands.blossom.set.rate", Blossom.config.rate), true);
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
                                                    Blossom.config.count = new Blossom.Count(min, max);
                                                    Blossom.saveConfig();
                                                    context.getSource().sendFeedback(Text.translatable("commands.blossom.set.count", Blossom.config.count.min, Blossom.config.count.max), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }

}