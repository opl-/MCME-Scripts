package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

public class VirtualEntityFactoryCompiler {

    private static final String KEY_SPAWN_DATA        = "spawn_data",
                                KEY_NAME              = "name";

    private static final Random random = new Random();

    public static List<VirtualEntityFactory> compile(JsonObject jsonObject) {
        return compile(jsonObject,KEY_SPAWN_DATA);
    }

    public static List<VirtualEntityFactory> compile(JsonObject jsonObject, String key) {
        List<VirtualEntityFactory> result = new ArrayList<>();
        JsonElement element = jsonObject.get(key);
        if (element == null) {
            DebugManager.warn(Modules.Trigger.create(VirtualEntityFactoryCompiler.class),"Can't compile VirtualEntityFactory. Missing spawn data.");
            return result;
        }

        Gson gson = EntitiesPlugin.getEntitiesGsonBuilder().create();
        if(element.isJsonPrimitive()) {
            File file = new File(EntitiesPlugin.getEntitiesFolder(),element.getAsString()+".json");
            try (JsonReader reader = gson.newJsonReader(new FileReader(file))) {
                reader.beginArray();
                while(reader.hasNext()) {
                    result.add(gson.fromJson(reader, VirtualEntityFactory.class));
                }
                reader.endArray();
            } catch (IOException e) {
                DebugManager.warn(Modules.Trigger.create(VirtualEntityFactoryCompiler.class),"Can't compile VirtualEntityFactory. Invalid data in external file.");
            }
        } else if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            for(int i = 0; i< jsonArray.size();i++) {
                compileObject(gson, jsonArray.get(i).getAsJsonObject()).ifPresent(result::add);
            }
        } else if(element.isJsonObject()) {
            compileObject(gson, element.getAsJsonObject()).ifPresent(result::add);
        }

        JsonElement nameData = jsonObject.get(KEY_NAME);
        String groupName;
        if (nameData != null) {
            groupName = nameData.getAsString();
        } else if(result.size() == 1 && result.get(0).getName() != null) {
            groupName = result.get(0).getName();
        } else {
            groupName = ""+random.nextInt(100000000);
        }
        int i = 0;
        for(VirtualEntityFactory factory: result) {
            if(result.size()==1) {
                factory.withName(groupName);
            } else if(factory.getName()!=null) {
                factory.withName(groupName+"__"+factory.getName());
            } else {
                factory.withName(groupName + "__" + i);
                i++;
            }
        }
        return result;
    }

    private static Optional<VirtualEntityFactory> compileObject(Gson gson, JsonObject jsonObject) {
        VirtualEntityFactory factory
            = gson.fromJson(new JsonReader(new StringReader(jsonObject.toString())),VirtualEntityFactory.class);
        return Optional.of(factory);
        /*return VirtualEntityFactory.getDefaults();
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
        return factory;*/
    }

    public static String getGroupName(List<VirtualEntityFactory> factories) {
        if(factories==null || factories.isEmpty()) return "no entities";
        String[] split = factories.get(0).getName().split("__");
        return split[split.length-1];
    }

}
