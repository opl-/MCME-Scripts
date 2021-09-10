package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.action.Action;
import org.bukkit.Bukkit;

import java.util.Collection;

public class PeriodicServerTimeTrigger extends PeriodicTrigger {

    public PeriodicServerTimeTrigger(Collection<Action> actions, long timeMillis) {
        super(actions, timeMillis);
    }

    public PeriodicServerTimeTrigger(Action action, long timeMillis) {
        super(action, timeMillis);
    }

    @Override
    public long getCurrentTime() {
        return Bukkit.getCurrentTick();

    }
}
