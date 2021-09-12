package com.mcmiddleearth.mcmescripts.selector;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VirtualEntitySelector extends EntitySelector<VirtualEntity> {

    public VirtualEntitySelector(String selector) throws IndexOutOfBoundsException {
        super(selector);
        DebugManager.log(Modules.Selector.create(this.getClass()),
                "Selector: "+selector);
    }

    @Override
    public List<VirtualEntity> select(TriggerContext context) {
        Location loc = context.getLocation().clone();
        List<VirtualEntity> entities = new ArrayList<>();
        List<EntitySelectorElement<VirtualEntity>> sort = new ArrayList<>();
        switch(selectorType) {
            case TRIGGER_ENTITY:
                if(context.getEntity()!=null)
                    entities.add(context.getEntity());
                DebugManager.log(Modules.Selector.select(this.getClass()),
                        "Selector: "+getSelector()+" Selected: "+(context.getEntity()!=null?context.getEntity().getName():null));
                return entities;
            case VIRTUAL_ENTITIES:
            case ALL_ENTITIES:
                loc = new Location(loc.getWorld(),getAbsolute(loc.getX(),xRelative,x),
                                                  getAbsolute(loc.getY(),yRelative,y),
                                                  getAbsolute(loc.getZ(),zRelative,z));
                if(hasAreaLimit()) {
                    entities.addAll(EntitiesPlugin.getEntityServer().getEntitiesAt(loc,
                                                                        (dx<0?Integer.MAX_VALUE:(int)dx),
                                                                        (dy<0?Integer.MAX_VALUE:(int)dy),
                                                                        (dz<0?Integer.MAX_VALUE:(int)dz))
                                  .stream().filter(entity -> entity instanceof VirtualEntity)
                                           .map(entity -> (VirtualEntity)entity).collect(Collectors.toSet()));
                } else {
                    entities.addAll(EntitiesPlugin.getEntityServer().getEntities(VirtualEntity.class)
                            .stream().map(entity -> (VirtualEntity)entity).collect(Collectors.toSet()));
                }
                if(entityType != null) {
                    entities = entities.stream().filter(entity -> entity.getType().equals(entityType) != excludeType)
                                            .collect(Collectors.toList());
                }
                if(name!=null) {
                    entities = entities.stream().filter(entity -> entity.getName().equals(name) != excludeName)
                                            .collect(Collectors.toList());
                }
                if(minPitch>-90 || maxPitch < 90) {
                    entities = entities.stream().filter(entity -> minPitch <= entity.getPitch() && entity.getPitch() < maxPitch)
                                            .collect(Collectors.toList());
                }
                if(minYaw>-180 || maxYaw < 180) {
                    sort = entities.stream().filter(entity -> minYaw <= entity.getYaw() && entity.getYaw() < maxYaw)
                            .map(EntitySelectorElement<VirtualEntity>::new).collect(Collectors.toList());
                }
                if(minDistance>0 || maxDistance < Double.MAX_VALUE) {
                    double minDistanceSquared = minDistance*minDistance;
                    double maxDistanceSquared = maxDistance*maxDistance;
                    Location finalLoc = loc;
                    sort = sort.stream()
                                   .filter(element -> {
                                       element.setValue(element.getContent().getLocation().distanceSquared(finalLoc));
                                       return minDistanceSquared <= element.getValue()
                                           && element.getValue() <= maxDistanceSquared;
                                   }).collect(Collectors.toList());
                }
                List<VirtualEntity> result = sort.stream().sorted((one,two) -> (Double.compare(two.getValue(), one.getValue()))).limit(limit)
                           .map(EntitySelectorElement::getContent).collect(Collectors.toList());
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
