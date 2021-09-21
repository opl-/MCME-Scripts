package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;

import java.awt.image.Kernel;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class VirtualEntityGoalFactoryCompiler {

    private static final String KEY_GOAL        = "goal";

    public static Optional<VirtualEntityGoalFactory> compile(JsonObject jsonObject) {
        VirtualEntityGoalFactory result = null;
        JsonElement element = jsonObject.get(KEY_GOAL);
        if (element == null) return Optional.empty();

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
                e.printStackTrace();
            }
        } else if(element.isJsonObject()) {
            result = compileObject(gson, element.getAsJsonObject());
        }

        if(result == null) return Optional.empty();

        return Optional.of(result);
    }

    private static VirtualEntityGoalFactory compileObject(Gson gson, JsonObject jsonObject) {
        VirtualEntityFactory factory
                = gson.fromJson(new JsonReader(new StringReader(jsonObject.toString())),VirtualEntityFactory.class);
        return factory.getGoalFactory();
    }
}
