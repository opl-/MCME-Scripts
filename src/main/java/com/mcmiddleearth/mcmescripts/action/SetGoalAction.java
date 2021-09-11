package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.ai.goal.Goal;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class SetGoalAction extends SelectingAction<VirtualEntity> {

    public SetGoalAction(VirtualEntityGoalFactory goalFactory, Selector<VirtualEntity> selector) {
        super(selector, (entity,context) -> {
            try {
                entity.setGoal(goalFactory.build(entity));
            } catch (InvalidLocationException | InvalidDataException e) {
                e.printStackTrace();
            }
        });
    }

    /*@Override
    public void execute(TriggerContext context) {
        if(context.getEntity()!=null)
            context.getEntity().setGoal(goal);
    }*/
}
