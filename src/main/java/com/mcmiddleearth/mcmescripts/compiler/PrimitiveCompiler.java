package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;

public class PrimitiveCompiler {

    public static double compileDouble(JsonElement element, double defaultValue) {
        if(element==null || !element.isJsonPrimitive()) {
            return defaultValue;
        } else {
            try {
                return Double.parseDouble(element.getAsString());
            } catch(NumberFormatException ex) {
                return defaultValue;
            }
        }

    }

    public static int compileInteger(JsonElement element, int defaultValue) {
        if(element==null || !element.isJsonPrimitive()) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(element.getAsString());
            } catch(NumberFormatException ex) {
                return defaultValue;
            }
        }

    }
}
