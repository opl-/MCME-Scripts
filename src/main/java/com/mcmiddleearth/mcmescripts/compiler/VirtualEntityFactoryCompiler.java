package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class VirtualEntityFactoryCompiler {

    public static List<VirtualEntityFactory> compile(JsonElement element) {
        List<VirtualEntityFactory> result = new ArrayList<>();
        if (element == null) return result;
        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            for(int i = 0; i< jsonArray.size();i++) {
                compileObject(jsonArray.get(i).getAsJsonObject()).ifPresent(result::add);
            }
        } else if(element.isJsonObject()) {
            compileObject(element.getAsJsonObject()).ifPresent(result::add);
        } else {

        }
        return result;
    }

    private static Optional<VirtualEntityFactory> compileObject(JsonObject jsonObject) {

        return VirtualEntityFactory.getDefaults();
        VirtualEntityFactory factory = VirtualEntityFactory.getDefaults();
        Gson gson = EntitiesPlugin.getEntitiesGsonBuilder().create();
        if(element.isJsonPrimitive()) {
            File file = new File(EntitiesPlugin.getEntitiesFolder(),element.getAsString()+".json");
            try (JsonReader reader = gson.newJsonReader(new FileReader(file))) {
                reader.beginArray();
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
