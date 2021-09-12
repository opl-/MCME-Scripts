package com.mcmiddleearth.mcmescripts.trigger;


import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.script.Script;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Trigger {

    private Script script;

    private boolean callOnce = false;

    private VirtualEntity entity;
    private Player player;
    private Location location;

    public void register(Script script) {
        script.addTrigger(this);
        this.script = script;
        DebugManager.log(Modules.Trigger.register(this.getClass()),
                "Scrip: "+script.getName()+" Call once: "+callOnce);
    }

    public void unregister() {
        script.removeTrigger(this);
        DebugManager.log(Modules.Trigger.unregister(this.getClass()),
                "Scrip: "+script.getName());
    }

    public Script getScript() {
        return script;
    }

    public VirtualEntity getEntity() {
        return entity;
    }

    public void setEntity(VirtualEntity entity) {
        this.entity = entity;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public boolean isCallOnce() {
        return callOnce;
    }

    public void setCallOnce(boolean callOnce) {
        this.callOnce = callOnce;
    }

    public void call(TriggerContext context) {
        if(callOnce) {
            unregister();
        }
    }
}
