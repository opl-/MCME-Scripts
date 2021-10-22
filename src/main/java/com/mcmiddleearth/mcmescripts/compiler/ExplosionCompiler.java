package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.effect.Explosion;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ExplosionCompiler {

    private static final String
        KEY_LOCATION    = "location",
        KEY_RADIUS      = "radius",
        KEY_DAMAGE      = "damage",
        KEY_VElOCITY    = "velocity",
        KEY_UNAFFECTED  = "unaffected",
        KEY_DAMAGER     = "damager",
        KEY_KNOCKBACK   = "knockback",
        KEY_PARTICLE    = "particle";

    public static Explosion compile(JsonObject jsonObject) {
        Location location = LocationCompiler.compile(jsonObject.get(KEY_LOCATION)).orElse(null);
        double radius = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_RADIUS), 5);
        double damage = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_DAMAGE), 10);
        double velocity = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_VElOCITY),1);
        double knockback = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_KNOCKBACK),1);
        Explosion explosion = new Explosion(location, radius, damage)
                                    .setKnockback(knockback)
                                    .setVelocity(velocity);
        Particle particle = Particle.SPELL_INSTANT;
        JsonElement particleElement = jsonObject.get(KEY_PARTICLE);
        if(particleElement instanceof JsonPrimitive) {
            try {
                particle = Particle.valueOf(particleElement.getAsString());
            } catch(IllegalArgumentException ex) {
                DebugManager.warn(Modules.Action.execute(ExplosionCompiler.class),"Can't parse explosion particle. Using SPELL_INSTANT.");
            }
            explosion.setParticle(particle);
        }
        return explosion;
    }

    public static McmeEntitySelector getUnaffectedSelector(JsonObject jsonObject) {
        JsonElement selectorElement = jsonObject.get(KEY_UNAFFECTED);
        if(selectorElement instanceof JsonPrimitive) {
            return new McmeEntitySelector(selectorElement.getAsString());
        } else {
            return null;
        }
    }

    public static McmeEntitySelector getDamagerSelector(JsonObject jsonObject) {
        JsonElement selectorElement = jsonObject.get(KEY_DAMAGER);
        if(selectorElement instanceof JsonPrimitive) {
            return new McmeEntitySelector(selectorElement.getAsString());
        } else {
            return null;
        }
    }
}
