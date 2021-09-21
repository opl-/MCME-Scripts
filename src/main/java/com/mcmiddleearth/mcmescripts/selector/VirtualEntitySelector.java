package com.mcmiddleearth.mcmescripts.selector;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VirtualEntitySelector extends EntitySelector<VirtualEntity> {

    public VirtualEntitySelector(String selector) throws IndexOutOfBoundsException {
        super(selector);
        DebugManager.log(Modules.Selector.create(this.getClass()),
                "Selector: "+selector);
    }

    @Override
    public List<VirtualEntity> select(TriggerContext context) {
        return selectVirtualEntities(context);
    }
}
