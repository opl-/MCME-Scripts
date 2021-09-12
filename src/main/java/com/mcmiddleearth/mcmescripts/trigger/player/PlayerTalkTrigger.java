package com.mcmiddleearth.mcmescripts.trigger.player;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.BukkitEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class PlayerTalkTrigger extends BukkitEventTrigger {

    public PlayerTalkTrigger(Action action) {
        super(action);
        DebugManager.log(Modules.Trigger.create(this.getClass()),
                "Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    @EventHandler
    public void onPlayerTalk(AsyncChatEvent event) {
        if(!ChatColor.stripColor(event.message().toString()).startsWith("!")) return;
        TriggerContext context = new TriggerContext(this).withPlayer(event.getPlayer())
                .withMessage(PlainComponentSerializer.plain().serialize(event.message()));
        if(event.isAsynchronous()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    callInternal(context);
                }
            }.runTask(MCMEScripts.getInstance());
        } else {
            callInternal(context);
        }
    }

    private void callInternal(TriggerContext context) {
        call(context);
        DebugManager.log(Modules.Trigger.call(this.getClass()),
                "Player: " + context.getPlayer().getName() + " Message: " + context.getMessage());
    }

}
