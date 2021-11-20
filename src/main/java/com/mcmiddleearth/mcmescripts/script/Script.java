package com.mcmiddleearth.mcmescripts.script;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.Entity;
import com.mcmiddleearth.mcmescripts.compiler.ConditionCompiler;
import com.mcmiddleearth.mcmescripts.compiler.ScriptCompiler;
import com.mcmiddleearth.mcmescripts.compiler.TriggerCompiler;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import com.mcmiddleearth.mcmescripts.utils.JsonUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Script {

    private final String name;

    private final Set<Trigger> triggers = new HashSet<>();
    private final Set<Entity> entities = new HashSet<>();

    private final Set<Condition> conditions;
    private boolean metAllConditions = true;

    private boolean active = false;

    private final File dataFile;

    public Script(File file) throws IOException {
        dataFile = file;
        JsonObject jsonData = JsonUtils.loadJsonData(dataFile);
        name = ScriptCompiler.getName(jsonData).orElse(System.currentTimeMillis()+"_"+Math.random());
        conditions = ConditionCompiler.compile(jsonData);
        if(!conditions.isEmpty()) metAllConditions = TriggerCompiler.getMetAllConditions(jsonData);
        DebugManager.info(Modules.Script.create(this.getClass()),
                "Name: "+name+" Conditions: "+conditions.size()+" met all: "+metAllConditions);
    }

    public void load() throws IOException {
        if(!active) {
            DebugManager.info(Modules.Script.load(this.getClass()),
                    "Name: "+name);
            JsonObject jsonData = JsonUtils.loadJsonData(dataFile);
            ScriptCompiler.load(jsonData,this);
            active = true;
        }
    }

    public void unload() {
        new HashSet<>(triggers).forEach(Trigger::unregister);
        entities.forEach(entity -> EntitiesPlugin.getEntityServer().removeEntity(entity));
        entities.clear();
        active = false;
        DebugManager.info(Modules.Script.unload(this.getClass()),
                "Name: "+name);
    }

    public boolean isActive() {
        return active;
    }

    public boolean isTriggered() {
        if(conditions.isEmpty()) return true;
        Script instance = this;
        TriggerContext context = new TriggerContext(new Trigger() {
            @Override
            public Script getScript() {
                return instance;
            }
            @Override
            public void call(TriggerContext context) {}
        });
        for(Condition condition: conditions) {
            boolean testResult = condition.test(context);
            if(metAllConditions && !testResult) {
                return false;
            } else if(!metAllConditions && testResult) {
//Logger.getGlobal().info("trigger!");
                return true;
            }
        }
//Logger.getGlobal().info("isTriggered: "+getName()+" "+metAllConditions);
        return metAllConditions;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    public void removeTrigger(Trigger trigger) {
        triggers.remove(trigger);
    }

    public String getName() {
        return name;
    }

    public Set<Trigger> getTriggers(String name) {
        if(name.equals("*")) return new HashSet<>(triggers);
        if(name.endsWith("*")) {
            return triggers.stream().filter(trigger->trigger.getName()!=null
                                                  && trigger.getName().startsWith(name.substring(0,name.length()-1)))
                    .collect(Collectors.toSet());
        } else {
            return triggers.stream().filter(trigger->trigger.getName()!=null && trigger.getName().equals(name))
                    .collect(Collectors.toSet());
        }
    }

    public Set<Trigger> getTriggers() {
        return triggers;
    }

    public Set<Entity> getEntities() {
        return entities;
    }
}
