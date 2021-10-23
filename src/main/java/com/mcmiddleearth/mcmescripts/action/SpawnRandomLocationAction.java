package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

public class SpawnRandomLocationAction extends Action {

    private final Location center;

    private final SpawnRandomSelectionAction.RandomSpawnData data;

    public SpawnRandomLocationAction(Location center, SpawnRandomSelectionAction.RandomSpawnData data) {
        DebugManager.info(Modules.Action.create(this.getClass()),"Location: "+center);
        this.data = data;
        this.center = center;
    }

    @Override
    protected void handler(TriggerContext context) {
        DebugManager.verbose(Modules.Action.execute(this.getClass()),"Location: "+center);
        data.spawn(context, center);
    }
}
