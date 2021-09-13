package com.mcmiddleearth.mcmescripts.action;

import com.google.common.base.Joiner;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class TalkAction extends SelectingAction<VirtualEntity> {

    public TalkAction(SpeechBalloonLayout layout, Selector<VirtualEntity> selector) {
        super(selector, (entity,context) -> {
            entity.say(layout);
            DebugManager.log(Modules.Action.execute(DespawnAction.class),"Target entity: "+entity.getName());
        });
        DebugManager.log(Modules.Action.execute(this.getClass()),"Message: "+ Joiner.on("/").join(layout.getLines()));
    }
}
