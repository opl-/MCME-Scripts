package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.Collection;

public abstract class PeriodicTrigger extends TimedTrigger {

    private final long periodMillis;
    private long timeLastCheck, timeNextCall;

    public PeriodicTrigger(Collection<Action> actions, long periodMillis) {
        super(actions);
        this.periodMillis = periodMillis;
        this.timeLastCheck = getCurrentTime();
        this.timeNextCall = this.timeLastCheck + this.periodMillis;
    }

    public PeriodicTrigger(Action action, long periodMillis) {
        super(action);
        this.periodMillis = periodMillis;
        this.timeLastCheck = getCurrentTime();
        this.timeNextCall = this.timeLastCheck + this.periodMillis;
    }

    @Override
    public void call(TriggerContext context) {
        long current = getCurrentTime();
        if(current < timeLastCheck) {
            timeLastCheck = current;
            timeNextCall = timeLastCheck + periodMillis;
        }
        if(current >= timeNextCall && timeLastCheck < timeNextCall) {
            super.call(context);
            timeNextCall = current + periodMillis;
        }
        timeLastCheck = current;
    }

    public abstract long getCurrentTime();
}
