package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.component.InventoryFilter;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;

public class HasItemCondition extends SelectingCondition<McmeEntity> {
    private final InventoryFilter inventoryFilter;

    private final Criterion criterion;

    public HasItemCondition(InventoryFilter inventoryFilter, McmeEntitySelector selector, Criterion criterion) {
        super(selector, (McmeEntity entity) -> {
            int itemsFound = inventoryFilter.count(entity.getInventory());

            return criterion.apply(itemsFound);
        });

        this.inventoryFilter = inventoryFilter;
        this.criterion = criterion;
    }

    public Descriptor getDescriptor() {
        Descriptor descriptor = super.getDescriptor();

        descriptor.indent()
                .addLine("Criterion: "+criterion.comparator+criterion.limit)
                .addLine("Inventory filter:")
                .add(inventoryFilter.getDescriptor());

        return descriptor.outdent();
    }
}
