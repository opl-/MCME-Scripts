package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.component.ItemFilter;
import com.mcmiddleearth.mcmescripts.component.ItemPropertyState;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;

import java.util.*;

public class ItemFilterCompiler {

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
        KEY_CUSTOM_MODEL_DATA = "custom_model_data",

        KEY_PRESENT = "present",
        KEY_VALUE = "value";

    public static Set<ItemFilter> compile(JsonElement jsonElement) {
        Set<ItemFilter> result = new HashSet<>();
        if(jsonElement instanceof JsonObject) {
            result.add(compileItemFilter(jsonElement.getAsJsonObject()));
        } else if(jsonElement instanceof JsonArray) {
            jsonElement.getAsJsonArray().forEach(element -> {
                if(element instanceof JsonObject) {
                    result.add(compileItemFilter(jsonElement.getAsJsonObject()));
                }
//                else if(element instanceof JsonPrimitive) {
//                    compileItemFilter(element.getAsString(),1).ifPresent(result::add);
//                }
            });
        }
//        else if(jsonElement instanceof JsonPrimitive) {
//            compileItemFilter(jsonElement.getAsString(),1).ifPresent(result::add);
//        }
        return result;
    }

    public static ItemFilter compileItemFilter(JsonObject jsonObject) {
        AbstractMap.SimpleEntry<ItemPropertyState, Material> material = null;
        AbstractMap.SimpleEntry<ItemPropertyState, Integer> quantity = null;
        AbstractMap.SimpleEntry<ItemPropertyState, Integer> damage = null;
        AbstractMap.SimpleEntry<ItemPropertyState, String> name = null;
        AbstractMap.SimpleEntry<ItemPropertyState, List<String>> lore = null;
        AbstractMap.SimpleEntry<ItemPropertyState, Set<AttributeModifier>> attributeModifiers = null;
        AbstractMap.SimpleEntry<ItemPropertyState, Set<WrappedEnchantment>> enchantments = null;
        AbstractMap.SimpleEntry<ItemPropertyState, Integer> customModelData = null;



        JsonObject nameJson = jsonObject.getAsJsonObject(KEY_NAME);
        if (nameJson != null) {
            lore = new AbstractMap.SimpleEntry<>(getPropertyState(nameJson.get(KEY_PRESENT)), getLore(nameJson.get(KEY_VALUE)));
        }

        JsonObject loreJson = jsonObject.getAsJsonObject(KEY_LORE);
        if (loreJson != null) {
            lore = new AbstractMap.SimpleEntry<>(getPropertyState(loreJson.get(KEY_PRESENT)), getLore(loreJson.get(KEY_VALUE)));
        }

        return new ItemFilter(material, quantity, damage, name, lore, attributeModifiers, enchantments, customModelData, equipmentSlot);
    }

//    public static Optional<ItemFilter> compileItemFilter(String material, int quantity){
//        Material materialFilter = compileMaterial(material).orElse(null);
//
//        ItemFilter itemFilter = new ItemFilter(materialFilter, quantity);
//        return Optional.of(itemFilter);
//    }

    public static ItemPropertyState getPropertyState(JsonElement propertyJson) {
        return ItemPropertyState.valueOf(propertyJson.getAsString());
    }

    public static String getName(JsonElement nameJson) {
        return nameJson.getAsString();
    }

    public static List<String> getLore(JsonElement loreJson) {
        List<String> lore = new ArrayList<>();
        lore.add(loreJson.getAsString());
        return lore;
    }

//    public static Optional<Material> compileMaterial(String material) {
//        try {
//            return Optional.of(Material.valueOf(material.toUpperCase()));
//        } catch(IllegalArgumentException ex) {
//            DebugManager.warn(Modules.Item.create(ItemFilterCompiler.class),"Illegal material, can't compile item.");
//            return Optional.empty();
//        }
//    }
//
//    private static void addEnchantments(ItemMeta meta, JsonElement enchantJson) {
//        if(enchantJson instanceof JsonArray) {
//            enchantJson.getAsJsonArray().forEach(element -> addEnchantment(meta, element));
//        } else if(enchantJson instanceof JsonObject) {
//            addEnchantment(meta, enchantJson);
//        }
//    }
//
//    private static void addEnchantment(ItemMeta meta, JsonElement enchantJson) {
//        try {
//            JsonObject jsonObject = enchantJson.getAsJsonObject();
//            int level = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_LEVEL),1);
//            NamespacedKey key = NamespacedKey.minecraft(jsonObject.get(KEY_TYPE).getAsString());
//            meta.addEnchant(Objects.requireNonNull(Enchantment.getByKey(key)),level,true);
//        } catch(IllegalStateException | ClassCastException | NullPointerException ex) {
//            DebugManager.warn(Modules.Item.create(ItemFilterCompiler.class), "Can't compile Enchantment: "+ex.getMessage());
//        }
//    }
//
//    private static void addAttributeModifiers(ItemMeta meta, JsonElement modJson) {
//        if(modJson instanceof JsonArray) {
//            modJson.getAsJsonArray().forEach(element -> addAttributeModifier(meta, element));
//        } else if(modJson instanceof JsonObject) {
//            addAttributeModifier(meta, modJson);
//        }
//    }
//
//    private static void addAttributeModifier(ItemMeta meta, JsonElement modJson) {
//        try {
//            JsonObject jsonObject = modJson.getAsJsonObject();
//            int amount = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_AMOUNT),1);
//            EquipmentSlot slot = null;
//            if(jsonObject.get(KEY_SLOT) != null) {
//                slot = EquipmentSlot.valueOf(jsonObject.get(KEY_SLOT).getAsString().toUpperCase());
//            }
//            AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(jsonObject.get(KEY_OPERATION).getAsString().toUpperCase());
//            Attribute attribute = Attribute.valueOf(jsonObject.get(KEY_ATTRIBUTE).getAsString().toUpperCase());
//            String name = jsonObject.get(KEY_NAME).getAsString();
//            UUID uuid = UUID.randomUUID();
//            AttributeModifier mod;
//            if(slot!=null) {
//                mod = new AttributeModifier(uuid, name, amount, operation, slot);
//            } else {
//                mod = new AttributeModifier(uuid, name, amount, operation);
//            }
////Logger.getGlobal().info("Modfiers: "+(meta.getAttributeModifiers()!=null?meta.getAttributeModifiers().size():"null"));
//            meta.addAttributeModifier(attribute,mod);
////Logger.getGlobal().info("add mod: "+name+" "+amount+" "+attribute.name());
////Logger.getGlobal().info("Modfiers: "+(meta.getAttributeModifiers()!=null?meta.getAttributeModifiers().size():"null"));
//        } catch(IllegalStateException | ClassCastException | NullPointerException | IllegalArgumentException ex) {
//            DebugManager.warn(Modules.Item.create(ItemFilterCompiler.class), "Can't compile attribute modifier: "+ex.getMessage());
//        }
//    }


}
