package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.condition.Criterion;
import com.mcmiddleearth.mcmescripts.condition.CriterionCondition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

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
        TriggerContext locationContext = new TriggerContext(context).withLocation(location);
        /*DebugManager.verbose(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector()+" Location: "+(location!=null?location.toString():"null"));
        List selectorResult = selector.select(context);
        return test.apply(selectorResult.size());*/
        boolean result = super.test(locationContext);
        context.setDescriptor(locationContext.getDescriptor());
        return result;
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
