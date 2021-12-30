package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public abstract class Condition {

    public abstract boolean test(TriggerContext data);

    public Descriptor getDescriptor() {
        return new Descriptor(this.getClass().getSimpleName());
    }
}
