package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.HashSet;
import java.util.Set;

public class TriggerRegisterAction extends Action {

    private final Set<Trigger> triggers;

    public TriggerRegisterAction(Trigger trigger) {
        triggers = new HashSet<>();
        triggers.add(trigger);
        //DebugManager.info(Modules.Action.create(this.getClass()),"Trigger: "+trigger.getClass().getSimpleName());
        getDescriptor().indent()
                .addLine("Trigger: ").indent()
                .addLine(trigger.getClass().getSimpleName()+"- Name: "+trigger.getName()).outdent().outdent();
    }

    public TriggerRegisterAction(Set<Trigger> triggers) {
        this.triggers = new HashSet<>(triggers);
        //triggers.forEach(trigger -> DebugManager.info(Modules.Action.create(this.getClass()),
        //                 "Trigger: "+trigger.getClass().getSimpleName()));
        if(!triggers.isEmpty()) {
            getDescriptor().indent().addLine("Triggers: ").indent();
            triggers.forEach(trigger -> getDescriptor().addLine(trigger.getClass().getSimpleName()+"- Name: "+trigger.getName()));
            getDescriptor().outdent().outdent();
        }
    }

    @Override
    public void handler(TriggerContext context) {
        //DebugManager.verbose(Modules.Action.execute(this.getClass()),"Registering "+triggers.size()+" triggers.");
        triggers.forEach(trigger -> trigger.register(context.getScript()));
    }

}
