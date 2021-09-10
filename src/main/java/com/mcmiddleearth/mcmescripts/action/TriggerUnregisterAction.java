package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.HashSet;
import java.util.Set;

public class TriggerUnregisterAction implements Action {

    private final Set<Trigger> triggers;

    public TriggerUnregisterAction(Trigger trigger) {
        triggers = new HashSet<>();
        triggers.add(trigger);
    }

    public TriggerUnregisterAction(Set<Trigger> triggers) {
        this.triggers = new HashSet<>(triggers);
    }

    @Override
    public void execute(TriggerContext context) {
        triggers.forEach(Trigger::unregister);
    }

}