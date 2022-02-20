package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import org.bukkit.potion.PotionEffect;

import java.util.Random;
import java.util.Set;

public class PotionEffectAddAction extends SelectingAction<McmeEntity> {

    private static final Random random = new Random();

    public PotionEffectAddAction(PotionEffect effect, Set<PotionEffectChoice> choices, McmeEntitySelector selector){
        super(selector, (entity, context) -> {
            if(effect!=null) {
                //DebugManager.verbose(Modules.Action.execute(SetGoalAction.class), "Effect: " + effect.getType());
                context.getDescriptor().addLine("Applying effect: "+effect.getType().getName());
                entity.addPotionEffect(effect);
            }
            int weightSum = 0;
            for(PotionEffectChoice choice: choices) {
                weightSum+=choice.getWeight();
            }
            int weightRandom = random.nextInt(weightSum+1);
            weightSum = 0;
            for(PotionEffectChoice choice: choices) {
                weightSum+=choice.getWeight();
                if(weightSum>=weightRandom) {
                    entity.addPotionEffect(choice.getEffect());
                    context.getDescriptor().addLine("Applying random effect: "+choice.getEffect().getType().getName());
                    //DebugManager.verbose(Modules.Action.execute(SetGoalAction.class), "Random Effect: " + choice.getEffect().getType());
                    break;
                }
            }

        });
        getDescriptor().indent()
                .addLine("Effect: ");
        addEffect(getDescriptor(), effect);
        if(!choices.isEmpty()) {
            getDescriptor().addLine("Potion effect choices: ").indent();
            choices.forEach(choice -> {
                getDescriptor().addLine("Weight: "+choice.getWeight());
                addEffect(getDescriptor(),choice.getEffect());
            });
            getDescriptor().outdent();
        } else {
            getDescriptor().addLine("Potion effect choices: --none--");
        }
        getDescriptor().outdent();
        //DebugManager.info(Modules.Action.create(this.getClass()),"Effect: "+effect+" Choices: "+choices.size());
    }

    public static void addEffect(Descriptor descriptor, PotionEffect effect) {
        descriptor.addLine("Effect: ").indent()
                .addLine("Type: "+effect.getType().getName())
                .addLine("Type: "+effect.getAmplifier())
                .addLine("Type: "+effect.getDuration())
                .addLine("Type: "+effect.isAmbient())
                .addLine("Type: "+effect.hasParticles())
                .addLine("Type: "+effect.hasIcon()).outdent();
    }

    public static class PotionEffectChoice {

        private final PotionEffect effect;
        private final int weight;

        public PotionEffectChoice(PotionEffect effect, int weight) {
            this.effect = effect;
            this.weight = weight;
        }

        public PotionEffect getEffect() {
            return effect;
        }

        public int getWeight() {
            return weight;
        }
    }
}
