package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.selector.EntitySelector;

public class TalkCondition extends SelectingCondition<VirtualEntity> {

    private final boolean noTalk;

    public TalkCondition(EntitySelector<VirtualEntity> talkerSelector, boolean noTalk) {
        super(talkerSelector);
        this.noTalk = noTalk;
        //DebugManager.info(Modules.Condition.create(this.getClass()),
        //        "Selector: "+talkerSelector.getSelector()+" Test not talking: "+noTalk);
   }

    @Override
    protected boolean test(VirtualEntity entity) {
        // Invert talking flag if noTalk is enabled
        return noTalk != entity.isTalking();
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("No talk: "+ noTalk);
    }

}
