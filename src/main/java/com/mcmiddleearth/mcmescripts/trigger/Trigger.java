package com.mcmiddleearth.mcmescripts.trigger;


import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.script.Script;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Trigger {

    private Script script;

    private VirtualEntity entity;
    private Player player;
    private Location location;

    public void register(Script script) {
        script.addTrigger(this);
        this.script = script;
    }

    public void unregister() {
        script.removeTrigger(this);
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

    public abstract void call(TriggerContext context);
}
