package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarRemoveAction extends SelectingAction<Player> {

    public BossBarRemoveAction(Selector<Player> selector, NamespacedKey barKey) {
        super(selector, (player, context) -> {
            BossBar bar = Bukkit.getBossBar(barKey);
            if(bar!=null) {
                //DebugManager.verbose(Modules.Action.execute(BossBarRemoveAction.class), "Removing: " + player.getName() + " from Boss bar.");
                bar.removePlayer(player);
            } else {
                DebugManager.warn(Modules.Action.execute(BossBarRemoveAction.class), "Expected Boss bar does not exist: "+barKey.asString());
            }
        });
        //DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector());
        getDescriptor().indent().addLine("Bar key: "+barKey).outdent();
    }
}