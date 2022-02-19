package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import org.bukkit.entity.Player;

public class OnlinePlayerCondition extends CriterionCondition<Player> {

    public OnlinePlayerCondition(PlayerSelector selector, Criterion test) {
        super(selector, test);
    }

    /*private final Criterion test;
    private final Selector<Player> selector;

    @SuppressWarnings({"unchecked","rawtypes"})
    public PlayerOnlineCondition(Selector selector, Criterion test) {
        this.selector = selector;
        this.test = test;
        DebugManager.info(Modules.Condition.create(this.getClass()),
                "Selector: "+selector.getSelector());
    }

    @Override
    public boolean test(TriggerContext context) {
        //DebugManager.verbose(Modules.Condition.test(this.getClass()),
        //        "Selector: "+selector.getSelector());
        int size = selector.select(context).size();
        context.getDescriptor().add(getDescriptor())
               .addLine("Selected players: "+size);
        return test.apply(size);
    }*/

    /*@Override
    public String toString() {
        return this.getClass().getSimpleName()+" Selector: "+selector.getSelector();
    }*/

    /*@Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Selector: "+selector.getSelector())
                .addLine("Criterion: "+test.getComparator()+test.getLimit());
    }*/

}
