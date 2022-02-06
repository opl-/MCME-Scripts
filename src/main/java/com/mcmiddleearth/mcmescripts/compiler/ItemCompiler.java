package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Logger;

public class ItemCompiler {

    private static final String
        KEY_MATERIAL        = "material",
        KEY_QUANTITY        = "quantity",
        KEY_DAMAGE          = "damage",
        KEY_NAME            = "name",
        KEY_LORE            = "lore",
        KEY_ATTRIBUTE       = "attribute",
        KEY_ATTRIBUTE_MOD   = "attribute_modifier",
        KEY_ATTRIBUTE_MODS  = "attribute_modifiers",
        KEY_AMOUNT          = "amount",
        KEY_OPERATION       = "operation",
        KEY_SLOT            = "slot",
        KEY_ENCHANTMENT     = "enchantment",
        KEY_ENCHANTMENTS    = "enchantments",
        KEY_LEVEL           = "level",
        KEY_TYPE            = "type",
        KEY_CUSTOM_MODEL_DATA = "custom_model_data";

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
        ItemMeta meta = item.getItemMeta();
        if(meta instanceof Damageable) {
            JsonElement damageJson = jsonObject.get(KEY_DAMAGE);
            if (damageJson instanceof JsonPrimitive) {
                try {
                    short damage = damageJson.getAsShort();
                    ((Damageable)meta).setDamage(damage);
                } catch (NumberFormatException ex) {
                    DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Number format exception, can't parse durability. Using full durability.");
                }
            }
        }
        JsonElement nameJson = jsonObject.get(KEY_NAME);
        if(nameJson instanceof JsonPrimitive) {
            meta.displayName(Component.text(nameJson.getAsString()));
        }
        addEnchantments(meta, jsonObject.get(KEY_ENCHANTMENT));
        addEnchantments(meta, jsonObject.get(KEY_ENCHANTMENTS));
        addAttributeModifiers(meta, jsonObject.get(KEY_ATTRIBUTE_MOD));
        addAttributeModifiers(meta, jsonObject.get(KEY_ATTRIBUTE_MODS));
        JsonElement loreJson = jsonObject.get(KEY_LORE);
        if(loreJson instanceof JsonArray) {
            loreJson.getAsJsonArray().forEach(element -> addLore(meta, element));
        } else {
            addLore(meta, loreJson);
        }
        int cmd = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_CUSTOM_MODEL_DATA),-1);
//Logger.getGlobal().info("CMD compiled: "+cmd+" type: "+item.getType());
        if(cmd>=0) {
            meta.setCustomModelData(cmd);
        }
        item.setItemMeta(meta);
        return Optional.of(item);
    }

    public static Optional<ItemStack> compileItem(String material, int quantity){
        Material type = compileMaterial(material).orElse(null);
        if(type == null) {
            return Optional.empty();
        }
        ItemStack item = new ItemStack(type, quantity);
        return Optional.of(item);
    }

    public static Optional<Material> compileMaterial(String material) {
        try {
            return Optional.of(Material.valueOf(material.toUpperCase()));
        } catch(IllegalArgumentException ex) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class),"Illegal material, can't compile item.");
            return Optional.empty();
        }
    }

    private static void addEnchantments(ItemMeta meta, JsonElement enchantJson) {
        if(enchantJson instanceof JsonArray) {
            enchantJson.getAsJsonArray().forEach(element -> addEnchantment(meta, element));
        } else if(enchantJson instanceof JsonObject) {
            addEnchantment(meta, enchantJson);
        }
    }

    private static void addEnchantment(ItemMeta meta, JsonElement enchantJson) {
        try {
            JsonObject jsonObject = enchantJson.getAsJsonObject();
            int level = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_LEVEL),1);
            NamespacedKey key = NamespacedKey.minecraft(jsonObject.get(KEY_TYPE).getAsString());
            meta.addEnchant(Objects.requireNonNull(Enchantment.getByKey(key)),level,true);
        } catch(IllegalStateException | ClassCastException | NullPointerException ex) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Can't compile Enchantment: "+ex.getMessage());
        }
    }

    private static void addAttributeModifiers(ItemMeta meta, JsonElement modJson) {
        if(modJson instanceof JsonArray) {
            modJson.getAsJsonArray().forEach(element -> addAttributeModifier(meta, element));
        } else if(modJson instanceof JsonObject) {
            addAttributeModifier(meta, modJson);
        }
    }

    private static void addAttributeModifier(ItemMeta meta, JsonElement modJson) {
        try {
            JsonObject jsonObject = modJson.getAsJsonObject();
            int amount = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_AMOUNT),1);
            EquipmentSlot slot = null;
            if(jsonObject.get(KEY_SLOT) != null) {
                slot = EquipmentSlot.valueOf(jsonObject.get(KEY_SLOT).getAsString().toUpperCase());
            }
            AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(jsonObject.get(KEY_OPERATION).getAsString().toUpperCase());
            Attribute attribute = Attribute.valueOf(jsonObject.get(KEY_ATTRIBUTE).getAsString().toUpperCase());
            String name = jsonObject.get(KEY_NAME).getAsString();
            UUID uuid = UUID.randomUUID();
            AttributeModifier mod;
            if(slot!=null) {
                mod = new AttributeModifier(uuid, name, amount, operation, slot);
            } else {
                mod = new AttributeModifier(uuid, name, amount, operation);
            }
//Logger.getGlobal().info("Modfiers: "+(meta.getAttributeModifiers()!=null?meta.getAttributeModifiers().size():"null"));
            meta.addAttributeModifier(attribute,mod);
//Logger.getGlobal().info("add mod: "+name+" "+amount+" "+attribute.name());
//Logger.getGlobal().info("Modfiers: "+(meta.getAttributeModifiers()!=null?meta.getAttributeModifiers().size():"null"));
        } catch(IllegalStateException | ClassCastException | NullPointerException | IllegalArgumentException ex) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Can't compile attribute modifier: "+ex.getMessage());
        }
    }

    public static void addLore(ItemMeta meta, JsonElement loreJson) {
        try {
            List<Component> lore = new ArrayList<>();
            if(meta.hasLore()) {
                Logger.getGlobal().info("add Lore: " + meta.lore().size());
                lore.addAll(Objects.requireNonNull(meta.lore()));
            }
            lore.add(Component.text(loreJson.getAsString()));
            meta.lore(lore);
//Logger.getGlobal().info("add Lore: "+meta.lore().size());
        } catch(NullPointerException | ClassCastException | IllegalStateException ignore) {}
    }
}
