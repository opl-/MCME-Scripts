package com.mcmiddleearth.mcmescripts;

import com.mcmiddleearth.mcmescripts.trigger.ExternalTrigger;

import java.util.HashSet;
import java.util.Set;

public class ExternalTriggerManager {

    private final Set<ExternalTrigger> externalTriggers = new HashSet<>();

    public void register(ExternalTrigger trigger) {
        externalTriggers.add(trigger);
    }

    public void unregister(ExternalTrigger trigger) {
        externalTriggers.remove(trigger);
    }

    public void call(String script, String triggerName, String[] args) {
        externalTriggers.stream().filter(trigger -> trigger.getScript().getName().equals(script)
                                                    && trigger.getName().equals(triggerName))
                .forEach(trigger -> trigger.call(args));
    }

}
