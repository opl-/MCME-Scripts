package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.HashSet;
import java.util.Set;

public class TriggerRegisterAction implements Action {

    private final Set<Trigger> triggers;

    public TriggerRegisterAction(Trigger trigger) {
        triggers = new HashSet<>();
        triggers.add(trigger);
    }

    public TriggerRegisterAction(Set<Trigger> triggers) {
        this.triggers = new HashSet<>(triggers);
    }

    @Override
    public void execute(TriggerContext context) {
        triggers.forEach(trigger -> trigger.register(context.getScript()));
    }

}
