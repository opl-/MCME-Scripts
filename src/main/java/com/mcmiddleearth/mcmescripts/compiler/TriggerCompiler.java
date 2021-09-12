package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.trigger.*;
import com.mcmiddleearth.mcmescripts.trigger.player.PlayerJoinTrigger;
import com.mcmiddleearth.mcmescripts.trigger.player.PlayerQuitTrigger;
import com.mcmiddleearth.mcmescripts.trigger.player.PlayerTalkTrigger;
import com.mcmiddleearth.mcmescripts.trigger.player.VirtualPlayerAttackTrigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.OnceRealTimeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.OnceServerTimeTrigger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TriggerCompiler {

    public static final String  KEY_TRIGGER         = "event",
                                KEY_TRIGGER_ARRAY   = "events",
                                KEY_TYPE            = "type",
                                KEY_TIME            = "time",
                                KEY_THEN            = "then",
                                KEY_ELSE            = "else",
                                KEY_CALL_ONCE       = "callOnce",
                                KEY_LOCATION        = "location",
                                KEY_MET_ALL_CONDITIONS  = "metAllConditions",

                                VALUE_REAL_TIMED_TRIGGER    = "realTimed",
                                VALUE_SERVER_TIMED_TRIGGER  = "serverTimed",
                                VALUE_PLAYER_TALK_TRIGGER   = "playerTalk",
                                VALUE_PLAYER_JOIN_TRIGGER   = "playerJoin",
                                VALUE_PLAYER_QUIT_TRIGGER   = "playerQuit",
                                VALUE_VIRTUAL_PLAYER_ATTACK_TRIGGER     = "virtualPlayerAttack",
                                VALUE_GOAL_FINISHED_TRIGGER             = "goalFinished";

    public static Set<Trigger> compile(JsonObject jsonData) {
        JsonElement triggerData = jsonData.get(KEY_TRIGGER);
        Set<Trigger> triggers = new HashSet<>(compileTriggers(triggerData));
        triggerData = jsonData.get(KEY_TRIGGER_ARRAY);
        triggers.addAll(compileTriggers(triggerData));
        return triggers;
    }

    private static Set<Trigger> compileTriggers(JsonElement triggerData) {
        Set<Trigger> triggers = new HashSet<>();
        if(triggerData.isJsonArray()) {
            for(int i = 0; i< triggerData.getAsJsonArray().size(); i++) {
                Trigger trigger = compileTrigger(triggerData.getAsJsonArray().get(i).getAsJsonObject());
                if(trigger!=null) triggers.add(trigger);
            }
        } else {
            Trigger trigger = compileTrigger(triggerData.getAsJsonObject());
            if(trigger!=null) triggers.add(trigger);
        }
        return triggers;
    }

    private static Trigger compileTrigger(JsonObject jsonObject) {
        JsonElement type = jsonObject.get(KEY_TYPE);
        if(type==null)  return null;

        DecisionTreeTrigger trigger = null;
        switch(type.getAsString()) {
            case VALUE_REAL_TIMED_TRIGGER:
                JsonElement time = jsonObject.get(KEY_TIME);
                if(time!=null) {
                    LocalDateTime localDateTime = LocalDateTime.parse(time.getAsString());
                    ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
                    long millis = zdt.toInstant().toEpochMilli();
                    trigger = new OnceRealTimeTrigger((Action) null, millis);
                }
                break;
            case VALUE_SERVER_TIMED_TRIGGER:
                time = jsonObject.get(KEY_TIME);
                if(time!=null) {
                    trigger = new OnceServerTimeTrigger((Action) null, time.getAsInt());
                }
                break;
            case VALUE_PLAYER_TALK_TRIGGER:
                trigger = new PlayerTalkTrigger((Action) null);
                break;
            case VALUE_PLAYER_JOIN_TRIGGER:
                /*JsonElement firstData = jsonObject.get(KEY_FIRST_JOIN);
                boolean firstJoin=false;
                if(first!=null) {
                    firstJoin = firstData.getAsBoolean();
                }*/
                trigger = new PlayerJoinTrigger((Action) null);
                break;
            case VALUE_PLAYER_QUIT_TRIGGER:
                trigger = new PlayerQuitTrigger((Action) null);
                break;
            case VALUE_VIRTUAL_PLAYER_ATTACK_TRIGGER:
                trigger = new VirtualPlayerAttackTrigger((Action) null);
                break;
            case VALUE_GOAL_FINISHED_TRIGGER:
                trigger = new GoalFinishedTrigger((Action)null);
                break;
        }
        if(trigger == null) return null;
        DecisionTreeTrigger.DecisionNode decisionNode = compileDecisionNode(jsonObject);
        trigger.setDecisionNode(decisionNode);
        trigger.setLocation(LocationCompiler.compile(jsonObject.get(KEY_LOCATION)));

        boolean callOnce = false;
        JsonElement callOnceJson = jsonObject.get(KEY_CALL_ONCE);
        if(callOnceJson!=null) {
            callOnce = callOnceJson.getAsBoolean();
        }
        trigger.setCallOnce(callOnce);


        return trigger;
    }

    private static DecisionTreeTrigger.DecisionNode compileDecisionNode(JsonObject jsonObject) {
        Collection<Condition> conditions = ConditionCompiler.compile(jsonObject);
        boolean metAllConditions = getMetAllConditions(jsonObject);
        Collection<Action> actions = ActionCompiler.compile(jsonObject);
        DecisionTreeTrigger.DecisionNode node = new DecisionTreeTrigger.DecisionNode(actions);
        node.setMetAllConditions(metAllConditions);
        node.addConditions(conditions);

        JsonElement thenData = jsonObject.get(KEY_THEN);
        if(thenData!=null) {
            node.setConditionSuccessTrigger(compileDecisionNode(thenData.getAsJsonObject()));
        }
        JsonElement elseData = jsonObject.get(KEY_ELSE);
        if(thenData!=null) {
            node.setConditionFailTrigger(compileDecisionNode(elseData.getAsJsonObject()));
        }
        return node;
    }

    public static boolean getMetAllConditions(JsonObject jsonData) {
        JsonElement data = jsonData.get(KEY_MET_ALL_CONDITIONS);
        if(data!=null) {
            return data.getAsBoolean();
        }
        return false;
    }


}
