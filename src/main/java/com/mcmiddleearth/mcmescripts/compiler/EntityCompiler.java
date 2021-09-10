package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.mcmescripts.ConfigKeys;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.*;
import com.mcmiddleearth.mcmescripts.condition.VirtualEntityProximityCondition;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.DecisionTreeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.PeriodicServerTimeTrigger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EntityCompiler {

    private static final String KEY_ENTITY            = "entity",
                                KEY_ENTITY_ARRAY      = "entities",
                                KEY_SPAWN_DATA        = "spawnData",
                                KEY_NAME              = "name",
                                KEY_SPAWN_DISTANCE    = "spawnDistance";

    private static final int DEFAULT_SPAWN_DISTANCE   = 64;

    private static final Random random = new Random();

    public static Set<Trigger> compile(JsonObject jsonData) {
        JsonElement entities = jsonData.get(KEY_ENTITY);
        Set<Trigger> triggers = new HashSet<>(compileEntities(entities));
        entities = jsonData.get(KEY_ENTITY_ARRAY);
        triggers.addAll(compileEntities(entities));
        return triggers;
    }

    private static Set<Trigger> compileEntities(JsonElement entities) {
        Set<Trigger> triggers = new HashSet<>();
        if(entities.isJsonArray()) {
            for(int i = 0; i< entities.getAsJsonArray().size(); i++) {
                Trigger trigger = compileEntity(entities.getAsJsonArray().get(i).getAsJsonObject());
                if(trigger!=null) triggers.add(trigger);
            }
        } else {
            Trigger trigger = compileEntity(entities.getAsJsonObject());
            if(trigger!=null) triggers.add(trigger);
        }
        return triggers;
    }

    private static Trigger compileEntity(JsonObject jsonObject) {
        JsonElement spawnData = jsonObject.get(KEY_SPAWN_DATA);
        VirtualEntityFactory factory = null;
        Gson gson = EntitiesPlugin.getEntitiesGsonBuilder().create();
        if(spawnData.isJsonObject()) {
            File file = new File(EntitiesPlugin.getEntitiesFolder(),spawnData.getAsString()+".json");
            try (JsonReader reader = gson.newJsonReader(new FileReader(file))) {
                factory = gson.fromJson(reader, VirtualEntityFactory.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
                factory = gson.fromJson(new JsonReader(new StringReader(jsonObject.toString())),VirtualEntityFactory.class);
        }
        if(factory==null)  return null;
        JsonElement nameData = jsonObject.get(KEY_NAME);
        if(nameData != null) {
            factory.withName(nameData.getAsString());
        }
        if(factory.getName()==null) {
            factory.withName(""+random.nextInt(10000000));
        }
        JsonElement spawnDistanceData = jsonObject.get(KEY_SPAWN_DISTANCE);
        int spawnDistance = DEFAULT_SPAWN_DISTANCE;
        if(spawnDistanceData != null) {
            spawnDistance = spawnDistanceData.getAsInt();
        }
        factory.withViewDistance((int)(spawnDistance*0.9));
        Set<Action> spawnActions = new HashSet<>();

        Set<Trigger> triggers = TriggerCompiler.compile(jsonObject);
        triggers.forEach(trigger -> spawnActions.add(new TriggerRegisterAction(trigger)));

        DecisionTreeTrigger despawnTrigger = new PeriodicServerTimeTrigger((Action)null, MCMEScripts.getConfigInt(ConfigKeys.TRIGGER_CHECKER_PERIOD,10));

        spawnActions.add(new TriggerRegisterAction(despawnTrigger));
        spawnActions.add(new SpawnAction(factory));
        DecisionTreeTrigger spawnTrigger = new PeriodicServerTimeTrigger(spawnActions, MCMEScripts.getConfigInt(ConfigKeys.TRIGGER_CHECKER_PERIOD,10));
        spawnTrigger.addCondition(new VirtualEntityProximityCondition(factory.getName(),new PlayerSelector("@a[distance=0.."+spawnDistance+"]"),
                                                                      count -> count > 0));

        Set<Action> despawnActions = new HashSet<>();
        despawnActions.add(new DespawnAction(new VirtualEntitySelector("@e[name="+factory.getName()+"]")));
        despawnActions.add(new TriggerRegisterAction(spawnTrigger));
        triggers.forEach(trigger -> despawnActions.add(new TriggerUnregisterAction(trigger)));
        DecisionTreeTrigger.DecisionNode despawnNode = new DecisionTreeTrigger.DecisionNode(despawnActions);
        despawnNode.addCondition(new VirtualEntityProximityCondition(factory.getName(),new PlayerSelector("@a[distance=0.."+spawnDistance+"]"),
                                                                     count -> count == 0));
        despawnTrigger.setDecisionNode(despawnNode);

        return spawnTrigger;
    }
}
