package com.mcmiddleearth.mcmescripts.selector;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PlayerSelector extends EntitySelector<Player>{

    public PlayerSelector(String selector) throws IndexOutOfBoundsException {
        super(selector);
        DebugManager.log(Modules.Selector.create(this.getClass()),
                "Selector: "+selector);
    }

    @Override
    public List<Player> select(TriggerContext context) {
        Location loc = context.getLocation();
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        switch(selectorType) {
            case TRIGGER_ENTITY:
                if(context.getPlayer()!=null)
                    players.add(context.getPlayer());
                DebugManager.log(Modules.Selector.select(this.getClass()),
                        "Selector: "+getSelector()+" Selected: "+(context.getPlayer()!=null?context.getPlayer().getName():null));
                return players;
            case ALL_PLAYERS:
            case ALL_ENTITIES:
            case RANDOM_PLAYER:
//Logger.getGlobal().info("Location rel: "+loc.toString());
                if (hasAreaLimit() && loc != null) {
    //Logger.getGlobal().info("Location: "+loc.toString() + " Players: "+players.size());
                    loc = new Location(loc.getWorld(), getAbsolute(loc.getX(), xRelative, x),
                            getAbsolute(loc.getY(), yRelative, y),
                            getAbsolute(loc.getZ(), zRelative, z));
                    double xMin = (dx < 0 ? Integer.MIN_VALUE : loc.getX() - dx);
                    double xMax = (dx < 0 ? Integer.MAX_VALUE : loc.getX() + dx);
                    double yMin = (dy < 0 ? Integer.MIN_VALUE : loc.getY() - dy);
                    double yMax = (dy < 0 ? Integer.MAX_VALUE : loc.getY() + dy);
                    double zMin = (dz < 0 ? Integer.MIN_VALUE : loc.getZ() - dz);
                    double zMax = (dz < 0 ? Integer.MAX_VALUE : loc.getZ() + dz);
                    players = players.stream().filter(player -> xMin <= player.getLocation().getX()
                            && player.getLocation().getX() < xMax
                            && yMin <= player.getLocation().getY()
                            && player.getLocation().getY() < yMax
                            && zMin <= player.getLocation().getZ()
                            && player.getLocation().getZ() < zMax)
                            .collect(Collectors.toList());
                }
//Logger.getGlobal().info("Area selection: "+players.size());
                if (name != null) {
                    if(name.endsWith("*")) {
                        players = players.stream().filter(player -> player.getName()
                                                          .startsWith(name.substring(0,name.length()-1)) != excludeName)
                                .collect(Collectors.toList());
                    } else {
                        players = players.stream().filter(player -> player.getName().equals(name) != excludeName)
                                .collect(Collectors.toList());
                    }
                }
//Logger.getGlobal().info("Name selection: "+players.size());
                if (gameMode != null) {
                    players = players.stream().filter(player -> player.getGameMode().equals(gameMode) != excludeGameMode)
                            .collect(Collectors.toList());
                }
//Logger.getGlobal().info("GM selection: "+players.size());
                if (minPitch > -90 || maxPitch < 90) {
                    players = players.stream().filter(player -> minPitch <= player.getLocation().getPitch()
                            && player.getLocation().getPitch() < maxPitch)
                            .collect(Collectors.toList());
                }
                if (minYaw > -180 || maxYaw < 180) {
                    players = players.stream().filter(player -> minYaw <= player.getLocation().getPitch()
                            && player.getLocation().getPitch() < maxYaw).collect(Collectors.toList());
                }
                List<EntitySelectorElement<Player>> sort = players.stream().map(EntitySelectorElement<Player>::new)
                                                                  .collect(Collectors.toList());
//DebugManager.log(Modules.Selector.select(this.getClass()),"Angular selection: "+sort.size());
//DebugManager.log(Modules.Selector.select(this.getClass()),"Distance: "+minDistanceSquared+" "+maxDistanceSquared);
                if (loc!=null && (minDistanceSquared > 0 || maxDistanceSquared < Double.MAX_VALUE)) {
                    //double minDistanceSquared = minDistance * minDistance;
                    //double maxDistanceSquared = maxDistance * maxDistance;
                    Location finalLoc = loc;
                    sort = sort.stream()
                            .filter(element -> {
                                element.setValue(element.getContent().getLocation().distanceSquared(finalLoc));
//DebugManager.log(Modules.Selector.select(this.getClass()),"Element distance: " + element.getValue());
                                return minDistanceSquared <= element.getValue()
                                        && element.getValue() <= maxDistanceSquared;
                            }).collect(Collectors.toList());
                }
//DebugManager.log(Modules.Selector.select(this.getClass()),"Distance selection: "+sort.size());
                List<Player> result = Collections.emptyList();
                switch (selectorType) {
                    case NEAREST_PLAYER:
                        result = sort.stream().sorted((one, two) -> (Double.compare(two.getValue(), one.getValue()))).limit(1)
                                .map(EntitySelectorElement::getContent).collect(Collectors.toList());
                        DebugManager.log(Modules.Selector.select(this.getClass()),
                                "Selector: "+getSelector()
                                      +" Selected: "+(result.size()>0?result.get(0).getName():null));
                        return result;
                    case RANDOM_PLAYER:
                        result = Collections.singletonList(sort.get(new Random().nextInt(sort.size())).getContent());
                        break;
                    case ALL_PLAYERS:
                    case ALL_ENTITIES:
                        result = sort.stream().sorted((one, two) -> (Double.compare(two.getValue(), one.getValue()))).limit(limit)
                                .map(EntitySelectorElement::getContent).collect(Collectors.toList());
                        break;
                }
//DebugManager.log(Modules.Selector.select(this.getClass()),"Result: "+result.size());
                DebugManager.log(Modules.Selector.select(this.getClass()),
                        "Selector: "+getSelector()
                              +" Selected: "+(result.size()>0?result.get(0).getName():null)+" and "+result.size()+" more");
                return result;
        }
        DebugManager.log(Modules.Selector.select(this.getClass()),
                "Selector: "+getSelector()
                       +" Selected: none");
        return Collections.emptyList();
    }

}
