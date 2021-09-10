package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class VirtualEntityProximityCondition implements Condition {

    private final Function<Integer,Boolean> test;
    private final String entityName;
    private final Selector selector;

    public VirtualEntityProximityCondition(String entityName, Selector selector, Function<Integer,Boolean> test) {
        this.selector = selector;
        this.entityName = entityName;
        this.test = test;
    }

    @Override
    public boolean test(TriggerContext context) {
        McmeEntity entity = EntitiesPlugin.getEntityServer().getEntity(entityName);
        if(entity instanceof VirtualEntity) {
            context.withEntity((VirtualEntity) entity);
        }
        return test.apply(selector.select(context).size());
    }
}
