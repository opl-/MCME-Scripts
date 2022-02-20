package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.effect.Explosion;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.Collection;

public class ExplosionAction extends Action {

    private final Explosion explosion;
    private final McmeEntitySelector unaffectedSelector, damagerSelector;

    public ExplosionAction(Explosion explosion, McmeEntitySelector unaffectedSelector, McmeEntitySelector damagerSelector) {
        this.explosion = explosion;
        this.unaffectedSelector = unaffectedSelector;
        this.damagerSelector = damagerSelector;
        //DebugManager.info(Modules.Action.create(this.getClass()),"Location: "+ explosion.getLocation());
        getDescriptor().indent()
                .addLine("Center: "+explosion.getLocation())
                .addLine("Damage: "+explosion.getDamage())
                .addLine("Particle: "+(explosion.getParticle()!=null?explosion.getParticle().name():"--none--"))
                .addLine("Velocity: "+explosion.getVelocity())
                .addLine("Radius: "+explosion.getRadius())
                .addLine("Knockback: "+explosion.getKnockback())
                .addLine("Damager: "+(damagerSelector!=null?damagerSelector.getSelector():"--none--"))
                .addLine("Unaffected: "+(unaffectedSelector!=null?unaffectedSelector.getSelector():"--none--")).outdent();
    }

    @Override
    protected void handler(TriggerContext context) {
        if(damagerSelector !=null) {
            McmeEntity damager = damagerSelector.select(context).stream().findFirst().orElse(null);
            context.getDescriptor().addLine("Selected damager: "+(damager!=null?damager.getName():"--none--"));
            explosion.setDamager(damager);
            if(damager!=null) {
                explosion.setLocation(damager.getLocation());
            }
        } else {
            context.getDescriptor().addLine("Selected damager: --none--");
            explosion.setDamager(null);
        }
        if(explosion.getLocation() == null) {
            context.getDescriptor().addLine("New center: "+context.getLocation());
            explosion.setLocation(context.getLocation());
        }
        if(explosion.getLocation() != null) {
            if(unaffectedSelector !=null) {
                Collection<McmeEntity> unaffectedList = unaffectedSelector.select(context);
                if(!unaffectedList.isEmpty()) {
                    context.getDescriptor().addLine("Unaffected: ").indent();
                    unaffectedList.forEach(unaffected -> {
                        explosion.addUnaffected(unaffected);
                        context.getDescriptor().addLine(unaffected.getName());
                    });
                    context.getDescriptor().outdent();
                } else {
                    context.getDescriptor().addLine("Unaffected: --none--");
                }
            } else {
                explosion.clearUnaffected();
            }
            explosion.explode();
        } else {
            DebugManager.warn(Modules.Action.execute(this.getClass()),"Can't trigger explosion. Missing location.");
        }
    }
}
