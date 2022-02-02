package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import sun.rmi.runtime.Log;

import java.util.Arrays;
import java.util.logging.Logger;

public class SpeechBalloonLayoutCompiler {

    public static final String KEY_LAYOUT       = "layout",

                               KEY_MESSAGE      = "message",
                               KEY_DURATION     = "duration";

    public static SpeechBalloonLayout compile(JsonObject jsonObject) {
        JsonElement element = jsonObject.get(KEY_LAYOUT);
        SpeechBalloonLayout layout;
        if(element == null) {
            layout = new SpeechBalloonLayout();
        } else {
            layout = EntitiesPlugin.getEntitiesGsonBuilder().create().fromJson(element.toString(),SpeechBalloonLayout.class);
        }
        JsonElement message = jsonObject.get(KEY_MESSAGE);
        if(message!=null) {
            layout.withMessage(message.getAsString());
        }
        JsonElement duration = jsonObject.get(KEY_DURATION);
        if(message!=null) {
            layout.withDuration(duration.getAsInt());
        }
        return layout;
    }
}
