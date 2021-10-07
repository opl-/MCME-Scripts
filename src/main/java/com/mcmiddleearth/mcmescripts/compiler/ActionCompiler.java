package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import com.mcmiddleearth.mcmescripts.action.*;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;

import java.util.*;


public class ActionCompiler {

    private static final String KEY_ACTION          = "action",
                                KEY_ACTION_ARRAY    = "actions",

                                KEY_ACTION_TYPE     = "type",
                                KEY_DELAY           = "delay",
                                KEY_TARGET          = "location",
                                KEY_GOAL_TARGET     = "goal_target",
                                KEY_TRIGGER_NAME    = "name",
                                KEY_TELEPORT_SPREAD = "spread",


                                VALUE_REGISTER_TRIGGER      = "register_event",
                                VALUE_UNREGISTER_TRIGGER    = "unregister_event",

                                VALUE_SET_GOAL              = "set_goal",
                                VALUE_SPAWN                 = "spawn",
                                VALUE_DESPAWN               = "despawn",
                                VALUE_STOP_TALK             = "stop_talk",
                                VALUE_TALK                  = "talk",
                                VALUE_TELEPORT              = "teleport",
                                VALUE_ADD_POTION_EFFECT     = "add_potion_effect",
                                VALUE_REMOVE_POTION_EFFECT  = "remove_potion_effect";


    public static Collection<Action> compile(JsonObject jsonData) {
        JsonElement actionData = jsonData.get(KEY_ACTION);
        Set<Action> result = new HashSet<>(compileActions(actionData));
        actionData = jsonData.get(KEY_ACTION_ARRAY);
        result.addAll(compileActions(actionData));
        return result;
    }

    private static Set<Action> compileActions(JsonElement actionData) {
        Set<Action> result = new HashSet<>();
        if(actionData == null) return result;
        if(actionData.isJsonArray()) {
            for(int i = 0; i< actionData.getAsJsonArray().size(); i++) {
                compileAction(actionData.getAsJsonArray().get(i).getAsJsonObject()).ifPresent(result::add);
            }
        } else {
            compileAction(actionData.getAsJsonObject()).ifPresent(result::add);
        }
        return result;
    }

    private static Optional<Action> compileAction(JsonObject jsonObject) {
        Action action;
        JsonElement type = jsonObject.get(KEY_ACTION_TYPE);
        if (type == null) {
            DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile action. Missing: "+KEY_ACTION_TYPE);
            return Optional.empty();
        }
        switch(type.getAsString()) {
            case VALUE_REGISTER_TRIGGER:
                Set<Trigger> triggers = TriggerCompiler.compile(jsonObject);
                if(triggers.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_REGISTER_TRIGGER+" action. Missing event.");
                    return Optional.empty();
                }
                action = new TriggerRegisterAction(triggers);
                break;
            case VALUE_UNREGISTER_TRIGGER:
                Set<String> triggerNames = compileTriggerNames(jsonObject);
                if(triggerNames.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_UNREGISTER_TRIGGER+" action. Missing events.");
                    return Optional.empty();
                }
                action = new TriggerUnregisterAction(triggerNames);
                break;
            case VALUE_SET_GOAL:
                Optional<VirtualEntityGoalFactory> goalFactory = VirtualEntityGoalFactoryCompiler.compile(jsonObject);
                if(!goalFactory.isPresent()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_SET_GOAL+" action. Missing goal.");
                    return Optional.empty();
                }
                VirtualEntitySelector selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                McmeEntitySelector goalTargetSelector = SelectorCompiler.compileMcmeEntitySelector(jsonObject, KEY_GOAL_TARGET);
                action = new SetGoalAction(goalFactory.get(), selector, goalTargetSelector);
                break;
            case VALUE_SPAWN:
                List<VirtualEntityFactory> factories = VirtualEntityFactoryCompiler.compile(jsonObject);
                if(factories.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_SPAWN+" action. Missing entity factory.");
                    return Optional.empty();
                }
                action = new SpawnAction(factories);
                break;
            case VALUE_DESPAWN:
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                action = new DespawnAction(selector);
                break;
            case VALUE_STOP_TALK:
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                action = new StopTalkAction(selector);
                break;
            case VALUE_TALK:
                SpeechBalloonLayout layout = SpeechBalloonLayoutCompiler.compile(jsonObject);
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                action = new TalkAction(layout,selector);
                break;
            case VALUE_TELEPORT:
                Location target = LocationCompiler.compile(jsonObject.get(KEY_TARGET)).orElse(null);
                if(target == null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_TELEPORT+" action. Missing target location.");
                    return Optional.empty();
                }
                PlayerSelector playerSelector = SelectorCompiler.compilePlayerSelector(jsonObject);
                double spread = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_TELEPORT_SPREAD),0);
                action = new TeleportAction(target,spread,playerSelector);
                break;
            case VALUE_ADD_POTION_EFFECT:
                McmeEntitySelector mcmeSelector = SelectorCompiler.compileMcmeEntitySelector(jsonObject);
                PotionEffect effect = PotionEffectCommpiler.compile(jsonObject);
                action = new PotionEffectAddAction(effect, mcmeSelector);
                break;
            case VALUE_REMOVE_POTION_EFFECT:
                mcmeSelector = SelectorCompiler.compileMcmeEntitySelector(jsonObject);
                effect = PotionEffectCommpiler.compile(jsonObject);
                action = new PotionEffectRemoveAction(effect, mcmeSelector);
            default:
                return Optional.empty();
        }
        JsonElement delayJson = jsonObject.get(KEY_DELAY);
        if(delayJson instanceof JsonPrimitive) {
            try {
                action.setDelay(delayJson.getAsInt());
            } catch(ClassCastException ex) {
                DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Ignoring invalid delay data!");
            }
        }
        return Optional.of(action);
    }

    private static Set<String> compileTriggerNames(JsonObject jsonObject) {
        JsonElement nameJson = jsonObject.get(KEY_TRIGGER_NAME);
        if(nameJson != null) {
            if (nameJson.isJsonPrimitive()) {
                return Collections.singleton(nameJson.getAsString());
            } else if (nameJson.isJsonArray()) {
                Set<String> result = new HashSet<>();
                nameJson.getAsJsonArray().forEach(element -> result.add(element.getAsString()));
                return result;
            }
        }
        return Collections.emptySet();
    }
}
