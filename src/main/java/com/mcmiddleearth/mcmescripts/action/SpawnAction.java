package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class SpawnAction implements Action  {

    private final VirtualEntityFactory factory;

    public SpawnAction(VirtualEntityFactory factory) {
        this.factory = factory;
    }

    @Override
    public void execute(TriggerContext context) {
        try {
            EntitiesPlugin.getEntityServer().spawnEntity(factory);
        } catch (InvalidLocationException | InvalidDataException e) {
            e.printStackTrace();
        }
    }
}
