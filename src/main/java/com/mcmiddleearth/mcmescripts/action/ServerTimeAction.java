package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;

public class ServerTimeAction extends Action{

    private long serverTimeTicks;

    public ServerTimeAction(long serverTimeTicks) {
        this.serverTimeTicks = serverTimeTicks;
    }

    @Override
    protected void handler(TriggerContext context) {
        DebugManager.verbose(Modules.Action.execute(ServerTimeAction.class),"Time: "+serverTimeTicks);
        Bukkit.getWorlds().forEach(world -> world.setFullTime(serverTimeTicks));
    }
}
