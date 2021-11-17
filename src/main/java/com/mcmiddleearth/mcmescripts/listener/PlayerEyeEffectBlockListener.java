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

    /*@EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.getPlayer().equals(player)) {
Logger.getGlobal().info("Block move!");
            event.setCancelled(true);
        }
    }*/

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        if(event.getPlayer().equals(player)) {
Logger.getGlobal().info("Block jump 1!");
            event.setCancelled(true);
            //player.teleport(cage);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getPlayer().equals(player)) {
            event.setCancelled(true);
            /*for(BlockState state: blockChanges) {
                player.sendBlockChange(state.getLocation(),state.getBlockData());
            }*/
Logger.getGlobal().info("Block interact and block change send!");
        }
    }
}
