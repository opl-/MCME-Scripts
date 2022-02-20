package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarAddAction extends SelectingAction<Player> {

    public BossBarAddAction(Selector<Player> selector, BossBar bossBar) {
        super(selector, (player, context) -> {
            //DebugManager.verbose(Modules.Action.execute(BossBarAddAction.class),"Adding: "+player.getName() +" to Boss bar.");
            bossBar.addPlayer(player);
        });
        //DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector());
        getDescriptor().indent().addLine("Title: "+bossBar.getTitle())
                .addLine("Color: "+bossBar.getColor().name())
                .addLine("Style: "+bossBar.getStyle().name())
                .addLine("Fog: "+bossBar.hasFlag(BarFlag.CREATE_FOG))
                .addLine("Darken: "+bossBar.hasFlag(BarFlag.DARKEN_SKY))
                .addLine("Music: "+bossBar.hasFlag(BarFlag.PLAY_BOSS_MUSIC))
                .addLine("Visible: "+bossBar.isVisible())
                .addLine("Progress: "+bossBar.getProgress()).outdent();
    }
}