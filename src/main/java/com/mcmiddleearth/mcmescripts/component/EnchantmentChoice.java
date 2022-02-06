package com.mcmiddleearth.mcmescripts.component;

import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class EnchantmentChoice {

    private final int weight;

    private final Set<WrappedEnchantment> enchantments;

    public EnchantmentChoice(int weight, Set<WrappedEnchantment> enchantments) {
        this.weight = weight;
        this.enchantments = enchantments;
    }

    public int getWeight() {
        return weight;
    }

    public Set<WrappedEnchantment> getEnchantments() {
        return enchantments;
    }
}
