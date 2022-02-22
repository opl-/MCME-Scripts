package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.mcmescripts.condition.Criterion;
import com.mcmiddleearth.mcmescripts.condition.CriterionCondition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings({"rawtypes","unchecked"})
public class PlayerProximityCondition extends CriterionCondition {

    private final String playerName;

    public PlayerProximityCondition(String playerName, Selector selector, Criterion test) {
        super(selector, test);
        this.playerName = playerName;
        DebugManager.info(Modules.Condition.create(this.getClass()),
                "Selector: " + selector.getSelector() + " Player: " + playerName);
    }

    @Override
    public boolean test(TriggerContext context) {
        context.getDescriptor().addLine(this.getClass().getSimpleName()).indent();
        Player player = Bukkit.getPlayer(playerName);
        if(player!=null) {
            context.getDescriptor()
                    .addLine("Found center player: "+player.getName());
        } else {
            context.getDescriptor()
                    .addLine("Found center player: --none--");
        }
        TriggerContext playerContext = new TriggerContext(context);
        if (player != null) {
            playerContext.withPlayer(player);
        }
        /*DebugManager.verbose(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector()+" Player: "+ player);
        return test.apply(selector.select(context).size());*/
        //context.setDescriptor(playerContext.getDescriptor());
        return super.test(playerContext);
    }

    /*@Override
    public String toString() {
        return this.getClass().getSimpleName()+" Player: "+playerName+" Selector: "+selector.getSelector();
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Criterion: "+test.getComparator()+test.getLimit());
    }*/

    public Descriptor getDescriptor() {
        return super.getDescriptor()
                .addLine("Entity: " + playerName);
    }
}
