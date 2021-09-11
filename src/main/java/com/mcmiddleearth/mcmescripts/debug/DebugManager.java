package com.mcmiddleearth.mcmescripts.debug;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.script.ScriptManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DebugManager {

    private static final Set<String> debugModules = new HashSet<>();

    private static final File logFile = new File(MCMEScripts.getInstance().getDataFolder(),"debug.txt");

    private static PrintWriter writer;

    static {
        if(!logFile.exists()) {
            try {
                logFile.createNewFile();
                writer = new PrintWriter(new FileWriter(logFile),true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void toggleDebug(String module) {
        debug(module,!debugModules.contains(module));
    }

    public static void debug(String module, String enable) {
        debug(module, enable.equalsIgnoreCase("true"));
    }

    private static void debug(String module, boolean enable) {
        if(enable) {
            debugModules.add(module);
        } else {
            debugModules.remove(module);
        }
    }

    public static void list(String module) {
        String[] split = module.split("\\.");
        log("")
        if(split[0].equalsIgnoreCase("script")) {
            MCMEScripts.getScriptManager().getScripts().entrySet().stream()
                       .filter(entry -> (split.length < 2 || entry.getKey().startsWith(split[1])))
                       .forEach(entry -> log(entry.getKey()+"("+(entry.getValue().isActive()?"active":"")+"): "
                                                               +entry.getValue().getTriggers().size()+" Triggers, "
                                                               +entry.getValue().getEntities().size()+" Entities."));
        } else if(split[0].equalsIgnoreCase("trigger")) {
            Triggers
        }
    }

    public static void log(String module, String message) {
        if(debugModules.contains(module) || debugModules.contains(module.split("\\.")[0])) {
            log(message);
        }
    }

    public static void log(String message) {
        Logger.getLogger(MCMEScripts.class.getSimpleName()).info(message);
        writer.println(message);
    }

}
