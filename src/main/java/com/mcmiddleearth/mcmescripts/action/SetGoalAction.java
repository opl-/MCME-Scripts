package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class SetGoalAction extends SelectingAction<VirtualEntity> {

    public SetGoalAction(VirtualEntityGoalFactory goalFactory, Selector<VirtualEntity> selector) {
        super(selector, (entity,context) -> {
            try {
                entity.setGoal(goalFactory.build(entity));
                DebugManager.log(Modules.Action.execute(SetGoalAction.class),"Target entity: "+entity.getName());
            } catch (InvalidLocationException | InvalidDataException e) {
                e.printStackTrace();
            }
        });
        DebugManager.log(Modules.Action.create(this.getClass()),"Goal type: "+goalFactory.getGoalType().name());
    }
}
