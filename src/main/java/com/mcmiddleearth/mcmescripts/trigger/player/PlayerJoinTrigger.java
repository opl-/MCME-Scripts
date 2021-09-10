package com.mcmiddleearth.mcmescripts.trigger.player;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.trigger.BukkitEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collection;

public class PlayerJoinTrigger extends BukkitEventTrigger {

    public PlayerJoinTrigger(Collection<Action> actions) {
        super(actions);
    }

    public PlayerJoinTrigger(Action action) {
        super(action);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        TriggerContext context = new TriggerContext(this).withPlayer(event.getPlayer())
                                                     .withFirstJoin(event.getPlayer().getLastLogin()==0);
        call(context);
    }
}
