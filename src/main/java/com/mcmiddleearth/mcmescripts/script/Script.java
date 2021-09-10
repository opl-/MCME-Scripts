package com.mcmiddleearth.mcmescripts.script;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.Entity;
import com.mcmiddleearth.mcmescripts.compiler.ConditionCompiler;
import com.mcmiddleearth.mcmescripts.compiler.ScriptCompiler;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Script {

    private String name;

    private final Set<Trigger> triggers = new HashSet<>();
    private final Set<Entity> entities = new HashSet<>();

    private final Set<Condition> conditions;
    private boolean metAllConditions = true;

    private boolean active = false;

    private final File dataFile;
    //private final JsonObject jsonData;

    public Script(File file) throws IOException {
        dataFile = file;
        JsonObject jsonData = loadJsonData(dataFile);
        name = ScriptCompiler.getName(jsonData);
        if(name==null) name = System.currentTimeMillis()+"_"+Math.random();
        conditions = ConditionCompiler.compile(jsonData);
        if(!conditions.isEmpty()) metAllConditions = ConditionCompiler.getMetAllConditions(jsonData);
    }

    private JsonObject loadJsonData(File dataFile) throws IOException {
        try (FileReader reader = new FileReader(dataFile)) {
            return new JsonParser().parse(new JsonReader(reader)).getAsJsonObject();
        }
    }

    public void load() throws IOException {
        if(!active) {
            JsonObject jsonData = loadJsonData(dataFile);
            ScriptCompiler.load(jsonData,this);
            active = true;
        }
    }

    public void unload() {
        new HashSet<>(triggers).forEach(Trigger::unregister);
        entities.forEach(entity -> EntitiesPlugin.getEntityServer().removeEntity(entity));
        entities.clear();
        active = false;
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
            if(metAllConditions && !condition.test(context)) {
                return false;
            } else if(!metAllConditions && condition.test(context)) {
                return true;
            }
        }
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
}
