package com.mcmiddleearth.mcmescripts.trigger;

import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.compiler.LocationCompiler;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.script.Script;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class ExternalTrigger extends DecisionTreeTrigger {

    public ExternalTrigger(Action action, String name) {
        super(action);
        setName(name);
    }

    @Override
    public void register(Script script) {
        super.register(script);
        MCMEScripts.getExternalTriggerManager().register(this);
    }

    @Override
    public void unregister() {
        super.unregister();
        MCMEScripts.getExternalTriggerManager().unregister(this);
    }

    public void call(String[] args) {
        TriggerContext context = new TriggerContext(this);
        //context.getDescriptor().add(getDescriptor());
        for(String arg: args) {
            if(arg.contains(":") && arg.indexOf(':')<arg.length()-1) {
                String key = arg.substring(0,arg.indexOf(':'));
                String value = arg.substring(arg.indexOf(':')+1);
                switch (key) {
                    case "player":
                        Player player = Bukkit.getPlayer(value);
                        if(player!=null) {
                            context.withPlayer(player);
                        } else {
                            DebugManager.warn(Modules.Trigger.call(this.getClass()),"Player not found.");
                        }
                        break;
                    case "entity":
                        McmeEntity entity = EntitiesPlugin.getEntityServer().getEntity(value);
                        if(entity instanceof VirtualEntity) {
                            context.withEntity(entity);
                        } else {
                            DebugManager.warn(Modules.Trigger.call(this.getClass()),"Entity not found.");
                        }
                        break;
                    case "location":
                        LocationCompiler.compile(new JsonPrimitive(value)).ifPresent(context::withLocation);
                    case "name":
                        context.withName(value);
                        break;
                    case "message":
                        context.withMessage(value);
                        break;
                }
            }
        }
        call(context);
    }
}
