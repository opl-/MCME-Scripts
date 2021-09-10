package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.selector.EntitySelector;

public class TalkCondition extends SelectingCondition<VirtualEntity> {

    public TalkCondition(EntitySelector<VirtualEntity> talkerSelector) {
        super(talkerSelector, VirtualEntity::isTalking);
    }
}
