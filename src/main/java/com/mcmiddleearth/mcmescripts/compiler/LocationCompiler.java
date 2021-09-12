package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

public class LocationCompiler {

    //private static final String KEY_LOCATION  = "location";

    public static Optional<Location> compile(JsonElement element) {
        //JsonElement element = jsonObject.get(KEY_LOCATION);
        if(element == null) return Optional.empty();
        String[] split = element.getAsString().replace(" ","").split(",");
        if(split.length<4) return Optional.empty();
        World world = Bukkit.getWorld(split[0]);
        if(world == null) return Optional.empty();
        try {
            double x = Double.parseDouble(split[1]);
            double y = Double.parseDouble(split[2]);
            double z = Double.parseDouble(split[3]);
            return Optional.of(new Location(world, x, y, z));
        } catch(NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
