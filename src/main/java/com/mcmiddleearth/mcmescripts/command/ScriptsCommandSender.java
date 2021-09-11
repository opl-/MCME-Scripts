package com.mcmiddleearth.mcmescripts.command;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.entities.api.McmeEntityType;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScriptsCommandSender implements McmeCommandSender {

    CommandSender sender;

    public ScriptsCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(BaseComponent[] baseComponents) {
        sender.sendMessage(new ComponentBuilder("[Scripts] ").color(ChatColor.AQUA).append(baseComponents[0]).create());
    }

    public CommandSender getCommandSender() {
        return sender;
    }

}
