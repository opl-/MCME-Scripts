package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.ai.goal.Goal;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class SetGoalAction extends SelectingAction<VirtualEntity> {

    public SetGoalAction(Goal goal, Selector<VirtualEntity> selector) {
        super(selector, (entity,context) -> entity.setGoal(goal) );
    }

    /*@Override
    public void execute(TriggerContext context) {
        if(context.getEntity()!=null)
            context.getEntity().setGoal(goal);
    }*/
}
