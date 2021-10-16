package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ItemCompiler {

    private static String
        KEY_MATERIAL        = "material",
        KEY_QUANTITY        = "quantity",
        KEY_DURABILITY      = "durability",
        KEY_LORE            = "lore",
        KEY_ENCHANTMENT     = "enchantment",
        KEY_ENCHANTMENTS    = "enchantments";

    public static Set<ItemStack> compile(JsonElement jsonElement) {
        Set<ItemStack> result = new HashSet<>();
        if(jsonElement instanceof JsonObject) {
            compileItem(jsonElement.getAsJsonObject()).ifPresent(result::add);
        } else if(jsonElement instanceof JsonArray) {
            jsonElement.getAsJsonArray().forEach(element -> {
                if(element instanceof JsonObject) {
                    compileItem(element.getAsJsonObject()).ifPresent(result::add);
                } else if(element instanceof JsonPrimitive) {
                    compileItem(element.getAsString(),1).ifPresent(result::add);
                }
            });
        } else if(jsonElement instanceof JsonPrimitive) {
            compileItem(jsonElement.getAsString(),1).ifPresent(result::add);
        }
        return result;
    }

    public static Optional<ItemStack> compileItem(JsonObject jsonObject) {
        JsonElement materialJson = jsonObject.get(KEY_MATERIAL);
        if(!(materialJson instanceof JsonPrimitive)) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Missing material, can't compile item.");
            return Optional.empty();
        }
        int quantity = 1;
        JsonElement quantityJson = jsonObject.get(KEY_QUANTITY);
        if(quantityJson instanceof JsonPrimitive) {
            try {
                quantity = quantityJson.getAsInt();
            } catch(NumberFormatException ex) {
                DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Number format exception, can't parse quantity. Using 1.");
            }
        }
        ItemStack item = compileItem(materialJson.getAsString(),quantity).orElse(null);
        if(item == null) return Optional.empty();
        JsonElement durabilityJson = jsonObject.get(KEY_DURABILITY);
        if(durabilityJson instanceof JsonPrimitive) {
            try{
                short durability = durabilityJson.getAsShort();
                item.setDurability(durability);
            } catch(NumberFormatException ex) {
                DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Number format exception, can't parse durability. Using full durability.");
            }
        }
        return Optional.of(item);
    }

    public static Optional<ItemStack> compileItem(String material, int quantity){
        Material type = compileMaterial(material).orElse(null);
        if(type == null) {
            return Optional.empty();
        }
        return Optional.of(new ItemStack(type, quantity));
    }

    public static Optional<Material> compileMaterial(String material) {
        try {
            return Optional.of(Material.valueOf(material.toUpperCase()));
        } catch(IllegalArgumentException ex) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class),"Illegal material, can't compile item.");
            return Optional.empty();
        }
    }

    public static Enchantment compileEnchantment(JsonObject jsonObject) {
        return null;
    }
}
