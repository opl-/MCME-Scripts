package com.mcmiddleearth.mcmescripts.selector;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;
import java.util.Set;

public interface Selector<T> {

    List<T> select(TriggerContext context);

}
