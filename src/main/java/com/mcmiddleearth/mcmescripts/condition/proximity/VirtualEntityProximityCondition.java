package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.condition.Criterion;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.function.Function;
import java.util.logging.Logger;

@SuppressWarnings("rawtypes")
public class VirtualEntityProximityCondition extends Condition {

    private final Criterion test;
    private final String entityName;
    private final Selector selector;

    public VirtualEntityProximityCondition(String entityName, Selector selector, Criterion test) {
        this.selector = selector;
        this.entityName = entityName;
        this.test = test;
        DebugManager.info(Modules.Condition.create(this.getClass()),
                "Selector: "+selector.getSelector()+" Entity: "+entityName);
    }

    @Override
    public boolean test(TriggerContext context) {
        McmeEntity entity = EntitiesPlugin.getEntityServer().getEntity(entityName);
        if(entity instanceof VirtualEntity) {
            context = new TriggerContext(context).withEntity((VirtualEntity) entity);
        }
        DebugManager.verbose(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector()+" Entity: "+(entity!=null?entity.getName():"null"));
        return test.apply(selector.select(context).size());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+" Entity: "+entityName+" Selector: "+selector.getSelector();
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Criterion: "+test.getComparator()+test.getLimit());
    }

}
