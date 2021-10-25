package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ItemRemoveAction extends SelectingAction<McmeEntity> {

    public ItemRemoveAction(Selector<McmeEntity> selector, Set<ItemStack> items) {
        super(selector, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(ItemRemoveAction.class),"Selector: "+selector.getSelector()
                    + " Items: "+items.size());
            items.forEach(entity::removeItem);
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()
                + " item: "+items.size());
    }

}
