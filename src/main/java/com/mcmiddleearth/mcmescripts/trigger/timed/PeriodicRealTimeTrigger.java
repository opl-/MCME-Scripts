package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;

import java.util.Collection;

public class PeriodicRealTimeTrigger extends PeriodicTrigger {

    public PeriodicRealTimeTrigger(Collection<Action> actions, long timeMillis) {
        super(actions, timeMillis);
    }

    public PeriodicRealTimeTrigger(Action action, long timeMillis) {
        super(action, timeMillis);
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

}
