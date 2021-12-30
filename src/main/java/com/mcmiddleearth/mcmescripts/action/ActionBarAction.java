package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ActionBarAction extends SelectingAction<Player> {

    private final String title;

    public ActionBarAction(Selector<Player> selector, String title) {
        super(selector, (player, context) -> {
            DebugManager.verbose(Modules.Action.execute(TitleAction.class),"Title for player: "+player.getName() +" "+title);
            player.sendActionBar(Component.text(title));
        });
        this.title = title;
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()+" Title: "+title);
    }

    @Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Title: "+title);
    }
}