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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class SpawnAction extends Action  {

    private final List<VirtualEntityFactory> factories;

    private final int lifespan;

    private final boolean serverSide;

    public SpawnAction(List<VirtualEntityFactory> factories, int lifespan, boolean serverSide) {
        this.factories = factories;
        this.lifespan = lifespan;
        this.serverSide = serverSide;
        //DebugManager.info(Modules.Action.create(this.getClass()),"Entities: "+ this.factories.size());
        getDescriptor().indent()
                .addLine("Lifespan: "+lifespan)
                .addLine("Server side: "+serverSide)
                .addLine("Entities: ").indent();
        if(!factories.isEmpty()) {
            factories.forEach(factory -> {
                getDescriptor().addLine("Name: "+factory.getName()).indent()
                        .addLine("Type: "+factory.getType().name())
                        .addLine("Location: "+factory.getLocation()).outdent();
            });
        }
        getDescriptor().outdent().outdent();
    }

    @Override
    protected void handler(TriggerContext context) {
        spawnEntity(context, factories, lifespan, serverSide);
    }

    public static Set<McmeEntity> spawnEntity(TriggerContext context, List<VirtualEntityFactory> factories, int lifespan, boolean serverSide) {
        Set<McmeEntity> entities = new HashSet<>();
        factories.forEach(factory -> {
            try {
                //DebugManager.verbose(Modules.Action.execute(SpawnAction.class),"Spawn entity: "+ factory.getName());
                String name = context.getName();
                if(name!=null) factory.withDisplayName(name);
                if(serverSide) {
                    spawnRealEntity(factory);
                } else {
                    McmeEntity entity = EntitiesPlugin.getEntityServer().spawnEntity(factory);
                    context.getScript().addEntity(entity);
                    context.withEntity(entity);
                    entities.add(entity);
                }
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
        return entities;
    }

    public static void spawnRealEntity(VirtualEntityFactory factory) {
        EntityType type = factory.getType().getBukkitEntityType();
        Location loc = factory.getLocation();
        if(type != null && loc != null && loc.getWorld() !=null) {
            Entity entity = loc.getWorld().spawnEntity(loc,type, CreatureSpawnEvent.SpawnReason.CUSTOM);
            if(entity instanceof Horse) {
                Horse horse = (Horse) entity;
                horse.setTamed(true);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            }
        }

    }
}
