package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

public class VirtualEntityGoalFactoryCompiler {

    private static final String KEY_GOAL        = "goal";

    public static Optional<VirtualEntityGoalFactory> compile(JsonObject jsonObject) {
        VirtualEntityGoalFactory result = null;
        JsonElement element = jsonObject.get(KEY_GOAL);
        if (element == null) {
            DebugManager.debug(Modules.Trigger.create(VirtualEntityGoalFactoryCompiler.class),"Can't compile VirtualEntityGoalFactory. Missing goal data.");
            return Optional.empty();
        }

        Gson gson = EntitiesPlugin.getEntitiesGsonBuilder().create();
        if(element.isJsonPrimitive()) {
            File file = new File(EntitiesPlugin.getEntitiesFolder(),element.getAsString()+".json");
            try (JsonReader reader = gson.newJsonReader(new FileReader(file))) {
                reader.beginArray();
                VirtualEntityFactory factory = gson.fromJson(reader, VirtualEntityFactory.class);
                if(factory.getGoalFactory()!=null) {
                    result = factory.getGoalFactory();
                }
            } catch (IOException e) {
                DebugManager.debug(Modules.Trigger.create(VirtualEntityGoalFactoryCompiler.class),"Can't compile VirtualEntityGoalFactory. Invalid goal data in external file.");
            }
        } else if(element.isJsonObject()) {
            result = compileObject(gson, element.getAsJsonObject());
        }

        if(result == null) {
            DebugManager.debug(Modules.Trigger.create(VirtualEntityGoalFactoryCompiler.class),"Can't compile VirtualEntityGoalFactory. Unknown error.");
            return Optional.empty();
        }

        return Optional.of(result);
    }

    private static VirtualEntityGoalFactory compileObject(Gson gson, JsonObject jsonObject) {
//Logger.getGlobal().info(jsonObject.toString());
        VirtualEntityGoalFactory factory
                = gson.fromJson(new JsonReader(new StringReader(jsonObject.toString())),VirtualEntityGoalFactory.class);
//Logger.getGlobal().info("Goal: "+(factory!=null?factory.getGoalType():"null"));
        return factory;//.getGoalFactory();
    }
}
