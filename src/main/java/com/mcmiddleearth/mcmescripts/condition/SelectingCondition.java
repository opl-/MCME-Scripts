package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.function.Function;

public class SelectingCondition<T> implements Condition {

    private boolean matchAllSelected = false;

    private final Selector<T> selector;

    private final Function<T,Boolean> test;

    public SelectingCondition(Selector<T> selector, Function<T, Boolean> test) {
        this.test = test;
        this.selector = selector;
    }

    @Override
    public boolean test(TriggerContext context) {
        boolean result = matchAllSelected;
        for(T element :selector.select(context)) {
            if(matchAllSelected && !test.apply(element)) {
                result = false;
                break;
            } else if(!matchAllSelected && test.apply(element)) {
                result = true;
                break;
            }
        }
        DebugManager.verbose(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector()+" Result: "+result);
        return result;
    }

    public void setMatchAllSelected(boolean matchAllSelected) {
        this.matchAllSelected = matchAllSelected;
    }

}
