package com.mcmiddleearth.mcmescripts.command;

import com.mcmiddleearth.command.McmeCommandSender;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ScriptsCommandSender implements McmeCommandSender {

    CommandSender sender;

    public ScriptsCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(BaseComponent[] baseComponents) {
        String legacy = Arrays.stream(baseComponents).map(BaseComponent::toLegacyText).collect(Collectors.joining());
        sender.sendMessage(Identity.nil(), Component.text("[Scripts] ").color(NamedTextColor.AQUA)
                                          .append(Component.text(legacy)),
                           MessageType.SYSTEM);
    }

    public CommandSender getCommandSender() {
        return sender;
    }

}
