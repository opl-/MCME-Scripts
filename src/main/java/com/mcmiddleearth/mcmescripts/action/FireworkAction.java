package com.mcmiddleearth.mcmescripts.action;

import com.google.common.base.Joiner;
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
        //DebugManager.info(Modules.Action.create(this.getClass()),"Location: "+ location);
        getDescriptor().indent()
                .addLine("Location: "+location)
                .addLine("Power: "+fireworkMeta.getPower())
                .addLine("Size: "+fireworkMeta.getEffectsSize());
        if(fireworkMeta.hasEffects()) {
            getDescriptor().addLine("Effects: ").indent();
            fireworkMeta.getEffects().forEach(effect -> {
                getDescriptor().addLine("Type: "+effect.getType())
                        .addLine("Colors: "+ Joiner.on(" ").join(effect.getColors()))
                        .addLine("Fade colors: "+ Joiner.on(" ").join(effect.getFadeColors()));
            });
            getDescriptor().outdent();
        } else {
            getDescriptor().addLine("Effects: --none--");
        }
        getDescriptor().outdent();
    }

    @Override
    protected void handler(TriggerContext context) {
        Location loc = location;
        if(loc == null) {
            loc = context.getLocation();
            context.getDescriptor().addLine("New location: "+loc);
        }
        if (loc != null) {
            //DebugManager.verbose(Modules.Action.execute(this.getClass()),"Location: "+loc);
            loc.getWorld().spawn(loc, Firework.class, firework -> {
                firework.setFireworkMeta(fireworkMeta);
            });
        } else {
            DebugManager.warn(Modules.Action.execute(this.getClass()),"Can't spawn Firework. No location found.");
        }
    }
}
