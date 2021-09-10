package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.script.Script;
import com.mcmiddleearth.mcmescripts.trigger.DecisionTreeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.Collection;

public abstract class TimedTrigger extends DecisionTreeTrigger {

    public TimedTrigger(Collection<Action> actions) {
        super(actions);
    }

    public TimedTrigger(Action action) {
        super(action);
    }

    @Override
    public void register(Script script) {
        MCMEScripts.getTimedTriggerChecker().register(this);
        super.register(script);
    }

    @Override
    public void unregister() {
        MCMEScripts.getTimedTriggerChecker().unregister(this);
        super.unregister();
    }

    public void call() {
        call(new TriggerContext(this));
    }

    public abstract long getCurrentTime();
}
