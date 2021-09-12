package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class VirtualEntityFactoryCompiler {

    public static VirtualEntityFactory compile(JsonElement element) {
        if(element == null) return VirtualEntityFactory.getDefaults();
        VirtualEntityFactory factory = VirtualEntityFactory.getDefaults();
        Gson gson = EntitiesPlugin.getEntitiesGsonBuilder().create();
        if(element.isJsonPrimitive()) {
            File file = new File(EntitiesPlugin.getEntitiesFolder(),element.getAsString()+".json");
            try (JsonReader reader = gson.newJsonReader(new FileReader(file))) {
                factory = gson.fromJson(reader, VirtualEntityFactory.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            factory = gson.fromJson(new JsonReader(new StringReader(element.toString())),VirtualEntityFactory.class);
        }
        return factory;
    }
}
