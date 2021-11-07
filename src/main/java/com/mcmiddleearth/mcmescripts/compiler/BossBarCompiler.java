package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

import java.util.ArrayList;
import java.util.List;

public class BossBarCompiler {

    public static BarStyle compileBarStyle(JsonElement element) {
        if(element instanceof JsonPrimitive) {
            try {
                return BarStyle.valueOf(element.getAsString().toUpperCase());
            } catch (IllegalArgumentException ex) {
                DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Ignoring invalid bar style data!");
            }
        }
        return null;
    }

    public static BarColor compileBarColor(JsonElement element) {
        if(element instanceof JsonPrimitive) {
            try {
                return BarColor.valueOf(element.getAsString().toUpperCase());
            } catch (IllegalArgumentException ex) {
                DebugManager.warn(Modules.Action.create(ActionCompiler.class), "Ignoring invalid bar color data!");
            }
        }
        return null;
    }

    public static BarFlag[] compileBarFlags(Boolean fog, Boolean dark, Boolean music) {
        List<BarFlag> flagList = new ArrayList<>();
        if(fog!=null && fog) flagList.add(BarFlag.CREATE_FOG);
        if(dark!=null && dark) flagList.add(BarFlag.DARKEN_SKY);
        if(music!=null && music) flagList.add(BarFlag.PLAY_BOSS_MUSIC);
        return flagList.toArray(new BarFlag[0]);
    }

}
