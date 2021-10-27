package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExecuteCommandAction extends SelectingAction<Player> {

    public ExecuteCommandAction(Selector<Player> selector, String command) {
        super(selector, (player, context) -> {
            DebugManager.verbose(Modules.Action.execute(ExecuteCommandAction.class),"Selector: "+selector.getSelector()
                    + " Player: "+player.getName());
            Bukkit.dispatchCommand(player,command.replace("@p",player.getName()));
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector());
    }

}
