package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.mcmescripts.selector.Selector;

import java.util.function.Function;

public class AnimationCondition extends SelectingCondition<VirtualEntity> {

    public AnimationCondition(Selector<VirtualEntity> selector, String currentAnimation,
                              Boolean manualAnimationControl,
                              Boolean instantAnimationSwitching, Boolean manualOverride) {
        super(selector, entity -> {
            if(entity instanceof BakedAnimationEntity) {
                return (currentAnimation == null || currentAnimation.equalsIgnoreCase(((BakedAnimationEntity)entity).getCurrentAnimation().getName()))
                    && (manualAnimationControl == null || manualAnimationControl == ((BakedAnimationEntity) entity).isManualAnimationControl())
                    && (instantAnimationSwitching == null || instantAnimationSwitching == ((BakedAnimationEntity) entity).isInstantAnimationSwitching())
                    && (manualOverride == null || manualOverride == ((BakedAnimationEntity)entity).isManualOverride());
            } else {
                return false;
            }
        });
    }
}
