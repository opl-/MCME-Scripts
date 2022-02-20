package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class StopTalkAction extends SelectingAction<VirtualEntity> {

    public StopTalkAction(Selector<VirtualEntity> selector) {
        super(selector, (entity,context)-> {
            //DebugManager.verbose(Modules.Action.execute(StopTalkAction.class),"Target entity: "+entity.getName());
            entity.stopTalking();
        });
        //DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector());
    }

}
