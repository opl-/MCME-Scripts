package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.component.ItemFilter;
import com.mcmiddleearth.mcmescripts.component.ItemPropertyState;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

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
        KEY_EQUIPMENT_SLOT  = "equipment_slot",

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
        AbstractMap.SimpleEntry<ItemPropertyState, EquipmentSlot> equipmentSlot = null;

        JsonObject materialJson = jsonObject.getAsJsonObject(KEY_MATERIAL);
        if (materialJson != null) {
            Material materialCheck = getMaterial(materialJson.get(KEY_VALUE));
            if (materialCheck != null)
                material = new AbstractMap.SimpleEntry<>(getPropertyState(materialJson.get(KEY_PRESENT)), materialCheck);
        }

        JsonObject quantityJson = jsonObject.getAsJsonObject(KEY_QUANTITY);
        if (quantityJson != null) {
            int quantityCheck = getInteger(quantityJson.get(KEY_VALUE));
            quantity = new AbstractMap.SimpleEntry<>(getPropertyState(quantityJson.get(KEY_PRESENT)), quantityCheck);
        }

        JsonObject damageJson = jsonObject.getAsJsonObject(KEY_DAMAGE);
        if (damageJson != null) {
            int damageCheck = getInteger(damageJson.get(KEY_VALUE));
            damage = new AbstractMap.SimpleEntry<>(getPropertyState(damageJson.get(KEY_PRESENT)), damageCheck);
        }

        JsonObject nameJson = jsonObject.getAsJsonObject(KEY_NAME);
        if (nameJson != null) {
            String nameCheck = getName(nameJson.get(KEY_VALUE));
            if (nameCheck != null)
                name = new AbstractMap.SimpleEntry<>(getPropertyState(nameJson.get(KEY_PRESENT)), nameCheck);
        }

        JsonObject loreJson = jsonObject.getAsJsonObject(KEY_LORE);
        if (loreJson != null) {
            List<String> loreCheck = getLore(loreJson.get(KEY_VALUE));
            if (!loreCheck.isEmpty())
                lore = new AbstractMap.SimpleEntry<>(getPropertyState(loreJson.get(KEY_PRESENT)), loreCheck);
        }

        JsonObject enchantmentJson = jsonObject.getAsJsonObject(KEY_ENCHANTMENT);
        if (enchantmentJson != null) {
            Set<WrappedEnchantment> wrappedEnchantmentsCheck = getEnchantments(enchantmentJson.get(KEY_VALUE));
            if (!wrappedEnchantmentsCheck.isEmpty())
                enchantments = new AbstractMap.SimpleEntry<>(getPropertyState(enchantmentJson.get(KEY_PRESENT)), wrappedEnchantmentsCheck);
        }
        JsonObject enchantmentsJson = jsonObject.getAsJsonObject(KEY_ENCHANTMENTS);
        if (enchantmentsJson != null) {
            Set<WrappedEnchantment> wrappedEnchantmentsCheck = getEnchantments(enchantmentsJson.get(KEY_VALUE));
            if (!wrappedEnchantmentsCheck.isEmpty())
                if (enchantments == null)
                    enchantments = new AbstractMap.SimpleEntry<>(getPropertyState(enchantmentsJson.get(KEY_PRESENT)), wrappedEnchantmentsCheck);
                else
                    enchantments.getValue().addAll(wrappedEnchantmentsCheck);
        }

        JsonObject attributeModifierJson = jsonObject.getAsJsonObject(KEY_ATTRIBUTE_MOD);
        if (attributeModifierJson != null) {
            Set<AttributeModifier> attributeModifiersCheck = getAttributeModifiers(attributeModifierJson.get(KEY_VALUE));
            if (!attributeModifiersCheck.isEmpty())
                attributeModifiers = new AbstractMap.SimpleEntry<>(getPropertyState(attributeModifierJson.get(KEY_PRESENT)), attributeModifiersCheck);
        }
        JsonObject attributeModifiersJson = jsonObject.getAsJsonObject(KEY_ATTRIBUTE_MODS);
        if (attributeModifiersJson != null) {
            Set<AttributeModifier> attributeModifiersCheck = getAttributeModifiers(attributeModifiersJson.get(KEY_VALUE));
            if (!attributeModifiersCheck.isEmpty())
                if (attributeModifiers == null)
                    attributeModifiers = new AbstractMap.SimpleEntry<>(getPropertyState(attributeModifiersJson.get(KEY_PRESENT)), attributeModifiersCheck);
                else
                    attributeModifiers.getValue().addAll(attributeModifiersCheck);
        }

        JsonObject customModelDataJson = jsonObject.getAsJsonObject(KEY_CUSTOM_MODEL_DATA);
        if (customModelDataJson != null) {
            int customModelDataCheck = getInteger(customModelDataJson.get(KEY_VALUE));
            customModelData = new AbstractMap.SimpleEntry<>(getPropertyState(customModelDataJson.get(KEY_PRESENT)), customModelDataCheck);
        }

        JsonObject equipmentJson = jsonObject.getAsJsonObject(KEY_EQUIPMENT_SLOT);
        if (loreJson != null) {
            EquipmentSlot equipmentSlotCheck = getEquipmentSlot(equipmentJson.get(KEY_VALUE));
            if (equipmentSlotCheck != null)
                equipmentSlot = new AbstractMap.SimpleEntry<>(getPropertyState(equipmentJson.get(KEY_PRESENT)), equipmentSlotCheck);
        }

        return new ItemFilter(material, quantity, damage, name, lore, attributeModifiers, enchantments, customModelData, equipmentSlot);
    }

