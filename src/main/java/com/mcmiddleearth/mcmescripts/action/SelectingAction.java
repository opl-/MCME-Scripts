package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;

public class SelectingAction<T> extends Action {

    private final Selector<T> selector;

    private final BiConsumer<T,TriggerContext> executor;

    public SelectingAction(Selector<T> selector, BiConsumer<T,TriggerContext> executor) {
        this.selector = selector;
        this.executor = executor;
        getDescriptor().indent().addLine("Selector: "+selector.getSelector()).outdent();
    }

    @Override
    protected void handler(TriggerContext context) {
        List<T> selected = selector.select(context);
        //DebugManager.verbose(Modules.Action.execute(this.getClass()),"Selector: "+selector.getSelector()+" Selected: "+selected.size());
        context.getDescriptor().indent();
        selected.forEach(element -> {
            if(element instanceof Player) {
                context.getDescriptor().addLine("Targeting: " + ((Player) element).getName());
            } else if(element instanceof McmeEntity) {
                context.getDescriptor().addLine("Targeting: " + ((McmeEntity) element).getName());
            }
            context.getDescriptor().indent();
            executor.accept(element,context);
            context.getDescriptor().outdent();
        });
        context.getDescriptor().outdent();
    }
}
