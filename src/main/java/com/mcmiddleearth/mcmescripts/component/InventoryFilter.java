package com.mcmiddleearth.mcmescripts.component;

import java.util.Set;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mcmiddleearth.mcmescripts.debug.Descriptor;

/**
 * Allows counting and performing actions on items in an inventory using a set of item filters.
 */
public class InventoryFilter {
    private final Set<ItemFilter> itemFilters;
    private final boolean requireAllFilters;

    public InventoryFilter(Set<ItemFilter> itemFilters, boolean requireAllFilters) {
        this.itemFilters = itemFilters;
        this.requireAllFilters = requireAllFilters;
    }

    public int count(Inventory inventory) {
        int itemsFound = 0;

        for(ItemStack stack : inventory) {
            if (checkStack(itemFilters, requireAllFilters, stack, inventory)) {
                itemsFound += stack.getAmount();
            }
        }

        return itemsFound;
    }

    private static boolean checkStack(Set<ItemFilter> itemFilters, boolean hasAllFilters, ItemStack stack, Inventory inventory) {
        for (ItemFilter filter : itemFilters) {
            boolean stackMatches = filter.match(stack, inventory);

            if(!stackMatches && hasAllFilters) {
                return false;
            } else if(stackMatches && !hasAllFilters) {
                return true;
            }
        }

        return hasAllFilters;
    }

    public Descriptor getDescriptor() {
        Descriptor descriptor = new Descriptor()
                .addLine("Require all filters: "+requireAllFilters);

        for(ItemFilter itemFilter : itemFilters) {
            descriptor
                    .addLine("Item filter:")
                    .indent()
                    .add(itemFilter.getDescriptor())
                    .outdent();
        }

        return descriptor.outdent();
    }
}
