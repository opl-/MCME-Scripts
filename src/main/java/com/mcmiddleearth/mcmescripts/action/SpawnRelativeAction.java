package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.List;

public class SpawnRelativeAction extends SelectingAction<McmeEntity> {

    public SpawnRelativeAction(Selector<McmeEntity> selector, List<VirtualEntityFactory> factories, int lifespan, boolean onGround) {
        super(selector, (entity,context) -> {
            DebugManager.verbose(Modules.Action.execute(SpawnRelativeAction.class),"Selected entity: "+entity.getName());
            factories.forEach(factory -> {
                if(factory.getLocation()!=null) {
                    Location location = entity.getLocation().clone().add(factory.getLocation().toVector());
                    factory.withLocation(findSafe(location,onGround));
                }
                if(factory.getGoalFactory()!=null) {
                    Location[] waypoints = factory.getGoalFactory().getCheckpoints();
                    if(waypoints!=null) {
                        for (int i = 0; i < waypoints.length; i++) {
                            waypoints[i] = findSafe(entity.getLocation().clone().add(waypoints[i].toVector()),onGround);
                        }
                        factory.getGoalFactory().withCheckpoints(waypoints);
                    }
                }
            });
            SpawnAction.spawnEntity(context,factories,lifespan);
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector());
    }

    private static Location findSafe(Location location, boolean onGround) {
        Block block = location.getBlock();
        while(!(block.isPassable() && block.getRelative(BlockFace.UP).isPassable()) && block.getY()-location.getBlockY()<10) {
            block = block.getRelative(BlockFace.UP);
        }
        if(onGround) {
            while ((block.isPassable() && block.getRelative(BlockFace.DOWN).isPassable()) && block.getY() - location.getBlockY() > -10) {
                block = block.getRelative(BlockFace.DOWN);
            }
        }
        return block.getLocation().add(new Vector(0.5,0,0.5));
    }
}
