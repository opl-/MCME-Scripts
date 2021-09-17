package com.mcmiddleearth.mcmescripts.selector;

import com.google.common.base.Joiner;
import com.mcmiddleearth.entities.api.McmeEntityType;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.GameMode;

import java.util.logging.Logger;

public abstract class EntitySelector<T> implements Selector<T> {

    private final String selector;

    protected VirtualEntitySelector.SelectorType selectorType;
    protected int limit = Integer.MAX_VALUE;
    protected double x,y,z;
    protected boolean xRelative = true, yRelative = true, zRelative = true;
    protected double dx = -1, dy = -1, dz = -1;
    protected double minDistanceSquared = -1, maxDistanceSquared = Double.MAX_VALUE;
    protected McmeEntityType entityType;
    protected boolean excludeType;
    protected String name;
    protected boolean excludeName;
    protected GameMode gameMode;
    protected boolean excludeGameMode;
    protected float minPitch = -90, maxPitch = 90;
    protected float minYaw = -180, maxYaw = 180;

    public EntitySelector(String selector) throws IndexOutOfBoundsException {
DebugManager.log(Modules.Selector.create(this.getClass()),"Selector: "+selector);
        this.selector = selector;
        selector = selector.replace(" ","");
        switch(selector.charAt(1)) {
            case 'a':
                selectorType = EntitySelector.SelectorType.ALL_PLAYERS;
                break;
            case 'e':
                selectorType = EntitySelector.SelectorType.ALL_ENTITIES;
                break;
            case 'v':
                selectorType = EntitySelector.SelectorType.VIRTUAL_ENTITIES;
                break;
            case 's':
                selectorType = EntitySelector.SelectorType.TRIGGER_ENTITY;
                break;
            case 'r':
                selectorType = EntitySelector.SelectorType.RANDOM_PLAYER;
                break;
            case 'p':
            default:
                selectorType = EntitySelector.SelectorType.NEAREST_PLAYER;
        }
        if(selector.length()>2) {
            if(selector.charAt(2) != '[' || selector.charAt(selector.length()-1)!=']') return;
            String[] arguments = selector.substring(3,selector.length()-1).split(",");
            for(String argument : arguments) {
                String[] split = argument.split("=");
DebugManager.log(Modules.Selector.create(this.getClass()),"split: "+ Joiner.on("_").join(split));
                switch(split[0]) {
                    case "limit":
                        limit = Integer.parseInt(split[1]);
                        break;
                    case "x":
                        if (split[1].startsWith("~")) {
                            split[1] = split[1].substring(1);
                        } else {
                            xRelative = false;
                        }
                        if (split[1].length() > 0) {
                            x = Double.parseDouble(split[1]);
                        }
                        break;
                    case "y":
                        if (split[1].startsWith("~")) {
                            split[1] = split[1].substring(1);
                        } else {
                            yRelative = false;
                        }
                        if (split[1].length() > 0) {
                            y = Double.parseDouble(split[1]);
                        }
                        break;
                    case "z":
                        if (split[1].startsWith("~")) {
                            split[1] = split[1].substring(1);
                        } else {
                            zRelative = false;
                        }
                        if (split[1].length() > 0) {
                            z = Double.parseDouble(split[1]);
                        }
                        break;
                    case "dx":
                        dx = Double.parseDouble(split[1]);
                        break;
                    case "dy":
                        dy = Double.parseDouble(split[1]);
                        break;
                    case "dz":
                        dz = Double.parseDouble(split[1]);
                        break;
                    case "distance":
                        String[] minMax = split[1].split("\\.\\.");
DebugManager.log(Modules.Selector.create(this.getClass()),"minMax "+Joiner.on("_").join(minMax));
                        double minDistance = Double.parseDouble(minMax[0]);
                        minDistanceSquared = minDistance * minDistance;
                        if(minMax.length > 1) {
                            double maxDistance = Double.parseDouble(minMax[1]);
                            maxDistanceSquared = maxDistance * maxDistance;
                        } else {
                            maxDistanceSquared = minDistanceSquared;
                        }
DebugManager.log(Modules.Selector.create(this.getClass()),"Set Distances: "+minDistanceSquared+" "+maxDistanceSquared);
                        break;
                    case "type":
                        if(split[1].startsWith("!")) {
                            entityType = McmeEntityType.valueOf(split[1].substring(1));
                            excludeType = true;
                        } else {
                            entityType = McmeEntityType.valueOf(split[1]);
                        }
                        break;
                    case "name":
                        if(split[1].startsWith("!")) {
                            name = split[1].substring(1);
                            excludeName = true;
                        } else {
                            name = split[1];
                        }
                        break;
                    case "gamemode":
                        if(split[1].startsWith("1")) {
                            gameMode = GameMode.valueOf(split[1].substring(1).toUpperCase());
                            excludeGameMode = true;
                        } else {
                            gameMode = GameMode.valueOf(split[1].toUpperCase());
                        }
                        break;
                    case "x_rotation":
                        minMax = split[1].split("\\.\\.");
                        minPitch = Float.parseFloat(minMax[0]);
                        if(minMax.length > 1) {
                            maxPitch = Float.parseFloat(minMax[1]);
                        } else {
                            maxPitch = minPitch;
                        }
                        break;
                    case "y_rotation":
                        minMax = split[1].split("\\.\\.");
                        minYaw = Float.parseFloat(minMax[0]);
                        if(minMax.length > 1) {
                            maxYaw = Float.parseFloat(minMax[1]);
                        } else {
                            maxYaw = minYaw;
                        }
                        break;
                }
            }
        }
    }

    public boolean hasAreaLimit() {
        return dx >= 0 || dy >=  0 || dz >= 0;
    }

    public double getAbsolute(double trigger, boolean relative, double selector) {
        if(relative) {
            return trigger+selector;
        } else {
            return selector;
        }
    }

    public String getSelector() {
        return selector;
    }

    public enum SelectorType {
        NEAREST_PLAYER,
        RANDOM_PLAYER,
        ALL_PLAYERS,
        VIRTUAL_ENTITIES,
        ALL_ENTITIES,
        TRIGGER_ENTITY
    }

    public static enum Order {
        NEAREST, FURTHEST, RANDOM, ARBITRARY;
    }

    public static class EntitySelectorElement<T> {
        private final T content;
        private double value;

        public EntitySelectorElement(T content) {
            this.content = content;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public T getContent() {
            return content;
        }
    }


}
