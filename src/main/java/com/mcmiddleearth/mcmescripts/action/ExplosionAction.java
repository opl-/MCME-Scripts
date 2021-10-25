package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.effect.Explosion;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class ExplosionAction extends Action {

    private final Explosion explosion;
    private final McmeEntitySelector unaffectedSelector, damagerSelector;

    public ExplosionAction(Explosion explosion, McmeEntitySelector unaffectedSelector, McmeEntitySelector damagerSelector) {
        this.explosion = explosion;
        this.unaffectedSelector = unaffectedSelector;
        this.damagerSelector = damagerSelector;
        DebugManager.info(Modules.Action.create(this.getClass()),"Location: "+ explosion.getLocation());
    }

    @Override
    protected void handler(TriggerContext context) {
        if(damagerSelector !=null) {
            McmeEntity damager = damagerSelector.select(context).stream().findFirst().orElse(null);
            explosion.setDamager(damager);
            if(damager!=null) {
                explosion.setLocation(damager.getLocation());
            }
        } else {
            explosion.setDamager(null);
        }
        if(explosion.getLocation() == null) {
            explosion.setLocation(context.getLocation());
        }
        if(explosion.getLocation() != null) {
            if(unaffectedSelector !=null) {
                unaffectedSelector.select(context).forEach(explosion::addUnaffected);
            } else {
                explosion.clearUnaffected();
            }
            explosion.explode();
        } else {
            DebugManager.warn(Modules.Action.execute(this.getClass()),"Can't trigger explosion. Missing location.");
        }
    }
}
