package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class PlayerOnlineCondition implements Condition {

    private final Function<Integer,Boolean> test;
    private final Selector<Player> selector;

    @SuppressWarnings({"unchecked","rawtypes"})
    public PlayerOnlineCondition(Selector selector, Function<Integer,Boolean> test) {
        this.selector = selector;
        this.test = test;
        DebugManager.info(Modules.Condition.create(this.getClass()),
                "Selector: "+selector.getSelector());
    }

    @Override
    public boolean test(TriggerContext context) {
        DebugManager.verbose(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector());
        return test.apply(selector.select(context).size());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+" Selector: "+selector.getSelector();
    }

}
