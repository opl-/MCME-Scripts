package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

public abstract class SelectingCondition<T> extends Condition {

    private boolean matchAllSelected = false;

    private final Selector<T> selector;

    public SelectingCondition(Selector<T> selector) {
        this.selector = selector;
    }

    /**
     * Called for each selected entity to check if it matches some condition.
     * @return <code>true</code> if the entity should match.
     */
    protected abstract boolean test(T entity);

    @Override
    public boolean test(TriggerContext context) {
        boolean result = matchAllSelected;
        context.getDescriptor().add(super.getDescriptor()).indent();
        for(T element :selector.select(context)) {
            if(element instanceof Player) {
                context.getDescriptor().addLine("Testing player: "+((Player)element).getName());
            } else if(element instanceof McmeEntity) {
                context.getDescriptor().addLine("Testing McmeEntity: "+(((McmeEntity) element).getName()));
            }
            if(matchAllSelected && !test(element)) {
                result = false;
                break;
            } else if(!matchAllSelected && test(element)) {
                result = true;
                break;
            }
        }
        context.getDescriptor().addLine("Test result: "+result).outdent();
        //DebugManager.verbose(Modules.Condition.test(this.getClass()),
        //        "Selector: "+selector.getSelector()+" Result: "+result);
        return result;
    }

    public void setMatchAllSelected(boolean matchAllSelected) {
        this.matchAllSelected = matchAllSelected;
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().indent()
                .addLine("Selector: "+selector.getSelector())
                .addLine("Match All: "+matchAllSelected)
                .outdent();
    }

}
