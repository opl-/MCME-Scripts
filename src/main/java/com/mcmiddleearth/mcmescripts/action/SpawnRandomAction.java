package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;

import java.util.List;
import java.util.Random;

public class SpawnRandomAction extends SelectingAction<McmeEntity> {

    private final int minQuantity, maxQuantity;

    private final List<Choice> choices;

    private final VirtualEntityGoalFactory goalFactory;

    private final Selector<McmeEntity> goalTargetSelector;

    private final double probability;

    public static final Random random = new Random();

    public SpawnRandomAction(Selector<McmeEntity> selector, List<Choice> choices, int minQuantity, int maxQuantity, double radius,
                             VirtualEntityGoalFactory goalFactory, Selector<McmeEntity> goalTargetSelector, double probability) {
        super(selector, (entity,context) -> {
            DebugManager.verbose(Modules.Action.execute(SpawnRandomAction.class),"Selected entity: "+entity.getName());
            if(probability < random.nextFloat()) {
                int quantity = minQuantity + random.nextInt(maxQuantity - minQuantity + 1);
                int weightSum = 0;
                for(Choice choice: choices) {
                    weightSum += choice.getWeight();
                }
                int randomWeight = random.nextInt(weightSum+1);
                Choice selectedChoice;
                int i = -1;
                int currentWeight = 0;
                do {
                    i++;
                    currentWeight = currentWeight + choices.get(i).weight;
                }
                while(randomWeight > currentWeight);
                selectedChoice = choices.get(i);
                McmeEntity goalTarget = goalTargetSelector.select(context).stream().findFirst().orElse(null);
                if(goalFactory!=null) {
                    if(goalTarget!=null) {
                        goalFactory.withTargetEntity(goalTarget);
                    }
                    selectedChoice.getFactories().forEach(factory -> factory.withGoalFactory(goalFactory));
                } else {
                    if(goalTarget!=null) {
                        selectedChoice.getFactories().forEach(factory -> factory.getGoalFactory().withTargetEntity(goalTarget));
                    }
                }
                //TODO: spawn location suchen.
                selectedChoice.getFactories().forEach(factory -> {
                    try {
                        context.getScript().addEntity(EntitiesPlugin.getEntityServer().spawnEntity(factory));
                    } catch (InvalidLocationException | InvalidDataException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Goal type: "+(goalFactory!=null?goalFactory.getGoalType().name():"null"));
        this.choices = choices;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.goalFactory = goalFactory;
        this.goalTargetSelector = goalTargetSelector;
        this.probability = probability;
    }

    private static class Choice {

        private final int weight;

        private final List<VirtualEntityFactory> factories;

        public Choice(int weight, List<VirtualEntityFactory> factories) {
            this.weight = weight;
            this.factories = factories;
        }

        public int getWeight() {
            return weight;
        }

        public List<VirtualEntityFactory> getFactories() {
            return factories;
        }
    }
}
