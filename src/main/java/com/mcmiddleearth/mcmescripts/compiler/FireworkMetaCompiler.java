package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkMetaCompiler {

    private static final String
        KEY_FIREWORK                = "firework",
        KEY_POWER                   = "power",
        KEY_COLORS                  = "colors",
        KEY_FADE_COLORS             = "fade_colors",
        KEY_EFFECTS                 = "effects",
        KEY_TYPE                    = "type",
        KEY_FLICKER                 = "flicker",
        KEY_TRAIL                   = "trail";

    public FireworkMeta compile(JsonObject jsonObject) {

    }

    private FireworkEffect compileEffect(JsonObject jsonObject) {

    }

    private Color compileColor(JsonObject jsonObject) {
        String[] rgb = jsonObject.getAsString().replace(" ","").split(",");
        int red = 255, green = 255, blue = 255;
        try {
            red = Integer.parseInt(rgb[0]);
            green = Integer.parseInt(rgb[1]);
            blue = Integer.parseInt(rgb[2]);
        } catch(NumberFormatException | IndexOutOfBoundsException ex) {
            DebugManager.warn(Modules.Action.create(this.getClass()),"Can't parse color. Using white.");
        }
        return Color.fromRGB(red,green,blue);
    }


}
