package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class GiveItemAction extends SelectingAction<McmeEntity> {

    public GiveItemAction(Selector<McmeEntity> selector, Set<ItemStack> items, EquipmentSlot slot, int slotId) {
        super(selector, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(GiveItemAction.class),"Selector: "+selector.getSelector()
                    + " Items: "+items.size()+ " Slot: "+slot.name()+" "+slotId);
            items.forEach(item -> entity.addItem(item,slot,slotId));
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()
                + " item: "+items.size()+" Slot: "+slot.name()+" "+slotId);
    }
}
