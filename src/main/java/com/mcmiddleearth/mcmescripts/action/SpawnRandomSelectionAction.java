package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class SpawnRandomSelectionAction extends SelectingAction<McmeEntity> {

    public SpawnRandomSelectionAction(Selector<McmeEntity> selector, RandomSpawnData data, int lifespan) {
        super(selector, (entity,context) -> {
            DebugManager.verbose(Modules.Action.execute(SpawnRandomSelectionAction.class),"Selected entity: "+entity.getName());
//data.getChoices().forEach(choice -> choice.getFactories().forEach(factory -> Logger.getGlobal().info("6: "+factory.getType())));
            data.spawn(context, entity.getLocation(), lifespan);
        });
/*new BukkitRunnable() {
    @Override
    public void run() {
        data.getChoices().forEach(choice -> choice.getFactories().forEach(factory -> Logger.getGlobal().info("5: "+factory.getType())));
    }
}.runTaskTimer(MCMEScripts.getInstance(),20,20);*/
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

        public void spawn(TriggerContext context, Location center, int lifespan) {
//choices.forEach(choice -> choice.getFactories().forEach(factory -> Logger.getGlobal().info("Factory random Spawn: "+factory.getType())));
            Set<McmeEntity> entities = new HashSet<>();
            float rand = random.nextFloat();
//Logger.getGlobal().info("probability: "+probability+ " > "+rand+" "+(probability>rand));
            if(probability > rand) {
                int quantity = getQuantity();
//Logger.getGlobal().info("spawn: "+quantity);
//Logger.getGlobal().info("choices: "+choices.size());
//choices.forEach(choice -> Logger.getGlobal().info("choice: "+choice.getFactories().size()));
                Choice selectedChoice = getSelectedChoice();
                if(quantity > 0 && selectedChoice.getFactories().size()>0) {
                    updateGoal(context, selectedChoice);
                    Location[] spawnLocations = new Location[quantity];
                    if(group) {
//Logger.getGlobal().info("group: "+group);
                        Location direction = center.clone();
                        direction.setYaw(random.nextFloat()*360-180);
                        int radius = getRadius();
                        Location groupCenter = center.clone().add(direction.getDirection().multiply(radius));
                        int edge = (int) FastMath.sqrt(quantity);
                        int i = 0;
                        int tries = 0;
                        while(i < spawnLocations.length && tries < 1000) {
                            Location location = groupCenter.clone().add(random.nextInt(edge*2+1)-edge,
                                                                        random.nextInt(edge*2+1)-edge, 0);
//Logger.getGlobal().info("try spawn location: "+location);
                            if(notContains(spawnLocations, location)) {
                                location = findSafe(location).add(0.5,0,0.5);;
                                location.setDirection(center.clone().subtract(location).toVector());
                                spawnLocations[i] = location;
//Logger.getGlobal().info("add spawn location: "+location);
                                i++;
                            }
                            tries++;
                        }
                    } else {
//Logger.getGlobal().info("group: "+group);
                        int i = 0;
                        int tries = 0;
                        while(i < spawnLocations.length && tries < 1000) {
                            Location direction = center.clone();
                            direction.setYaw(random.nextFloat()*360-180);
                            int radius = getRadius();
                            Location location = center.clone().add(direction.getDirection().multiply(radius));
//Logger.getGlobal().info("try spawn location: "+location);
                            if(notContains(spawnLocations, location)) {
                                location = findSafe(location).add(0.5,0,0.5);
                                location.setDirection(center.clone().subtract(location).toVector());
                                spawnLocations[i] = location;
//Logger.getGlobal().info("try spawn location: "+location);
                                i++;
                            }
                            tries++;
                        }
                    }
                    String name = context.getName();
                    for(int i =0; i < quantity; i++) {
                        int finalI = i;
                        selectedChoice.getFactories().forEach(factory -> {
                            if(spawnLocations[finalI]!=null) {
                                try {
                                    factory.withLocation(spawnLocations[finalI]);
                                    if(name!=null) factory.withDisplayName(name);
//Logger.getGlobal().info("Factory type Action: "+factory.getType());
                                    McmeEntity entity = EntitiesPlugin.getEntityServer().spawnEntity(factory);
//Logger.getGlobal().info("Spawn done!");
                                    context.getScript().addEntity(entity);
                                    entities.add(entity);
//Logger.getGlobal().info("Execute spawn: " + factory.getLocation());
                                } catch (InvalidLocationException | InvalidDataException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
            if(lifespan > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        entities.forEach(entity -> context.getScript().removeEntity(entity));
                        EntitiesPlugin.getEntityServer().removeEntity(entities);
                    }
                }.runTaskLater(MCMEScripts.getInstance(), lifespan);
            }
        }

        private Location findSafe(Location location) {
            Block lower = location.getBlock();
            Block upper = lower.getRelative(BlockFace.UP);
            while((lower.isPassable() && upper.isPassable()) && lower.getY()>1) {
                upper = lower;
                lower = upper.getRelative(BlockFace.DOWN);
            }
            if(!lower.isPassable() && upper.isPassable() && upper.getRelative(BlockFace.UP).isPassable()) {
                return upper.getLocation();
            }
            while(!(lower.isPassable() && upper.isPassable()) && upper.getY() < location.getWorld().getMaxHeight()-1) {
                lower = upper;
                upper = lower.getRelative(BlockFace.UP);
            }
            if(lower.isPassable() && upper.isPassable()) {
                return lower.getLocation();
            } else {
                if(upper.isPassable()) {
                    return upper.getLocation();
                } else {
                    return upper.getLocation().add(0,1,0);
                }
            }
        }

        private boolean notContains(Location[] spawnLocations, Location newLocation) {
            for(Location search: spawnLocations) {
                if(search!= null && search.getBlockX() == newLocation.getBlockX()
                                 && search.getBlockZ() == newLocation.getBlockZ()) {
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
//Logger.getGlobal().info("weightsum: "+weightSum+" rand: "+randomWeight);
            Choice selectedChoice;
            int i = -1;
            int currentWeight = 0;
            do {
                i++;
                currentWeight = currentWeight + choices.get(i).weight;
            }
            while(randomWeight > currentWeight);
//Logger.getGlobal().info("i: "+i);
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

        public List<Choice> getChoices() {
            return choices;
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
