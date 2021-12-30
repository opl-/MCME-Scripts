package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ServerDaytimeCondition extends Condition{

    private final long startTick, endTick;

    World world;

    boolean negate;

    public ServerDaytimeCondition(World world, long startTick, long endTick, boolean negate) {
        this.startTick = startTick;
        this.endTick = endTick;
        this.world = world;
        this.negate = negate;
    }

    @Override
    public boolean test(TriggerContext context) {
        long time = world.getTime();
        if(negate) {
            return !(startTick < time && time < endTick);
        } else {
            return startTick < time && time < endTick;
        }
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("World: "+world)
                .addLine("Negate: "+negate)
                .addLine("Start: "+startTick)
                .addLine("End: "+endTick);
    }

}
