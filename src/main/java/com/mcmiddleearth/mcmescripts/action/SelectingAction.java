package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;
import java.util.function.BiConsumer;

public class SelectingAction<T> implements Action {

    private final Selector<T> selector;

    private final BiConsumer<T,TriggerContext> executor;

    public SelectingAction(Selector<T> selector, BiConsumer<T,TriggerContext> executor) {
        this.selector = selector;
        this.executor = executor;
    }

    @Override
    public void execute(TriggerContext context) {
        List<T> selected = selector.select(context);
        DebugManager.log(Modules.Action.execute(this.getClass()),"Selector: "+selector.getSelector()+" Selected: "+selected.size());
        selected.forEach(element -> executor.accept(element,context));
    }
}
