package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;
import java.util.logging.Logger;

public class PotionEffectCompiler {

    private static final String
    KEY_TYPE        = "type",
    KEY_DURATION    = "duration",
    KEY_PARTICLES   = "particles",
    KEY_AMBIENT     = "ambient",
    KEY_AMPLIFIER   = "amplifier",
    KEY_ICON        = "icon";

    public static PotionEffect compile(JsonElement jsonElement) {
//Logger.getGlobal().info("1");
        if(! (jsonElement instanceof JsonObject)) return null;
//Logger.getGlobal().info("2");
        JsonElement typeJson = jsonElement.getAsJsonObject().get(KEY_TYPE);
        if(!(typeJson instanceof JsonPrimitive)) return null;
        PotionEffectType type = PotionEffectType.getByName(typeJson.getAsString().toUpperCase());
//Logger.getGlobal().info("3: "+typeJson.getAsString()+" "+type);
        JsonElement durationJson = jsonElement.getAsJsonObject().get(KEY_DURATION);
        int duration = 200;
        if(durationJson instanceof JsonPrimitive) {
            duration = durationJson.getAsInt();
        }
        JsonElement particlesJson = jsonElement.getAsJsonObject().get(KEY_PARTICLES);
        boolean particles = true;
        if(particlesJson instanceof JsonPrimitive) {
            particles = particlesJson.getAsBoolean();
        }
        JsonElement ambientJson = jsonElement.getAsJsonObject().get(KEY_AMBIENT);
        boolean ambient = false;
        if(ambientJson instanceof JsonPrimitive) {
            ambient = ambientJson.getAsBoolean();
        }
        JsonElement amplifierJson = jsonElement.getAsJsonObject().get(KEY_AMPLIFIER);
        int amplifier = 1;
        if(amplifierJson instanceof JsonPrimitive) {
            amplifier = amplifierJson.getAsInt();
        }
        JsonElement iconJson = jsonElement.getAsJsonObject().get(KEY_ICON);
        boolean icon = true;
        if(iconJson instanceof JsonPrimitive) {
            icon = iconJson.getAsBoolean();
        }
        return new PotionEffect(type,duration,amplifier,ambient,particles,icon);
    }
}
