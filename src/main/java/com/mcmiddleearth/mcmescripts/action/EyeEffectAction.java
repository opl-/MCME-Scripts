package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.logging.Logger;

public class EyeEffectAction extends SelectingAction<Player> {

    public EyeEffectAction(Selector<Player> selector, int duration) {
        super(selector, (player, context) -> {
            DebugManager.verbose(Modules.Action.execute(EyeEffectAction.class),"Selector: "+selector.getSelector()
                                                        + " Player: "+player.getName());
            player.teleport(player.getLocation().toBlockLocation().add(new Vector(0.5,0,0.5)));
            Block block = player.getLocation().getWorld().getBlockAt(player.getLocation()).getRelative(BlockFace.UP);
            BlockState tempState = block.getState();
            tempState.setType(Material.NETHER_PORTAL);
            //block.setType(Material.NETHER_PORTAL,false);
            player.sendBlockChange(block.getLocation(),tempState.getBlockData());
            /*AttributeModifier modifier = new AttributeModifier("PortalEffect",-1, AttributeModifier.Operation.ADD_NUMBER);
            AttributeInstance attrib = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if(attrib != null) {
                //attrib.getModifiers().forEach(attrib::removeModifier);
                attrib.addModifier(modifier);
            }*/
            //Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_FLYING_SPEED)).addModifier(modifier);
            float walkSpeed = player.getWalkSpeed();
            float flySpeed = player.getFlySpeed();
//Logger.getGlobal().info("walk: "+walkSpeed+" fly: "+flySpeed);
            player.setFlySpeed(0);
            player.setWalkSpeed(0);
            new BukkitRunnable() {
                @Override
                public void run() {
                    //saveState.update(true, false);
                    player.sendBlockChange(block.getLocation(),block.getBlockData());
                    player.setFlySpeed(flySpeed);
                    player.setWalkSpeed(walkSpeed);
                    //Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).removeModifier(modifier);
                    //Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_FLYING_SPEED)).removeModifier(modifier);
                }
            }.runTaskLater(MCMEScripts.getInstance(),duration);
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector());
    }
}
