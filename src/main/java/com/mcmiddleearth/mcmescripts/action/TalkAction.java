package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class TalkAction extends SelectingAction<VirtualEntity> {

    public TalkAction(SpeechBalloonLayout layout, Selector<VirtualEntity> selector) {
        super(selector, (entity,context) -> entity.say(layout));
    }

    /*@Override
    public void execute(TriggerContext context) {
        if(context.getEntity()!=null)
            context.getEntity().say(layout);
    }*/
}
