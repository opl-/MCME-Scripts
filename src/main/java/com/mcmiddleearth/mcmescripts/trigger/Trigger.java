package com.mcmiddleearth.mcmescripts.trigger;


import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.script.Script;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Trigger {

    private Script script;

    private String name;

    private boolean callOnce = false;

    private VirtualEntitySelector entity;
    private PlayerSelector player;
    private Location location;

    public void register(Script script) {
        script.addTrigger(this);
        this.script = script;
        //DebugManager.info(Modules.Trigger.register(this.getClass()),
        //        "Scrip: "+script.getName()+" Call once: "+callOnce);
    }

    public void unregister() {
        script.removeTrigger(this);
        //DebugManager.info(Modules.Trigger.unregister(this.getClass()),
        //        "Scrip: "+script.getName());
    }

    public Script getScript() {
        return script;
    }

    public VirtualEntitySelector getEntity() {
        return entity;
    }

    public void setEntity(VirtualEntitySelector entity) {
        this.entity = entity;
    }

    public PlayerSelector getPlayer() {
        return player;
    }

    public void setPlayer(PlayerSelector player) {
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
            context.getDescriptor().addLine("Unregistering call once event!");
        }
        DebugManager.info(Modules.Trigger.call(this.getClass()),context.getDescriptor().print(""));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Descriptor getDescriptor() {
        return new Descriptor(this.getClass().getSimpleName() + ": "+name).indent()
                .addLine("Call once: "+callOnce)
                .addLine("Trigger entity: "+(entity!=null?entity.getSelector():"--none--"))//+" at "+entity.getLocation().toString():"--none--"))
                .addLine("Trigger player: "+(entity!=null?player.getSelector():"--none--"))//+" at "+player.getLocation().toString():"--none--"))
                .addLine("Trigger location: "+(location!=null?location:"--none--")).outdent();
    }

    /*public String print(String indent) {
        return getDescriptor().print(indent);
    }*/

}
