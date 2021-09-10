package com.mcmiddleearth.mcmescripts.trigger.player;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.trigger.BukkitEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;

public class PlayerQuitTrigger extends BukkitEventTrigger {

    public PlayerQuitTrigger(Collection<Action> actions) {
        super(actions);
    }

    public PlayerQuitTrigger(Action action) {
        super(action);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        TriggerContext context = new TriggerContext(this);
        context.withPlayer(event.getPlayer());
        call(context);
    }
}
