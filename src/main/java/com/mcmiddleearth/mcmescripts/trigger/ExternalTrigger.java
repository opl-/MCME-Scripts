package com.mcmiddleearth.mcmescripts.trigger;

import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.compiler.LocationCompiler;
import com.mcmiddleearth.mcmescripts.script.Script;
import org.bukkit.Bukkit;

public class ExternalTrigger extends DecisionTreeTrigger {

    private String name;

    public ExternalTrigger(Action action, String name) {
        super(action);
        this.name = name;
    }

    public String getName() {
        return name;
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
        for(String arg: args) {
            if(arg.contains(":") && arg.indexOf(':')<arg.length()-1) {
                String key = arg.substring(0,arg.indexOf(':'));
                String value = arg.substring(arg.indexOf(':')+1);
                switch (key) {
                    case "player":
                        context.withPlayer(Bukkit.getPlayer(value));
                        break;
                    case "entity":
                        context.withEntity(EntitiesPlugin.getEntityServer().getEntity(value));
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
