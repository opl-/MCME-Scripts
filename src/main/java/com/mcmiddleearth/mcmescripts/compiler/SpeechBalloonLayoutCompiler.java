package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;

public class SpeechBalloonLayoutCompiler {

    public static final String KEY_LAYOUT = "layout";

    public static SpeechBalloonLayout compile(JsonObject jsonObject) {
        JsonElement element = jsonObject.get(KEY_LAYOUT);
        if(element == null) {
            return new SpeechBalloonLayout();
        } else {
            return EntitiesPlugin.getEntitiesGsonBuilder().create().fromJson(element.toString(),SpeechBalloonLayout.class);
        }
    }
}
