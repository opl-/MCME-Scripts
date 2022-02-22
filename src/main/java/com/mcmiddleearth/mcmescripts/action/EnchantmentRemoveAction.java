package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.component.EnchantmentChoice;
import com.mcmiddleearth.mcmescripts.component.ItemFilter;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.looting.LootTable;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchantmentRemoveAction extends SelectingAction<McmeEntity> {

    public EnchantmentRemoveAction(Selector<McmeEntity> selector, Set<ItemFilter> itemFilters, Set<WrappedEnchantment> enchantments, Set<EnchantmentChoice> enchantmentChoices, int quantity) {
        super(selector, (entity, context) -> {
            //DebugManager.verbose(Modules.Action.execute(EnchantmentRemoveAction.class),"Selector: "+selector.getSelector()+" Filters:"+itemFilters.size()
            //        + " Enchantments: "+enchantments.size()+ " Choices: "+ enchantmentChoices.size()+" Quantity: "+quantity);

            int calculatedQuantity = quantity;
            List<ItemStack> applyItems = new ArrayList<>();
            LootTable lootTable = new LootTable(enchantmentChoices);

            Inventory entityInventory = entity.getInventory();
            for (ItemStack entityItem : entityInventory) {
                if (itemFilters.isEmpty()) {
                    applyItems.add(entityItem);
                    calculatedQuantity -= 1;
                    break;
                }
                else {
                    for (ItemFilter filter : itemFilters) {
                        if (filter.match(entityItem, entityInventory)) {
                            applyItems.add(entityItem);
                            calculatedQuantity -= 1;
                            break;
                        }
                    }
                }
            }

            if (calculatedQuantity >= 0) {
                for (ItemStack applyItem : applyItems) {
                    Set<WrappedEnchantment> removeEnchantments = new HashSet<>(enchantments);
                    removeEnchantments.addAll(lootTable.selectEnchantments());
                    removeEnchantment(applyItem, removeEnchantments);
                    context.getDescriptor().addLine("Removing enchantment: "+applyItem.getType().name());
                }
            }
            else {
                List<ItemStack> limitedItems = new ArrayList<>(applyItems);
                Collections.shuffle(limitedItems);
                calculatedQuantity = quantity;
                for (ItemStack limitedItem : limitedItems) {
                    if (calculatedQuantity >= 0) {
                        break;
                    }
                    Set<WrappedEnchantment> removeEnchantments = new HashSet<>(enchantments);
                    removeEnchantments.addAll(lootTable.selectEnchantments());
                    removeEnchantment(limitedItem, removeEnchantments);
                    context.getDescriptor().addLine("Removing enchantment: "+limitedItem.getType().name());
                    calculatedQuantity -= 1;
                }
                context.getDescriptor().addLine("Stopping de-enchanting: Max quantity reached.");
            }
        });

        Descriptor descriptor = getDescriptor().indent();
        descriptor.addLine("Max. Quantity: "+quantity);
        if(!itemFilters.isEmpty()) {
            descriptor.addLine("Filters: ").indent();
            itemFilters.forEach(itemFilter -> descriptor.add(itemFilter.getDescriptor()));
            descriptor.outdent();
        } else {
            descriptor.addLine("Filters: --none--");
        }
        if(!enchantments.isEmpty()) {
            descriptor.addLine("Enchantments: ").indent();
            enchantments.forEach(enchantment -> descriptor.add(enchantment.getDescriptor()));
            descriptor.outdent();
        } else {
            descriptor.addLine("Enchantments: --none--");
        }
        if(!enchantmentChoices.isEmpty()) {
            descriptor.addLine("Enchantment choices: ").indent();
            enchantmentChoices.forEach(choice -> descriptor.add(choice.getDescriptor()));
            descriptor.outdent();
        } else {
            descriptor.addLine("Enchantments choices: --none--");
        }
        descriptor.outdent();
        //DebugManager.verbose(Modules.Action.create(EnchantmentRemoveAction.class),"Selector: "+selector.getSelector()+" Filters:"+itemFilters.size()
        //        + " Enchantments: "+enchantments.size()+ " Choices: "+ enchantmentChoices.size()+" Quantity: "+quantity);
    }

    private static void removeEnchantment(ItemStack item, Set<WrappedEnchantment> enchantments) {
        ItemMeta meta = item.getItemMeta();

        for (WrappedEnchantment wrappedEnchantment : enchantments) {
            meta.removeEnchant(wrappedEnchantment.getEnchantment());
        }

        item.setItemMeta(meta);
    }

}
