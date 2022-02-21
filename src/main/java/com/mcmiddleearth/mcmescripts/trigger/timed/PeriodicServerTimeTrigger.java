package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.Bukkit;

public class PeriodicServerTimeTrigger extends PeriodicTrigger {

    public PeriodicServerTimeTrigger(Action action, long timeMillis) {
        super(action, timeMillis);
        //DebugManager.info(Modules.Trigger.create(this.getClass()),
        //        "Time: "+timeMillis+" Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    @Override
    public long getCurrentTime() {
        return Bukkit.getCurrentTick();

    }
}
