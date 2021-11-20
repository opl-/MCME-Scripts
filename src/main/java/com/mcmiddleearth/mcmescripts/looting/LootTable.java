package com.mcmiddleearth.mcmescripts.looting;

import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class LootTable {

    private final Set<ItemChoice> choices;

    private static final Random random = new Random();

    public LootTable(Set<ItemChoice> choices) {
        this.choices = choices;
    }

    public Set<ItemStack> selectItems() {
        if(choices == null) return Collections.emptySet();

        int weightSum = 0;
        for(ItemChoice choice: choices) {
            weightSum+=choice.getWeight();
        }
        int weightRandom = random.nextInt(weightSum+1);
        weightSum = 0;
        for(ItemChoice choice: choices) {
            weightSum+=choice.getWeight();
            if(weightSum>=weightRandom) {
                return choice.getItems();
            }
        }
        return Collections.emptySet();
    }

}
