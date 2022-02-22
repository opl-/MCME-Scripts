package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.condition.Criterion;
import com.mcmiddleearth.mcmescripts.condition.CriterionCondition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

@SuppressWarnings({"rawtypes","unchecked"})
public class VirtualEntityProximityCondition extends CriterionCondition {

    //private final Criterion test;
    private final String entityName;
    //private final Selector selector;

    public VirtualEntityProximityCondition(String entityName, Selector selector, Criterion test) {
        super(selector,test);
        //this.selector = selector;
        this.entityName = entityName;
        //this.test = test;
        DebugManager.info(Modules.Condition.create(this.getClass()),
                "Selector: "+selector.getSelector()+" Entity: "+entityName);
    }

    @Override
    public boolean test(TriggerContext context) {
        context.getDescriptor().addLine(this.getClass().getSimpleName()).indent();
        McmeEntity entity = EntitiesPlugin.getEntityServer().getEntity(entityName);
        if(entity instanceof VirtualEntity) {
            context.getDescriptor()
                    .addLine("Found center entity: "+entity.getName());
        } else {
            context.getDescriptor()
                    .addLine("Found center entity: --none--");
        }
        TriggerContext virtualEntityContext = new TriggerContext(context);
        if(entity instanceof VirtualEntity) {
            virtualEntityContext.withEntity(entity);
        }
        //context.setDescriptor(virtualEntityContext.getDescriptor());
        return super.test(virtualEntityContext);
        //DebugManager.verbose(Modules.Condition.test(this.getClass()),
        //        "Selector: "+selector.getSelector()+" Entity: "+(entity!=null?entity.getName():"null"));
        /*int size = selector.select(context).size();
        context.getDescriptor().add(getDescriptor())
                .addLine("Selected entities: "+size);
        return test.apply(size);*/
    }

    /*@Override
    public String toString() {
        return this.getClass().getSimpleName()+" Entity: "+entityName+" Selector: "+selector.getSelector();
    }*/

    public Descriptor getDescriptor() {
        return super.getDescriptor()
                .addLine("Entity: "+entityName);
                //.addLine("Selector: "+selector.getSelector())
                //.addLine("Criterion: "+test.getComparator()+test.getLimit());
    }

}
