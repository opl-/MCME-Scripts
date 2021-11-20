package com.mcmiddleearth.mcmescripts.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonUtils {

    public static JsonObject loadJsonData(File dataFile) throws IOException {
        try (FileReader reader = new FileReader(dataFile)) {
            JsonElement element =  new JsonParser().parse(new JsonReader(reader));
            if(element instanceof JsonObject) {
                return element.getAsJsonObject();
            }
        }
        return null;
    }

}
