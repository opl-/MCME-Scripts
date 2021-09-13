package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
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
        DebugManager.log(Modules.Condition.create(this.getClass()),
                "Selector: "+selector.getSelector()+" Entity: "+entityName);
    }

    @Override
    public boolean test(TriggerContext context) {
        McmeEntity entity = EntitiesPlugin.getEntityServer().getEntity(entityName);
        if(entity instanceof VirtualEntity) {
            context = new TriggerContext(context).withEntity((VirtualEntity) entity);
        }
        DebugManager.log(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector()+" Entity: "+(entity!=null?entity.getName():"null"));
        return test.apply(selector.select(context).size());
    }
}
