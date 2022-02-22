package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.EntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class TalkCondition extends SelectingCondition<VirtualEntity> {

    private final boolean exclude;

    public TalkCondition(EntitySelector<VirtualEntity> talkerSelector, boolean noTalk) {
        super(talkerSelector, (noTalk ? entity -> !entity.isTalking() : VirtualEntity::isTalking));
        this.exclude = noTalk;
        //DebugManager.info(Modules.Condition.create(this.getClass()),
        //        "Selector: "+talkerSelector.getSelector()+" Test not talking: "+noTalk);
   }

    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("No talk: "+exclude);
    }

}
