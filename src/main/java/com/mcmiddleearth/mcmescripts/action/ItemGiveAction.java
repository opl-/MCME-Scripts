package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.PersistentDataKey;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

public class ItemGiveAction extends SelectingAction<McmeEntity> {

    public ItemGiveAction(Selector<McmeEntity> selector, Set<ItemStack> items, EquipmentSlot slot, int slotId,
                          int duration) {
        super(selector, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(ItemGiveAction.class),"Selector: "+selector.getSelector()
                    + " Items: "+items.size()+ " Slot: "+slot.name()+" "+slotId);
            NamespacedKey key = EntitiesPlugin.getInstance().getPersistentDataKey(PersistentDataKey.ITEM_REMOVAL_TIME);
            items.forEach(item -> {
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(key, PersistentDataType.PrimitivePersistentDataType.LONG,
                                                      EntitiesPlugin.getEntityServer().getCurrentTick()+duration);
                item.setItemMeta(meta);

                entity.addItem(item,slot,slotId);
            });
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()
                + " item: "+items.size()+" Slot: "+slot.name()+" "+slotId);
    }
}
