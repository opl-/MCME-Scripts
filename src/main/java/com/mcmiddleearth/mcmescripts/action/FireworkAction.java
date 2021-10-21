package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkAction extends Action {

    private final FireworkMeta fireworkMeta;

    private final Location location;

    public FireworkAction(Location location, FireworkMeta fireworkMeta) {
        this.fireworkMeta = fireworkMeta;
        this.location = location;
        DebugManager.info(Modules.Action.create(this.getClass()),"Location: "+ location);
    }

    @Override
    protected void handler(TriggerContext context) {
        Location loc = location;
        if(loc == null) {
            loc = context.getLocation();
        }
        if (loc != null) {
            DebugManager.verbose(Modules.Action.execute(this.getClass()),"Location: "+loc);
            loc.getWorld().spawn(loc, Firework.class, firework -> {
                firework.setFireworkMeta(fireworkMeta);
            });
        }
    }
}
