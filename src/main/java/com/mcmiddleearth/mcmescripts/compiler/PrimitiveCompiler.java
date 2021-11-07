package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;

public class PrimitiveCompiler {

    public static Double compileDouble(JsonElement element, Double defaultValue) {
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

    public static Boolean compileBoolean(JsonElement element, Boolean defaultValue) {
        if(element==null || !element.isJsonPrimitive()) {
            return defaultValue;
        } else {
            try {
                return Boolean.parseBoolean(element.getAsString());
            } catch(NumberFormatException ex) {
                return defaultValue;
            }
        }
    }

    public static int compileLowerInt(JsonElement element, int defaultValue) {
        if(element==null || !element.isJsonPrimitive()) {
            return defaultValue;
        } else {
            try {
                String[] split = element.getAsString().split("\\.\\.");
                return Integer.parseInt(split[0]);
            } catch(NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                return defaultValue;
            }
        }
    }

    public static int compileUpperInt(JsonElement element, int defaultValue) {
        if(element==null || !element.isJsonPrimitive()) {
            return defaultValue;
        } else {
            try {
                String[] split = element.getAsString().split("\\.\\.");
                return Integer.parseInt(split[1]);
            } catch(NumberFormatException ex) {
                return defaultValue;
            } catch(ArrayIndexOutOfBoundsException ex) {
                return compileLowerInt(element, defaultValue);
            }
        }
    }

    public static String compileString(JsonElement element, String defaultValue) {
        if(element==null || !element.isJsonPrimitive()) {
            return defaultValue;
        } else {
            try {
                return element.getAsString();
            } catch(ClassCastException | IllegalStateException ex) {
                return defaultValue;
            }
        }
    }
}
