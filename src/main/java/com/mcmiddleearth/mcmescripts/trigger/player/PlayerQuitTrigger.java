package com.mcmiddleearth.mcmescripts.trigger.player;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.BukkitEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitTrigger extends BukkitEventTrigger {

    public PlayerQuitTrigger(Action action) {
        super(action);
        DebugManager.info(Modules.Trigger.create(this.getClass()),
                "Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        TriggerContext context = new TriggerContext(this);
        context.withPlayer(event.getPlayer());
        call(context);
        DebugManager.verbose(Modules.Trigger.call(this.getClass()),
                "Player: " + event.getPlayer().getName());
    }
}
