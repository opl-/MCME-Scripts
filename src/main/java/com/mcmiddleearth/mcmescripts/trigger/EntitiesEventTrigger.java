package com.mcmiddleearth.mcmescripts.trigger;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.events.listener.McmeEventListener;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.script.Script;

import java.util.Collection;

public abstract class EntitiesEventTrigger extends EventTrigger implements McmeEventListener {

    public EntitiesEventTrigger(Action action) {
        super(action);
    }

    @Override
    public void register(Script script) {
        super.register(script);
        EntitiesPlugin.getEntityServer().registerEvents(MCMEScripts.getInstance(),this);
    }

    @Override
    public void unregister() {
        super.unregister();
        EntitiesPlugin.getEntityServer().unregisterEvents(MCMEScripts.getInstance(),this);
    }
}
