package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class DespawnAction extends SelectingAction<VirtualEntity> {

    public DespawnAction(Selector<VirtualEntity> selector) {
        super(selector, (entity,context) -> {
            EntitiesPlugin.getEntityServer().removeEntity(entity);
            context.getScript().removeEntity(entity);
            DebugManager.log(Modules.Action.execute(DespawnAction.class),"Despawn entity: "+entity.getName());
        });
        DebugManager.log(Modules.Action.create(this.getClass()), "Selector: "+selector.getSelector());
    }
}
