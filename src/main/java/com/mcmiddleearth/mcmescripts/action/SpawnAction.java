package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public class SpawnAction extends Action  {

    private final List<VirtualEntityFactory> factories;

    public SpawnAction(List<VirtualEntityFactory> factory) {
        this.factories = factory;
        DebugManager.info(Modules.Action.create(this.getClass()),"Entities: "+ factories.size());
    }

    @Override
    protected void handler(TriggerContext context) {
        factories.forEach(factory -> {
            try {
                DebugManager.verbose(Modules.Action.execute(SpawnAction.class),"Spawn entity: "+ factory.getName());
                context.getScript().addEntity(EntitiesPlugin.getEntityServer().spawnEntity(factory));
            } catch (InvalidLocationException | InvalidDataException e) {
                e.printStackTrace();
            }
        });
    }
}
