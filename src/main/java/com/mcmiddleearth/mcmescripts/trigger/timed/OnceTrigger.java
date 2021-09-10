package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.Collection;

public abstract class OnceTrigger extends TimedTrigger {

    private final long timeMillis;
    private long timeLastCheck;

    public OnceTrigger(Collection<Action> actions, long timeMillis) {
        super(actions);
        this.timeMillis = timeMillis;
    }

    public OnceTrigger(Action action, long timeMillis) {
        super(action);
        this.timeMillis = timeMillis;
    }

    @Override
    public void call(TriggerContext context) {
        long current = getCurrentTime();
        if(current >= timeMillis && timeLastCheck < timeMillis) {
            super.call(context);
        }
        timeLastCheck = current;
    }

    public abstract long getCurrentTime();

}
