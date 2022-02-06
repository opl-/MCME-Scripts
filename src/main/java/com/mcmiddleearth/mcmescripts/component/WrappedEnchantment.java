package com.mcmiddleearth.mcmescripts.component;

import org.bukkit.enchantments.Enchantment;

public class WrappedEnchantment {

    private Enchantment enchantment;
    private int level;

    public WrappedEnchantment(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
