package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import com.mcmiddleearth.mcmescripts.action.*;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import org.bukkit.Location;

import java.util.*;


public class ActionCompiler {

    private static final String KEY_ACTION          = "action",
                                KEY_ACTION_ARRAY    = "actions",
                                KEY_ACTION_TYPE     = "type",
                                KEY_TARGET          = "target",
                                KEY_GOAL_TARGET = "goal_target",


                                VALUE_REGISTER_TRIGGER      = "register_trigger",
                                VALUE_UNREGISTER_TRIGGER    = "unregister_trigger",

                                VALUE_SET_GOAL              = "set_goal",
                                VALUE_SPAWN                 = "spawn",
                                VALUE_STOP_TALK             = "stop_talk",
                                VALUE_TALK                  = "talk",
                                VALUE_TELEPORT              = "teleport",

                                KEY_TELEPORT_SPREAD         = "spread";

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
        JsonElement type = jsonObject.get(KEY_ACTION_TYPE);
        if (type == null) return Optional.empty();
        switch(type.getAsString()) {
            case VALUE_REGISTER_TRIGGER:
                Set<Trigger> triggers = TriggerCompiler.compile(jsonObject);
                if(triggers.isEmpty()) return Optional.empty();
                return Optional.of(new TriggerRegisterAction(triggers));
            case VALUE_UNREGISTER_TRIGGER:
                triggers = TriggerCompiler.compile(jsonObject);
                if(triggers.isEmpty()) return Optional.empty();
                return Optional.of(new TriggerUnregisterAction(triggers)); kann nicht funktionieren
            case VALUE_SET_GOAL:
                Optional<VirtualEntityGoalFactory> goalFactory = VirtualEntityGoalFactoryCompiler.compile(jsonObject);
                if(!goalFactory.isPresent()) return Optional.empty();
                VirtualEntitySelector selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                McmeEntitySelector goalTargetSelector = SelectorCompiler.compileMcmeEntitySelector(jsonObject, KEY_GOAL_TARGET)
                                                                        .orElse(null);
                return Optional.of(new SetGoalAction(goalFactory.get(), selector, goalTargetSelector));
            case VALUE_SPAWN:
                List<VirtualEntityFactory> factories = VirtualEntityFactoryCompiler.compile(jsonObject);
                return Optional.of(new SpawnAction(factories));
            case VALUE_STOP_TALK:
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                return Optional.of(new StopTalkAction(selector));
            case VALUE_TALK:
                SpeechBalloonLayout layout = SpeechBalloonLayoutCompiler.compile(jsonObject);
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                return Optional.of(new TalkAction(layout,selector));
            case VALUE_TELEPORT:
                Location target = LocationCompiler.compile(jsonObject.get(KEY_TARGET)).orElse(null);
                PlayerSelector playerSelector = SelectorCompiler.compilePlayerSelector(jsonObject);
                double spread = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_TELEPORT_SPREAD),0);
                return Optional.of(new TeleportAction(target,spread,playerSelector));
            default:
                return Optional.empty();
        }
    }
}
