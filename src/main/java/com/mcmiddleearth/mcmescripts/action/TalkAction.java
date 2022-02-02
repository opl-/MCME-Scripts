package com.mcmiddleearth.mcmescripts.action;

import com.google.common.base.Joiner;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;

import java.util.Arrays;
import java.util.logging.Logger;

public class TalkAction extends SelectingAction<VirtualEntity> {

    public TalkAction(SpeechBalloonLayout layout, Selector<VirtualEntity> selector) {
        super(selector, (entity,context) -> {
            DebugManager.verbose(Modules.Action.execute(TalkAction.class),"Target entity: "+entity.getName());
            SpeechBalloonLayout tempLayout = layout.clone();
            if(context.getMessage()!=null) {
                tempLayout.withMessage(context.getMessage());
            }
            entity.say(tempLayout);
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Message: "+ Joiner.on("/").join(layout.getLines()));
    }
}
