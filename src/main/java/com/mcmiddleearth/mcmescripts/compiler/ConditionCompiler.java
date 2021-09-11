package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.condition.TalkCondition;
import com.mcmiddleearth.mcmescripts.condition.proximity.LocationProximityCondition;
import com.mcmiddleearth.mcmescripts.condition.proximity.PlayerProximityCondition;
import com.mcmiddleearth.mcmescripts.condition.proximity.VirtualEntityProximityCondition;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ConditionCompiler {

    private static final String KEY_CONDITION           = "condition",
                                KEY_CONDITION_ARRAY     = "conditions",
                                KEY_CONDITION_TYPE      = "type",
                                KEY_CONSTRAIN           = "constrain",
                                KEY_CENTER              = "center",

                                VALUE_TALK                  = "talk",
                                VALUE_NO_TALK               = "noTalk",
                                VALUE_PROXIMITY_LOCATION    = "locationProximity",
                                VALUE_PROXIMITY_PLAYER      = "playerProximity",
                                VALUE_PROXIMITY_ENTITY      = "entityProximity";

    public static Set<Condition> compile(JsonObject jsonData) {
        JsonElement conditions = jsonData.get(KEY_CONDITION);
        Set<Condition> result = new HashSet<>(compileConditions(conditions));
        conditions = jsonData.get(KEY_CONDITION_ARRAY);
        result.addAll(compileConditions(conditions));
        return result;
    }

    private static Set<Condition> compileConditions(JsonElement conditionData) {
        Set<Condition> result = new HashSet<>();
        if(conditionData.isJsonArray()) {
            for(int i = 0; i< conditionData.getAsJsonArray().size(); i++) {
                Condition condition = compileCondition(conditionData.getAsJsonArray().get(i).getAsJsonObject());
                if(condition!=null) result.add(condition);
            }
        } else {
            Condition condition = compileCondition(conditionData.getAsJsonObject());
            if(condition!=null) result.add(condition);
        }
        return result;
    }

    private static Condition compileCondition(JsonObject jsonObject) {
        JsonElement type = jsonObject.get(KEY_CONDITION_TYPE);
        if(type==null)  return null;
        @SuppressWarnings("rawtypes")
        Selector selector;
        boolean noTalk = false;
        try {
            switch (type.getAsString()) {
                case VALUE_NO_TALK:
                    noTalk = true;
                case VALUE_TALK:
                    selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                    return new TalkCondition((VirtualEntitySelector) selector,noTalk);
                case VALUE_PROXIMITY_LOCATION:
                    selector = SelectorCompiler.compileEntitySelector(jsonObject);
                    Location location = LocationCompiler.compile(jsonObject.get(KEY_CENTER));
                    return new LocationProximityCondition(location, selector, compileFunction(jsonObject));
                case VALUE_PROXIMITY_ENTITY:
                    selector = SelectorCompiler.compileEntitySelector(jsonObject);
                    String entityName = jsonObject.get(KEY_CENTER).getAsString();
                    return new VirtualEntityProximityCondition(entityName, selector, compileFunction(jsonObject));
                case VALUE_PROXIMITY_PLAYER:
                    selector = SelectorCompiler.compileEntitySelector(jsonObject);
                    String playerName = jsonObject.get(KEY_CENTER).getAsString();
                    return new PlayerProximityCondition(playerName, selector, compileFunction(jsonObject));
            }
        } catch(NullPointerException ignore) {}
        return null;
    }

    private static Function<Integer,Boolean> compileFunction(JsonObject jsonObject) {
        JsonElement constrainData = jsonObject.get(KEY_CONSTRAIN);
        if(constrainData!=null) {
            try {
                String constrain = constrainData.getAsString();
                String comparator;
                int limit;
                if (constrain.charAt(1) == '=' || constrain.charAt(1) == '<') {
                    comparator = constrain.substring(0, 2);
                    limit = Integer.parseInt(constrain.substring(2));
                } else {
                    comparator = constrain.substring(0, 1);
                    limit = Integer.parseInt(constrain.substring(1));
                }
                switch(comparator) {
                    case "<":
                        return a -> a < limit;
                    case ">":
                        return a -> a > limit;
                    case "<=":
                        return a -> a <= limit;
                    case ">=":
                        return a -> a >= limit;
                    case "=":
                        return a -> a == limit;
                    case "<>":
                    case "!=":
                        return a -> a != limit;
                }
            } catch(NumberFormatException ignore) {}
        }
        return a -> true;
    }
}
