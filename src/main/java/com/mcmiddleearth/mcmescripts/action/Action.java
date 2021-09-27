package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Action {

    private int delay = 0;

    public void execute(TriggerContext context) {
        new BukkitRunnable() {
            @Override
            public void run() {
                handler(context);
            }
        }.runTaskLater(MCMEScripts.getInstance(),delay);
    }

    protected abstract void handler(TriggerContext context);

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
