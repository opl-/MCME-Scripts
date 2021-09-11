package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import com.mcmiddleearth.mcmescripts.action.*;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import org.bukkit.Location;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class ActionCompiler {

    private static final String KEY_ACTION          = "action",
                                KEY_ACTION_ARRAY    = "actions",
                                KEY_ACTION_TYPE     = "type",
                                KEY_TARGET          = "target",
                                KEY_GOAL            = "goal",
                                KEY_SPAWN_DATA      = "spawnData",

                                VALUE_REGISTER_TRIGGER      = "registerTrigger",
                                VALUE_UNREGISTER_TRIGGER    = "unregisterTrigger",

                                VALUE_SET_GOAL              = "setGoal",
                                VALUE_SPAWN                 = "spawn",
                                VALUE_STOP_TALK             = "stopTalk",
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
        if(actionData.isJsonArray()) {
            for(int i = 0; i< actionData.getAsJsonArray().size(); i++) {
                Action action = compileAction(actionData.getAsJsonArray().get(i).getAsJsonObject());
                if(action!=null) result.add(action);
            }
        } else {
            Action action = compileAction(actionData.getAsJsonObject());
            if(action!=null) result.add(action);
        }
        return result;
    }

    private static Action compileAction(JsonObject jsonObject) {
        JsonElement type = jsonObject.get(KEY_ACTION_TYPE);
        if (type == null) return null;
        switch(type.getAsString()) {
            case VALUE_REGISTER_TRIGGER:
                Set<Trigger> triggers = TriggerCompiler.compile(jsonObject);
                if(triggers.isEmpty()) return null;
                return new TriggerRegisterAction(triggers);
            case VALUE_UNREGISTER_TRIGGER:
                triggers = TriggerCompiler.compile(jsonObject);
                if(triggers.isEmpty()) return null;
                return new TriggerUnregisterAction(triggers);
            case VALUE_SET_GOAL:
                VirtualEntityFactory factory = VirtualEntityFactoryCompiler.compile(jsonObject.get(KEY_GOAL));
                VirtualEntitySelector selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                return new SetGoalAction(factory.getGoalFactory(),selector);
            case VALUE_SPAWN:
                factory = VirtualEntityFactoryCompiler.compile(jsonObject.get(KEY_SPAWN_DATA));
                return new SpawnAction(factory);
            case VALUE_STOP_TALK:
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                return new StopTalkAction(selector);
            case VALUE_TALK:
                SpeechBalloonLayout layout = SpeechBalloonLayoutCompiler.compile(jsonObject);
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                return new TalkAction(layout,selector);
            case VALUE_TELEPORT:
                Location target = LocationCompiler.compile(jsonObject.get(KEY_TARGET));
                PlayerSelector playerSelector = SelectorCompiler.compilePlayerSelector(jsonObject);
                double spread = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_TELEPORT_SPREAD),0);
                return new TeleportAction(target,spread,playerSelector);
        }
        return null;
    }
}
