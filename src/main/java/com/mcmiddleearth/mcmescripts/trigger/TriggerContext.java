package com.mcmiddleearth.mcmescripts.trigger;

import com.mcmiddleearth.entities.ai.goal.Goal;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.script.Script;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TriggerContext {

    //private Script script;
    private final Trigger trigger;

    private Player player;
    private String message;

    private boolean firstJoin;

    private VirtualEntity entity;

    private Location location;

    private Goal goal;

    public TriggerContext(Trigger trigger) {//Script script) {
        //this.script = script;
        this.trigger = trigger;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Script getScript() {
        return  trigger.getScript();
    }

    public Player getPlayer() {
        return player;
    }

    public TriggerContext withPlayer(Player player) {
        this.player = player;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public TriggerContext withMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isFirstJoin() {
        return firstJoin;
    }

    public TriggerContext withFirstJoin(boolean firstJoin) {
        this.firstJoin = firstJoin;
        return this;
    }

    public VirtualEntity getEntity() {
        return entity;
    }

    public TriggerContext withEntity(VirtualEntity entity) {
        this.entity = entity;
        return this;
    }

    public Location getLocation() {
        if(location!=null) {
            return location;
        } else if(trigger.getLocation()!=null) {
            return trigger.getLocation();
        } else if(trigger.getPlayer()!=null) {
            return trigger.getPlayer().getLocation();
        } else if(trigger.getEntity()!=null) {
            return trigger.getEntity().getLocation();
        } else if(player!=null) {
            return player.getLocation();
        } else if(entity!=null) {
            return entity.getLocation();
        } else {
            return null;
        }
    }

    public TriggerContext withLocation(Location location) {
        this.location = location;
        return this;
    }

    public Goal getGoal() {
        return goal;
    }

    public TriggerContext withGoal(Goal goal) {
        this.goal = goal;
        return this;
    }
}
