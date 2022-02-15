package com.mcmiddleearth.mcmescripts.action;

import com.craftmend.thirdparty.reactorcore.scheduler.Schedulers;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.inventory.McmeInventory;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.component.EnchantmentChoice;
import com.mcmiddleearth.mcmescripts.component.ItemFilter;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.looting.LootTable;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchantmentAddAction extends SelectingAction<McmeEntity> {

    public EnchantmentAddAction(Selector<McmeEntity> selector, Set<ItemFilter> itemFilters, Set<WrappedEnchantment> enchantments, Set<EnchantmentChoice> enchantmentChoices, int quantity, int duration) {
        super(selector, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(EnchantmentAddAction.class),"Selector: "+selector.getSelector()+" Filters:"+itemFilters.size()
                    + " Enchantments: "+enchantments.size()+ " Choices: "+ enchantmentChoices.size()+" Quantity: "+quantity+" Duration:"+duration);

            int calculatedQuantity = quantity;
            Set<ItemStack> applyItems = new HashSet<>();
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
                    Set<WrappedEnchantment> applyEnchantments = new HashSet<>(enchantments);
                    applyEnchantments.addAll(lootTable.selectEnchantments());
                    applyEnchantment(applyItem, applyEnchantments, duration);
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
                    Set<WrappedEnchantment> applyEnchantments = new HashSet<>(enchantments);
                    applyEnchantments.addAll(lootTable.selectEnchantments());
                    applyEnchantment(limitedItem, applyEnchantments, duration);
                    calculatedQuantity -= 1;
                }
            }
        });
        DebugManager.verbose(Modules.Action.create(EnchantmentAddAction.class),"Selector: "+selector.getSelector()+" Filters:"+itemFilters.size()
                + " Enchantments: "+enchantments.size()+ " Choices: "+ enchantmentChoices.size()+" Quantity: "+quantity+" Duration:"+duration);
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
