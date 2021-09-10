package com.mcmiddleearth.mcmescripts;

import com.mcmiddleearth.mcmescripts.script.ScriptManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class MCMEScripts extends JavaPlugin {

    private static TimedTriggerChecker timedTriggerChecker;
    private static BukkitTask timedTriggerTask;
    private static ScriptManager scriptManager;
    private static MCMEScripts instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        scriptManager = new ScriptManager();
        scriptManager.startChecker();
        timedTriggerChecker = new TimedTriggerChecker();
        timedTriggerTask = timedTriggerChecker.runTaskTimer(this, getConfigInt(ConfigKeys.START_UP_DELAY,600),
                                                                        getConfigInt(ConfigKeys.TRIGGER_CHECKER_PERIOD,10));
        BukkitAudiences.create(this);
        instance = this;
    }

    @Override
    public void onDisable() {
        if(timedTriggerTask!=null && !timedTriggerTask.isCancelled()) {
            timedTriggerTask.cancel();
        }
        scriptManager.stopChecker();
    }

    public static ScriptManager getScriptManager() {
        return scriptManager;
    }

    public static MCMEScripts getInstance() {
        return instance;
    }

    public static TimedTriggerChecker getTimedTriggerChecker() {
        return timedTriggerChecker;
    }

    public static int getConfigInt(ConfigKeys key, int defaultValue) {
        return instance.getConfig().getInt(key.getKey(),defaultValue);
    }

    public static String getConfigString(ConfigKeys key, String defaultValue) {
        return instance.getConfig().getString(key.getKey(),defaultValue);
    }

    public static double getConfigValueDouble(ConfigKeys key, double defaultValue) {
        return instance.getConfig().getDouble(key.getKey(),defaultValue);
    }

}
