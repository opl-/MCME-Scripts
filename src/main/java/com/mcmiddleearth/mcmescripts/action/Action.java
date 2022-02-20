package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public abstract class Action {

    private int delay = 0;

    private static final Random random = new Random();

    private final Descriptor descriptor = new Descriptor(this.getClass().getSimpleName());

    public void execute(TriggerContext context) {
        //DebugManager.info(Modules.Action.execute(this.getClass()),
        //        "Delay: "+delay);
        int id = random.nextInt(10000);
        context.getDescriptor().add(getDescriptor()).indent()
                               .addLine("Execution delayed by "+delay+" ticks with ID: "+id);
        Bukkit.getScheduler().runTaskLater(MCMEScripts.getInstance(), ()-> {
            TriggerContext actionContext = new TriggerContext(context);
            actionContext.setDescriptor(new Descriptor("Execute action! ID: "+id).indent());
            handler(actionContext);
            actionContext.getDescriptor().outdent();
            DebugManager.info(Modules.Action.execute(this.getClass()),actionContext.getDescriptor().print(""));
        }, delay);
    }

    protected abstract void handler(TriggerContext context);

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }
}
