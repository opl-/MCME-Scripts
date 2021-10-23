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
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Location;

import java.util.List;
import java.util.Random;

public class SpawnRandomSelectionAction extends SelectingAction<McmeEntity> {

    public SpawnRandomSelectionAction(Selector<McmeEntity> selector, RandomSpawnData data) {
        super(selector, (entity,context) -> {
            DebugManager.verbose(Modules.Action.execute(SpawnRandomSelectionAction.class),"Selected entity: "+entity.getName());
            data.spawn(context, entity.getLocation());
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector());
    }

    public static class RandomSpawnData {
        private int minQuantity = 2, maxQuantity = 5, minRadius = 5, maxRadius = 10;

        private boolean group = true;

        private final List<Choice> choices;

        private VirtualEntityGoalFactory goalFactory = null;

        private Selector<McmeEntity> goalTargetSelector = null;

        private double probability = 0.1;

        public static final Random random = new Random();

        public RandomSpawnData(List<Choice> choices) {
            this.choices = choices;
        }

        public void spawn(TriggerContext context, Location center) {
            if(probability < random.nextFloat()) {
                int quantity = getQuantity();
                Choice selectedChoice = getSelectedChoice();
                if(quantity > 0 && selectedChoice.getFactories().size()>0) {
                    updateGoal(context, selectedChoice);
                    //TODO: spawn location suchen.
                    Location[] spawnLocations = new Location[quantity];
                    if(group) {
                        Location direction = center.clone();
                        direction.setYaw(random.nextFloat()*360-180);
                        int radius = getRadius();
                        Location groupCenter = center.clone().add(direction.getDirection().multiply(radius));
                        int edge = (int) FastMath.sqrt(quantity);
                        int i = 0;
                        while(i < spawnLocations.length) {
                            Location location = groupCenter.clone().add(random.nextInt(edge*2+1)-edge,
                                                                        random.nextInt(edge*2+1)-edge, 0);
                            if(notContains(spawnLocations, location)) {
                                location = findSave(location);
                                location.setDirection(center.clone().subtract(location).toVector());
                                spawnLocations[i] = location;
                                i++;
                            }
                        }
                    } else {
                        int i = 0;
                        while(i < spawnLocations.length) {
                            Location direction = center.clone();
                            direction.setYaw(random.nextFloat()*360-180);
                            int radius = getRadius();
                            Location location = center.clone().add(direction.getDirection().multiply(radius));
                            if(notContains(spawnLocations, location)) {
                                location = findSave(location);
                                location.setDirection(center.clone().subtract(location).toVector());
                                spawnLocations[i] = location;
                                i++;
                            }
                        }
                    }
                    for(int i =0; i < quantity; i++) {
                        int finalI = i;
                        selectedChoice.getFactories().forEach(factory -> {
                            try {
                                factory.withLocation(spawnLocations[finalI]);
                                context.getScript().addEntity(EntitiesPlugin.getEntityServer().spawnEntity(factory));
                            } catch (InvalidLocationException | InvalidDataException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        }

        private Location findSave(Location location) {

        }

        private boolean notContains(Location[] spawnLocations, Location newLocation) {
            for(Location search: spawnLocations) {
                if(search.getBlockX() == newLocation.getBlockX() && search.getBlockZ() == newLocation.getBlockZ()) {
                    return false;
                }
            }
            return true;
        }

        private Choice getSelectedChoice() {
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
            return choices.get(i);
        }

        private void updateGoal(TriggerContext context, Choice selectedChoice) {
            McmeEntity goalTarget = goalTargetSelector.select(context).stream().findFirst().orElse(null);
            if (goalFactory != null) {
                if (goalTarget != null) {
                    goalFactory.withTargetEntity(goalTarget);
                }
                selectedChoice.getFactories().forEach(factory -> factory.withGoalFactory(goalFactory));
            } else {
                if (goalTarget != null) {
                    selectedChoice.getFactories().forEach(factory -> factory.getGoalFactory().withTargetEntity(goalTarget));
                }
            }
        }
        public RandomSpawnData withMinQuantity(int minQuantity) {
            this.minQuantity = minQuantity;
            return this;
        }

        public RandomSpawnData withMaxQuantity(int maxQuantity) {
            this.maxQuantity = maxQuantity;
            return this;
        }

        public RandomSpawnData withMinRadius(int minRadius) {
            this.minRadius = minRadius;
            return this;
        }

        public RandomSpawnData withMaxRadius(int maxRadius) {
            this.maxRadius = maxRadius;
            return this;
        }

        public RandomSpawnData withGroup(boolean group) {
            this.group = group;
            return this;
        }

        public RandomSpawnData withGoalFactory(VirtualEntityGoalFactory goalFactory) {
            this.goalFactory = goalFactory;
            return this;
        }

        public RandomSpawnData withGoalTargetSelector(Selector<McmeEntity> goalTargetSelector) {
            this.goalTargetSelector = goalTargetSelector;
            return this;
        }

        public RandomSpawnData withProbability(double probability) {
            this.probability = probability;
            return this;
        }

        public int getQuantity() {
            return minQuantity + random.nextInt(maxQuantity - minQuantity + 1);
        }

        public int getRadius() {
            return minRadius + random.nextInt(maxRadius - minRadius + 1);
        }


    }

    public static class Choice {

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
