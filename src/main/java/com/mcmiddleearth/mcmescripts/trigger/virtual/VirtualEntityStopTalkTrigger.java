package com.mcmiddleearth.mcmescripts.trigger.virtual;

import com.mcmiddleearth.entities.events.events.virtual.VirtualEntityStopTalkEvent;
import com.mcmiddleearth.entities.events.handler.EntityEventHandler;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.trigger.EntitiesEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.Collection;

public class VirtualEntityStopTalkTrigger extends EntitiesEventTrigger {

    public VirtualEntityStopTalkTrigger(Collection<Action> actions) {
        super(actions);
    }

    public VirtualEntityStopTalkTrigger(Action action) {
        super(action);
    }

    @EntityEventHandler
    public void onEntityStopTalk(VirtualEntityStopTalkEvent event) {
        TriggerContext context = new TriggerContext(this)
                                         .withEntity(event.getVirtualEntity());
        call(context);
    }
}
