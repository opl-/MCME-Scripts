package com.mcmiddleearth.mcmescripts.looting;

import com.mcmiddleearth.mcmescripts.component.EnchantmentChoice;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class LootTable {

    private final Set<ItemChoice> itemChoices;
    private final Set<EnchantmentChoice> enchantmentChoices;

    private static final Random random = new Random();

    public LootTable(Set<?> choices) {
        itemChoices = new HashSet<>();
        enchantmentChoices = new HashSet<>();
        if(choices != null) {
            choices.forEach(choice -> {
                if (choice instanceof ItemChoice) {
                    itemChoices.add((ItemChoice) choice);
                } else if (choice instanceof EnchantmentChoice) {
                    enchantmentChoices.add((EnchantmentChoice) choice);
                }
            });
        }
    }

    public Set<ItemStack> selectItems() {
        if(itemChoices == null) return Collections.emptySet();

        int weightSum = 0;
        for(ItemChoice choice: itemChoices) {
            weightSum+=choice.getWeight();
        }
        int weightRandom = random.nextInt(weightSum+1);
        weightSum = 0;
        for(ItemChoice choice: itemChoices) {
            weightSum+=choice.getWeight();
            if(weightSum>=weightRandom) {
                return choice.getItems();
            }
        }
        return Collections.emptySet();
    }

    public Set<WrappedEnchantment> selectEnchantments() {
        if(enchantmentChoices == null) return Collections.emptySet();

        int weightSum = 0;
        for(EnchantmentChoice choice: enchantmentChoices) {
            weightSum+=choice.getWeight();
        }
        int weightRandom = random.nextInt(weightSum+1);
        weightSum = 0;
        for(EnchantmentChoice choice: enchantmentChoices) {
            weightSum+=choice.getWeight();
            if(weightSum>=weightRandom) {
                return choice.getEnchantments();
            }
        }
        return Collections.emptySet();
    }

}
