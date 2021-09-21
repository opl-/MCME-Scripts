package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.condition.TalkCondition;
import com.mcmiddleearth.mcmescripts.condition.proximity.LocationProximityCondition;
import com.mcmiddleearth.mcmescripts.condition.proximity.PlayerProximityCondition;
import com.mcmiddleearth.mcmescripts.condition.proximity.VirtualEntityProximityCondition;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ConditionCompiler {

    private static final String KEY_CONDITION           = "condition",
                                KEY_CONDITION_ARRAY     = "conditions",
                                KEY_CONDITION_TYPE      = "type",
                                KEY_CONSTRAIN           = "constrain",
                                KEY_CENTER              = "center",
                                KEY_MATCH_ALL_SELECTED = "matchAll",

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
        if(conditionData==null) return result;
        if(conditionData.isJsonArray()) {
            for(int i = 0; i< conditionData.getAsJsonArray().size(); i++) {
                compileCondition(conditionData.getAsJsonArray().get(i).getAsJsonObject()).ifPresent(result::add);
            }
        } else {
            compileCondition(conditionData.getAsJsonObject()).ifPresent(result::add);
            //if(condition!=null) result.add(condition);
        }
        return result;
    }

    private static Optional<Condition> compileCondition(JsonObject jsonObject) {
        JsonElement type = jsonObject.get(KEY_CONDITION_TYPE);
        if(type==null)  return Optional.empty();
        @SuppressWarnings("rawtypes")
        Selector selector;
        boolean noTalk = false;
        try {
            switch (type.getAsString()) {
                case VALUE_NO_TALK:
                    noTalk = true;
                case VALUE_TALK:
                    selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                    TalkCondition condition = new TalkCondition((VirtualEntitySelector) selector,noTalk);
                    getMatchAll(jsonObject).ifPresent(condition::setMatchAllSelected);
                    return Optional.of(condition);
                case VALUE_PROXIMITY_LOCATION:
                    selector = SelectorCompiler.compileMcmeEntitySelector(jsonObject).orElse(new McmeEntitySelector("@p"));
                    Location location = LocationCompiler.compile(jsonObject.get(KEY_CENTER)).orElse(null);
                    return Optional.of(new LocationProximityCondition(location, selector, compileFunction(jsonObject)));
                case VALUE_PROXIMITY_ENTITY:
                    selector = SelectorCompiler.compileMcmeEntitySelector(jsonObject).orElse(new McmeEntitySelector("@p"));
                    String entityName = jsonObject.get(KEY_CENTER).getAsString();
                    return Optional.of(new VirtualEntityProximityCondition(entityName, selector, compileFunction(jsonObject)));
                case VALUE_PROXIMITY_PLAYER:
                    selector = SelectorCompiler.compileMcmeEntitySelector(jsonObject).orElse(new McmeEntitySelector("@p"));
                    String playerName = jsonObject.get(KEY_CENTER).getAsString();
                    return Optional.of(new PlayerProximityCondition(playerName, selector, compileFunction(jsonObject)));
            }
        } catch(NullPointerException ignore) {}
        return Optional.empty();
    }

    private static Optional<Boolean> getMatchAll(JsonObject jsonObject) {
        JsonElement selectorJson = jsonObject.get(KEY_MATCH_ALL_SELECTED);
        if(selectorJson == null || selectorJson.isJsonPrimitive()) return Optional.empty();
        return Optional.of(selectorJson.getAsBoolean());
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
