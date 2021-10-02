package com.mcmiddleearth.mcmescripts.command;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ScriptsCommandSender implements McmeCommandSender {

    CommandSender sender;

    public ScriptsCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(BaseComponent[] baseComponents) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String legacy = Arrays.stream(baseComponents).map(comp -> comp.toLegacyText()).collect(Collectors.joining());
                sender.sendMessage(Identity.nil(), Component.text("[Scripts] ").color(NamedTextColor.AQUA)
                                                  .append(Component.text(legacy)),
                                   MessageType.SYSTEM);
            }
        }.runTask(MCMEScripts.getInstance());
    }

    public CommandSender getCommandSender() {
        return sender;
    }

}
