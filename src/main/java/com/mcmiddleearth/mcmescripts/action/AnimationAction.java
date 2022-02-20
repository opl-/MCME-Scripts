package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class AnimationAction extends SelectingAction<VirtualEntity> {

    public AnimationAction(Selector<VirtualEntity> selector, String animation, boolean override) {
        super(selector, (entity, context) -> {
            //DebugManager.verbose(Modules.Action.execute(AnimationAction.class),"Selector: "+selector.getSelector()
            //        + " Animation: "+animation);
            if(entity instanceof BakedAnimationEntity) {
                ((BakedAnimationEntity)entity).setAnimation(animation, override,null,0);
            }
        });
        //DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()
        //        + " Animation: "+animation);
        getDescriptor().indent().addLine("Animation: "+animation).addLine("Override: "+override).outdent();
    }

}