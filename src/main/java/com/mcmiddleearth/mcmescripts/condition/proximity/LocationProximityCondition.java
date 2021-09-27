package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

@SuppressWarnings("rawtypes")
public class LocationProximityCondition implements Condition {

    private final Function<Integer,Boolean> test;
    private final Location location;
    private final Selector selector;

    public LocationProximityCondition(Location center, Selector selector, Function<Integer,Boolean> test) {
        this.selector = selector;
        this.location = center;
        this.test = test;
        DebugManager.info(Modules.Condition.create(this.getClass()),
                "Selector: "+selector.getSelector()+" Location: "+(location!=null?location.toString():"null"));
    }

    @Override
    public boolean test(TriggerContext context) {
//Logger.getGlobal().info("TEST");
        context = new TriggerContext(context).withLocation(location);
        DebugManager.verbose(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector()+" Location: "+(location!=null?location.toString():"null"));
        List selectorResult = selector.select(context);
//Logger.getGlobal().info("Selected entities: "+selectorResult.size());
        //Logger.getGlobal().info("Result: "+result);
        return test.apply(selectorResult.size());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+" Loc: "+location+" Selector: "+selector.getSelector();
    }
}
