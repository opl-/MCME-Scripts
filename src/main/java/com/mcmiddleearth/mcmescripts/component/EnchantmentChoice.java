package com.mcmiddleearth.mcmescripts.component;

import com.mcmiddleearth.mcmescripts.debug.Descriptor;
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

    public Descriptor getDescriptor() {
        Descriptor descriptor = new Descriptor();
        if(!enchantments.isEmpty()) {
            descriptor.addLine("Enchantment choices: ").indent();
            enchantments.forEach(enchantment -> descriptor.add(enchantment.getDescriptor()));
            descriptor.outdent();
        } else {
            descriptor.addLine("Enchantment choices: --none--");
        }
        return descriptor;
    }
}
