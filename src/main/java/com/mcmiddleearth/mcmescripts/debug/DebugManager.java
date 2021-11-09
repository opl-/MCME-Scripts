package com.mcmiddleearth.mcmescripts.debug;

import com.mcmiddleearth.mcmescripts.MCMEScripts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Logger;

public class DebugManager {

    private static Map<String,Level> debugModules = new HashMap<>();

    private static final File logFile = new File(MCMEScripts.getInstance().getDataFolder(),"debug.txt");

    private static PrintWriter writer;

    public static void open() {
        Arrays.stream(Modules.values()).filter(module -> !debugModules.containsKey(module.getModule()))
                .forEach(module -> debugModules.put(module.getModule(),Level.INFO));
        Arrays.stream(Modules.values()).filter(module -> !debugModules.containsKey(module.getModule().split("\\.")[0]))
                .forEach(module -> debugModules.put(module.getModule().split("\\.")[0],Level.INFO));
        if(logFile.exists()) {
            if(!logFile.delete()) {
                Logger.getLogger(MCMEScripts.class.getSimpleName()).info("Can't delete old log file!");
            }
        }
        try {
            if(logFile.createNewFile()) {
                Logger.getLogger(MCMEScripts.class.getSimpleName()).info("Creating log file!");
            }
            writer = new PrintWriter(new FileWriter(logFile),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        writer.close();
    }

    public static void cycleDebug(String module) {
        if(module.equals("all")) {
            debug("all", Level.VERBOSE);
        } else if(module.equals("none")) {
            debug("all", Level.CRITICAL);
        } else {
            debug(module, Level.next(debugModules.get(module)));
        }
        log("Debug Modules: ");
        debugModules.forEach((key, value) -> log(key+" - "+value.name()));
    }

    public static void debug(String module, String debugLevel) {
        try {
            debug(module, Level.valueOf(debugLevel.toUpperCase()));
        } catch(IllegalArgumentException ignore) {}
        log("Debug Modules: ");
        debugModules.forEach((key, value) -> log(key+" - "+value.name()));
    }

    public static void debug(String module, Level debugLevel) {
        if(module.equalsIgnoreCase("all")) {
            new HashSet<>(debugModules.keySet()).forEach(mod -> debug(mod, debugLevel));
        } else {
            debugModules.put(module, debugLevel);
        }
    }

    public static void list(String module) {
        String[] split = module.split("\\.");
        log("List of registered "+split[0]+"s:");
        if(split[0].equalsIgnoreCase("script")) {
            MCMEScripts.getScriptManager().getScripts().entrySet().stream()
                       .filter(entry -> (split.length < 2 || entry.getKey().toLowerCase().startsWith(split[1].toLowerCase())))
                       .forEach(entry -> log(entry.getKey()+(entry.getValue().isActive()?"(active)":"")+": "
                                                               +entry.getValue().getTriggers().size()+" Triggers, "
                                                               +entry.getValue().getEntities().size()+" Entities."));
        } else if(split[0].equalsIgnoreCase("trigger")) {
            MCMEScripts.getScriptManager().getScripts().entrySet().stream()
                       .filter(entry -> (split.length<2 || entry.getKey().startsWith(split[1])))
                       .forEach(entry -> entry.getValue().getTriggers().stream()
                            .filter(trigger -> split.length < 3
                                    || trigger.getClass().getSimpleName().toLowerCase().startsWith(split[2].toLowerCase()))
                            .forEach(trigger -> log(trigger.getClass().getSimpleName()+" "+trigger.toString())));
        }
    }

    public static void verbose(String module, String message) {
        log(module, message, Level.VERBOSE);
    }

    public static void info(String module, String message) {
        log(module, message, Level.INFO);
    }

    public static void warn(String module, String message) {
        log(module, message, Level.WARNING);
    }

    public static void severe(String module, String message) {
        log(module, message, Level.SEVERE);
    }

    public static void critical(String module, String message) {
        log(module, message, Level.CRITICAL);
    }

    public static void log(String module, String message, Level debugLevel) {
        String initialModule = ""+module;
        module = module + ".";
        int lastDot = module.lastIndexOf('.');
        do {
            module = module.substring(0,lastDot);
            //log(module);
            if(isActive(module, debugLevel)) {
                //log("Found: "+module);
                break;
            }
            lastDot = module.lastIndexOf('.');
        } while(lastDot > 0);
        //log("Final module: "+module);
        if(isActive(module,debugLevel) || isActive(module.split("\\.")[0],debugLevel)) {
            //log("found final module.");
            log(initialModule + " -> " + message);
        }
    }

    private static boolean isActive(String module, Level debugLevel) {
        return debugModules.get(module)!=null && debugModules.get(module).getDebugLevel()<=debugLevel.getDebugLevel();
    }

    public static void log(String message) {
        Logger.getLogger(MCMEScripts.class.getSimpleName()).info(message);
        writer.println(message);
    }

}
