package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.ai.goal.Goal;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.selector.Selector;

import java.util.List;

public class SetGoalAction extends SelectingAction<VirtualEntity> {

    public SetGoalAction(VirtualEntityGoalFactory goalFactory, Selector<VirtualEntity> selector, McmeEntitySelector goalTargetSelector) {
        super(selector, (entity,context) -> {
            try {
//DebugManager.log(Modules.Action.execute(SetGoalAction.class),"GoalTarget selector: "+goalTargetSelector.getSelector());
                List<McmeEntity> goalTargets = goalTargetSelector.select(context);
                if(!goalTargets.isEmpty()) {
//DebugManager.log(Modules.Action.execute(SetGoalAction.class),"GoalTarget entity: "+goalTargets.get(0));
                    goalFactory.withTargetEntity(goalTargets.get(0));
                }
                entity.setGoal(goalFactory.build(entity));
                DebugManager.log(Modules.Action.execute(SetGoalAction.class),"Target entity: "+entity.getName());
            } catch (InvalidLocationException | InvalidDataException e) {
                e.printStackTrace();
            }
        });
        DebugManager.log(Modules.Action.create(this.getClass()),"Goal type: "+(goalFactory!=null?goalFactory.getGoalType().name():"null"));
    }
}
