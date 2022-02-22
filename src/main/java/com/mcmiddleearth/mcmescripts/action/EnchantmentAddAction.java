package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.component.EnchantmentChoice;
import com.mcmiddleearth.mcmescripts.component.ItemFilter;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.looting.LootTable;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Logger;

public class EnchantmentAddAction extends SelectingAction<McmeEntity> {

    public EnchantmentAddAction(Selector<McmeEntity> selector, Set<ItemFilter> itemFilters, Set<WrappedEnchantment> enchantments, Set<EnchantmentChoice> enchantmentChoices, int quantity, int duration) {
        super(selector, (entity, context) -> {
            //DebugManager.verbose(Modules.Action.execute(EnchantmentAddAction.class),"Selector: "+selector.getSelector()+" Filters:"+itemFilters.size()
            //        + " Enchantments: "+enchantments.size()+ " Choices: "+ enchantmentChoices.size()+" Quantity: "+quantity+" Duration:"+duration);

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
//Logger.getGlobal().info("Add items: "+applyItems.size()+" calculated quantity: "+calculatedQuantity);
                            break;
                        }
                    }
                }
            }
//Logger.getGlobal().info("quantity: "+quantity+" calculated: "+calculatedQuantity);
            if (calculatedQuantity >= 0) {
                for (ItemStack applyItem : applyItems) {
                    Set<WrappedEnchantment> applyEnchantments = new HashSet<>(enchantments);
                    applyEnchantments.addAll(lootTable.selectEnchantments());
                    applyEnchantment(applyItem, applyEnchantments, duration);
                    context.getDescriptor().addLine("Enchanting: "+applyItem.getType().name());
                }
            }
            else {
                List<ItemStack> limitedItems = new ArrayList<>(applyItems);
                Collections.shuffle(limitedItems);
                calculatedQuantity = quantity;
//Logger.getGlobal().info("limted Items: "+limitedItems.size());
                for (ItemStack limitedItem : limitedItems) {
                    if (calculatedQuantity == 0) {
                        break;
                    }
                    Set<WrappedEnchantment> applyEnchantments = new HashSet<>(enchantments);
                    applyEnchantments.addAll(lootTable.selectEnchantments());
                    applyEnchantment(limitedItem, applyEnchantments, duration);
                    context.getDescriptor().addLine("Enchanting: "+limitedItem.getType().name());
                    calculatedQuantity -= 1;
//Logger.getGlobal().info("Quantity: "+quantity+" calculated: "+calculatedQuantity);
                }
                context.getDescriptor().addLine("Stopping enchanting: Max quantity reached.");
            }
        });
        Descriptor descriptor =  getDescriptor().indent();
        descriptor.addLine("Max. Quantity: "+quantity);
        descriptor.addLine("Duration: "+duration);
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

        //DebugManager.verbose(Modules.Action.create(EnchantmentAddAction.class),"Selector: "+selector.getSelector()+" Filters:"+itemFilters.size()
        //        + " Enchantments: "+enchantments.size()+ " Choices: "+ enchantmentChoices.size()+" Quantity: "+quantity+" Duration:"+duration);
    }

    private static void applyEnchantment(ItemStack item, Set<WrappedEnchantment> enchantments, int duration) {
        ItemMeta meta = item.getItemMeta();

        for (WrappedEnchantment wrappedEnchantment : enchantments) {
            meta.addEnchant(wrappedEnchantment.getEnchantment(), wrappedEnchantment.getLevel(), true);
        }

        item.setItemMeta(meta);

        // This operation probably can be done with your persistent data system on the entities but as it's not a thing right now this will probably do
        if (duration > 0) {
            Bukkit.getScheduler().runTaskLater(MCMEScripts.getInstance(), () -> {
                ItemMeta removeMeta = item.getItemMeta();
                for (WrappedEnchantment wrappedEnchantment : enchantments) {
                    removeMeta.removeEnchant(wrappedEnchantment.getEnchantment());
                }
                item.setItemMeta(removeMeta);
            }, 20L * duration);
        }
    }

}
