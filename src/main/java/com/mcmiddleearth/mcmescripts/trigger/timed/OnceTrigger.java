package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public abstract class OnceTrigger extends TimedTrigger {

    private final long timeMillis;
    private long timeLastCheck;

    public OnceTrigger(Action action, long timeMillis) {
        super(action);
        this.timeMillis = timeMillis;
    }

    @Override
    public void call(TriggerContext context) {
        long current = getCurrentTime();
        if(current >= timeMillis && timeLastCheck < timeMillis) {
            super.call(context);
            //DebugManager.verbose(Modules.Trigger.call(this.getClass()),
            //        "Current: " + current + " trigger time: " + timeMillis);
        }
        timeLastCheck = current;
    }

    public abstract long getCurrentTime();


    @Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Time: "+timeMillis);
    }

}
