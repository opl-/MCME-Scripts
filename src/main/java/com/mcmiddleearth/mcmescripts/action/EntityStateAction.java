package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.entities.entities.composite.animation.BakedAnimation;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.function.BiConsumer;

public class EntityStateAction extends SelectingAction<VirtualEntity> {

    public EntityStateAction(Selector<VirtualEntity> selector, String state) {
        super(selector, (entity, context) -> {
            //DebugManager.verbose(Modules.Action.execute(EntityStateAction.class),"Selector: "+selector.getSelector()
            //                                                                     + " State: "+state);
            if(entity instanceof BakedAnimationEntity) {
                ((BakedAnimationEntity)entity).setState(state);
            }
        });
        //DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()
        //                                                                + " State: "+state);
        getDescriptor().indent().addLine("State: "+state).outdent();
    }
}
