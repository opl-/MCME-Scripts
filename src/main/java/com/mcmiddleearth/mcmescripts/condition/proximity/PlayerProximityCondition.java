package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class PlayerProximityCondition implements Condition {

    private final Function<Integer,Boolean> test;
    private final String playerName;
    private final Selector selector;

    public PlayerProximityCondition(String playerName, Selector selector, Function<Integer,Boolean> test) {
        this.selector = selector;
        this.playerName = playerName;
        this.test = test;
        DebugManager.log(Modules.Condition.create(this.getClass()),
                "Selector: "+selector.getSelector()+" Player: "+playerName);
    }

    @Override
    public boolean test(TriggerContext context) {
        Player player = Bukkit.getPlayer(playerName);
        if(player!=null) {
            context = new TriggerContext(context).withPlayer(player);
        }
        DebugManager.log(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector()+" Player: "+ player);
        return test.apply(selector.select(context).size());
    }
}
