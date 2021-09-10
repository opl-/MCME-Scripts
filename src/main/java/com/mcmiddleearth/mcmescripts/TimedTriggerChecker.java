package com.mcmiddleearth.mcmescripts;

import com.mcmiddleearth.mcmescripts.trigger.timed.TimedTrigger;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class TimedTriggerChecker extends BukkitRunnable {

    private final Set<TimedTrigger> triggers = new HashSet<>();

    @Override
    public void run() {
        triggers.forEach(TimedTrigger::call);
    }

    public void register(TimedTrigger trigger) {
        triggers.add(trigger);
    }

    public void unregister(TimedTrigger trigger) {
        triggers.remove(trigger);
    }
}
