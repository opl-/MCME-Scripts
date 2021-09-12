package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;

import java.util.Collection;

public class PeriodicRealTimeTrigger extends PeriodicTrigger {

    public PeriodicRealTimeTrigger(Action action, long timeMillis) {
        super(action, timeMillis);
        DebugManager.log(Modules.Trigger.create(this.getClass()),
                "Time: "+timeMillis+" Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

}
