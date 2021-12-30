package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public abstract class PeriodicTrigger extends TimedTrigger {

    private final long periodMillis;
    private long timeLastCheck, timeNextCall;

    public PeriodicTrigger(Action action, long periodMillis) {
        super(action);
        this.periodMillis = periodMillis;
        this.timeLastCheck = getCurrentTime();
        this.timeNextCall = this.timeLastCheck + this.periodMillis;
    }

    @Override
    public void call(TriggerContext context) {
        if (checkPeriod()) {
            super.call(context);
        }
    }

    protected boolean checkPeriod() {
        long current = getCurrentTime();
        if(current < timeLastCheck) {
            timeLastCheck = current;
            timeNextCall = timeLastCheck + periodMillis;
            DebugManager.info(Modules.Trigger.call(this.getClass()),
                    "Reset current time: " + current);
        }
        if(current >= timeNextCall && timeLastCheck < timeNextCall) {
            DebugManager.verbose(Modules.Trigger.call(this.getClass()),
                    "Current time: " + current +" last check: "+timeLastCheck + "next call: "+timeNextCall);
            timeNextCall = current + periodMillis;
            return true;
        }
        timeLastCheck = current;
        return false;
    }

    public abstract long getCurrentTime();

    @Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Period: "+periodMillis);
    }

}
