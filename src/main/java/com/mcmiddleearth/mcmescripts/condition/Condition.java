package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public interface Condition {

    boolean test(TriggerContext data);

}
