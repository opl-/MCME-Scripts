package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FireworkMetaCompiler {

    private static final String
        KEY_POWER                   = "power",
        KEY_COLORS                  = "colors",
        KEY_FADE_COLORS             = "fade_colors",
        KEY_EFFECTS                 = "effects",
        KEY_TYPE                    = "type",
        KEY_FLICKER                 = "flicker",
        KEY_TRAIL                   = "trail";

    public static FireworkMeta compile(JsonObject jsonObject) {
        int power = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_POWER),64);
        JsonElement effectsJson = jsonObject.get(KEY_EFFECTS);
        ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
        if(effectsJson instanceof JsonArray) {
            effectsJson.getAsJsonArray().forEach(element -> compileEffect(element.getAsJsonObject()).ifPresent(meta::addEffect));
        }
        return meta;
    }

    private static Optional<FireworkEffect> compileEffect(JsonObject jsonObject) {
        JsonElement typeJson = jsonObject.get(KEY_TYPE);
        if(!(typeJson instanceof JsonPrimitive)) {
            DebugManager.warn(Modules.Action.create(FireworkMetaCompiler.class),"Can't compile firework effect. Missing type.");
            return Optional.empty();
        }
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        try {
            type = FireworkEffect.Type.valueOf(typeJson.getAsString().toUpperCase());
        } catch (IllegalArgumentException ex) {
            DebugManager.warn(Modules.Action.create(FireworkMetaCompiler.class),"Can't parse firework effect type. Using BALL.");
        }
        return Optional.of(FireworkEffect.builder()
                  .trail(PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_TRAIL), false))
                  .flicker(PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_FLICKER), false))
                  .with(type)
                  .withColor(compileColors(jsonObject.get(KEY_COLORS)))
                  .withFade(compileColors(jsonObject.get(KEY_FADE_COLORS)))
                  .build());
    }

    private static Collection<Color> compileColors(JsonElement element) {
        List<Color> result = new ArrayList<>();
        if(element instanceof JsonArray) {
            element.getAsJsonArray().forEach(entry -> result.add(compileColor(entry.getAsJsonObject())));
        }
        return result;
    }

    private static Color compileColor(JsonObject jsonObject) {
        String[] rgb = jsonObject.getAsString().replace(" ","").split(",");
        int red = 255, green = 255, blue = 255;
        try {
            red = Integer.parseInt(rgb[0]);
            green = Integer.parseInt(rgb[1]);
            blue = Integer.parseInt(rgb[2]);
        } catch(NumberFormatException | IndexOutOfBoundsException ex) {
            DebugManager.warn(Modules.Action.create(FireworkMetaCompiler.class),"Can't parse color. Using white.");
        }
        return Color.fromRGB(red,green,blue);
    }


}
