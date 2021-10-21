package com.mcmiddleearth.mcmescripts.listener;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Logger;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //TODO load PlayerScriptData in ScriptManager
        /*AttributeInstance attrib = event.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if(attrib != null) {
            Logger.getGlobal().info("Modiiers: "+attrib.getModifiers().size());
            attrib.getModifiers().forEach(attrib::removeModifier);
        }*/
        event.getPlayer().setWalkSpeed(0.2f);
        event.getPlayer().setFlySpeed(0.1f);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        //TODO remove PlayerScriptData from ScriptManager
    }
}
