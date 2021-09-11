package com.mcmiddleearth.mcmescripts.command;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.entities.Permission;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class ScriptsCommandHandler extends AbstractCommandHandler implements TabExecutor {

    public ScriptsCommandHandler(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender().hasPermission(Permission.ADMIN.getNode()))
            .then(HelpfulLiteralBuilder.literal("debug")
                .then(HelpfulRequiredArgumentBuilder.argument("module",word())
                    .executes(context -> {
                        DebugManager.toggleDebug(context.getArgument("module",String.class));
                        return 0; }))
                    .then(HelpfulRequiredArgumentBuilder.argument("enable",word())
                        .executes(context -> {
                            DebugManager.debug(context.getArgument("module",String.class),
                                    context.getArgument("enable",String.class));
                            return 0; })))
            .then(HelpfulLiteralBuilder.literal("list")
                .then(HelpfulRequiredArgumentBuilder.argument("module",word())
                    .executes(context -> {
                        DebugManager.list(context.getArgument("module",String.class));
                        return 0; })))
            .then(HelpfulLiteralBuilder.literal("reload")
                .executes(context -> {
                    MCMEScripts.getInstance().disableScripts();
                    MCMEScripts.getInstance().reloadConfig();
                    MCMEScripts.getInstance().enableScripts();
                    return 0; }))
            .then(HelpfulLiteralBuilder.literal("disable")
                .executes(context -> {
                    MCMEScripts.getInstance().disableScripts();
                    return 0; }))
            .then(HelpfulLiteralBuilder.literal("enable")
                .executes(context -> {
                    MCMEScripts.getInstance().enableScripts();
                    return 0; }));
        return commandNodeBuilder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ScriptsCommandSender wrappedSender = new ScriptsCommandSender(sender);
        execute(wrappedSender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        TabCompleteRequest request = new SimpleTabCompleteRequest(new ScriptsCommandSender(sender),
                                                                  String.format("/%s %s", alias, Joiner.on(' ').join(args)).trim());
        onTabComplete(request);
        return request.getSuggestions();
    }

}
