package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import org.bukkit.potion.PotionEffect;

import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class PotionEffectAddAction extends SelectingAction<McmeEntity> {

    private static final Random random = new Random();

    public PotionEffectAddAction(PotionEffect effect, Set<PotionEffectChoice> choices, McmeEntitySelector selector){
        super(selector, (entity, context) -> {
            if(effect!=null) {
                DebugManager.verbose(Modules.Action.execute(SetGoalAction.class), "Effect: " + effect.getType());
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
//Logger.getGlobal().info("Potion effect: "+choice.getEffect());
                    entity.addPotionEffect(choice.getEffect());
                    DebugManager.verbose(Modules.Action.execute(SetGoalAction.class), "Random Effect: " + choice.getEffect().getType());
                    break;
                }
            }

        });
        //this.effect = effect;
        DebugManager.info(Modules.Action.create(this.getClass()),"Effect: "+effect+" Choices: "+choices.size());
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
