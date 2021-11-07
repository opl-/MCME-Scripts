package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class SpawnAction extends Action  {

    private final List<VirtualEntityFactory> factories;

    private final int lifespan;

    public SpawnAction(List<VirtualEntityFactory> factories, int lifespan) {
        this.factories = factories;
        this.lifespan = lifespan;
        DebugManager.info(Modules.Action.create(this.getClass()),"Entities: "+ this.factories.size());
    }

    @Override
    protected void handler(TriggerContext context) {
        spawnEntity(context, factories, lifespan);
    }

    public static void spawnEntity(TriggerContext context, List<VirtualEntityFactory> factories, int lifespan) {
        Set<McmeEntity> entities = new HashSet<>();
        factories.forEach(factory -> {
            try {
                DebugManager.verbose(Modules.Action.execute(SpawnAction.class),"Spawn entity: "+ factory.getName());
                String name = context.getName();
                if(name!=null) factory.withDisplayName(name);
                McmeEntity entity = EntitiesPlugin.getEntityServer().spawnEntity(factory);
                context.getScript().addEntity(entity);
                entities.add(entity);
            } catch (InvalidLocationException | InvalidDataException e) {
                e.printStackTrace();
            }
        });
        if(lifespan>0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    entities.forEach(entity -> context.getScript().removeEntity(entity));
                    EntitiesPlugin.getEntityServer().removeEntity(entities);
                }
            }.runTaskLater(MCMEScripts.getInstance(), lifespan);
        }
    }
}
