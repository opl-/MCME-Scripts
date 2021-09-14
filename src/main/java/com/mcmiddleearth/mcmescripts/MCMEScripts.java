package com.mcmiddleearth.mcmescripts;

import com.mcmiddleearth.mcmescripts.command.ScriptsCommandHandler;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.script.ScriptManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCMEScripts extends JavaPlugin {

    private static TimedTriggerManager timedTriggerManager;
    private static ScriptManager scriptManager;
    private static MCMEScripts instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        DebugManager.open();
        scriptManager = new ScriptManager();
        timedTriggerManager = new TimedTriggerManager();
        enableScripts();
        //BukkitAudiences.create(this);
        setExecutor("scripts", new ScriptsCommandHandler("scripts"));
    }

    @Override
    public void onDisable() {
        disableScripts();
        DebugManager.close();
    }

    public void enableScripts() {
        scriptManager.readScripts();
        scriptManager.startChecker();
        timedTriggerManager.startChecker();
    }

    public void disableScripts() {
        timedTriggerManager.stopChecker();
        scriptManager.stopChecker();
        scriptManager.removeScripts();
    }

    public static ScriptManager getScriptManager() {
        return scriptManager;
    }

    public static MCMEScripts getInstance() {
        return instance;
    }

    public static TimedTriggerManager getTimedTriggerChecker() {
        return timedTriggerManager;
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

    private void setExecutor(String command, CommandExecutor executor) {
        PluginCommand pluginCommand = Bukkit.getServer().getPluginCommand(command);
        if(pluginCommand!=null) {
            pluginCommand.setExecutor(executor);
            if (executor instanceof TabCompleter)
                pluginCommand.setTabCompleter((TabCompleter) executor);
        }

    }



}
