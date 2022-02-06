package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EnchantmentCompiler {

    private static final String
            KEY_TYPE            = "type",
            KEY_LEVEL           = "level";

    public static Set<WrappedEnchantment> compile(JsonElement jsonElement) {
        Set<WrappedEnchantment> result = new HashSet<>();
        if(jsonElement == null) return result;
        if(jsonElement.isJsonArray()) {
            for(int i = 0; i< jsonElement.getAsJsonArray().size(); i++) {
                compileEnchantment(jsonElement.getAsJsonArray().get(i).getAsJsonObject()).ifPresent(result::add);
            }
        } else {
            compileEnchantment(jsonElement.getAsJsonObject()).ifPresent(result::add);
        }
        return result;
    }

    public static Optional<WrappedEnchantment> compileEnchantment(JsonObject jsonObject) {
        try {
            JsonObject enchantJson = jsonObject.getAsJsonObject();
            int level = PrimitiveCompiler.compileInteger(enchantJson.get(KEY_LEVEL),1);
            NamespacedKey key = NamespacedKey.minecraft(enchantJson.get(KEY_TYPE).getAsString());
            return Optional.of(new WrappedEnchantment(Enchantment.getByKey(key),level));
        } catch(IllegalStateException | ClassCastException | NullPointerException ex) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Can't compile Enchantment: " + ex.getMessage());
        }
        return Optional.empty();
    }
}
