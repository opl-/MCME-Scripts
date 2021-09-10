package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class StopTalkAction extends SelectingAction<VirtualEntity> {

    public StopTalkAction(Selector<VirtualEntity> selector) {
        super(selector, (entity,context)-> entity.stopTalking());
    }

    /*@Override
    public void execute(TriggerContext context) {
        if(context.getEntity()!=null)
            context.getEntity().stopTalking();
    }*/
}
