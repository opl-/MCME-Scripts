package com.mcmiddleearth.mcmescripts.selector;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.McmeEntityType;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class McmeEntitySelector extends EntitySelector<McmeEntity> {

    public McmeEntitySelector(String selector) throws IndexOutOfBoundsException {
        super(selector);
        DebugManager.log(Modules.Selector.create(this.getClass()),
                "Selector: "+selector);
    }

    @Override
    public List<McmeEntity> select(TriggerContext context) {
        List<McmeEntity> result = new ArrayList<>();
        switch(selectorType) {
            case ALL_ENTITIES:
                result.addAll(selectVirtualEntities(context));
                selectPlayer(context).stream().map(EntitiesPlugin.getEntityServer().getPlayerProvider()::getOrCreateMcmePlayer)
                        .forEach(result::add);
                Location loc = context.getLocation();
                if (loc!=null && (minDistanceSquared > 0 || maxDistanceSquared < Double.MAX_VALUE)) {
                    loc = new Location(loc.getWorld(),getAbsolute(loc.getX(),xRelative,x),
                            getAbsolute(loc.getY(),yRelative,y),
                            getAbsolute(loc.getZ(),zRelative,z));
                    Location finalLoc = loc;
                    List<EntitySelectorElement<McmeEntity>> sort = result.stream().map(EntitySelectorElement<McmeEntity>::new)
                            .filter(element -> {
                                element.setValue(element.getContent().getLocation().distanceSquared(finalLoc));
                                return minDistanceSquared <= element.getValue()
                                        && element.getValue() <= maxDistanceSquared;
                            }).collect(Collectors.toList());
                    result = sort.stream().sorted((one, two) -> (Double.compare(two.getValue(), one.getValue()))).limit(limit)
                            .map(EntitySelectorElement::getContent).collect(Collectors.toList());
                    DebugManager.log(Modules.Selector.select(this.getClass()),
                            "Selector: "+getSelector()
                                    +" Selected: "+(result.size()>0?result.get(0).getName():null));
                }
                break;
            case NEAREST_PLAYER:
            case ALL_PLAYERS:
            case RANDOM_PLAYER:
//Logger.getGlobal().info("Selector type: "+selectorType);
                selectPlayer(context).stream().map(EntitiesPlugin.getEntityServer().getPlayerProvider()::getOrCreateMcmePlayer)
                        .forEach(result::add);
                break;
            case VIRTUAL_ENTITIES:
                result.addAll(selectVirtualEntities(context));
                break;
            case TRIGGER_ENTITY:
                if(context.getEntity()!=null) result.add(context.getEntity());
                if(context.getPlayer()!=null) result.add(EntitiesPlugin.getEntityServer().getPlayerProvider().getOrCreateMcmePlayer(context.getPlayer()));
                break;
        }
        return result;
    }
}
