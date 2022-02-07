package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.component.EnchantmentChoice;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.looting.ItemChoice;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LootTableCompiler {

    public static Optional<Set<ItemChoice>> compileItemChoices(JsonObject jsonObject) {
        Set<ItemChoice> itemWeightChoices = new HashSet<>();
        JsonElement itemChoicesJson = jsonObject.get(ActionCompiler.KEY_CHOICES);
        if(itemChoicesJson instanceof JsonArray) {
            itemChoicesJson.getAsJsonArray().forEach(element -> {
                if(element instanceof JsonObject) {
                    int weight = PrimitiveCompiler.compileInteger(element.getAsJsonObject().get(ActionCompiler.KEY_WEIGHT),10);
                    Set<ItemStack> choiceItems = ItemCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ITEM));
                    choiceItems.addAll(ItemCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ITEMS)));
                    itemWeightChoices.add(new ItemChoice(weight,choiceItems));
                }
            });
        }
        if(itemWeightChoices.isEmpty()) return Optional.empty();
        return Optional.of(itemWeightChoices);
    }

    public static Optional<Set<EnchantmentChoice>> compileEnchantmentChoices(JsonObject jsonObject) {
        Set<EnchantmentChoice> enchantmentWeightChoices = new HashSet<>();
        JsonElement itemChoicesJson = jsonObject.get(ActionCompiler.KEY_CHOICES);
        if(itemChoicesJson instanceof JsonArray) {
            itemChoicesJson.getAsJsonArray().forEach(element -> {
                if(element instanceof JsonObject) {
                    int weight = PrimitiveCompiler.compileInteger(element.getAsJsonObject().get(ActionCompiler.KEY_WEIGHT),10);
                    Set<WrappedEnchantment> choiceEnchantments = EnchantmentCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ENCHANTMENT));
                    choiceEnchantments.addAll(EnchantmentCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ENCHANTMENTS)));
                    enchantmentWeightChoices.add(new EnchantmentChoice(weight,choiceEnchantments));
                }
            });
        }
        if(enchantmentWeightChoices.isEmpty()) return Optional.empty();
        return Optional.of(enchantmentWeightChoices);
    }
}
