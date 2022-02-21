package com.mcmiddleearth.mcmescripts.selector;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerSelector extends EntitySelector<Player>{

    public PlayerSelector(String selector) throws IndexOutOfBoundsException {
        super(selector);
        //DebugManager.info(Modules.Selector.create(this.getClass()),
        //        "Selector: "+selector);
    }

    @Override
    public List<Player> select(TriggerContext context) {
        return selectPlayer(context);
    }
}
