package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Random;

public class TeleportAction extends SelectingAction<Player> {

    private static final Random random = new Random();

    public TeleportAction(Location location, double spread, Selector<Player> selector) {
        super(selector, (player,context) -> {
            Location loc = location;
            if(loc==null) {
                loc = context.getLocation();
                if(loc==null) return;
            }
            if(spread == 0) {
                player.teleport(loc);
            } else {
                player.teleport(randomClose(loc, spread));
            }
            DebugManager.log(Modules.Action.execute(TeleportAction.class),"Teleport player: "+player.getName());
        });
        DebugManager.log(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector());
    }

    private static Location randomClose(Location location, double spread) {
        double span = 2*spread;
        Location loc = location.clone().add(random.nextDouble()*span-spread,0,random.nextDouble()*span-spread);
        while(!(loc.getBlock().isPassable() && loc.getBlock().getRelative(BlockFace.UP).isPassable())) {
            loc = loc.add(0,1,0);
        }
        return loc;
    }

}
