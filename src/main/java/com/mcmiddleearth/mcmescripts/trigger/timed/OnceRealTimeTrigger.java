package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;

import java.util.Collection;

public class OnceRealTimeTrigger extends OnceTrigger {

    public OnceRealTimeTrigger(Collection<Action> actions, long timeMillis) {
        super(actions, timeMillis);
    }

    public OnceRealTimeTrigger(Action action, long timeMillis) {
        super(action, timeMillis);
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

}
