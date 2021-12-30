package com.mcmiddleearth.mcmescripts.trigger.timed;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.RealPlayer;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.*;

public class SelectionTrigger extends PeriodicServerTimeTrigger {

    McmeEntitySelector selector;

    Process process;

    List<McmeEntity> selectedEntities = new ArrayList<>();

    public SelectionTrigger(Action action, long timeMillis, McmeEntitySelector selector, Process process) {
        super(action, timeMillis);
        this.selector = selector;
        this.process = process;
        DebugManager.info(Modules.Trigger.create(this.getClass()),
                "Selector: "+selector+" periode: "+timeMillis+" process: "+process.name());
    }

    @Override
    public void call(TriggerContext context) {
        if(checkPeriod()) {
            context.withLocation(getLocation());
            List<McmeEntity> newSelection = selector.select(context);
            if(process.equals(Process.ENTER)) {
                newSelection.stream().filter(entity -> !selectedEntities.contains(entity)).forEach(entity -> {
                            TriggerContext cont = new TriggerContext(context).withEntity(entity);
                            if(entity instanceof RealPlayer) {
                                cont.withPlayer(((RealPlayer)entity).getBukkitPlayer());
                            }
                            DebugManager.info(Modules.Trigger.call(this.getClass()),
                                    "Entity: " + entity.getName());
                            ignorePeriodCheck = true;
                            super.call(cont);
                            ignorePeriodCheck = false;
                });
                selectedEntities = newSelection;
            } else if(process.equals(Process.LEAVE)) {
                selectedEntities.stream().filter(entity -> !newSelection.contains(entity)).forEach(entity -> {
                    TriggerContext cont = new TriggerContext(context).withEntity(entity);
                    if(entity instanceof RealPlayer) {
                        cont.withPlayer(((RealPlayer)entity).getBukkitPlayer());
                    }
                    ignorePeriodCheck = true;
                    super.call(cont);
                    ignorePeriodCheck = false;
                });
                selectedEntities = newSelection;
            }
        }
    }

    private boolean ignorePeriodCheck = false;

    protected boolean checkPeriod() {
        if(ignorePeriodCheck) {
            return true;
        } else {
            return super.checkPeriod();
        }
    }

    public enum Process {
        ENTER, LEAVE;
    }

    @Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().addLine("Process: "+process)
                                    .addLine("Selector: "+selector.getSelector());
    }
}
