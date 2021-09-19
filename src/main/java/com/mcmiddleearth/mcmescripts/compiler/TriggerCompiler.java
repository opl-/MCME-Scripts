package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.TimedTriggerManager;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.trigger.*;
import com.mcmiddleearth.mcmescripts.trigger.player.PlayerJoinTrigger;
import com.mcmiddleearth.mcmescripts.trigger.player.PlayerQuitTrigger;
import com.mcmiddleearth.mcmescripts.trigger.player.PlayerTalkTrigger;
import com.mcmiddleearth.mcmescripts.trigger.player.VirtualPlayerAttackTrigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.OnceRealTimeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.OnceServerTimeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.PeriodicRealTimeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.PeriodicServerTimeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.virtual.GoalFinishedTrigger;
import com.mcmiddleearth.mcmescripts.trigger.virtual.VirtualEntityStopTalkTrigger;
import com.mcmiddleearth.mcmescripts.trigger.virtual.VirtualEntityTalkTrigger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class TriggerCompiler {

    public static final String  KEY_TRIGGER         = "event",
                                KEY_TRIGGER_ARRAY   = "events",
                                KEY_TYPE            = "type",
                                KEY_TIME            = "time",
                                KEY_PERIOD          = "period",
                                KEY_THEN            = "then",
                                KEY_ELSE            = "else",
                                KEY_CALL_ONCE       = "callOnce",
                                KEY_LOCATION        = "location",
                                KEY_MET_ALL_CONDITIONS  = "metAllConditions",

                                VALUE_REAL_TIMED_TRIGGER    = "realTimed",
                                VALUE_REAL_PERIODIC_TRIGGER   = "realPeriodic",
                                VALUE_SERVER_TIMED_TRIGGER  = "serverTimed",
                                VALUE_SERVER_PERIODIC_TRIGGER   = "serverPeriodic",
                                VALUE_PLAYER_TALK_TRIGGER   = "playerTalk",
                                VALUE_PLAYER_JOIN_TRIGGER   = "playerJoin",
                                VALUE_PLAYER_QUIT_TRIGGER   = "playerQuit",
                                VALUE_VIRTUAL_PLAYER_ATTACK_TRIGGER     = "virtualPlayerAttack",
                                VALUE_VIRTUAL_TALK_TRIGGER              = "virtualTalk",
                                VALUE_VIRTUAL_STOP_TALK_TRIGGER         = "virtualStopTalk",
                                VALUE_GOAL_FINISHED_TRIGGER             = "goalFinished";

    public static Set<Trigger> compile(JsonObject jsonData) {
        JsonElement triggerData = jsonData.get(KEY_TRIGGER);
        Set<Trigger> triggers = new HashSet<>(compileTriggers(triggerData));
        triggerData = jsonData.get(KEY_TRIGGER_ARRAY);
        triggers.addAll(compileTriggers(triggerData));
        return triggers;
    }

    private static Set<Trigger> compileTriggers(JsonElement triggerData) {
Logger.getGlobal().info("TriggerData: "+triggerData);
        Set<Trigger> triggers = new HashSet<>();
        if(triggerData == null) return triggers;
        if(triggerData.isJsonArray()) {
Logger.getGlobal().info("ArraySize: "+triggerData.getAsJsonArray().size());
            for(int i = 0; i< triggerData.getAsJsonArray().size(); i++) {
                compileTrigger(triggerData.getAsJsonArray().get(i).getAsJsonObject()).ifPresent(triggers::add);
Logger.getGlobal().info("add: "+triggers.size());
            }
        } else {
Logger.getGlobal().info("Single!");
            compileTrigger(triggerData.getAsJsonObject()).ifPresent(triggers::add);
        }
        return triggers;
    }

    private static Optional<Trigger> compileTrigger(JsonObject jsonObject) {
        JsonElement type = jsonObject.get(KEY_TYPE);
Logger.getGlobal().info("Type: "+type);
        if(type==null)  return Optional.empty();

        DecisionTreeTrigger trigger = null;
        switch(type.getAsString()) {
            case VALUE_REAL_TIMED_TRIGGER:
                JsonElement time = jsonObject.get(KEY_TIME);
Logger.getGlobal().info("RealTime: "+time);
                if(time!=null) {
                    LocalDateTime localDateTime = LocalDateTime.parse(time.getAsString());
                    ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
                    long millis = zdt.toInstant().toEpochMilli();
                    trigger = new OnceRealTimeTrigger(null, millis);
                    trigger.setCallOnce(true);
                }
                break;
            case VALUE_REAL_PERIODIC_TRIGGER:
                time = jsonObject.get(KEY_PERIOD);
Logger.getGlobal().info("Periodic RealTime: "+time);
                if(time != null && time.isJsonPrimitive()) {
                    trigger = new PeriodicRealTimeTrigger(null,time.getAsInt());
                }
                break;
            case VALUE_SERVER_TIMED_TRIGGER:
                time = jsonObject.get(KEY_TIME);
Logger.getGlobal().info("ServerTime: "+time);
                if(time!=null) {
                    trigger = new OnceServerTimeTrigger(null, time.getAsInt());
                }
                break;
            case VALUE_SERVER_PERIODIC_TRIGGER:
                time = jsonObject.get(KEY_PERIOD);
Logger.getGlobal().info("Periodic RealTime: "+time);
                if(time != null && time.isJsonPrimitive()) {
                    trigger = new PeriodicServerTimeTrigger(null,time.getAsInt());
                } else {
                    trigger = new PeriodicServerTimeTrigger(null, TimedTriggerManager.MIN_TRIGGER_CHECK_PERIOD);
                }
                break;
            case VALUE_PLAYER_TALK_TRIGGER:
                trigger = new PlayerTalkTrigger(null);
                break;
            case VALUE_PLAYER_JOIN_TRIGGER:
                trigger = new PlayerJoinTrigger(null);
                break;
            case VALUE_PLAYER_QUIT_TRIGGER:
                trigger = new PlayerQuitTrigger(null);
                break;
            case VALUE_VIRTUAL_PLAYER_ATTACK_TRIGGER:
                trigger = new VirtualPlayerAttackTrigger(null);
                break;
            case VALUE_GOAL_FINISHED_TRIGGER:
                trigger = new GoalFinishedTrigger(null);
                break;
            case VALUE_VIRTUAL_TALK_TRIGGER:
                trigger = new VirtualEntityTalkTrigger(null);
                break;
            case VALUE_VIRTUAL_STOP_TALK_TRIGGER:
                trigger = new VirtualEntityStopTalkTrigger(null);
                break;
        }
        if(trigger == null) return Optional.empty();
        DecisionTreeTrigger.DecisionNode decisionNode = compileDecisionNode(jsonObject);
        trigger.setDecisionNode(decisionNode);
        trigger.setLocation(LocationCompiler.compile(jsonObject.get(KEY_LOCATION)).orElse(null));

        boolean callOnce = false;
        JsonElement callOnceJson = jsonObject.get(KEY_CALL_ONCE);
        if(callOnceJson!=null) {
            callOnce = callOnceJson.getAsBoolean();
        }
        trigger.setCallOnce(callOnce);

Logger.getGlobal().info("Return: "+trigger);
        return Optional.of(trigger);
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
