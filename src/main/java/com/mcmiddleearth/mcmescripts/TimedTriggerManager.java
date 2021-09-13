package com.mcmiddleearth.mcmescripts;

import com.mcmiddleearth.mcmescripts.trigger.timed.TimedTrigger;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public class
TimedTriggerManager {

    private static BukkitTask timedTriggerTask;

    private final Set<TimedTrigger> triggers = new HashSet<>();

    public void startChecker() {
        stopChecker();
        timedTriggerTask = new BukkitRunnable() {
            @Override
            public void run() {
                triggers.forEach(TimedTrigger::call);
            }
        }.runTaskTimer(MCMEScripts.getInstance(), MCMEScripts.getConfigInt(ConfigKeys.START_UP_DELAY,600),
                                                  MCMEScripts.getConfigInt(ConfigKeys.TRIGGER_CHECKER_PERIOD,10));
    }

    public void stopChecker() {
        if(timedTriggerTask!=null && !timedTriggerTask.isCancelled()) {
            timedTriggerTask.cancel();
        }
    }

    public void register(TimedTrigger trigger) {
        triggers.add(trigger);
    }

    public void unregister(TimedTrigger trigger) {
        triggers.remove(trigger);
    }
}
