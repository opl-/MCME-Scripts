package com.mcmiddleearth.mcmescripts.condition;

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
        for(T element :selector.select(context)) {
            if(matchAllSelected && !test.apply(element)) {
                return false;
            } else if(!matchAllSelected && test.apply(element)) {
                return true;
            }
        }
        return matchAllSelected;
    }

    public boolean isMatchAllSelected() {
        return matchAllSelected;
    }

    public void setMatchAllSelected(boolean matchAllSelected) {
        this.matchAllSelected = matchAllSelected;
    }

}
