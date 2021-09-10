package com.mcmiddleearth.mcmescripts.trigger;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.events.events.goal.GoalFinishedEvent;
import com.mcmiddleearth.entities.events.handler.EntityEventHandler;
import com.mcmiddleearth.mcmescripts.action.Action;

import java.util.Collection;

public class GoalFinishedTrigger extends EntitiesEventTrigger {

    public GoalFinishedTrigger(Collection<Action> actions) {
        super(actions);
    }

    public GoalFinishedTrigger(Action action) {
        super(action);
    }

    @EntityEventHandler
    public void onGoalFinished(GoalFinishedEvent event) {
        TriggerContext context = new TriggerContext(this);
        if(event.getEntity() instanceof VirtualEntity) {
            context.withEntity((VirtualEntity) event.getEntity());
        }
        call(context);
    }
}
