package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.listener.PlayerEyeEffectBlockListener;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class EyeEffectAction extends SelectingAction<Player> {

    public EyeEffectAction(Selector<Player> selector, int duration) {
        super(selector, (player, context) -> {
            DebugManager.verbose(Modules.Action.execute(EyeEffectAction.class),"Selector: "+selector.getSelector()
                                                        + " Player: "+player.getName());
            player.teleport(player.getLocation().toBlockLocation().add(new Vector(0.5,0,0.5)));
            Block block = player.getLocation().getWorld().getBlockAt(player.getLocation()).getRelative(BlockFace.UP);
            Block[] adjacent = new Block[4];
            adjacent[0] = block.getRelative(BlockFace.NORTH);
            adjacent[1] = block.getRelative(BlockFace.SOUTH);
            adjacent[2] = block.getRelative(BlockFace.WEST);
            adjacent[3] = block.getRelative(BlockFace.EAST);

            Set<BlockState> blockChanges = new HashSet<>();
            blockChanges.add(block.getState());
            BlockState tempState = block.getState();
            tempState.setType(Material.NETHER_PORTAL);
            //block.setType(Material.NETHER_PORTAL,false);
            block.setBlockData(tempState.getBlockData(),false);
            //player.sendBlockChange(block.getLocation(),tempState.getBlockData());
            for(Block ad: adjacent) {
                if(ad.isPassable()) {
                    blockChanges.add(ad.getState());
                    tempState = ad.getState();
                    tempState.setType(Material.BARRIER);
                    ad.setBlockData(tempState.getBlockData(),false);
                    //player.sendBlockChange(ad.getLocation(),tempState.getBlockData());
                }
            }
            /*AttributeModifier modifier = new AttributeModifier("PortalEffect",-1, AttributeModifier.Operation.ADD_NUMBER);
            AttributeInstance attrib = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if(attrib != null) {
                //attrib.getModifiers().forEach(attrib::removeModifier);
                attrib.addModifier(modifier);
            }*/
            //Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_FLYING_SPEED)).addModifier(modifier);
            float walkSpeed = player.getWalkSpeed();
            float flySpeed = player.getFlySpeed();
            Listener blockJump = new PlayerEyeEffectBlockListener(player,blockChanges);
            Bukkit.getPluginManager().registerEvents(blockJump,MCMEScripts.getInstance());
//Logger.getGlobal().info("walk: "+walkSpeed+" fly: "+flySpeed);
            player.setFlySpeed(0);
            player.setWalkSpeed(0);
            new BukkitRunnable() {
                @Override
                public void run() {
                    //saveState.update(true, false);
                    /*player.sendBlockChange(block.getLocation(),block.getBlockData());
                    for(Block ad: adjacent) {
                        player.sendBlockChange(ad.getLocation(),ad.getBlockData());
                    }*/
                    for(BlockState state: blockChanges) {
                        state.update(true,false);
                    }
                    player.setFlySpeed(flySpeed);
                    player.setWalkSpeed(walkSpeed);
                    HandlerList.unregisterAll(blockJump);
                    //Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).removeModifier(modifier);
                    //Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_FLYING_SPEED)).removeModifier(modifier);
                }
            }.runTaskLater(MCMEScripts.getInstance(),duration);
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector());
    }
}
