package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.entity.Player;

public class TitleAction extends SelectingAction<Player> {

    public TitleAction(Selector<Player> selector, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        super(selector, (player, context) -> {
            DebugManager.verbose(Modules.Action.execute(TitleAction.class),"Title for player: "+player.getName() +" "+title);
            player.sendTitle(title,subtitle,fadeIn,stay,fadeOut);
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()+" Title: "+title);
    }
}
