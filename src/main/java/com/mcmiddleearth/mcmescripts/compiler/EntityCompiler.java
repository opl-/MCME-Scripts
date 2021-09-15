package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.mcmescripts.ConfigKeys;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.*;
import com.mcmiddleearth.mcmescripts.condition.proximity.LocationProximityCondition;
import com.mcmiddleearth.mcmescripts.condition.proximity.VirtualEntityProximityCondition;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.DecisionTreeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.PeriodicServerTimeTrigger;

import java.util.*;

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
        if(entities == null) return triggers;
        if(entities.isJsonArray()) {
            for(int i = 0; i< entities.getAsJsonArray().size(); i++) {
                compileEntity(entities.getAsJsonArray().get(i).getAsJsonObject()).ifPresent(triggers::add);
            }
        } else {
            compileEntity(entities.getAsJsonObject()).ifPresent(triggers::add);
        }
        return triggers;
    }

    private static Optional<Trigger> compileEntity(JsonObject jsonObject) {
        List<VirtualEntityFactory> factories = VirtualEntityFactoryCompiler.compile(jsonObject.get(KEY_SPAWN_DATA));
        if(factories.isEmpty())  return Optional.empty();

        JsonElement spawnDistanceData = jsonObject.get(KEY_SPAWN_DISTANCE);
        int spawnDistance = DEFAULT_SPAWN_DISTANCE;
        if(spawnDistanceData != null) {
            spawnDistance = spawnDistanceData.getAsInt();
        }

        Set<Trigger> triggers = TriggerCompiler.compile(jsonObject);
        DecisionTreeTrigger spawnTrigger = new PeriodicServerTimeTrigger(null, MCMEScripts.getConfigInt(ConfigKeys.TRIGGER_CHECKER_PERIOD,10));
        DecisionTreeTrigger despawnTrigger = new PeriodicServerTimeTrigger(null, MCMEScripts.getConfigInt(ConfigKeys.TRIGGER_CHECKER_PERIOD,10));

        JsonElement nameData = jsonObject.get(KEY_NAME);
        String groupName;
        if (nameData != null) {
            groupName = nameData.getAsString();
        } else if(factories.get(0).getName() != null) {
            groupName = factories.get(0).getName();
        } else {
            groupName = ""+random.nextInt(100000000);
        }
        for(int i = 0; i< factories.size(); i++) {
            VirtualEntityFactory factory = factories.get(i);
            factory.withViewDistance((int) (spawnDistance * 0.9));
            factory.withName(groupName+"_"+i);
        }

        Set<Action> spawnActions = new HashSet<>();
        spawnActions.add(new SpawnAction(factories));
        triggers.forEach(trigger -> spawnActions.add(new TriggerRegisterAction(trigger)));
        spawnActions.add(new TriggerRegisterAction(despawnTrigger));
        spawnActions.add(new TriggerUnregisterAction(spawnTrigger));
        DecisionTreeTrigger.DecisionNode spawnNode = new DecisionTreeTrigger.DecisionNode(spawnActions);
        spawnNode.addCondition(new LocationProximityCondition(factories.get(0).getLocation(),new PlayerSelector("@a[distance=0.."+spawnDistance+"]"),
                count -> count > 0));
        spawnTrigger.setDecisionNode(spawnNode);


        Set<Action> despawnActions = new HashSet<>();
        despawnActions.add(new DespawnAction(new VirtualEntitySelector("@e[name="+groupName+"*]")));
        triggers.forEach(trigger -> despawnActions.add(new TriggerUnregisterAction(trigger)));
        despawnActions.add(new TriggerRegisterAction(spawnTrigger));
        despawnActions.add(new TriggerUnregisterAction(despawnTrigger));
        DecisionTreeTrigger.DecisionNode despawnNode = new DecisionTreeTrigger.DecisionNode(despawnActions);
        despawnNode.addCondition(new LocationProximityCondition(factories.get(0).getLocation(),new PlayerSelector("@a[distance=0.."+spawnDistance+"]"),
                                                                     count -> count == 0));
        despawnTrigger.setDecisionNode(despawnNode);

        return Optional.of(spawnTrigger);
    }
}
