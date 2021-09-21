package com.mcmiddleearth.mcmescripts.selector;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PlayerSelector extends EntitySelector<Player>{

    public PlayerSelector(String selector) throws IndexOutOfBoundsException {
        super(selector);
        DebugManager.log(Modules.Selector.create(this.getClass()),
                "Selector: "+selector);
    }

    @Override
    public List<Player> select(TriggerContext context) {
        return selectPlayer(context);
    }
}
