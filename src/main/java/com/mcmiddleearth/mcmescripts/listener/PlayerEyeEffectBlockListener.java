package com.mcmiddleearth.mcmescripts.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;
import java.util.logging.Logger;

public class PlayerEyeEffectBlockListener implements Listener {

    Player player;
    Location cage;
    Set<BlockState> blockChanges;

    public PlayerEyeEffectBlockListener(Player player, Set<BlockState> blockChanges) {
        this.player = player;
        this.cage = player.getLocation();
        this.blockChanges = blockChanges;
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        if(event.getPlayer().equals(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getPlayer().equals(player)) {
            event.setCancelled(true);
        }
    }
}
