package com.mcmiddleearth.mcmescripts.trigger.player;

import com.mcmiddleearth.entities.events.events.player.VirtualPlayerAttackEvent;
import com.mcmiddleearth.entities.events.handler.EntityEventHandler;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.EntitiesEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.Collection;

public class VirtualPlayerAttackTrigger extends EntitiesEventTrigger {

    public VirtualPlayerAttackTrigger(Action action) {
        super(action);
        DebugManager.log(Modules.Trigger.create(this.getClass()),
                "Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    @EntityEventHandler
    public void playerJoin(VirtualPlayerAttackEvent event) {
        TriggerContext context = new TriggerContext(this)
                .withPlayer(event.getPlayer().getBukkitPlayer())
                .withEntity(event.getVirtualEntity());
        call(context);
        DebugManager.log(Modules.Trigger.call(this.getClass()),
                "Player: " + event.getPlayer().getName() + " Entity: " + context.getEntity().getName());
    }

}
