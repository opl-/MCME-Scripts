package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.PersistentDataKey;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemGiveAction extends SelectingAction<McmeEntity> {

    private static final Random random = new Random();

    public ItemGiveAction(Selector<McmeEntity> selector, Set<ItemStack> items, Set<ItemChoice> choices,
                          @Nullable EquipmentSlot slot, int slotId, int duration) {
        super(selector, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(ItemGiveAction.class),"Selector: "+selector.getSelector()
                    + " Items: "+items.size()+ " Choices: "+choices.size()+" Slot: "+(slot!=null?slot.name():"null")+" "+slotId);
            items.forEach(item -> giveItem(entity, context, item, slot, slotId, duration));

            int weightSum = 0;
            for(ItemChoice choice: choices) {
                weightSum+=choice.getWeight();
            }
            int weightRandom = random.nextInt(weightSum+1);
            weightSum = 0;
            for(ItemChoice choice: choices) {
                weightSum+=choice.getWeight();
                if(weightSum>=weightRandom) {
                    choice.getItems().forEach(item->giveItem(entity, context, item, slot, slotId, duration));
                    break;
                }
            }
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()
                + " item: "+items.size()+" Choices: "+choices.size()+" Slot: "+(slot!=null?slot.name():"null")+" "+slotId);
    }

    private static void giveItem(McmeEntity entity, TriggerContext context, ItemStack item, EquipmentSlot slot, int slotId, int duration) {
        ItemMeta meta = item.getItemMeta();
        if(duration>0) {
            NamespacedKey key = EntitiesPlugin.getInstance().getPersistentDataKey(PersistentDataKey.ITEM_REMOVAL_TIME);
            meta.getPersistentDataContainer().set(key, PersistentDataType.PrimitivePersistentDataType.LONG,
                    EntitiesPlugin.getEntityServer().getCurrentTick() + duration);
        }
        if(context.getMessage()!=null) {
            if (meta.hasLore()) {
                Objects.requireNonNull(meta.lore()).add(Component.text(context.getMessage()));
            } else {
                meta.lore(Collections.singletonList(Component.text(context.getMessage())));
            }
        }
        item.setItemMeta(meta);

        entity.addItem(item,slot,slotId);
    }

    public static class ItemChoice {

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
}
