package com.mcmiddleearth.mcmescripts.command;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.Permission;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.drive.DriveUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class ScriptsCommandHandler extends AbstractCommandHandler implements TabExecutor {

    public ScriptsCommandHandler(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender().hasPermission(Permission.USER.getNode()))
            .then(HelpfulLiteralBuilder.literal("debug")
                .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender().hasPermission(Permission.ADMIN.getNode()))
                .then(HelpfulRequiredArgumentBuilder.argument("module",word())
                    .executes(context -> {
                        DebugManager.cycleDebug(context.getArgument("module",String.class));
                        return 0; })
                    .then(HelpfulRequiredArgumentBuilder.argument("level",word())
                        .executes(context -> {
                            DebugManager.debug(context.getArgument("module",String.class),
                                    context.getArgument("level",String.class));
                            return 0; }))))
            .then(HelpfulLiteralBuilder.literal("list")
                .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender().hasPermission(Permission.ADMIN.getNode()))
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
                .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender().hasPermission(Permission.ADMIN.getNode()))
                .executes(context -> {
                    MCMEScripts.getInstance().disableScripts();
                    return 0; }))
            .then(HelpfulLiteralBuilder.literal("enable")
                .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender().hasPermission(Permission.ADMIN.getNode()))
                .executes(context -> {
                    MCMEScripts.getInstance().enableScripts();
                    return 0; }))
            .then(HelpfulLiteralBuilder.literal("import")
                .then(HelpfulRequiredArgumentBuilder.argument("type",word())
                    .suggests((commandContext, suggestionsBuilder) -> suggestionsBuilder.suggest("animations").suggest("entities").suggest("scripts").buildFuture())
                    .then(HelpfulRequiredArgumentBuilder.argument("file",word())
                        .executes(context -> {
                            try {
                                DriveUtil.importFile(context.getSource(),
                                        context.getArgument("type",String.class),
                                        context.getArgument("file",String.class));
                            } catch (IOException | GeneralSecurityException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }))))
            .then(HelpfulLiteralBuilder.literal("export")
                .then(HelpfulRequiredArgumentBuilder.argument("type",word())
                    .suggests((commandContext, suggestionsBuilder) -> suggestionsBuilder.suggest("animations").suggest("entities").suggest("scripts").buildFuture())
                    .then(HelpfulRequiredArgumentBuilder.argument("file",word())
                        .executes(context -> {
                            try {
                                DriveUtil.exportFile(context.getSource(),
                                                     context.getArgument("type",String.class),
                                                     context.getArgument("file",String.class));
                            } catch (IOException | GeneralSecurityException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }))));
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
