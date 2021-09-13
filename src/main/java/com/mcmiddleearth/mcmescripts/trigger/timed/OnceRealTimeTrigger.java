package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;

public class OnceRealTimeTrigger extends OnceTrigger {

    public OnceRealTimeTrigger(Action action, long timeMillis) {
        super(action, timeMillis);
        DebugManager.log(Modules.Trigger.create(this.getClass()),
                "Time: "+timeMillis+" Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

}
