package com.mcmiddleearth.mcmescripts.trigger;

import com.mcmiddleearth.mcmescripts.action.Action;

public abstract class EventTrigger extends DecisionTreeTrigger {

    public EventTrigger(Action action) {
        super(action);
    }
}
