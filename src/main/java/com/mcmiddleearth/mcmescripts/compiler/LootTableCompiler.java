package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.action.ItemGiveAction;
import com.mcmiddleearth.mcmescripts.looting.ItemChoice;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LootTableCompiler {

    public static Optional<Set<ItemChoice>> compileItemChoices(JsonObject jsonObject) {
        Set<ItemChoice> itemChoices = new HashSet<>();
        JsonElement itemChoicesJson = jsonObject.get(ActionCompiler.KEY_CHOICES);
        if(itemChoicesJson instanceof JsonArray) {
            itemChoicesJson.getAsJsonArray().forEach(element -> {
                if(element instanceof JsonObject) {
                    int weight = PrimitiveCompiler.compileInteger(element.getAsJsonObject().get(ActionCompiler.KEY_WEIGHT),10);
                    Set<ItemStack> choiceItems = ItemCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ITEM));
                    choiceItems.addAll(ItemCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ITEMS)));
                    itemChoices.add(new ItemChoice(weight,choiceItems));
                }
            });
        }
        if(itemChoices.isEmpty()) return Optional.empty();
        return Optional.of(itemChoices);
    }

}
