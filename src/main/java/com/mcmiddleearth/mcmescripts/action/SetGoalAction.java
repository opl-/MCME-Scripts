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
                //DebugManager.verbose(Modules.Action.execute(SetGoalAction.class),"Target entity: "+entity.getName());
                List<McmeEntity> goalTargets = goalTargetSelector.select(context);
                if(goalTargets.isEmpty()) {
                    context.getDescriptor().addLine("Selected Enemies: --none--");
                } else {
                    context.getDescriptor().addLine("Selected Enemies:").indent();
                    goalTargets.forEach(target -> context.getDescriptor().addLine(target.getName()));
                    context.getDescriptor().outdent();
                }
                if (!goalTargets.isEmpty()) {
                    goalFactory.withTargetEntity(goalTargets.get(0));
                }
                entity.setGoal(goalFactory.build(entity));
            } catch (InvalidLocationException | InvalidDataException e) {
                DebugManager.warn(Modules.Action.execute(SetGoalAction.class), "Can't assign goal to "+entity.getName()+". "+e.getClass().getSimpleName()+" "+e.getMessage());
                e.printStackTrace();
            }
        });
        getDescriptor().indent()
                .addLine("Goal: "+goalFactory.getGoalType())
                .addLine("Goal target entity: "+goalFactory.getTargetEntity().getName())
                .addLine("Goal target location: "+goalFactory.getTargetLocation())
                .addLine("Target selector: "+goalTargetSelector).outdent();
        //DebugManager.info(Modules.Action.create(this.getClass()),"Goal type: "+(goalFactory!=null?goalFactory.getGoalType().name():"null"));
    }
}
