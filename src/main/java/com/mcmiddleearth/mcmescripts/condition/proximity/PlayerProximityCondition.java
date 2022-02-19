package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.condition.Criterion;
import com.mcmiddleearth.mcmescripts.condition.CriterionCondition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Function;

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
        Player player = Bukkit.getPlayer(playerName);
        TriggerContext playerContext = new TriggerContext(context);
        if (player != null) {
            playerContext.withPlayer(player);
        }
        /*DebugManager.verbose(Modules.Condition.test(this.getClass()),
                "Selector: "+selector.getSelector()+" Player: "+ player);
        return test.apply(selector.select(context).size());*/
        boolean result = super.test(playerContext);
        context.setDescriptor(playerContext.getDescriptor());
        if(player!=null) {
            context.getDescriptor().indent()
                    .addLine("Found center player: "+player.getName()).outdent();
        } else {
            context.getDescriptor().indent()
                    .addLine("Found center player: --none--").outdent();
        }
        return result;
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
