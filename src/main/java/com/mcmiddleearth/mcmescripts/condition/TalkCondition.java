package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.EntitySelector;

public class TalkCondition extends SelectingCondition<VirtualEntity> {

    public TalkCondition(EntitySelector<VirtualEntity> talkerSelector, boolean noTalk) {
        super(talkerSelector, (noTalk ? entity -> !entity.isTalking() : VirtualEntity::isTalking));
        DebugManager.log(Modules.Condition.create(this.getClass()),
                "Selector: "+talkerSelector.getSelector()+" Test not talking: "+noTalk);
   }
}
