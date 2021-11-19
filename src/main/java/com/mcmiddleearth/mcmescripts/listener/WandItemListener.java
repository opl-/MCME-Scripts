package com.mcmiddleearth.mcmescripts.listener;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.logging.Logger;

public class WandItemListener implements Listener {

    public static String SCRIPT_WAND = "script_wand";

    @EventHandler
    public void onWandInteract(PlayerInteractEvent event) {
//Logger.getGlobal().info("onWand");
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
//Logger.getGlobal().info("right click");
            Player player = event.getPlayer();
            ItemStack wand = player.getInventory().getItemInMainHand();
            if(hasScript(wand)) {
//Logger.getGlobal().info("has script");
                String[] split = getScript(wand).split(" ");
                if(split.length>=2) {
//Logger.getGlobal().info("valid: "+split[0]+" "+split[1]);
                    MCMEScripts.getExternalTriggerManager().call(split[0], split[1], Arrays.copyOfRange(split, 2, split.length));
                    event.setCancelled(true);
                    player.sendMessage(Component.text("[Scripts] ").color(NamedTextColor.AQUA)
                          .append(Component.text("Calling script: "+split[0]+" event: "+split[1])));
                }
            }
        }
    }

    public static void addScript(ItemStack wand, String script) {
//Logger.getGlobal().info("add: "+script);
        NamespacedKey key = new NamespacedKey(MCMEScripts.getInstance(),SCRIPT_WAND);
        ItemMeta meta = wand.getItemMeta();
        meta.getPersistentDataContainer().set(key, PersistentDataType.PrimitivePersistentDataType.STRING, script);
        wand.setItemMeta(meta);
    }

    public static boolean hasScript(ItemStack wand) {
        if(wand == null) return false;
        ItemMeta meta = wand.getItemMeta();
        return meta.getPersistentDataContainer().has(new NamespacedKey(MCMEScripts.getInstance(),SCRIPT_WAND),
                PersistentDataType.STRING);
    }

    public static String getScript(ItemStack wand) {
        if(wand == null) return null;
        ItemMeta meta = wand.getItemMeta();
        return meta.getPersistentDataContainer().get(new NamespacedKey(MCMEScripts.getInstance(),SCRIPT_WAND),
                PersistentDataType.STRING);
    }

}
