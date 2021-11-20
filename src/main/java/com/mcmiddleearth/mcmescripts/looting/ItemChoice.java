package com.mcmiddleearth.mcmescripts.looting;

import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ItemChoice {

    private final int weight;

    private final Set<ItemStack> items;

    public ItemChoice(int weight, Set<ItemStack> items) {
        this.weight = weight;
        this.items = items;
    }

    public int getWeight() {
        return weight;
    }

    public Set<ItemStack> getItems() {
        return items;
    }
}
