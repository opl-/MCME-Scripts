package com.mcmiddleearth.mcmescripts.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BlockPlayerMovementListener implements Listener {

    Player player;

    public BlockPlayerMovementListener(Player player) {
        this.player = player;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.getPlayer().equals(player)) event.setCancelled(true);
    }
}
