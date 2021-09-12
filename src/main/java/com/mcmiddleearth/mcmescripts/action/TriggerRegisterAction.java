package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.HashSet;
import java.util.Set;

public class TriggerRegisterAction implements Action {

    private final Set<Trigger> triggers;

    public TriggerRegisterAction(Trigger trigger) {
        triggers = new HashSet<>();
        triggers.add(trigger);
        DebugManager.log(Modules.Action.create(this.getClass()),"Trigger: "+trigger.getClass().getSimpleName());
    }

    public TriggerRegisterAction(Set<Trigger> triggers) {
        this.triggers = new HashSet<>(triggers);
        triggers.forEach(trigger -> DebugManager.log(Modules.Action.create(this.getClass()),
                         "Trigger: "+trigger.getClass().getSimpleName()));
    }

    @Override
    public void execute(TriggerContext context) {
        triggers.forEach(trigger -> trigger.register(context.getScript()));
        DebugManager.log(Modules.Action.execute(this.getClass()),"Registering "+triggers.size()+" triggers.");
    }

}
