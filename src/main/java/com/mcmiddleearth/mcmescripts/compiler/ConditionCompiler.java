package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.condition.TalkCondition;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;

import java.util.HashSet;
import java.util.Set;

public class ConditionCompiler {

    private static final String KEY_MET_ALL_CONDITIONS  = "metAllConditions",
                                KEY_CONDITION           = "condition",
                                KEY_CONDITION_ARRAY     = "conditions",
                                KEY_CONDITION_TYPE      = "type",

                                VALUE_TALK              = "talk";

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
        @SuppressWarnings("all")
        Selector selector;
        switch(type.getAsString()) {
            case VALUE_TALK:
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                return new TalkCondition((VirtualEntitySelector)selector);
        }
        return null;
    }

    public static boolean getMetAllConditions(JsonObject jsonData) {
        JsonElement data = jsonData.get(KEY_MET_ALL_CONDITIONS);
        if(data!=null) {
            return data.getAsBoolean();
        }
        return false;
    }
}
