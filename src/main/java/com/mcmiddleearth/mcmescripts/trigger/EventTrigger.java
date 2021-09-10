package com.mcmiddleearth.mcmescripts.trigger;

import com.mcmiddleearth.mcmescripts.action.Action;

import java.util.Collection;

public abstract class EventTrigger extends DecisionTreeTrigger {

    public EventTrigger(Collection<Action> actions) {
        super(actions);
    }

    public EventTrigger(Action action) {
        super(action);
    }
}
