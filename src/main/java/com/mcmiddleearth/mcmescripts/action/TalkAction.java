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
//Logger.getGlobal().info("layout line: "+ Arrays.toString(layout.getLines()));
            if(context.getMessage()!=null) {
                layout.withMessage(context.getMessage());
//Logger.getGlobal().info("layout line message: "+ Arrays.toString(layout.getLines()));
            }
            entity.say(layout);
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Message: "+ Joiner.on("/").join(layout.getLines()));
    }
}
