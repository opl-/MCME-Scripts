package com.mcmiddleearth.mcmescripts.command;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.Permission;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.drive.DriveUtil;
import com.mcmiddleearth.mcmescripts.listener.WandItemListener;
import com.mcmiddleearth.mcmescripts.script.Script;
import com.mcmiddleearth.mcmescripts.trigger.ExternalTrigger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
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
                .then(HelpfulLiteralBuilder.literal("modules")
                    .then(HelpfulRequiredArgumentBuilder.argument("module",word())
                        .executes(context -> {
                            DebugManager.cycleDebug(context.getArgument("module",String.class));
                            return 0; })
                        .then(HelpfulRequiredArgumentBuilder.argument("level",word())
                            .executes(context -> {
                                DebugManager.debug(context.getArgument("module",String.class),
                                        context.getArgument("level",String.class));
                                return 0; }))))
                .then(HelpfulLiteralBuilder.literal("filter")
                    .then(HelpfulRequiredArgumentBuilder.argument("player", word())
                        .then(HelpfulRequiredArgumentBuilder.argument("script", word())
                                .executes(context -> {
                                    setFilter(context.getSource(), context.getArgument("player",String.class),
                                                                context.getArgument("script",String.class));
                                    return 0; })))))
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
            .then(HelpfulLiteralBuilder.literal("wand")
                .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender() instanceof Player)
                .then(HelpfulRequiredArgumentBuilder.argument("script",word())
                    .suggests(((commandContext, suggestionsBuilder) -> {
                        MCMEScripts.getScriptManager().getScripts().values().stream().filter(Script::isActive)
                                .forEach(script -> suggestionsBuilder.suggest(script.getName()));
                        return suggestionsBuilder.buildFuture();}))
                    .then(HelpfulRequiredArgumentBuilder.argument("name",word())
                        .suggests((((commandContext, suggestionsBuilder) -> {
                            Script script = MCMEScripts.getScriptManager().getScript(commandContext.getArgument("script",String.class));
                            if(script!=null) {
                                script.getTriggers().stream().filter(trigger->trigger instanceof ExternalTrigger)
                                        .forEach(trigger -> suggestionsBuilder.suggest(trigger.getName()));
                            }
                            return suggestionsBuilder.buildFuture();})))
                        .executes(context -> {
                            Player player = (Player) ((ScriptsCommandSender)context.getSource()).getCommandSender();
                            WandItemListener.addScript(player.getInventory().getItemInMainHand(),
                                    context.getArgument("script", String.class) + " "
                                            + context.getArgument("name", String.class));
                            return 0;
                        })
                        .then(HelpfulRequiredArgumentBuilder.argument("arguments",greedyString())
                            .executes(context -> {
                                Player player = (Player) ((ScriptsCommandSender)context.getSource()).getCommandSender();
                                WandItemListener.addScript(player.getInventory().getItemInMainHand(),
                                        context.getArgument("script", String.class) + " "
                                               + context.getArgument("name", String.class) + " "
                                               + context.getArgument("arguments", String.class));
                                return 0;
                            })))))
            .then(HelpfulLiteralBuilder.literal("external")
                    .then(HelpfulRequiredArgumentBuilder.argument("script",word())
                            .suggests(((commandContext, suggestionsBuilder) -> {
                                MCMEScripts.getScriptManager().getScripts().values().stream().filter(Script::isActive)
                                        .forEach(script -> suggestionsBuilder.suggest(script.getName()));
                                return suggestionsBuilder.buildFuture();}))
                            .then(HelpfulRequiredArgumentBuilder.argument("name",word())
                                    .suggests((((commandContext, suggestionsBuilder) -> {
                                        Script script = MCMEScripts.getScriptManager().getScript(commandContext.getArgument("script",String.class));
                                        if(script!=null) {
                                            script.getTriggers().stream().filter(trigger->trigger instanceof ExternalTrigger)
                                                    .forEach(trigger -> suggestionsBuilder.suggest(trigger.getName()));
                                        }
                                        return suggestionsBuilder.buildFuture();})))
                                    .executes(context -> {
                                        MCMEScripts.getExternalTriggerManager().call(context.getArgument("script",String.class),
                                                context.getArgument("name",String.class),
                                                new String[0]);
                                        return 0;
                                    })
                                    .then(HelpfulRequiredArgumentBuilder.argument("arguments",greedyString())
                                            .executes(context -> {
                                                MCMEScripts.getExternalTriggerManager().call(context.getArgument("script",String.class),
                                                        context.getArgument("name",String.class),
                                                        context.getArgument("arguments",String.class).split(" "));
                                                return 0;
                                            })))))
            .then(HelpfulLiteralBuilder.literal("import")
                .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender().hasPermission(Permission.ADMIN.getNode()))
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
                .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender().hasPermission(Permission.ADMIN.getNode()))
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
                        }))
                .then(HelpfulLiteralBuilder.literal("debug"))
                    .executes(context -> {
                        try {
                            DriveUtil.exportFile(context.getSource(),"scripts","debug.txt");
                        } catch (IOException | GeneralSecurityException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    })));
        return commandNodeBuilder;
    }

    private void setFilter(McmeCommandSender sender, String playerName, String script) {
        if("console".equalsIgnoreCase(playerName)) {
            DebugManager.setConsoleDebugScript(script);
        } else {
            Player player = Bukkit.getPlayer(playerName);
            if(player!=null) {
                DebugManager.setPlayerDebugScript(player, script);
            }
        }
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
