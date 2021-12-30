package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.ai.goal.GoalType;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.EntitySelector;

public class GoalTypeCondition extends SelectingCondition<VirtualEntity> {

    private final GoalType goalType;
    private final boolean exclude;

    public GoalTypeCondition(EntitySelector<VirtualEntity> selector, GoalType goalType, boolean exclude) {
        super(selector, entity -> (entity.getGoal() != null ? entity.getGoal().getType().equals(goalType) != exclude :
                                                              (goalType == null) != exclude));
        this.goalType = goalType;
        this.exclude = exclude;
        DebugManager.info(Modules.Condition.create(this.getClass()),
                "Selector: "+selector.getSelector()+" Test goalType: "+goalType.name()+" exclude: "+exclude);
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Goal Type: "+goalType.name()).addLine("Negate: "+exclude);
    }

}

