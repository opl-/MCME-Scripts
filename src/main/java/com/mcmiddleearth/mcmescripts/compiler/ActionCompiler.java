package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

                                VALUE_REGISTER_TRIGGER      = "registerTrigger",
                                VALUE_UNREGISTER_TRIGGER    = "unregisterTrigger",

                                KEY_SET_GOAL        = "setGoal",
                                KEY_SPAWN           = "spawn",
                                KEY_STOP_TALK       = "stopTalk",
                                KEY_TALK            = "talk",
                                KEY_TELEPORT        = "teleport",

                                KEY_TELEPORT_SPREAD = "spread",
                                KEY_MESSAGE         = "message";

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
            case KEY_SET_GOAL:
            case KEY_SPAWN:
            case KEY_STOP_TALK:
                VirtualEntitySelector selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                return new StopTalkAction(selector);
            case KEY_TALK:
                SpeechBalloonLayout layout = SpeechBalloonLayoutCompiler.compile(jsonObject);
                JsonElement message = jsonObject.get(KEY_MESSAGE);
                if(message!=null) {
                    layout.withMessage(message.getAsString());
                }
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                return new TalkAction(layout,selector);
            case KEY_TELEPORT:
                Location target = LocationCompiler.compile(jsonObject);
                PlayerSelector playerSelector = SelectorCompiler.compilePlayerSelector(jsonObject);
                double spread = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_TELEPORT_SPREAD),0);
                return new TeleportAction(target,spread,playerSelector);
        }
        return null;
    }
}