//    public static Optional<ItemFilter> compileItemFilter(String material, int quantity){
//        Material materialFilter = compileMaterial(material).orElse(null);
//
//        ItemFilter itemFilter = new ItemFilter(materialFilter, quantity);
//        return Optional.of(itemFilter);
//    }

    private static ItemPropertyState getPropertyState(JsonElement propertyJson) {
        return ItemPropertyState.valueOf(propertyJson.getAsString());
    }

    private static Material getMaterial(JsonElement materialJson) {
        return Material.valueOf(materialJson.getAsString());
    }

    private static int getInteger(JsonElement quantityJson) {
        return quantityJson.getAsInt();
    }

    private static String getName(JsonElement nameJson) {
        return nameJson.getAsString();
    }

    private static List<String> getLore(JsonElement loreJson) {
        List<String> lore = new ArrayList<>();
        lore.add(loreJson.getAsString());
        return lore;
    }

    private static Set<AttributeModifier> getAttributeModifiers(JsonElement modJson) {
        Set<AttributeModifier> attributeModifiers = new HashSet<>();
        if(modJson instanceof JsonArray) {
            modJson.getAsJsonArray().forEach(element -> {
                AttributeModifier attributeModifierCheck = getAttributeModifier(modJson);
                if (attributeModifierCheck != null)
                    attributeModifiers.add(attributeModifierCheck);
            });
        } else if(modJson instanceof JsonObject) {
            AttributeModifier attributeModifierCheck = getAttributeModifier(modJson);
            if (attributeModifierCheck != null)
                attributeModifiers.add(attributeModifierCheck);
        }
        return attributeModifiers;
    }

    private static AttributeModifier getAttributeModifier(JsonElement modJson) {
        try {
            JsonObject jsonObject = modJson.getAsJsonObject();

            EquipmentSlot slot = null;
            int amount = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_AMOUNT),1);

            if(jsonObject.get(KEY_SLOT) != null) {
                slot = EquipmentSlot.valueOf(jsonObject.get(KEY_SLOT).getAsString().toUpperCase());
            }
            AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(jsonObject.get(KEY_OPERATION).getAsString().toUpperCase());
            String name = jsonObject.get(KEY_NAME).getAsString();
            UUID uuid = UUID.randomUUID();
            AttributeModifier attributeModifier;
            if(slot!=null) {
                attributeModifier = new AttributeModifier(uuid, name, amount, operation, slot);
            } else {
                attributeModifier = new AttributeModifier(uuid, name, amount, operation);
            }
            return attributeModifier;
        } catch(IllegalStateException | ClassCastException | NullPointerException | IllegalArgumentException ex) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Can't compile attribute modifier: "+ex.getMessage());
        }

        return null;
    }

    private static Set<WrappedEnchantment> getEnchantments(JsonElement enchantmentsJson) {
        Set<WrappedEnchantment> wrappedEnchantments = new HashSet<>();
        if(enchantmentsJson instanceof JsonArray) {
            enchantmentsJson.getAsJsonArray().forEach(element -> {
                WrappedEnchantment wrappedEnchantmentCheck = getEnchantment(enchantmentsJson);
                if (wrappedEnchantmentCheck != null)
                    wrappedEnchantments.add(wrappedEnchantmentCheck);
            });
        } else if(enchantmentsJson instanceof JsonObject) {
            WrappedEnchantment wrappedEnchantmentCheck = getEnchantment(enchantmentsJson);
            if (wrappedEnchantmentCheck != null)
                wrappedEnchantments.add(wrappedEnchantmentCheck);
        }
        return wrappedEnchantments;
    }

    private static WrappedEnchantment getEnchantment(JsonElement enchantmentJson) {
        try {
            JsonObject jsonObject = enchantmentJson.getAsJsonObject();

            int level = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_LEVEL),1);
            NamespacedKey key = NamespacedKey.minecraft(jsonObject.get(KEY_TYPE).getAsString());
            Enchantment enchantment = Enchantment.getByKey(key);

            return new WrappedEnchantment(enchantment, level);
        } catch(IllegalStateException | ClassCastException | NullPointerException | IllegalArgumentException ex) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Can't compile enchantment: "+ex.getMessage());
        }

        return null;
    }

    private static EquipmentSlot getEquipmentSlot(JsonElement equipmentJson) {
        return EquipmentSlot.valueOf(equipmentJson.getAsString());
    }


}
