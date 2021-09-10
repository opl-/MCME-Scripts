package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;

public class SelectorCompiler {

    private static final String KEY_SELECTOR = "select";

    public static PlayerSelector compilePlayerSelector(JsonObject jsonObject) {
        String selectorData = getSelectorData(jsonObject);
        return new PlayerSelector(selectorData);
    }

    public static VirtualEntitySelector compileVirtualEntitySelector(JsonObject jsonObject) {
        String selectorData = getSelectorData(jsonObject);
        return new VirtualEntitySelector(selectorData);
    }

    private static String getSelectorData(JsonObject jsonObject) {
        JsonElement selectorJson = jsonObject.get(KEY_SELECTOR);
        if(selectorJson == null) return "@s";
        return selectorJson.getAsString();
    }
}
