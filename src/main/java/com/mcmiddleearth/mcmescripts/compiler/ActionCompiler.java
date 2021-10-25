package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.effect.Explosion;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import com.mcmiddleearth.mcmescripts.action.*;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import org.bukkit.Location;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
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
                                KEY_POTION_EFFECT   = "potion_effect",
                                KEY_TIME            = "time",
                                KEY_STATE           = "state",
                                KEY_ANIMATION       = "animation",
                                KEY_OVERRIDE        = "override",
                                KEY_ITEM            = "item",
                                KEY_ITEMS           = "items",
                                KEY_SLOT            = "slot",
                                KEY_SLOT_ID         = "slot_id",
                                KEY_DURATION        = "duration",
                                KEY_COMMAND         = "command",
                                KEY_PROBABILITY     = "probability",
                                KEY_GROUP           = "group",
                                KEY_RADIUS          = "radius",
                                KEY_QUANTITY        = "quantity",
                                KEY_CHOICES         = "choices",
                                KEY_WEIGHT          = "weight",
                                KEY_CENTER          = "center",


                                VALUE_REGISTER_TRIGGER      = "register_event",
                                VALUE_UNREGISTER_TRIGGER    = "unregister_event",

                                VALUE_SET_GOAL              = "set_goal",
                                VALUE_SPAWN                 = "spawn",
                                VALUE_DESPAWN               = "despawn",
                                VALUE_STOP_TALK             = "stop_talk",
                                VALUE_TALK                  = "talk",
                                VALUE_TELEPORT              = "teleport",
                                VALUE_ADD_POTION_EFFECT     = "add_potion_effect",
                                VALUE_REMOVE_POTION_EFFECT  = "remove_potion_effect",
                                VALUE_SET_SERVER_TIME       = "set_server_time",
                                VALUE_ENTITY_STATE          = "entity_state",
                                VALUE_ANIMATION             = "animation",
                                VALUE_GIVE_ITEM             = "give_item",
                                VALUE_REMOVE_ITEM           = "remove_item",
                                VALUE_EYE_EFFECT            = "eye_effect",
                                VALUE_EXECUTE_COMMAND       = "execute_command",
                                VALUE_FIREWORK              = "firework",
                                VALUE_EXPLOSION             = "explosion",
                                VALUE_RANDOM_SPAWN          = "random_spawn";


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
                //TODO: optional - set Goal
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
                PotionEffect effect = PotionEffectCompiler.compile(jsonObject.get(KEY_POTION_EFFECT));
                if(effect == null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_ADD_POTION_EFFECT+" action. Missing potion effect.");
                    return Optional.empty();
                }
                action = new PotionEffectAddAction(effect, mcmeSelector);
                break;
            case VALUE_REMOVE_POTION_EFFECT:
                mcmeSelector = SelectorCompiler.compileMcmeEntitySelector(jsonObject);
                effect = PotionEffectCompiler.compile(jsonObject.get(KEY_POTION_EFFECT));
                if(effect == null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_REMOVE_POTION_EFFECT+" action. Missing potion effect.");
                    return Optional.empty();
                }
                action = new PotionEffectRemoveAction(effect, mcmeSelector);
                break;
            case VALUE_SET_SERVER_TIME:
                JsonElement timeJson = jsonObject.get(KEY_TIME);
                if(! (timeJson instanceof JsonPrimitive)) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_SET_SERVER_TIME+" action. Missing server time.");
                    return Optional.empty();
                }
                long serverTime = timeJson.getAsLong();
                action = new ServerTimeAction(serverTime);
                break;
            case VALUE_ENTITY_STATE:
                JsonElement stateJson = jsonObject.get(KEY_STATE);
                if(! (stateJson instanceof JsonPrimitive)) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_ENTITY_STATE+" action. Missing entity state.");
                    return Optional.empty();
                }
                String state = stateJson.getAsString();
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                action = new EntityStateAction(selector, state);
                break;
            case VALUE_ANIMATION:
                JsonElement animationJson = jsonObject.get(KEY_ANIMATION);
                if(! (animationJson instanceof JsonPrimitive)) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_ANIMATION+" action. Missing animation name.");
                    return Optional.empty();
                }
                String animationName = animationJson.getAsString();
                JsonElement overrideJson = jsonObject.get(KEY_OVERRIDE);
                boolean override = true;
                if(overrideJson instanceof JsonPrimitive) {
                    override = overrideJson.getAsBoolean();
                }
                selector = SelectorCompiler.compileVirtualEntitySelector(jsonObject);
                action = new AnimationAction(selector, animationName, override);
                break;
            case VALUE_GIVE_ITEM:
                mcmeSelector = SelectorCompiler.compileMcmeEntitySelector(jsonObject);
                Set<ItemStack> items = ItemCompiler.compile(jsonObject.get(KEY_ITEM));
                items.addAll(ItemCompiler.compile(jsonObject.get(KEY_ITEMS)));
                if(items.isEmpty()) return Optional.empty();
                EquipmentSlot slot = null;
                JsonElement slotJson = jsonObject.get(KEY_SLOT);
                if(slotJson instanceof JsonPrimitive) {
                    try {
                        slot = EquipmentSlot.valueOf(slotJson.getAsString().toUpperCase());
                    } catch (IllegalArgumentException ex) {
                        DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Illegal equipment slot for "+VALUE_GIVE_ITEM+" action. Using main hand slot.");
                        slot = EquipmentSlot.HAND;
                    }
                }
                int slotId = -1;
                JsonElement slotIdJson = jsonObject.get(KEY_SLOT_ID);
                if(slotIdJson instanceof JsonPrimitive) {
                    try {
                        slotId = slotIdJson.getAsInt();
                    } catch(NumberFormatException ex) {
                        DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't parse slot id for action "+VALUE_GIVE_ITEM+".");
                    }
                }
                int duration = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_DURATION),-1);
                action = new ItemGiveAction(mcmeSelector, items, slot, slotId, duration);
                break;
            case VALUE_REMOVE_ITEM:
                mcmeSelector = SelectorCompiler.compileMcmeEntitySelector(jsonObject);
                items = ItemCompiler.compile(jsonObject.get(KEY_ITEM));
                items.addAll(ItemCompiler.compile(jsonObject.get(KEY_ITEMS)));
                if(items.isEmpty()) return Optional.empty();
                action = new ItemRemoveAction(mcmeSelector, items);
                break;
            case VALUE_EYE_EFFECT:
                playerSelector = SelectorCompiler.compilePlayerSelector(jsonObject);
                duration = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_DURATION),200);
                action = new EyeEffectAction(playerSelector, duration);
                break;
            case VALUE_EXECUTE_COMMAND:
                playerSelector = SelectorCompiler.compilePlayerSelector(jsonObject);
                JsonElement commandJson = jsonObject.get(KEY_COMMAND);
                if(!(commandJson instanceof JsonPrimitive)) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_EXECUTE_COMMAND+" action. Missing command line.");
                    return Optional.empty();
                }
                action = new ExecuteCommandAction(playerSelector, commandJson.getAsString());
                break;
            case VALUE_FIREWORK:
                Location location = LocationCompiler.compile(jsonObject.get(KEY_TARGET)).orElse(null);
                FireworkMeta fireworkMeta = FireworkMetaCompiler.compile(jsonObject);
                if(fireworkMeta == null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_FIREWORK+" action. Missing firework meta.");
                    return Optional.empty();
                }
                action = new FireworkAction(location, fireworkMeta);
                break;
            case VALUE_EXPLOSION:
                Explosion explosion = ExplosionCompiler.compile(jsonObject);
                if(explosion == null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_EXPLOSION+" action. Missing explosion data.");
                }
                McmeEntitySelector unaffectedSelector = ExplosionCompiler.getUnaffectedSelector(jsonObject);
                McmeEntitySelector damagerSelector = ExplosionCompiler.getDamagerSelector(jsonObject);
                action = new ExplosionAction(explosion, unaffectedSelector, damagerSelector);
                break;
            case VALUE_RANDOM_SPAWN:
                JsonElement choicesJson = jsonObject.get(KEY_CHOICES);
                if(!(choicesJson instanceof JsonArray)) {
                    return Optional.empty();
                }
                List<SpawnRandomSelectionAction.Choice> choices = new ArrayList<>();
                for(JsonElement choiceJson: choicesJson.getAsJsonArray()) {
                    factories = VirtualEntityFactoryCompiler.compile(choiceJson.getAsJsonObject());
                    int weight = PrimitiveCompiler.compileInteger(choiceJson.getAsJsonObject().get(KEY_WEIGHT), 10);
                    choices.add(new SpawnRandomSelectionAction.Choice(weight, factories));
                }
                double probability = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PROBABILITY),1);
                boolean group = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_GROUP),true);
                int minRadius = PrimitiveCompiler.compileLowerInt(jsonObject.get(KEY_RADIUS),5);
                int maxRadius = PrimitiveCompiler.compileUpperInt(jsonObject.get(KEY_RADIUS),10);
                int minQuantity = PrimitiveCompiler.compileLowerInt(jsonObject.get(KEY_QUANTITY),2);
                int maxQuantity = PrimitiveCompiler.compileUpperInt(jsonObject.get(KEY_QUANTITY),5);
                SpawnRandomSelectionAction.RandomSpawnData randomSpawnData = new SpawnRandomSelectionAction.RandomSpawnData(choices)
                        .withMinQuantity(minQuantity).withMaxQuantity(maxQuantity)
                        .withMinRadius(minRadius).withMaxRadius(maxRadius)
                        .withProbability(probability).withGroup(group);
                JsonElement goalTargetJson = jsonObject.get(KEY_GOAL_TARGET);
                if(goalTargetJson instanceof JsonPrimitive) {
                    randomSpawnData.withGoalTargetSelector(new McmeEntitySelector(goalTargetJson.getAsString()));
                }
                VirtualEntityGoalFactoryCompiler.compile(jsonObject).ifPresent(randomSpawnData::withGoalFactory);
                Location center = LocationCompiler.compile(jsonObject.get(KEY_CENTER)).orElse(null);
                if(center != null) {
                    action = new SpawnRandomLocationAction(center, randomSpawnData);
                } else {
                    mcmeSelector = SelectorCompiler.compileMcmeEntitySelector(jsonObject);
                    action = new SpawnRandomSelectionAction(mcmeSelector,randomSpawnData);
                }
                break;
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
