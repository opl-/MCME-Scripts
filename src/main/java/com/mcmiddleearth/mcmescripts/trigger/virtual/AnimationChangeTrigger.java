package com.mcmiddleearth.mcmescripts.trigger.virtual;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.events.events.virtual.composite.BakedAnimationEntityAnimationChangeEvent;
import com.mcmiddleearth.entities.events.handler.EntityEventHandler;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.EntitiesEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.Locale;

public class AnimationChangeTrigger extends EntitiesEventTrigger {

    private final String current, next;

    public AnimationChangeTrigger(Action action, String current, String next) {
        super(action);
        this.current = current;
        this.next = next;
        DebugManager.info(Modules.Trigger.create(this.getClass()),
                "Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    @SuppressWarnings("unused")
    @EntityEventHandler
    public void onAnimationChange(BakedAnimationEntityAnimationChangeEvent event) {
        if((current == null || current.equalsIgnoreCase(event.getCurrentAnimation()))
                && (next == null || next.equalsIgnoreCase(event.getNextAnimation()))) {
            TriggerContext context = new TriggerContext(this);
            context.withEntity(event.getEntity());
            context.withEntityEvent(event);
            call(context);
            DebugManager.verbose(Modules.Trigger.call(this.getClass()),
                    "Entity: " + context.getEntity() + " Current: " + event.getCurrentAnimation()
                            + " Next: " + event.getNextAnimation());
        }
    }

    @Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Current animation: "+current)
                                    .addLine("Next animation: "+next);
    }
}