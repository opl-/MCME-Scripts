package com.mcmiddleearth.mcmescripts.script;

import com.mcmiddleearth.mcmescripts.ConfigKeys;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ScriptManager {

    private final Map<String,Script> scripts = new HashMap<>();

    private BukkitTask checker;

    private static final File scripFolder = new File(MCMEScripts.getInstance().getDataFolder(),"scripts");

    public void readScripts() {
        if(!scripFolder.exists()) {
            if(scripFolder.mkdir()) {
                Logger.getLogger(MCMEScripts.class.getSimpleName()).info("Scripts folder created.");
            }
        }
        for(File file : scripFolder.listFiles(((dir, name) -> name.endsWith(".json")))) {
            try {
                addScript(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addScript(File file) throws IOException {
        Script script = new Script(file);
        scripts.put(script.getName(), script);
    }

    public void removeScript(String name) {
        Script script = scripts.get(name);
        if(script!=null)
        {
            script.unload();
            scripts.remove(name);
        }
    }

    public void removeScripts() {
        Set<String> names = new HashSet<>(scripts.keySet());
        names.forEach(this::removeScript);
    }

    public void startChecker() {
        stopChecker();
        checker = new BukkitRunnable() {
            @Override
            public void run() {
                scripts.forEach((name,script) -> {
                    if (script.isTriggered() && !script.isActive()) {
                        try {
                            script.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if(!script.isTriggered() && script.isActive()) {
                        script.unload();
                    }
                });
            }
        }.runTaskTimer(MCMEScripts.getInstance(),MCMEScripts.getConfigInt(ConfigKeys.START_UP_DELAY,195),
                                                 MCMEScripts.getConfigInt(ConfigKeys.SCRIPT_CHECKER_PERIOD,100));
    }

    public void stopChecker() {
        if(checker!=null && !checker.isCancelled()) {
            checker.cancel();
        }
    }

    public  Map<String, Script> getScripts() {
        return scripts;
    }
}
