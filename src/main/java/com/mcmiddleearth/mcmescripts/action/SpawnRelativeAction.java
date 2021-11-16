package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.List;

public class SpawnRelativeAction extends SelectingAction<McmeEntity> {

    public SpawnRelativeAction(Selector<McmeEntity> selector, List<VirtualEntityFactory> factories, int lifespan, boolean onGround,
                               McmeEntitySelector goalTargetSelector, VirtualEntityGoalFactory goalFactory,
                               Location location, Location[] waypoints, boolean serverSide, int quantity) {
        super(selector, (entity,context) -> {
            DebugManager.verbose(Modules.Action.execute(SpawnRelativeAction.class),"Selected entity: "+entity.getName());
            McmeEntity tempGoalTarget = null;
            List<McmeEntity> goalTargets = goalTargetSelector.select(context);
            if(!goalTargets.isEmpty()) {
                tempGoalTarget = goalTargets.get(0);
//DebugManager.log(Modules.Action.execute(SetGoalAction.class),"GoalTarget entity: "+goalTargets.get(0));
                //goalFactory.withTargetEntity(goalTargets.get(0));
            }
            McmeEntity goalTarget = tempGoalTarget;
            /*if(tempFactory==null) {
                tempfactory = entity.
            }
            if (goalFactory !=null) {
            }*/
            int edge = (int) Math.sqrt(quantity);
            for(int j = 0; j< quantity; j++) {
                Location finalLocation;
                if(location!=null) {
                    finalLocation =location.clone().add(j % edge,0, j/edge);
                } else {
                    finalLocation = null;
                }
                factories.forEach(factory -> {
                    if (finalLocation != null) {
                        Location loc = entity.getLocation().clone().add(rotate(finalLocation.toVector(), entity));
                        factory.withLocation(findSafe(loc, onGround));
                    }
                    VirtualEntityGoalFactory tempGoalFactory = goalFactory;
                    if (tempGoalFactory != null) {
                        factory.withGoalFactory(tempGoalFactory);
                    } else {
                        tempGoalFactory = factory.getGoalFactory();
                    }
                    if (tempGoalFactory != null && goalTarget != null) {
                        tempGoalFactory.withTargetEntity(goalTarget);
                    }
                    if (tempGoalFactory != null && waypoints != null) {
                        Location[] checkpoints = new Location[waypoints.length];
                        for (int i = 0; i < waypoints.length; i++) {
                            checkpoints[i] = findSafe(entity.getLocation().clone()
                                    .add(rotate(waypoints[i].toVector(), entity)), onGround);
                        }
                        tempGoalFactory.withCheckpoints(checkpoints);
                        //Arrays.stream(factory.getGoalFactory().getCheckpoints()).forEach(check -> Logger.getGlobal().info("+ "+check));
                    }
                });
                SpawnAction.spawnEntity(context, factories, lifespan, serverSide);
            }
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

    private static Vector rotate(Vector vector, McmeEntity entity) {
        float yaw = entity.getYaw();
        while(yaw < -180) yaw += 360; while(yaw > 180) yaw -= 360;
        if(yaw < -135 || yaw > 135) {
//Logger.getGlobal().info("North: "+yaw+" "+new Vector(-vector.getX(),vector.getY(),-vector.getZ()));
            return new Vector(-vector.getX(),vector.getY(),-vector.getZ());
        } else if(yaw < -45) {
//Logger.getGlobal().info("East: "+yaw+" "+new Vector(vector.getZ(),vector.getY(),-vector.getX()));
            return new Vector(vector.getZ(),vector.getY(),-vector.getX());
        } else if(yaw > 45) {
//Logger.getGlobal().info("West: "+yaw+" "+new Vector(-vector.getZ(),vector.getY(),vector.getX()));
            return new Vector(-vector.getZ(),vector.getY(),vector.getX());
        } else {
//Logger.getGlobal().info("South: "+yaw+" "+vector);
            return vector;
        }
    }
}
