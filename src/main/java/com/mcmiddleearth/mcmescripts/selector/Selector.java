package com.mcmiddleearth.mcmescripts.selector;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public interface Selector<T> {

    List<T> select(TriggerContext context);

    String getSelector();

}
