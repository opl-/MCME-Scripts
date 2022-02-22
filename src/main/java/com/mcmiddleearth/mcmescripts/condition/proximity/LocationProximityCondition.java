package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.mcmescripts.condition.Criterion;
import com.mcmiddleearth.mcmescripts.condition.CriterionCondition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

@SuppressWarnings({"rawtypes","unchecked"})
public class LocationProximityCondition extends CriterionCondition {

    private final Location location;

    public LocationProximityCondition(Location center, Selector selector, Criterion test) {
        super(selector, test);
        this.location = center;
        DebugManager.info(Modules.Condition.create(this.getClass()),
                "Selector: "+selector.getSelector()+" Location: "+(location!=null?location.toString():"null"));
    }

    @Override
    public boolean test(TriggerContext context) {
        context.getDescriptor().addLine(this.getClass().getSimpleName()).indent();
        TriggerContext locationContext = new TriggerContext(context).withLocation(location);
        /*DebugManager.verbose(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector()+" Location: "+(location!=null?location.toString():"null"));
        List selectorResult = selector.select(context);
        return test.apply(selectorResult.size());*/
        //context.setDescriptor(locationContext.getDescriptor());
        return super.test(locationContext);
    }

    /*@Override
    public String toString() {
        return this.getClass().getSimpleName()+" Loc: "+location+" Selector: "+selector.getSelector();
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Criterion: "+test.getComparator()+test.getLimit());
    }*/

    public Descriptor getDescriptor() {
        return super.getDescriptor()
                .addLine("Location: " + location);
    }

}
