package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Action {

    private int delay = 0;

    public void execute(TriggerContext context) {
        DebugManager.info(Modules.Action.execute(this.getClass()),
                "Delay: "+delay);
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

    public Descriptor getDescriptor() {
        return new Descriptor(this.getClass().getSimpleName());
    }
}
