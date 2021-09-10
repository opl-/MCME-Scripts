package com.mcmiddleearth.mcmescripts.trigger.virtual;

import com.google.common.base.Joiner;
import com.mcmiddleearth.entities.events.events.virtual.VirtualEntityTalkEvent;
import com.mcmiddleearth.entities.events.handler.EntityEventHandler;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.trigger.EntitiesEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.Collection;

public class VirtualEntityTalkTrigger extends EntitiesEventTrigger {

    public VirtualEntityTalkTrigger(Collection<Action> actions) {
        super(actions);
    }

    public VirtualEntityTalkTrigger(Action action) {
        super(action);
    }

    @EntityEventHandler
    public void onEntityTalk(VirtualEntityTalkEvent event) {
        TriggerContext context = new TriggerContext(this)
                .withEntity(event.getVirtualEntity())
                .withMessage(Joiner.on("\n").join(event.getLayout().getLines()));
        call(context);
    }
}
