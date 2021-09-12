package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class SpawnAction implements Action  {

    private final VirtualEntityFactory factory;

    public SpawnAction(VirtualEntityFactory factory) {
        this.factory = factory;
    }

    @Override
    public void execute(TriggerContext context) {
        try {
            context.getScript().addEntity(EntitiesPlugin.getEntityServer().spawnEntity(factory));
            DebugManager.log(Modules.Action.execute(SpawnAction.class),"Spawn entity: "+factory.getName());
        } catch (InvalidLocationException | InvalidDataException e) {
            e.printStackTrace();
        }
        DebugManager.log(Modules.Action.create(this.getClass()),"Entity name: "+factory.getName());
    }
}
